package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SendReportRejectRequest;
import com.operators.reportrejectnetworkbridge.server.response.StandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface EmeraldSendReportReject {

    @POST("/LeaderMESApi/ReportReject")
    Call<StandardResponse> sendReportReject(@Body SendReportRejectRequest sendReportRejectRequest);

}
