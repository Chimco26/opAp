package com.operators.shiftlogcore;

import com.operators.shiftlogcore.interfaces.ShiftLogPersistenceManagerInterface;
import com.operators.shiftloginfra.ErrorObjectInterface;
import com.operators.shiftloginfra.ShiftLog;
import com.operators.shiftloginfra.ShiftLogCoreCallback;
import com.operators.shiftloginfra.ShiftLogNetworkBridgeInterface;

import java.util.ArrayList;

public class ShiftLogCore {

    private static ShiftLogCore msInstance;
    private ShiftLogPersistenceManagerInterface mShiftLogPersistenceManagerInterface;
    private ShiftLogNetworkBridgeInterface mShiftLogNetworkBridgeInterface;
    private ArrayList<ShiftLog> shiftLogs;

    public static ShiftLogCore getInstance() {
        if (msInstance == null) {
            msInstance = new ShiftLogCore();
        }
        return msInstance;
    }

    public void getShiftLog() {

        mShiftLogNetworkBridgeInterface.getShiftLog(new ShiftLogCoreCallback() {
            @Override
            public void onLoginSucceeded(ShiftLog shiftLog) {
                if (shiftLogs == null) {
                    shiftLogs = new ArrayList<>();
                }
                shiftLogs.add(shiftLog);
                mShiftLogPersistenceManagerInterface.saveShiftLogs(shiftLogs);
            }

            @Override
            public void onLoginFailed(ErrorObjectInterface reason) {

            }
        });
    }

    public void setShiftLogs(ArrayList<ShiftLog> shiftLogs) {

        mShiftLogPersistenceManagerInterface.saveShiftLogs(shiftLogs);
    }

    public void inject(ShiftLogPersistenceManagerInterface shiftLogPersistenceManagerInterface, ShiftLogNetworkBridgeInterface shiftLogNetworkBridgeInterface) {
        mShiftLogPersistenceManagerInterface = shiftLogPersistenceManagerInterface;
        mShiftLogNetworkBridgeInterface = shiftLogNetworkBridgeInterface;
    }


}
