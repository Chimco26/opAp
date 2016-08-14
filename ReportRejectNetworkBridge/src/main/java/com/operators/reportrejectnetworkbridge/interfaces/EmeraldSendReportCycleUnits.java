package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SendReportCycleUnitsRequest;
import com.operators.reportrejectnetworkbridge.server.response.SendReportCycleUnitsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface EmeraldSendReportCycleUnits {
    @POST("/LeaderMESApi/ReportCycleUnits")
    Call<SendReportCycleUnitsResponse> sendReportCycleUnits(@Body SendReportCycleUnitsRequest reportCycleUnitsRequest);
}
