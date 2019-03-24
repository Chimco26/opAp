package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface GetJobDetailsCallback {

    void onGetJobDetailsSuccess(Object response);

    void onGetJobDetailsFailed(ErrorObjectInterface reason);
}
