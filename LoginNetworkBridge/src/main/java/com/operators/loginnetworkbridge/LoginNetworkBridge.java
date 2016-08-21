package com.operators.loginnetworkbridge;


import com.operators.errorobject.ErrorObjectInterface;
import com.operators.infra.LoginCoreCallback;
import com.operators.infra.LoginNetworkBridgeInterface;
import com.operators.loginnetworkbridge.interfaces.LoginNetworkManagerInterface;
import com.operators.loginnetworkbridge.server.ErrorObject;
import com.operators.loginnetworkbridge.server.requests.LoginRequest;
import com.operators.loginnetworkbridge.server.responses.ErrorResponse;
import com.operators.loginnetworkbridge.server.responses.SessionResponse;
import com.zemingo.logrecorder.ZLogger;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginNetworkBridge implements LoginNetworkBridgeInterface {
    private static final String LOG_TAG = LoginNetworkBridge.class.getSimpleName();

    private int mRetryCount = 0;
    private LoginNetworkManagerInterface mLoginNetworkManagerInterface;

    @Override
    public void login(final String siteUrl, final String userName, final String password,String language, final LoginCoreCallback loginCoreCallback, final int totalRetries, int specificRequestTimeout) {
        HttpUrl httpUrl = HttpUrl.parse(siteUrl);
        if (httpUrl == null) {
            loginCoreCallback.onLoginFailed(new ErrorObject(ErrorObjectInterface.ErrorCode.Url_not_correct, "Site URL is not correct"));
            return;
        }

        LoginRequest loginRequest = new LoginRequest(userName, password, language);
        Call<SessionResponse> call = mLoginNetworkManagerInterface.getLoginRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getUserSessionId(loginRequest);
        call.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                SessionResponse.UserSessionIDResult sessionResult = response.body().getUserSessionIDResult();
                if (sessionResult.getErrorResponse() == null) {
                    ZLogger.d(LOG_TAG, "onRequestSucceed(), " + response.body().getUserSessionIDResult());
                    loginCoreCallback.onLoginSucceeded(response.body().getUserSessionIDResult().getSessionIds().get(0).getSessionId());
                } else {
                    ZLogger.d(LOG_TAG, "onRequest(), getSessionId failed");
                    ErrorObject errorObject = errorObjectWithErrorCode(sessionResult.getErrorResponse());
                    loginCoreCallback.onLoginFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    ZLogger.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "General Error");
                    loginCoreCallback.onLoginFailed(errorObject);
                }
            }
        });
    }

    public void inject(LoginNetworkManagerInterface loginNetworkManagerInterface) {
        mLoginNetworkManagerInterface = loginNetworkManagerInterface;
    }

    private ErrorObject errorObjectWithErrorCode(ErrorResponse errorResponse) {
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
