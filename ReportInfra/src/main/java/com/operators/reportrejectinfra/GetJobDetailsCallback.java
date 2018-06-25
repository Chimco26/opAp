package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

public interface GetJobDetailsCallback {

    void onGetJobDetailsSuccess(Object response);

    void onGetJobDetailsFailed(ErrorObjectInterface reason);
}
