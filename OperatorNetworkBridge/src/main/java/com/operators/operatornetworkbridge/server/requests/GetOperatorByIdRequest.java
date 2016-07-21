package com.operators.operatornetworkbridge.server.requests;


import com.google.gson.annotations.SerializedName;

public class GetOperatorByIdRequest {
    @SerializedName("SessionId")
    private String mSessionId;
    @SerializedName("operatorId")
    private String mOperatorId;

    public GetOperatorByIdRequest(String sessionId, String operatorId) {
        mSessionId = sessionId;
        mOperatorId = operatorId;
    }
}
