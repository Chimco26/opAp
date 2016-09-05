package com.operators.machinestatuscore.interfaces;


import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinestatusinfra.models.MachineStatus;

public interface MachineStatusUICallback {
    void onStatusReceivedSuccessfully(MachineStatus machineStatus);

    void onTimerChanged(long millisUntilFinished);

    void onStatusReceiveFailed(ErrorObjectInterface reason);
}
