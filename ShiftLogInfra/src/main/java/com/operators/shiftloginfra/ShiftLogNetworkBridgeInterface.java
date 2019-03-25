package com.operators.shiftloginfra;


import com.example.common.Event;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.callback.GetMachineJoshDataCallback;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.request.MachineJoshDataRequest;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;

public interface ShiftLogNetworkBridgeInterface {
    void getShiftLog(String siteUrl, String sessionId, int machineId, String startingFrom, ShiftLogCoreCallback<Event> shiftLogCoreCallback, final int totalRetries, int specificRequestTimeout);
    void GetShiftForMachine(String siteUrl, String sessionId, int machineId, ShiftForMachineCoreCallback<ShiftForMachineResponse> shiftForMachineCoreCallback, final int totalRetries, int specificRequestTimeout);
    void GetActualBarExtraDetails(String siteUrl, String sessionId, String startTime, String endTime, String machineId, ActualBarExtraDetailsCallback<ActualBarExtraResponse> actualBarExtraResponseActualBarExtraDetailsCallback, final int totalRetries, int specificRequestTimeout);
    void GetMachineJoshData(String siteUrl, String sessionId, String startTime, String endTime, String machineId, GetMachineJoshDataCallback<MachineJoshDataResponse> actualBarExtraResponseActualBarExtraDetailsCallback, final int totalRetries, int specificRequestTimeout, MachineJoshDataRequest machineJoshDataRequest);
}
