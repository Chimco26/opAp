package com.operators.reportrejectnetworkbridge.server.request;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 09/08/2016.
 */
public class SendReportStopRequest {
    @SerializedName("SessionID")
    private String mSessionId;
    @SerializedName("MachineID")
    private String mMachineId;
    @SerializedName("OperatorID")
    private String mOperatorId;
    @SerializedName("StopReasonID")
    private int mStopReasonId;
    @SerializedName("StopSubReasonID")
    private int mStopSubReasonId;
    @SerializedName("JobId")
    private Integer mJobId;




    public SendReportStopRequest(String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, @Nullable Integer jobId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mOperatorId = operatorId;
        mStopReasonId = stopReasonId;
        mStopSubReasonId = stopSubReasonId;
        mJobId = jobId;
    }
}
