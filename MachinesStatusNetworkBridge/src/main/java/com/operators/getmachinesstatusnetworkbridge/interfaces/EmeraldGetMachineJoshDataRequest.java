package com.operators.getmachinesstatusnetworkbridge.interfaces;

import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.request.MachineJoshDataRequest;
import com.operators.getmachinesstatusnetworkbridge.server.requests.SetProductionModeForMachineRequest;
import com.operators.reportrejectnetworkbridge.server.response.ResponseStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetMachineJoshDataRequest {
    @POST("/LeaderMESApi/GetMachineJoshData")
    Call<MachineJoshDataResponse> getMachineJoshData(@Body MachineJoshDataRequest request);
}
