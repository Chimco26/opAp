package com.app.operatorinfra;


import com.operators.errorobject.ErrorObjectInterface;

public interface SetOperatorForMachineCallback {
    void onSetOperatorForMachineSuccess();

    void onSetOperatorForMachineFailed(ErrorObjectInterface reason);
}
