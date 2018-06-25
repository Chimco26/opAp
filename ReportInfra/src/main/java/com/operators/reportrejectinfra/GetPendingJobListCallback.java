package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

public interface GetPendingJobListCallback {

    void onGetPendingJobListSuccess(Object response);

    void onGetPendingJobListFailed(ErrorObjectInterface reason);
}
