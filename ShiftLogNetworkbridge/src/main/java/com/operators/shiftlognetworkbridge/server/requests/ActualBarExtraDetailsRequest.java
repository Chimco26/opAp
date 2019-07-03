package com.operators.shiftlognetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

public class ActualBarExtraDetailsRequest {

    @SerializedName("")
    private String mSessionID;
    @SerializedName("StartTime")
    private String StartTime;
    @SerializedName("EndTime")
    private String EndTime;
    @SerializedName("MachineID")
    private String MachineID;
    @SerializedName("IncludeWorkingEvents")
    private boolean isIncludeWorking = true;

    public ActualBarExtraDetailsRequest(String mSessionID, String startTime, String endTime, String machineId) {
        this.mSessionID = mSessionID;
        StartTime = startTime;
        EndTime = endTime;
        MachineID = machineId;
    }
}
