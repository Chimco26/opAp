package com.operators.networkbridge.interfaces;

import com.operators.networkbridge.server.requests.LoginRequest;
import com.operators.networkbridge.server.responses.SessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldServiceRequests {

    @POST("/LeaderMESApi/JGetUserSessionID")
    Call<SessionResponse> getUserSessionId(@Body LoginRequest loginRequest);
}

