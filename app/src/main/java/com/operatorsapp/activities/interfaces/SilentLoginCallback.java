package com.operatorsapp.activities.interfaces;

import com.example.common.StandardResponse;

/**
 * Created by Admin on 31-Jul-16.
 */
public interface SilentLoginCallback {

    void onSilentLoginSucceeded();
    void onSilentLoginFailed(StandardResponse reason);
}
