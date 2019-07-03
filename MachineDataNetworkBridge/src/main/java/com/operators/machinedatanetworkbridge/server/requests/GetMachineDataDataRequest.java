package com.operators.machinedatanetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class GetMachineDataDataRequest {
    @SerializedName("")
    private String mSessionId;
    @SerializedName("MachineID")
    private int mMachineId;
    @SerializedName("StartingFrom")
    private String mStartingFrom;
    @SerializedName("JobID")
    private String mJobId;

    public GetMachineDataDataRequest(String sessionId, int machineId, String startingFrom, String jobId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mStartingFrom = startingFrom;
        mJobId = jobId;
    }

}
