package com.operators.loginnetworkbridge.interfaces;

import com.operators.loginnetworkbridge.server.requests.LoginRequest;
import com.operators.loginnetworkbridge.server.responses.SessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldLoginServiceRequests {

    @POST("/LeaderMESApi/JGetUserSessionID")
    Call<SessionResponse> getUserSessionId(@Body LoginRequest loginRequest);

}

