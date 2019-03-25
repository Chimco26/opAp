package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface GetVersionCallback {

    void onGetVersionSuccess(Object response);

    void onGetVersionFailed(ErrorObjectInterface reason);
}
