package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JobDetailsRequest {

    @SerializedName("")
    @Expose
    private String sessionID;
    @SerializedName("Jobs")
    @Expose
    private List<Integer> jobs = null;

    public JobDetailsRequest(String sessionId, ArrayList<Integer> jobIds) {

        sessionID = sessionId;
        jobs = jobIds;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public List<Integer> getJobs() {
        return jobs;
    }

    public void setJobs(List<Integer> jobs) {
        this.jobs = jobs;
    }

}
