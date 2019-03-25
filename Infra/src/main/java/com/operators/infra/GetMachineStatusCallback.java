package com.operators.infra;

import com.example.common.callback.ErrorObjectInterface;

public interface GetMachineStatusCallback<T>
{
    void onGetMachineStatusSucceeded(MachineStatus machineStatus);

    void onGetMachineStatusFailed(ErrorObjectInterface reason);
}
