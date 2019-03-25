package com.operators.jobsinfra;


import com.example.common.callback.ErrorObjectInterface;

public interface StartJobForMachineCallback {
    void onStartJobForMachineSuccess();

    void onStartJobForMachineFailed(ErrorObjectInterface reason);
}
