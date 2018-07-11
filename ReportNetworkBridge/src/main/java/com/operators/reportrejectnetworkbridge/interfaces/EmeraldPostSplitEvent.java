package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SplitEventRequest;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alex on 05/07/2018.
 */

public interface EmeraldPostSplitEvent {

    @POST("/LeaderMESApi/PostSplitEvent")
    Call<String> postSplitEvent(@Body SplitEventRequest splitEventRequest);
}
