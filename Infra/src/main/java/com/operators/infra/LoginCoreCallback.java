package com.operators.infra;

import com.example.common.callback.ErrorObjectInterface;

public interface LoginCoreCallback {

    void onLoginSucceeded(String sessionId, String siteName, int userId);

    void onLoginFailed(ErrorObjectInterface reason);
}
