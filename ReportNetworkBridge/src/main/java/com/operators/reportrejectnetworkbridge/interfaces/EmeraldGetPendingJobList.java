package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetPendingJobList {

    @POST("/LeaderMESApi/GetPendingJobList")
    Call<PendingJobResponse> getPendingJobListRequest(@Body ActivateJobRequest activateJobRequest);
}
