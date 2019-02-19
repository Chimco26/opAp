package com.operators.shiftlognetworkbridge.interfaces;

import com.operators.shiftloginfra.model.ShiftForMachineResponse;
import com.operators.shiftlognetworkbridge.server.requests.GetShiftForMachineRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldShiftForMachineServiceRequests {

    @POST("/LeaderMESApi/GetShiftForMachine")
    Call<ShiftForMachineResponse> getGetShiftForMachine(@Body GetShiftForMachineRequest getShiftForMachineRequest);

}

