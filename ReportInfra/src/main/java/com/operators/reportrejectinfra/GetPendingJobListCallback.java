package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

public interface GetPendingJobListCallback {

    void onGetPendingJobListSuccess(Object response);

    void onGetPendingJobListFailed(StandardResponse reason);
}
