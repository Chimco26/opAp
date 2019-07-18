package com.operators.reportfieldsformachinenetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 02/08/2016.
 */
public class GetReportFieldsForMachineRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private int mMachineId;

    public GetReportFieldsForMachineRequest(String sessionId, int machineId) {
        mSessionId = sessionId;
        mMachineId = machineId;
    }
}
