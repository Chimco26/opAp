package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SplitEventRequest;
import com.example.common.StandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alex on 05/07/2018.
 */

public interface EmeraldPostSplitEvent {

    @POST("/LeaderMESApi/SplitActiveEvent")
    Call<StandardResponse> postSplitEvent(@Body SplitEventRequest splitEventRequest);
}
