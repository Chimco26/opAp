package com.operatorsapp.interfaces;

import com.example.common.Event;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.permissions.WidgetInfo;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.example.common.callback.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;

import java.util.ArrayList;
import java.util.HashMap;

public interface DashboardUICallbackListener {


    enum CallType {
        Status,
        MachineData,
        ShiftLog;
    }
    void onPermissionForMachinePolling(HashMap<Integer, WidgetInfo> permissionResponse);

    void onDeviceStatusChanged(MachineStatus machineStatus);

    void onMachineDataReceived(ArrayList<Widget> widgetList);

    void onShiftLogDataReceived(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse, MachineJoshDataResponse mMachineJoshDataResponse);

    void onTimerChanged(String timeToEndInHours);

    void onDataFailure(ErrorObjectInterface reason, CallType callType);

    void onApproveFirstItemEnabledChanged(boolean enabled);

    void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine mActiveJobsListForMachine);
}
