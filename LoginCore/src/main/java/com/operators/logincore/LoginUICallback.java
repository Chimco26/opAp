package com.operators.logincore;


import com.operators.infra.ErrorObjectInterface;

import java.util.ArrayList;

public interface LoginUICallback {

    void onLoginSucceeded(ArrayList machines);

    void onLoginFailed(ErrorObjectInterface reason);
}
