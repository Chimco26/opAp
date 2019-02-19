package com.operators.shiftlognetworkbridge.interfaces;

import com.operators.shiftloginfra.model.ActualBarExtraResponse;
import com.operators.shiftlognetworkbridge.server.requests.ActualBarExtraDetailsRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldActualBarExtraDetailsServiceRequest {

    @POST("/LeaderMESApi/GetActualBarExtraDetails")
    Call<ActualBarExtraResponse> getActualBarExtra(@Body ActualBarExtraDetailsRequest actualBarExtraDetailsRequest);

}
