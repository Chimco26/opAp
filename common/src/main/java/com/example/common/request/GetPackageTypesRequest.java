package com.example.common.request;

import com.google.gson.annotations.SerializedName;

public class GetPackageTypesRequest {

    @SerializedName("SessionID")
    private String sessionID;

    @SerializedName("MachineID")
    private long machineID;

    @SerializedName("JobID")
    private long jobId;

    public GetPackageTypesRequest(String sessionID, long machineID, long jobId) {
        this.sessionID = sessionID;
        this.machineID = machineID;
        this.jobId = jobId;
    }
}
