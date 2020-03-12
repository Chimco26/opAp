package com.operators.reportrejectnetworkbridge.interfaces;

import com.example.common.StandardResponse;

public interface UpdateTaskStatusCallback {

    void onUpdateTaskStatusCallbackSuccess(StandardResponse response);

    void onUpdateTaskStatusCallbackFailed(StandardResponse reason);
}
