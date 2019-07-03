package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

public class TechnicianNotificationRequest {
    @SerializedName("sourceMachineID")
    private int sourceMachineID;
    @SerializedName("")
    private String sessionID;

    public TechnicianNotificationRequest(int sourceMachineID, String sessionID) {
        this.sourceMachineID = sourceMachineID;
        this.sessionID = sessionID;
    }

}
