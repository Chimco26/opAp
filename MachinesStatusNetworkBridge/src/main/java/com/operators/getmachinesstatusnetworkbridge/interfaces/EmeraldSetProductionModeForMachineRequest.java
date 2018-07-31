package com.operators.getmachinesstatusnetworkbridge.interfaces;

import com.operators.getmachinesstatusnetworkbridge.server.requests.SetProductionModeForMachineRequest;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alex on 30/07/2018.
 */

public interface EmeraldSetProductionModeForMachineRequest {
    @POST("/LeaderMESApi/SetProductionModeForMachine")
    Call<ErrorResponseNewVersion> postProductionModeForMachine(@Body SetProductionModeForMachineRequest setProductionModeForMachineRequest);
}
