package com.operators.infra;


import com.operators.errorobject.ErrorObjectInterface;

public interface LoginCoreCallback {

    void onLoginSucceeded(String sessionId);

    void onLoginFailed(ErrorObjectInterface reason);
}
