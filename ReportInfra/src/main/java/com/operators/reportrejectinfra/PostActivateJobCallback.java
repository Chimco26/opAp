package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface PostActivateJobCallback {

    void onPostActivateJobSuccess(Object response);

    void onPostActivateJobFailed(ErrorObjectInterface reason);
}
