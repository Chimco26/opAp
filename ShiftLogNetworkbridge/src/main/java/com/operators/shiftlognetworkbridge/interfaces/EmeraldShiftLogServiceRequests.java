package com.operators.shiftlognetworkbridge.interfaces;

import com.operators.shiftlognetworkbridge.server.requests.GetShiftLogRequest;
import com.operators.shiftlognetworkbridge.server.responses.ShiftLogResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldShiftLogServiceRequests {

    @POST("/LeaderMESApi/GetMachineShiftLog")
    Call<ShiftLogResponse> getMachineShiftLog(@Body GetShiftLogRequest getShiftLogRequest);

}

