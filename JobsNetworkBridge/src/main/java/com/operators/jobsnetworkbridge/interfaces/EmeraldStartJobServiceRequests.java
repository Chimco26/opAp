package com.operators.jobsnetworkbridge.interfaces;

import com.operators.jobsnetworkbridge.server.requests.StartJobForMachineRequest;
import com.operators.jobsnetworkbridge.server.responses.StartJobForMachineResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface EmeraldStartJobServiceRequests {
    @POST("/LeaderMESApi/StartJobListForMachine")
    Call<StartJobForMachineResponse> startJobForMachine(@Body StartJobForMachineRequest startJobForMachineRequest);
}
