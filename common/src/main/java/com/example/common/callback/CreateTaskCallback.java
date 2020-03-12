package com.example.common.callback;

import com.example.common.StandardResponse;

public interface CreateTaskCallback {

    void onCreateTaskCallbackSuccess(StandardResponse response);

    void onCreateTaskCallbackFailed(StandardResponse reason);
}
