package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SendApproveFirstItemRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportRejectRequest;
import com.operators.reportrejectnetworkbridge.server.response.SendApproveFirstItemResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportRejectResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface EmeraldSendApproveFirstItem
{
    @POST("/LeaderMESApi/ApproveFirstItem") // TODO validate endpoint with client
    Call<SendApproveFirstItemResponse> sendApproveFirstItem(@Body SendApproveFirstItemRequest sendApproveFirstItemRequest);


}
