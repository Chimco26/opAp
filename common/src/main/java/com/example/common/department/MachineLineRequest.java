package com.example.common.department;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineLineRequest {
    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("LineID")
    @Expose
    private Integer lineID;

    public MachineLineRequest(String sessionID, Integer lineID) {
        this.sessionID = sessionID;
        this.lineID = lineID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getLineID() {
        return lineID;
    }

    public void setLineID(Integer lineID) {
        this.lineID = lineID;
    }
}
