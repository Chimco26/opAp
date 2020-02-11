package com.operators.reportrejectnetworkbridge.interfaces;

import com.example.common.StandardResponse;
import com.example.common.task.TaskObjectsForCreateOrEditResponse;

public interface GetTaskObjectsForCreateCallback {

    void onGetTaskObjectsForCreateCallbackSuccess(TaskObjectsForCreateOrEditResponse response);

    void onGetTaskObjectsForCreateCallbackFailed(StandardResponse reason);
}
