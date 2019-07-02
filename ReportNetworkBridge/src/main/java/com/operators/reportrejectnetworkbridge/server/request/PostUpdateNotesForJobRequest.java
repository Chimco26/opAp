package com.operators.reportrejectnetworkbridge.server.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 01/08/2018.
 */

public class PostUpdateNotesForJobRequest {


    @SerializedName("")
    @Expose
    private String sessionID;
    @SerializedName("jobID")
    @Expose
    private int jobId;
    @SerializedName("notes")
    @Expose
    private String notes;


    public PostUpdateNotesForJobRequest(String sessionID, int jobId, String notes) {
        this.sessionID = sessionID;
        this.jobId = jobId;
        this.notes = notes;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
