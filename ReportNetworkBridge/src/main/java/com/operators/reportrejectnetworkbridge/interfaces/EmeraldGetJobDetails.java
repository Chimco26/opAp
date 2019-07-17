package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsStandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetJobDetails {

    @POST("/LeaderMESApi/GetPendingJobDetails")
    Call<JobDetailsStandardResponse> getPendingJobListRequest(@Body JobDetailsRequest jobDetailsRequest);
}
