package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface GetPendingJobListCallback {

    void onGetPendingJobListSuccess(Object response);

    void onGetPendingJobListFailed(ErrorObjectInterface reason);
}
