package com.operators.jobsnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class GetJobsListForMachineDataRequest {


    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private int mMachineId;

    public GetJobsListForMachineDataRequest(String sessionId, int machineId) {
        mSessionId = sessionId;
        mMachineId = machineId;
    }


}
