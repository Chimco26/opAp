package com.operatorsapp.interfaces;

import com.operators.machinestatusinfra.interfaces.ErrorObjectInterface;
import com.operators.machinestatusinfra.models.MachineStatus;

public interface DashboardUICallbackListener {
    void onDeviceStatusChanged(MachineStatus machineStatus);

    void onTimerChanged(String timeToEndInHours);

    void onDataFailure(ErrorObjectInterface reason);
}
