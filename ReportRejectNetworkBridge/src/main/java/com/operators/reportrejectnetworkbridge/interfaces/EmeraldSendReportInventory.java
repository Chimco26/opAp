package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.SendReportCycleUnitsRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportInventoryRequest;
import com.operators.reportrejectnetworkbridge.server.response.SendReportCycleUnitsResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportInventoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface EmeraldSendReportInventory {
    @POST("/LeaderMESApi/ReportInventory")
    Call<SendReportInventoryResponse> sendReportInventory(@Body SendReportInventoryRequest reportInventoryRequest);
}
