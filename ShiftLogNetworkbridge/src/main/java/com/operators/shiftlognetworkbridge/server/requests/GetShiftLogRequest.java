package com.operators.shiftlognetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class GetShiftLogRequest {
    @SerializedName("SessionID")
    private String mSessionID;
    @SerializedName("MachineID")
    private int mMachineID;
    @SerializedName("StartingFrom")
    private String mStartingFrom;

    public GetShiftLogRequest(String sessionID, int machineId, String startingFrom) {
        mSessionID = sessionID;
        mMachineID = machineId;
        mStartingFrom = startingFrom;
    }
}
