package com.operators.machinestatusinfra.interfaces;

import com.example.common.StandardResponse;
import com.operators.machinestatusinfra.models.MachineStatus;

public interface GetMachineStatusCallback<T> {
    void onGetMachineStatusSucceeded(MachineStatus machineStatus);

    void onGetMachineStatusFailed(StandardResponse reason);
}
