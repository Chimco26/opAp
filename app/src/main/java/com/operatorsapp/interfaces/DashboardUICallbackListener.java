package com.operatorsapp.interfaces;

import com.operators.infra.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;

import java.util.ArrayList;
import java.util.List;

public interface DashboardUICallbackListener {
    void onDeviceStatusChanged(MachineStatus machineStatus);

    void onMachineDataReceived(ArrayList<Widget> widgetList);

    void onTimerChanged(String timeToEndInHours);

    void onDataFailure(ErrorObjectInterface reason);
}
