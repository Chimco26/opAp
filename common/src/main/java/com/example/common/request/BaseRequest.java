package com.example.common.request;

import com.google.gson.annotations.SerializedName;

public class BaseRequest {

    @SerializedName("")
    private String sessionId;

    public BaseRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
