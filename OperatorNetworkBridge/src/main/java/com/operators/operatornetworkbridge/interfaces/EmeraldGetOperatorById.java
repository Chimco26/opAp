package com.operators.operatornetworkbridge.interfaces;

import com.operators.operatornetworkbridge.server.requests.GetOperatorByIdRequest;
import com.operators.operatornetworkbridge.server.responses.OperatorDataResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetOperatorById {
    @POST("/LeaderMESApi/GetOperatorById")
    Call<OperatorDataResponse> getOperator(@Body GetOperatorByIdRequest getOperatorByIdRequest);
}
