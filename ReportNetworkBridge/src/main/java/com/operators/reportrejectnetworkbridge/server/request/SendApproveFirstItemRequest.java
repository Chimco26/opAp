package com.operators.reportrejectnetworkbridge.server.request;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 08/08/2016.
 */
public class SendApproveFirstItemRequest
{
    @SerializedName("")
    private String mSessionId;
    @SerializedName("MachineID")
    private String mMachineId;
    @SerializedName("OperatorID")
    private String mOperatorId;
    @SerializedName("RejectReasonID")
    private int mRejectReasonId;
    @SerializedName("TechnicianUserID")
    private Integer mApprovingTechnicianId;
    @SerializedName("JoshID")
    private Integer mJobId;

    public SendApproveFirstItemRequest(String sessionId, String machineId, String operatorId, int rejectReasonId, Integer approvingTechnicianId, @Nullable Integer jobId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mOperatorId = operatorId;
        mRejectReasonId = rejectReasonId;
        mApprovingTechnicianId = approvingTechnicianId;
        mJobId = jobId;
    }
}
