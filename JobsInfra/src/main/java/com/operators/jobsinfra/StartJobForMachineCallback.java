package com.operators.jobsinfra;


public interface StartJobForMachineCallback {
    void onStartJobForMachineSuccess();

    void onStartJobForMachineFailed(ErrorObjectInterface reason);
}
