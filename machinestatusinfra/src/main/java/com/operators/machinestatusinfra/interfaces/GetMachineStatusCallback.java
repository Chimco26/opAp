package com.operators.machinestatusinfra.interfaces;

import com.example.common.callback.ErrorObjectInterface;
import com.operators.machinestatusinfra.models.MachineStatus;

public interface GetMachineStatusCallback<T> {
    void onGetMachineStatusSucceeded(MachineStatus machineStatus);

    void onGetMachineStatusFailed(ErrorObjectInterface reason);
}
