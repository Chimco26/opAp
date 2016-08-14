package com.operators.activejobslistformachinenetworkbridge.interfaces;

import com.operators.activejobslistformachinenetworkbridge.server.requests.GetActiveJobsListForMachineRequest;
import com.operators.activejobslistformachinenetworkbridge.server.responses.GetActiveJobsListForMachineResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface EmeraldGetActiveJobsListForMachineServiceRequests {
    @POST("/LeaderMESApi/GetActiveJobsListForMachine")
    Call<GetActiveJobsListForMachineResponse> getActiveJobsForMachine(@Body GetActiveJobsListForMachineRequest getActiveJobsListForMachineRequest);
}
