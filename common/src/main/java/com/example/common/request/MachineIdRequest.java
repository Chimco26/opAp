package com.example.common.request;

import com.google.gson.annotations.SerializedName;

public class MachineIdRequest {
    @SerializedName("MachineID")
    private String machineId;
    @SerializedName("SessionID")
    private String sessionId;

    public MachineIdRequest(String machineId) {
        this.machineId = machineId;
    }

    public MachineIdRequest(String machineId, String sessionId) {
        this.machineId = machineId;
        this.sessionId = sessionId;

    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
