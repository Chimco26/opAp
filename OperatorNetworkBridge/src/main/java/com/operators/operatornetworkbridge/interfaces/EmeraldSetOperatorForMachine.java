package com.operators.operatornetworkbridge.interfaces;

import com.operators.operatornetworkbridge.server.requests.SetOperatorForMachineRequest;
import com.operators.operatornetworkbridge.server.responses.SetOperatorForMachineResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldSetOperatorForMachine {
    @POST("/LeaderMESApi/SetOperatorForMachine")
    Call<ResponseBody> setOperatorForMachine(@Body SetOperatorForMachineRequest setOperatorForMachineRequest);
}
