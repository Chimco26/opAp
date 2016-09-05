package com.operators.getmachinesstatusnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class GetMachineStatusDataRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private int mMachineId;

    public GetMachineStatusDataRequest(String sessionId, int machineId) {
        mSessionId = sessionId;
        mMachineId = machineId;
    }

}
