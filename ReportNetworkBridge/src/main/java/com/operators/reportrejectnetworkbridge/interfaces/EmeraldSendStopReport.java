package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SendReportRejectRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportStopRequest;
import com.operators.reportrejectnetworkbridge.server.response.SendReportRejectResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportStopResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 09/08/2016.
 */
public interface EmeraldSendStopReport {
    @POST("/LeaderMESApi/ReportStop")
    Call<SendReportStopResponse> sendStopReport(@Body SendReportStopRequest sendReportStopRequest);
}
