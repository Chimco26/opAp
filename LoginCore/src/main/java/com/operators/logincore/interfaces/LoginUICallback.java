package com.operators.logincore.interfaces;

import com.example.common.StandardResponse;

import java.util.ArrayList;

public interface LoginUICallback<T> {

    void onLoginSucceeded(ArrayList<T> machines, String siteName);

    void onLoginFailed(StandardResponse reason);
}
