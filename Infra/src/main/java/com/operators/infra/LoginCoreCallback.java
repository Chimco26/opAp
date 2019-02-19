package com.operators.infra;


import com.operators.errorobject.ErrorObjectInterface;

public interface LoginCoreCallback {

    void onLoginSucceeded(String sessionId, String siteName, int userId);

    void onLoginFailed(ErrorObjectInterface reason);
}
