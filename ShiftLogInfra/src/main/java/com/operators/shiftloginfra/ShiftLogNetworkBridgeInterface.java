package com.operators.shiftloginfra;


public interface ShiftLogNetworkBridgeInterface {
    void getShiftLog(String siteUrl, String sessionId, int machineId, String startingFrom, ShiftLogCoreCallback<Event> shiftLogCoreCallback, final int totalRetries, int specificRequestTimeout);
    void GetShiftForMachine(String siteUrl, String sessionId, int machineId, ShiftForMachineCoreCallback<ShiftForMachineResponse> shiftForMachineCoreCallback, final int totalRetries, int specificRequestTimeout);
}
