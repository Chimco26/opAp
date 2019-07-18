package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SendMultipleStopRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportStopRequest;
import com.example.common.StandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 09/08/2016.
 */
public interface EmeraldSendStopReport {
    @POST("/LeaderMESApi/ReportStop")
    Call<StandardResponse> sendStopReport(@Body SendReportStopRequest sendReportStopRequest);

    @POST("/LeaderMESApi/ReportMultiStopEvents")
    Call<StandardResponse> sendMultipleStopReport(@Body SendMultipleStopRequest sendMultipleStopRequest);
}
