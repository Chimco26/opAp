package com.operators.getmachinesnetworkbridge.interfaces;


import com.operators.getmachinesnetworkbridge.server.requests.GetMachinesRequest;
import com.operators.getmachinesnetworkbridge.server.responses.MachinesResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetMachinesServiceRequests {

    @POST("/LeaderMESApi/GetMachinesForFactory")
    Call<MachinesResponse> getMachinesForFactory(@Body GetMachinesRequest getMachinesRequest);
}

