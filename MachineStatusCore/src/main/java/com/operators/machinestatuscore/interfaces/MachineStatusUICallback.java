package com.operators.machinestatuscore.interfaces;


import com.operators.infra.MachineStatus;

public interface MachineStatusUICallback
{
    void onStatusReceivedSuccessfully(MachineStatus machineStatus);
    void onTimerChanged(String timeToEndInHours);
    void onStatusReceiveFailed();
}
