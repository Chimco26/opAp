package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

public interface GetVersionCallback {

    void onGetVersionSuccess(Object response);

    void onGetVersionFailed(StandardResponse reason);
}
