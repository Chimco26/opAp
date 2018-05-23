package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

public interface GetVersionCallback {

    void onGetVersionSuccess(Object response);

    void onGetVersionFailed(ErrorObjectInterface reason);
}
