package com.operatorsapp.activities.interfaces;

import com.operators.errorobject.ErrorObjectInterface;

/**
 * Created by Admin on 31-Jul-16.
 */
public interface SilentLoginCallback {

    void onSilentLoginSucceeded();
    void onSilentLoginFailed(ErrorObjectInterface reason);
}
