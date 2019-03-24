package com.example.common.request;

import com.google.gson.annotations.SerializedName;

public class MachineJoshDataRequest {

    @SerializedName("MachineID")
    private int machineID;

    @SerializedName("StartTime")
    private String startTime;

    @SerializedName("SessionID")
    private String sessionID;

    public MachineJoshDataRequest(int machineID, String startTime, String sessionID) {
        this.machineID = machineID;
        this.startTime = startTime;
        this.sessionID = sessionID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}