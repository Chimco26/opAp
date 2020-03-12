package com.operators.loginnetworkbridge;


import androidx.annotation.NonNull;

import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.infra.LoginCoreCallback;
import com.operators.infra.LoginNetworkBridgeInterface;
import com.operators.loginnetworkbridge.interfaces.LoginNetworkManagerInterface;
import com.operators.loginnetworkbridge.server.requests.LoginRequest;
import com.operators.loginnetworkbridge.server.responses.SessionResponse;

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
    public void login(final String siteUrl, final String userName, final String password, String language, final LoginCoreCallback loginCoreCallback, final int totalRetries, int specificRequestTimeout) {
        HttpUrl httpUrl = HttpUrl.parse(siteUrl);
        if (httpUrl == null) {
            loginCoreCallback.onLoginFailed(new StandardResponse(ErrorObjectInterface.ErrorCode.Url_not_correct, "Site URL is not correct"));
            return;
        }

        LoginRequest loginRequest = new LoginRequest(userName, password, language);
        Call<SessionResponse> call;
        try {
            call = mLoginNetworkManagerInterface.getLoginRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getUserSessionId(loginRequest);
        }catch (NullPointerException e){
            loginCoreCallback.onLoginFailed(new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Network error"));
            return;
        }
        call.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SessionResponse> call, @NonNull Response<SessionResponse> response) {
                if (response.body() == null) {
                    onFailure(call, new NullPointerException());
                } else {
                    SessionResponse.UserSessionIDResult sessionResult = response.body().getUserSessionIDResult();
                    if (sessionResult.getError().getErrorDesc() == null) {
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestSucceed(), " + response.body().getUserSessionIDResult());
                        loginCoreCallback.onLoginSucceeded(response.body().getUserSessionIDResult().getSessionIds().get(0).getSessionId(), sessionResult.getSessionIds().get(0).getmSiteName(), sessionResult.getSessionIds().get(0).getUserID());
                    } else {
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequest(), getSessionId failed");
                        sessionResult.getError().setDefaultErrorCodeConstant(sessionResult.getError().getErrorCode());
                        loginCoreCallback.onLoginFailed(sessionResult);
                    }
                }

            }


            @Override
            public void onFailure(@NonNull Call<SessionResponse> call, @NonNull Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "General Error");
                    loginCoreCallback.onLoginFailed(errorObject);
                }
            }
        });

    }

    public void inject(LoginNetworkManagerInterface loginNetworkManagerInterface) {
        mLoginNetworkManagerInterface = loginNetworkManagerInterface;
    }

}
