package com.operators.reportrejectnetworkbridge.interfaces;

import com.example.common.StandardResponse;
import com.example.common.task.TaskFilesResponse;

public interface GetTaskFilesCallback {

    void onGetTaskFilesCallbackSuccess(TaskFilesResponse response);

    void onGetTaskFilesCallbackFailed(StandardResponse reason);

}
