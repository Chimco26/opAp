package com.operators.infra;

import java.util.ArrayList;

public interface GetMachineStatusCallback<T>
{
    void onGetMachineStatusSucceeded(MachineStatus machineStatus);

    void onGetMachineStatusFailed();
}
