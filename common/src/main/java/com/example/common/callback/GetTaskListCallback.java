package com.example.common.callback;

import com.example.common.StandardResponse;

public interface GetTaskListCallback {
    void onGetTaskListCallbackSuccess(Object response);

    void onGetTaskListCallbackFailed(StandardResponse reason);
}
