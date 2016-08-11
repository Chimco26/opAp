package com.operators.jobsinfra;


import com.operators.errorobject.ErrorObjectInterface;

public interface StartJobForMachineCallback {
    void onStartJobForMachineSuccess();

    void onStartJobForMachineFailed(ErrorObjectInterface reason);
}
