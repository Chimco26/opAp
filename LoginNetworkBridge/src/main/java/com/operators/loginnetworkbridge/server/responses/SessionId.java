package com.operators.loginnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;

public class SessionId {
    @SerializedName("session")
    private String mSessionId;

    @SerializedName("UserID")
    private int mUserID;

    public String getSessionId() {
        return mSessionId;
    }

    public int getUserID() {
        return mUserID;
    }
}
