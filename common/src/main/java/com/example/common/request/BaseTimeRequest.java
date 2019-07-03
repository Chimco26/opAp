package com.example.common.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseTimeRequest {

    @SerializedName("")
    @Expose
    private String sessionID;
    @SerializedName("StartTime")
    @Expose
    private String startTime;
    @SerializedName("EndTime")
    @Expose
    private String endTime;
    @SerializedName("MachineID")
    @Expose
    private int machineId;

    public BaseTimeRequest(String sessionId, String startTime, String endTime, int machineId) {
        this.sessionID = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.machineId = machineId;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
