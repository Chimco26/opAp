package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SessionIdModel;
import com.operators.reportrejectnetworkbridge.server.response.IntervalAndTimeOutResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetIntervalAndTimeOut {

    @POST("/LeaderMESApi/GetPollingInterval")
    Call<IntervalAndTimeOutResponse> getIntervalAndTimeOutRequest(@Body SessionIdModel sessionIdModel);
}
