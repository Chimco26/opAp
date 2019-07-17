package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SendApproveFirstItemRequest;
import com.example.common.StandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface EmeraldSendApproveFirstItem
{
    @POST("/LeaderMESApi/ReportSetupEnd")
    Call<StandardResponse> sendApproveFirstItem(@Body SendApproveFirstItemRequest sendApproveFirstItemRequest);


}
