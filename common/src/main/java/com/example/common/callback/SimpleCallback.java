package com.example.common.callback;

import com.example.common.StandardResponse;

public interface SimpleCallback {

    void onRequestSuccess(StandardResponse response);

    void onRequestFailed(StandardResponse reason);
}

