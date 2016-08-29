package com.operatorsapp.interfaces;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.shiftloginfra.Event;

import java.util.ArrayList;

public interface DashboardUICallbackListener {
    enum CallType {
        Status,
        MachineData,
        ShiftLog
    }

    void onDeviceStatusChanged(MachineStatus machineStatus);

    void onMachineDataReceived(ArrayList<Widget> widgetList);

    void onShiftLogDataReceived(ArrayList<Event> events);

    void onTimerChanged(String timeToEndInHours);

    void onDataFailure(ErrorObjectInterface reason, CallType callType);

    void onShiftForMachineEnded();
}
