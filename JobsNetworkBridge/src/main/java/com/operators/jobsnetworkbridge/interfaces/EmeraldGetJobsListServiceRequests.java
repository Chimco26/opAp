package com.operators.jobsnetworkbridge.interfaces;

import com.operators.jobsnetworkbridge.server.requests.GetJobsListForMachineDataRequest;
import com.operators.jobsnetworkbridge.server.responses.JobsListForMachineResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface EmeraldGetJobsListServiceRequests {
    @POST("/LeaderMESApi/GetJobsListForMachine")
    Call<JobsListForMachineResponse> getJobsForMachine(@Body GetJobsListForMachineDataRequest getJobsListForMachineDataRequest);
}
