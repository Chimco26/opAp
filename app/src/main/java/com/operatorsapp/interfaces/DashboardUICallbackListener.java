package com.operatorsapp.interfaces;

import com.operators.infra.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;

import java.util.List;

public interface DashboardUICallbackListener {
    void onDeviceStatusChanged(MachineStatus machineStatus);

    void onMachineDataReceived(List<Widget> widgetList);

    void onTimerChanged(String timeToEndInHours);

    void onDataFailure(ErrorObjectInterface reason);
}
