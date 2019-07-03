package com.operators.reportrejectnetworkbridge.server.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionIdModel {

    @SerializedName("")
    @Expose
    private double sessionID;

    public SessionIdModel(double sessionId) {

        sessionID = sessionId;
    }

    public double getSessionID() {
        return sessionID;
    }

    public void setSessionID(double sessionID) {
        this.sessionID = sessionID;
    }

}
