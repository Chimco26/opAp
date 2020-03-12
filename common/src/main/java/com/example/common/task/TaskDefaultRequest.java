package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskDefaultRequest {
    @SerializedName("SourceTaskCreationPlatform")
    @Expose
    private Integer sourceTaskCreationPlatform = 3;
    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("MachineID")
    @Expose
    private int machineId;

    public TaskDefaultRequest(String sessionID, int machineId) {
        this.sessionID = sessionID;
        this.machineId = machineId;
    }

    public Integer getSourceTaskCreationPlatform() {
        return sourceTaskCreationPlatform;
    }

    public void setSourceTaskCreationPlatform(Integer sourceTaskCreationPlatform) {
        this.sourceTaskCreationPlatform = sourceTaskCreationPlatform;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

}
