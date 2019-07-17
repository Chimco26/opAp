package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.example.common.StandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldPostActivateJob {

    @POST("/LeaderMESApi/ActivateJobForMachine")
    Call<StandardResponse> postActivateJobRequest(@Body ActivateJobRequest activateJobRequest);
}
