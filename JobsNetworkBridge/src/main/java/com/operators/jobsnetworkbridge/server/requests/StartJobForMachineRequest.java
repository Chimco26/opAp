package com.operators.jobsnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class StartJobForMachineRequest {
    @SerializedName("SessionId")
    private String mSessionId;
    @SerializedName("MachineId")
    private int mMachineId;
    @SerializedName("JobId")
    private int mJobId;

    public StartJobForMachineRequest(String sessionId, int machineId, int jobId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mJobId = jobId;
    }
}
