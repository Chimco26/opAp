package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActionsUpdateRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldPostActivateJob {

    @POST("/LeaderMESApi/ActivateJobForMachine")
    Call<Response> postActivateJobRequest(@Body ActivateJobRequest activateJobRequest);
}
