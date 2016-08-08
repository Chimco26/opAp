package com.operators.reportrejectnetworkbridge.server.request;

import android.support.annotation.Nullable;

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
    private double mUnits;
    @SerializedName("Weight")
    private Double mWeight;

    public SendReportRejectRequest(String sessionId, String machineId, String operatorId, int rejectReasonId, double units, @Nullable Integer rejectCauseId, @Nullable Double weight) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mOperatorId = operatorId;
        mRejectReasonId = rejectReasonId;
        mRejectCauseId = rejectCauseId;
        mUnits = units;
        mWeight = weight;
    }
}
