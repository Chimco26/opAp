package com.operators.infra;

import com.example.common.StandardResponse;

public interface LoginCoreCallback {

    void onLoginSucceeded(String sessionId, String siteName, int userId);

    void onLoginFailed(StandardResponse reason);
}
