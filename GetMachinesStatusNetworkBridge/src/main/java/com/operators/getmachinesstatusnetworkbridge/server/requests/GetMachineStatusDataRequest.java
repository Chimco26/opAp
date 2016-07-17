package com.operators.getmachinesstatusnetworkbridge.server.requests;


import com.google.gson.annotations.SerializedName;

public class GetMachineStatusDataRequest
{
    @SerializedName("SessionId")
    private String mSessionId;
    @SerializedName("MachineId")
    private int mMachineId;

    public GetMachineStatusDataRequest(String sessionId, int machineId)
    {
        mSessionId = sessionId;
        mMachineId = machineId;
    }

}
