package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.GetPendingJobListRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobStandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetPendingJobList {

    @POST("/LeaderMESApi/GetPendingJobList")
    Call<PendingJobStandardResponse> getPendingJobListRequest(@Body GetPendingJobListRequest getPendingJobListRequest);
}
