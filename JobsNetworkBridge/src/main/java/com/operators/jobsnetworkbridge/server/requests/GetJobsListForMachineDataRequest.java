package com.operators.jobsnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class GetJobsListForMachineDataRequest {


    @SerializedName("SessionId")
    private String mSessionId;
    @SerializedName("MachineId")
    private int mMachineId;

    public GetJobsListForMachineDataRequest(String sessionId, int machineId) {
        mSessionId = sessionId;
        mMachineId = machineId;
    }


}
