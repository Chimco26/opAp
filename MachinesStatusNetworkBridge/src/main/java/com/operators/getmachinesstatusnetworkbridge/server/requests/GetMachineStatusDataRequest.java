package com.operators.getmachinesstatusnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class GetMachineStatusDataRequest {
    @SerializedName("")
    private String mSessionId;
    @SerializedName("MachineID")
    private int mMachineId;
    @SerializedName("JobID")
    private int mJobId;

    public GetMachineStatusDataRequest(String sessionId, int machineId, Integer joshID) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mJobId = joshID;
    }

}
