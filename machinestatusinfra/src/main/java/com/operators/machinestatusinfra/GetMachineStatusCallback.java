package com.operators.machinestatusinfra;

public interface GetMachineStatusCallback<T>
{
    void onGetMachineStatusSucceeded(MachineStatus machineStatus);

    void onGetMachineStatusFailed(ErrorObjectInterface reason);
}
