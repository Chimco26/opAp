package com.operatorsapp.interfaces;

import android.util.SparseArray;

import com.example.common.Event;
import com.example.common.StandardResponse;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.permissions.WidgetInfo;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;

import java.util.ArrayList;

public interface DashboardUICallbackListener {


    enum CallType {
        Status,
        MachineData,
        ShiftLog;
    }
    void onPermissionForMachinePolling(SparseArray<WidgetInfo> permissionResponse);

    void onDeviceStatusChanged(MachineStatus machineStatus);

    void onMachineDataReceived(ArrayList<Widget> widgetList);

    void onShiftLogDataReceived(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse, MachineJoshDataResponse mMachineJoshDataResponse);

    void onTimerChanged(String timeToEndInHours);

    void onDataFailure(StandardResponse reason, CallType callType);

    void onApproveFirstItemEnabledChanged(boolean enabled);

    void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine mActiveJobsListForMachine);

    public class DashboardUICallbackListenerTaged{
        private DashboardUICallbackListener listener;
        private String tag;

        public DashboardUICallbackListenerTaged(DashboardUICallbackListener listener, String tag) {
            this.listener = listener;
            this.tag = tag;
        }

        public DashboardUICallbackListener getListener() {
            return listener;
        }

        public String getTag() {
            return tag;
        }
    }
}
