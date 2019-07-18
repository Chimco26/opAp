package com.operators.reportrejectnetworkbridge.interfaces;

import com.example.common.MultipleRejectRequestModel;
import com.example.common.StandardResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldReportMultipleRejects {
    @POST("/LeaderMESApi/ReportMultiRejects")
    Call<StandardResponse> reportMultipleRejects(@Body MultipleRejectRequestModel multipleRejectRequestModel);
}