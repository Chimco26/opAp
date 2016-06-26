package com.operators.networkbridge;


import com.operators.infra.LoginCoreCallback;
import com.operators.infra.LoginNetworkBridgeInterface;
import com.operators.networkbridge.interfaces.LoginNetworkManagerInterface;
import com.operators.networkbridge.server.ErrorObject;
import com.operators.networkbridge.server.requests.LoginRequest;
import com.operators.networkbridge.server.responses.ErrorResponse;
import com.operators.networkbridge.server.responses.SessionResponse;
import com.zemingo.logrecorder.ZLogger;

import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Response;

public class LoginNetworkBridge implements LoginNetworkBridgeInterface {
    private static final String LOG_TAG = LoginNetworkBridge.class.getSimpleName();

    private LoginNetworkManagerInterface mLoginNetworkManagerInterface;

    public LoginNetworkBridge() {

    }

    @Override
    public void login(final String siteUrl, final String userName, final String password, final LoginCoreCallback loginCoreCallback) {
        LoginRequest loginRequest = new LoginRequest(userName, password);
        Call<SessionResponse> call = mLoginNetworkManagerInterface.getRetroFitServiceRequests(siteUrl).getUserSessionId(loginRequest);
        call.enqueue(new retrofit2.Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                SessionResponse.UserSessionIDResult sessionResult = response.body().getUserSessionIDResult();
                if (sessionResult.getErrorResponse() == null) {
                    ZLogger.d(LOG_TAG, "onRequestSucceed(), " + response.body().getUserSessionIDResult());
                    loginCoreCallback.onLoginSucceeded(response.body().getUserSessionIDResult().getSessionIds().get(0).toString());
                } else {
                    ZLogger.d(LOG_TAG, "onRequest(), getSessionId failed");
                    ErrorObject errorObject = translateToErrorCode(sessionResult.getErrorResponse());
                    loginCoreCallback.onLoginFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "General Error");
                loginCoreCallback.onLoginFailed(errorObject);
            }
        });
    }

    public void inject(LoginNetworkManagerInterface loginNetworkManagerInterface) {
        mLoginNetworkManagerInterface = loginNetworkManagerInterface;
    }

    private ErrorObject translateToErrorCode(ErrorResponse errorResponse) {
        //todo
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
