package com.operators.shiftlognetworkbridge.interfaces;

import com.operators.shiftloginfra.ShiftForMachineResponse;
import com.operators.shiftlognetworkbridge.server.requests.GetShiftForMachineRequest;
import com.operators.shiftlognetworkbridge.server.requests.GetShiftLogRequest;
import com.operators.shiftlognetworkbridge.server.responses.ShiftLogResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldShiftForMachineServiceRequests {

    @POST("/LeaderMESApi/GetShiftForMachine")
    Call<ShiftForMachineResponse> getGetShiftForMachine(@Body GetShiftForMachineRequest getShiftForMachineRequest);

}

