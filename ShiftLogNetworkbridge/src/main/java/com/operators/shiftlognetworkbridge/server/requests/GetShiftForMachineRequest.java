package com.operators.shiftlognetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class GetShiftForMachineRequest {
    @SerializedName("SessionID")
    private String mSessionID;
    @SerializedName("MachineID")
    private int mMachineID;


    public GetShiftForMachineRequest(String sessionID, int machineId) {
        mSessionID = sessionID;
        mMachineID = machineId;
    }
}
