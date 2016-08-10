package com.operators.machinedatanetworkbridge.interfaces;



import com.operators.machinedatanetworkbridge.server.requests.GetMachineDataDataRequest;
import com.operators.machinedatanetworkbridge.server.responses.MachineDataDataResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface EmeraldGetMachinesDataServiceRequest {
    @POST("/LeaderMESApi/GetMachineData")
    Call<MachineDataDataResponse> getMachineData(@Body GetMachineDataDataRequest getMachineDataDataRequest);
}
