package com.operators.getmachinesstatusnetworkbridge.interfaces;




import com.operators.getmachinesstatusnetworkbridge.server.requests.GetMachineStatusDataRequest;
import com.operators.getmachinesstatusnetworkbridge.server.responses.MachineStatusDataResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface EmeraldGetMachinesStatusServiceRequest
{
    @POST("/LeaderMESApi/GetMachineData")
    Call<MachineStatusDataResponse> getMachineStatus(@Body GetMachineStatusDataRequest getMachineStatusDataRequest);
}
