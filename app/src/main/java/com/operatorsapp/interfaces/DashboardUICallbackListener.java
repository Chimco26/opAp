package com.operatorsapp.interfaces;

import com.github.mikephil.charting.data.Entry;
import com.operators.infra.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.shiftloginfra.Event;

import java.util.ArrayList;
import java.util.List;

public interface DashboardUICallbackListener {
    void onDeviceStatusChanged(MachineStatus machineStatus);

    void onMachineDataReceived(ArrayList<Widget> widgetList);

    void onShiftLogDataReceived(ArrayList<Event> events);

    void onTimerChanged(String timeToEndInHours);

    void onDataFailure(ErrorObjectInterface reason);
}
