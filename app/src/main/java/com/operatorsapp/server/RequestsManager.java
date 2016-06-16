package com.operatorsapp.server;

import com.operatorsapp.server.interfaces.EmeraldServiceRequests;
import com.operatorsapp.server.mocks.RetrofitMockClient;
import com.operatorsapp.server.requests.LoginRequest;
import com.operatorsapp.server.responses.ErrorResponse;
import com.operatorsapp.server.responses.SessionResponse;
import com.zemingo.logrecorder.ZLogger;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestsManager {
    private static final String LOG_TAG = RequestsManager.class.getSimpleName();
    private static RequestsManager msInstance;
    private HashMap<String, EmeraldServiceRequests> mEmeraldServiceRequestsHashMap;

    public static RequestsManager getInstance() {
        if (msInstance == null) {
            msInstance = new RequestsManager();
        }
        return msInstance;
    }

    private RequestsManager() {
        mEmeraldServiceRequestsHashMap = new HashMap<>();

    }

    private EmeraldServiceRequests getRetroFitServiceRequests(String siteUrl) {
        return getRetroFitServiceRequests(siteUrl, -1, null);
    }

    private EmeraldServiceRequests getRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit) {
        if (mEmeraldServiceRequestsHashMap.containsKey(siteUrl)) {
            return mEmeraldServiceRequestsHashMap.get(siteUrl);
        } else {
            OkHttpClient okHttpClient;
            if (timeout >= 0 && timeUnit != null) {
                okHttpClient = new OkHttpClient.Builder()
                        //add mock
                        .addInterceptor(new RetrofitMockClient())
                        .writeTimeout(timeout, timeUnit)
                        .connectTimeout(timeout, timeUnit)
                        .readTimeout(timeout, timeUnit)
                        .build();
            } else {
                okHttpClient = new OkHttpClient.Builder()
                        //add mock
                        .addInterceptor(new RetrofitMockClient())
                        .build();
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(siteUrl)
                    .client(okHttpClient)
                    .build();

            EmeraldServiceRequests emeraldServiceRequests = retrofit.create(EmeraldServiceRequests.class);
            mEmeraldServiceRequestsHashMap.put(siteUrl, emeraldServiceRequests);
            return emeraldServiceRequests;
        }
    }

    // In use
    public void getUserSessionId(String siteUrl, String userName, String password, final OnRequestManagerResponseListener<String> listener) {
        LoginRequest loginRequest = new LoginRequest(userName, password);
        Call<SessionResponse> call = getRetroFitServiceRequests(siteUrl).getUserSessionId(loginRequest);
        call.enqueue(new retrofit2.Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, retrofit2.Response<SessionResponse> response) {
                ZLogger.d(LOG_TAG, "onRequestSucceed(), ");
                SessionResponse.GetUserSessionIDResult sessionResult = response.body().getGetUserSessionIDResult();
                if (sessionResult.getErrorResponse() == null) {
                    if (response.body().getGetUserSessionIDResult().getSessionIds() != null && response.body().getGetUserSessionIDResult().getSessionIds().get(0) != null) {
                        listener.onRequestSucceed(response.body().getGetUserSessionIDResult().getSessionIds().get(0).getSessionId());
                    }
                } else {
                    ErrorObject errorObject = translateToErrorCode(sessionResult.getErrorResponse());
                    listener.onRequestFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                ZLogger.d(LOG_TAG, "onRequestFailed(), ");
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "General Error");
                listener.onRequestFailed(errorObject);
            }
        });
    }

    public interface OnRequestManagerResponseListener<T> {
        void onRequestSucceed(T result);

        void onRequestFailed(ErrorObject reason);
    }

    private ErrorObject translateToErrorCode(ErrorResponse errorResponse) {
        ErrorObject.ErrorCode code = toCode(errorResponse.getErrorCode());
        return new ErrorObject(code, errorResponse.getErrorDesc());
    }

    private ErrorObject.ErrorCode toCode(int errorCode) {
        switch (errorCode) {
            case 101:
                return ErrorObject.ErrorCode.Credentials_mismatch;
        }
        return ErrorObject.ErrorCode.Unknown;
    }
}
