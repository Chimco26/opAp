package com.operators.jobsinfra;


import com.example.common.StandardResponse;

public interface StartJobForMachineCallback {
    void onStartJobForMachineSuccess();

    void onStartJobForMachineFailed(StandardResponse reason);
}
