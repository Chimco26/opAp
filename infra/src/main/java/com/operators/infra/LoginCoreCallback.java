package com.operators.infra;


public interface LoginCoreCallback {

    void onLoginSucceeded(String sessionId);

    void onLoginFailed(ErrorObjectInterface reason);
}
