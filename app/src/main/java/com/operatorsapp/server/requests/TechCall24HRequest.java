package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

public class TechCall24HRequest {

    public TechCall24HRequest(String sessionID, String machineId) {
        this.sessionID = sessionID;
        this.machineId = machineId;
    }

    @SerializedName("SessionID")
    private String sessionID;

    @SerializedName("sourceMachineID")
    private String machineId;
}
