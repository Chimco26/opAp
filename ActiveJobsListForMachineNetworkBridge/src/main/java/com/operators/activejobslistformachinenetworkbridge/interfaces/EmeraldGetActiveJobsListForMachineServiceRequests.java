package com.operators.activejobslistformachinenetworkbridge.interfaces;

import com.operators.activejobslistformachinenetworkbridge.server.requests.GetActiveJobsListForMachineRequest;
import com.operators.activejobslistformachinenetworkbridge.server.responses.ActiveJobsListForMachineResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface EmeraldGetActiveJobsListForMachineServiceRequests {
    @POST("/LeaderMESApi/GetActiveJoshListForMachine")
    Call<ActiveJobsListForMachineResponse> getActiveJobsForMachine(@Body GetActiveJobsListForMachineRequest getActiveJobsListForMachineRequest);
}
