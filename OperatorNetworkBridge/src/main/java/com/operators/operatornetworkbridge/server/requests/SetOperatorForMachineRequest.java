package com.operators.operatornetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;


public class SetOperatorForMachineRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private String mMachineId;
    @SerializedName("OperatorID")
    private String mOperatorId;

    public SetOperatorForMachineRequest(String sessionId, String machineId, String operatorId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mOperatorId = operatorId;
    }
}
