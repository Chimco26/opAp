package com.operators.shiftlognetworkbridge;

import com.operators.shiftloginfra.ShiftLogCoreCallback;
import com.operators.shiftloginfra.ShiftLogNetworkBridgeInterface;
import com.operators.shiftlognetworkbridge.interfaces.ShiftLogNetworkManagerInterface;

/**
 * Created by Admin on 19-Jul-16.
 */
public class ShiftLogNetworkBridge implements ShiftLogNetworkBridgeInterface {

    private ShiftLogNetworkManagerInterface mShiftLogNetworkManagerInterface;

    public void inject(ShiftLogNetworkManagerInterface shiftLogNetworkManagerInterface) {
        mShiftLogNetworkManagerInterface = shiftLogNetworkManagerInterface;
    }

    @Override
    public void getShiftLog(ShiftLogCoreCallback shiftLogCoreCallback) {

    }
}
