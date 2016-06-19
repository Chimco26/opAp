package com.operatorsapp.server.interfaces;


import com.operatorsapp.server.requests.LoginRequest;
import com.operatorsapp.server.responses.SessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldServiceRequests {

    @POST("/LeaderMESApi/JGetUserSessionID")
    Call<SessionResponse> getUserSessionId(@Body LoginRequest loginRequest);
}

