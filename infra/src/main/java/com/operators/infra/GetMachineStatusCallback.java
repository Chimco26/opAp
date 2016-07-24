package com.operators.infra;


public interface GetMachineStatusCallback<T>
{
    void onGetMachineStatusSucceeded(MachineStatus machineStatus);

    void onGetMachineStatusFailed(ErrorObjectInterface reason);
}
