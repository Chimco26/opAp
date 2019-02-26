package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SplitEventRequest;
import com.operators.reportrejectnetworkbridge.server.response.ResponseStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alex on 05/07/2018.
 */

public interface EmeraldPostSplitEvent {

    @POST("/LeaderMESApi/SplitActiveEvent")
    Call<ResponseStatus> postSplitEvent(@Body SplitEventRequest splitEventRequest);
}
