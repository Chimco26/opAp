package com.operators.infra;


import com.operators.errorobject.ErrorObjectInterface;

public interface GetMachineStatusCallback<T>
{
    void onGetMachineStatusSucceeded(MachineStatus machineStatus);

    void onGetMachineStatusFailed(ErrorObjectInterface reason);
}
