package com.operators.reportrejectnetworkbridge.server.request;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class SendMultipleStopRequest {

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
    @SerializedName("joshID")
    private Integer mJoshId;
    @SerializedName("EventID")
    private long[] mEventID;

    public SendMultipleStopRequest(String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, @Nullable Integer jobId, long[] eventId) {
        mSessionId = sessionId;
        mMachineId = machineId;
        mOperatorId = operatorId;
        mStopReasonId = stopReasonId;
        mStopSubReasonId = stopSubReasonId;
        mJoshId = jobId;
        mEventID = eventId;
    }
}
