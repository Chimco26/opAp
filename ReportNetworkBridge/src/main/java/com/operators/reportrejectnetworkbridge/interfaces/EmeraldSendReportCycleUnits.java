package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.ReportFixUnitsProducedRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportCycleUnitsRequest;
import com.example.common.StandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface EmeraldSendReportCycleUnits {
    @POST("/LeaderMESApi/ReportCycleUnits")
    Call<StandardResponse> sendReportCycleUnits(@Body SendReportCycleUnitsRequest reportCycleUnitsRequest);

    @POST("/LeaderMESApi/ReportFixUnits")
    Call<StandardResponse> reportFixUnitsProduced(@Body ReportFixUnitsProducedRequest reportFixUnitsProducedRequest);
}
