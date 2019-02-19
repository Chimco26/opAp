package com.operators.shiftloginfra;


import com.operators.shiftloginfra.model.ActualBarExtraResponse;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;
import com.ravtech.david.sqlcore.Event;

public interface ShiftLogNetworkBridgeInterface {
    void getShiftLog(String siteUrl, String sessionId, int machineId, String startingFrom, ShiftLogCoreCallback<Event> shiftLogCoreCallback, final int totalRetries, int specificRequestTimeout);
    void GetShiftForMachine(String siteUrl, String sessionId, int machineId, ShiftForMachineCoreCallback<ShiftForMachineResponse> shiftForMachineCoreCallback, final int totalRetries, int specificRequestTimeout);
    void GetActualBarExtraDetails(String siteUrl, String sessionId, String startTime, String endTime, ActualBarExtraDetailsCallback<ActualBarExtraResponse> actualBarExtraResponseActualBarExtraDetailsCallback, final int totalRetries, int specificRequestTimeout);
}
