package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

public interface GetJobDetailsCallback {

    void onGetJobDetailsSuccess(Object response);

    void onGetJobDetailsFailed(StandardResponse reason);
}
