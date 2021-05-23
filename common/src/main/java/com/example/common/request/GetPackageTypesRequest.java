package com.example.common.request;

import com.google.gson.annotations.SerializedName;

public class GetPackageTypesRequest {

    @SerializedName("SessionID")
    private String sessionID;

    @SerializedName("MachineID")
    private long machineID;

    @SerializedName("JoshID")
    private long joshID;

    public GetPackageTypesRequest(String sessionID, long machineID, long joshID) {
        this.sessionID = sessionID;
        this.machineID = machineID;
        this.joshID = joshID;
    }
}
