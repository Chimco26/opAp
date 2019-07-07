package com.operatorsapp.server.pulling.interfaces;


import com.example.common.callback.ErrorObjectInterface;
import com.operators.machinestatusinfra.models.MachineStatus;

public interface MachineStatusUICallback {
    void onStatusReceivedSuccessfully(MachineStatus machineStatus);

    void onTimerChanged(long millisUntilFinished);

    void onStatusReceiveFailed(ErrorObjectInterface reason);
}
