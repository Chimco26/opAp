package com.operatorsapp.server.interfaces;


import com.operatorsapp.server.requests.LoginRequest;
import com.operatorsapp.server.responses.SessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldServiceRequests {

    @POST("/LeaderMESApi/JGetUserSessionID")
    Call<SessionResponse> getUserSessionId(@Body LoginRequest loginRequest);

    /*@POST("/LeaderMESApi/JGetMachinesStatus")
    void getFactoryData(@Body BaseRequest baseRequest, Callback<FactoryServerDataResponse> callback);

    @POST("/LeaderMESApi/JGetOEEPPEE")
    void getFactoryHistoryPEE(@Body GetRangeOEEPEE getRangeOEEPEE, Callback<GetOeePeeDataResponse> callback);

    @POST("/LeaderMESApi/JGetMachinesData")
    void getMachinesData(@Body BaseRequest baseRequest, Callback<MachinesParametersBySiteResponse> callback);

    @POST("/LeaderMESApi/GetSiteGeneralData")
    void getSiteGeneralData(@Body BaseRequest baseRequest, Callback<ApiAllDepartmentsResponse> callback);*/
}

