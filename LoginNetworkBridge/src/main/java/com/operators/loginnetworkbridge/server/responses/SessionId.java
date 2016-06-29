package com.operators.loginnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;

public class SessionId {
    @SerializedName("session")
    private String mSessionId;

    public String getSessionId() {
        return mSessionId;
    }
}
