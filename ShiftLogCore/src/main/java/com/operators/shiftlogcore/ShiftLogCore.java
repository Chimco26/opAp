package com.operators.shiftlogcore;

import com.operators.shiftlogcore.interfaces.ShiftLogPersistenceManagerInterface;
import com.operators.shiftlogcore.interfaces.ShiftLogUICallback;
import com.operators.shiftloginfra.ErrorObjectInterface;
import com.operators.shiftloginfra.ShiftLog;
import com.operators.shiftloginfra.ShiftLogCoreCallback;
import com.operators.shiftloginfra.ShiftLogNetworkBridgeInterface;

import java.util.ArrayList;
import java.util.Stack;

public class ShiftLogCore {

    private static ShiftLogCore msInstance;
    private ShiftLogPersistenceManagerInterface mShiftLogPersistenceManagerInterface;
    private ShiftLogNetworkBridgeInterface mShiftLogNetworkBridgeInterface;
    private ArrayList<ShiftLog> mShiftLogs;

    public static ShiftLogCore getInstance() {
        if (msInstance == null) {
            msInstance = new ShiftLogCore();
        }
        return msInstance;
    }

    public void getShiftLogs(String siteUrl, final String sessionId, int machineId, String startingFrom, final ShiftLogUICallback shiftLogUICallback) {

        mShiftLogNetworkBridgeInterface.getShiftLog(siteUrl, sessionId, machineId, startingFrom, new ShiftLogCoreCallback<ShiftLog>() {
            @Override
            public void onShiftLogSucceeded(ArrayList<ShiftLog> shiftLogs) {

                for (ShiftLog shiftLog : shiftLogs) {
                    shiftLog.setDialogShown(false);
                    shiftLog.setTimeOfAdded(System.currentTimeMillis());
                }
                if (mShiftLogs == null) {
                    mShiftLogs = new ArrayList<>();
                }
                mShiftLogs.addAll(shiftLogs);
                shiftLogUICallback.onGetShiftLogSucceeded(mShiftLogs);
            }

            @Override
            public void onShiftLogFailed(ErrorObjectInterface reason) {
                shiftLogUICallback.onGetShiftLogFailed(reason);
            }
        }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout());
    }


    public void setShiftLogDialogStatus(Stack<ShiftLog> shiftLogs) {
        mShiftLogs.clear();
        mShiftLogs.addAll(shiftLogs);
    }

    public void inject(ShiftLogPersistenceManagerInterface shiftLogPersistenceManagerInterface, ShiftLogNetworkBridgeInterface shiftLogNetworkBridgeInterface) {
        mShiftLogPersistenceManagerInterface = shiftLogPersistenceManagerInterface;
        mShiftLogNetworkBridgeInterface = shiftLogNetworkBridgeInterface;
    }


}
