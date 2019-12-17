package com.operators.reportrejectnetworkbridge.server.request;

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
    @SerializedName("ByRootEvent")
    private boolean mByRootEvent;
    @SerializedName("OnlyUnReported")
    private boolean mOnlyUnReported;

    public SendMultipleStopRequest(String mSessionId, String mMachineId, String mOperatorId, int mStopReasonId, int mStopSubReasonId, Integer mJoshId, long[] mEventID, boolean mByRootEvent, boolean mOnlyUnReported) {
        this.mSessionId = mSessionId;
        this.mMachineId = mMachineId;
        this.mOperatorId = mOperatorId;
        this.mStopReasonId = mStopReasonId;
        this.mStopSubReasonId = mStopSubReasonId;
        this.mJoshId = mJoshId;
        this.mEventID = mEventID;
        this.mByRootEvent = mByRootEvent;
        this.mOnlyUnReported = mOnlyUnReported;
    }
}
