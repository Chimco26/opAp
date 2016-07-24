package com.operators.operatornetworkbridge.server.requests;

import com.google.gson.annotations.SerializedName;


public class SetOperatorForMachineRequest {
    @SerializedName("SessionId")
    private String mSessionId;
    @SerializedName("MachineId")
    private String mMachineId;
    @SerializedName("OperatorId")
    private String mOperatorId;

    public SetOperatorForMachineRequest(String sessionId, String machineId, String operatorId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mOperatorId = operatorId;
    }
}
