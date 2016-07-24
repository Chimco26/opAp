package com.app.operatorinfra;


public interface SetOperatorForMachineCallback {
    void onSetOperatorForMachineSuccess();

    void onSetOperatorForMachineFailed(ErrorObjectInterface reason);
}
