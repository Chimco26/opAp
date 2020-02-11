package com.example.common.callback;

import com.example.common.StandardResponse;

public interface CreateTaskCallback {

    void onCreateTaskCallbackSuccess(Object response);

    void onCreateTaskCallbackFailed(StandardResponse reason);
}
