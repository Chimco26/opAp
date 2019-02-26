package com.example.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultipleRejectRequestModel {

    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("Rejects")
    @Expose
    private List<RejectForMultipleRequest> rejects = null;

    public MultipleRejectRequestModel(String sessionID, List<RejectForMultipleRequest> rejects) {
        this.sessionID = sessionID;
        this.rejects = rejects;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public List<RejectForMultipleRequest> getRejects() {
        return rejects;
    }

    public void setRejects(List<RejectForMultipleRequest> rejects) {
        this.rejects = rejects;
    }

}