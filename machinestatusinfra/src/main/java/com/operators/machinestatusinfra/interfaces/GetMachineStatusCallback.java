package com.operators.machinestatusinfra.interfaces;

import com.operators.machinestatusinfra.models.MachineStatus;

public interface GetMachineStatusCallback<T> {
    void onGetMachineStatusSucceeded(MachineStatus machineStatus);

    void onGetMachineStatusFailed(ErrorObjectInterface reason);
}
