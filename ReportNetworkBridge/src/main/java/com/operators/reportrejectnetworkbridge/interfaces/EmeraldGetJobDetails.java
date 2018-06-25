package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetJobDetails {

    @POST("/LeaderMESApi/GetPendingJobDetails")
    Call<JobDetailsResponse> getPendingJobListRequest(@Body JobDetailsRequest jobDetailsRequest);
}
