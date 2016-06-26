package com.operators.logincore;


import com.operators.infra.ErrorObjectInterface;

public interface LoginUICallback {

    void onLoginSucceeded();

    void onLoginFailed(ErrorObjectInterface reason);
}
