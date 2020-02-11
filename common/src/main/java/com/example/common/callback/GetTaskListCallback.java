package com.example.common.callback;

import com.example.common.StandardResponse;
import com.example.common.task.TaskListResponse;

public interface GetTaskListCallback {
    void onGetTaskListCallbackSuccess(TaskListResponse response);

    void onGetTaskListCallbackFailed(StandardResponse reason);
}
