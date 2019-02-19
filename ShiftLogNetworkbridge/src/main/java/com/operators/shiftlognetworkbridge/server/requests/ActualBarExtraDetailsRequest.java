package com.operators.shiftlognetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class ActualBarExtraDetailsRequest {

    @SerializedName("SessionID")
    private String mSessionID;
    @SerializedName("StartTime")
    private String StartTime;
    @SerializedName("EndTime")
    private String EndTime;

    public ActualBarExtraDetailsRequest(String mSessionID, String startTime, String endTime) {
        this.mSessionID = mSessionID;
        StartTime = startTime;
        EndTime = endTime;
    }
}
