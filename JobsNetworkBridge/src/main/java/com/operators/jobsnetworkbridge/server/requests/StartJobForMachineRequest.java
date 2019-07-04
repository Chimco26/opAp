package com.operators.jobsnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class StartJobForMachineRequest {
    @SerializedName("")
    private String mSessionId;
    @SerializedName("MachineID")
    private int mMachineId;
    @SerializedName("JobID")
    private int mJobId;

    public StartJobForMachineRequest(String sessionId, int machineId, int jobId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mJobId = jobId;
    }
}
