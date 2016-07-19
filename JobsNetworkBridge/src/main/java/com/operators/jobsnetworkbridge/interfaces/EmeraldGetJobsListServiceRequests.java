package com.operators.jobsnetworkbridge.interfaces;

import com.operators.jobsnetworkbridge.server.requests.GetJobsListForMachineDataRequest;
import com.operators.jobsnetworkbridge.server.responses.JobsListForMachineResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by User on 19/07/2016.
 */
public interface EmeraldGetJobsListServiceRequests {
    @POST("/LeaderMESApi/GetJobsListForMachine")
    Call<JobsListForMachineResponse> getJobsForMachine(@Body GetJobsListForMachineDataRequest getJobsListForMachineDataRequest);
}
