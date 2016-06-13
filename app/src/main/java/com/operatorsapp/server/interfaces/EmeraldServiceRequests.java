package com.operatorsapp.server.interfaces;


import com.operatorsapp.server.requests.BaseRequest;
import com.operatorsapp.server.requests.LoginRequest;
import com.operatorsapp.server.responses.FactoryServerDataResponse;
import com.operatorsapp.server.responses.MachinesParametersBySiteResponse;
import com.operatorsapp.server.responses.SessionResponse;

import retrofit.http.Body;
import retrofit.http.POST;
import retrofit2.Callback;

public interface EmeraldServiceRequests {

    @POST("/LeaderMESApi/JGetUserSessionID")
    void getUserSessionId(@Body LoginRequest loginRequest, Callback<SessionResponse> callback);

    @POST("/LeaderMESApi/JGetMachinesStatus")
    void getFactoryData(@Body BaseRequest baseRequest, Callback<FactoryServerDataResponse> callback);

    @POST("/LeaderMESApi/JGetMachinesData")
    void getMachinesData(@Body BaseRequest baseRequest, Callback<MachinesParametersBySiteResponse> callback);
}

