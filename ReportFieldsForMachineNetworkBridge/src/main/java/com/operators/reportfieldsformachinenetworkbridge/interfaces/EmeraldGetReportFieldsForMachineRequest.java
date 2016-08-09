package com.operators.reportfieldsformachinenetworkbridge.interfaces;

import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachinenetworkbridge.server.requests.GetReportFieldsForMachineRequest;
import com.operators.reportfieldsformachinenetworkbridge.server.responses.GetReportFieldsForMachineResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sergey on 02/08/2016.
 */
public interface EmeraldGetReportFieldsForMachineRequest {
    @POST("/LeaderMESApi/getReportfieldsForMachine")
    Call<GetReportFieldsForMachineResponse> getReportFieldsForMachine(@Body GetReportFieldsForMachineRequest getReportFieldsForMachineRequest);

}
