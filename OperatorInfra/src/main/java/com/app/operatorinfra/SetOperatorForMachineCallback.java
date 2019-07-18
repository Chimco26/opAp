package com.app.operatorinfra;

import com.example.common.StandardResponse;

public interface SetOperatorForMachineCallback {
    void onSetOperatorForMachineSuccess();

    void onSetOperatorForMachineFailed(StandardResponse reason);
}
