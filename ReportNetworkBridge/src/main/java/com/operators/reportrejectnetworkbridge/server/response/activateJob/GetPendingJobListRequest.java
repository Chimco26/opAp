package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPendingJobListRequest {

    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("MachineID")
    @Expose
    private Integer machineID;

    public GetPendingJobListRequest(String sessionId, int machineId) {

        sessionID = sessionId;
        machineID = machineId;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getMachineID() {
        return machineID;
    }

    public void setMachineID(Integer machineID) {
        this.machineID = machineID;
    }

}
