package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

public interface PostActivateJobCallback {

    void onPostActivateJobSuccess(Object response);

    void onPostActivateJobFailed(ErrorObjectInterface reason);
}
