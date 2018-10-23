package com.operators.logincore.interfaces;


import com.operators.errorobject.ErrorObjectInterface;

import java.util.ArrayList;

public interface LoginUICallback<T> {

    void onLoginSucceeded(ArrayList<T> machines, String siteName);

    void onLoginFailed(ErrorObjectInterface reason);
}
