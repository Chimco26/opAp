package com.operators.shiftloginfra;


public interface ShiftLogNetworkBridgeInterface {
    void getShiftLog(String siteUrl, String sessionId, int machineId, String startingFrom, ShiftLogCoreCallback shiftLogCoreCallback, final int totalRetries, int specificRequestTimeout);
}
