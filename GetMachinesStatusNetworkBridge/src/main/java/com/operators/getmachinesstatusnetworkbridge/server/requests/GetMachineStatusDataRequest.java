package com.operators.getmachinesstatusnetworkbridge.server.requests;


import com.google.gson.annotations.SerializedName;

public class GetMachineStatusDataRequest
{
    @SerializedName("SessionId")
    private String mSessionId;
    @SerializedName("MachineId")
    private String mMachineId;

    public GetMachineStatusDataRequest(String sessionId, String machineId)
    {
        mSessionId = sessionId;
        mMachineId = machineId;
    }

}
