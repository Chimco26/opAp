package com.operators.reportrejectnetworkbridge.server.request;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 08/08/2016.
 */
public class SendReportRejectRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private String mMachineId;
    @SerializedName("OperatorID")
    private String mOperatorId;
    @SerializedName("RejectReasonID")
    private int mRejectReasonId;
    @SerializedName("RejectCauseID")
    private Integer mRejectCauseId;
    @SerializedName("Units")
    private Double mUnits;
    @SerializedName("Weight")
    private Double mWeight;
    @SerializedName("JoshID")
    private Integer mJobId;

    public SendReportRejectRequest(String sessionId, String machineId, String operatorId, int rejectReasonId, @Nullable Double units, @Nullable Integer rejectCauseId, @Nullable Double weight, @Nullable Integer jobId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mOperatorId = operatorId;
        mRejectReasonId = rejectReasonId;
        mRejectCauseId = rejectCauseId;
        mUnits = units;
        mWeight = weight;
        mJobId = jobId;
    }
}
