package com.operators.machinedatanetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class GetMachineDataDataRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private int mMachineId;
    @SerializedName("StartingFrom")
    private String mStartingFrom;

    public GetMachineDataDataRequest(String sessionId, int machineId, String startingFrom) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mStartingFrom = startingFrom;
    }

}
