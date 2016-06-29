package com.operators.getmachinesnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class GetMachinesRequest {
    @SerializedName("SessionID")
    private String mSessionID;

    public GetMachinesRequest(String sessionID) {
        mSessionID = sessionID;
    }
}
