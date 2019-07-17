package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

public interface PostActivateJobCallback {

    void onPostActivateJobSuccess(StandardResponse response);

    void onPostActivateJobFailed(StandardResponse reason);
}
