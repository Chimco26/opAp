package com.operators.getmachinesstatusnetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 30/07/2018.
 */

public class SetProductionModeForMachineRequest {

    @SerializedName("")
    private String mSessionId;
    @SerializedName("MachineID")
    private int mMachineId;
    @SerializedName("ProductionModeID")
    private int mProductionModeID;

    public SetProductionModeForMachineRequest(String sessionId, int machineId, int ProductionModeID) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mProductionModeID = ProductionModeID;
    }
}
