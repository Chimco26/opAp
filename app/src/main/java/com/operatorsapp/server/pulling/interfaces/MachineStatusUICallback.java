package com.operatorsapp.server.pulling.interfaces;


import com.example.common.StandardResponse;
import com.operators.machinestatusinfra.models.MachineStatus;

public interface MachineStatusUICallback {
    void onStatusReceivedSuccessfully(MachineStatus machineStatus);

    void onTimerChanged(long millisUntilFinished);

    void onStatusReceiveFailed(StandardResponse reason);
}
