package com.operators.machinestatuscore.interfaces;


import com.operators.machinestatusinfra.ErrorObjectInterface;
import com.operators.machinestatusinfra.MachineStatus;

public interface MachineStatusUICallback {
    void onStatusReceivedSuccessfully(MachineStatus machineStatus);

    void onTimerChanged(long millisUntilFinished);

    void onStatusReceiveFailed(ErrorObjectInterface reason);
}
