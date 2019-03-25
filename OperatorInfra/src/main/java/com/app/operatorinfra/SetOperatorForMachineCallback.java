package com.app.operatorinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface SetOperatorForMachineCallback {
    void onSetOperatorForMachineSuccess();

    void onSetOperatorForMachineFailed(ErrorObjectInterface reason);
}
