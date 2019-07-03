package com.operators.shiftloginfra.model;

import com.example.common.ErrorResponse;
import com.google.gson.annotations.SerializedName;

public class ShiftForMachineResponse {

    @SerializedName("Duration")
    private long mDuration;
    @SerializedName("EndTime")
    private String mEndTime;
    @SerializedName("StartTime")
    private String mStartTime;
    @SerializedName("TimeFormat")
    private String mTimeFormat;
    @SerializedName("error")
    private ErrorResponse mError;


    public long getDuration() {
        return mDuration;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public String getTimeFormat() {
        return mTimeFormat;
    }

    public ErrorResponse getError() {
        return mError;
    }
}
