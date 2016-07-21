package com.operators.shiftloginfra;

import com.google.gson.annotations.SerializedName;

public class ShiftLog {

    @SerializedName("timestamp")
    private String mTimestamp;
    @SerializedName("type")
    private int mType;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("subtitle")
    private String mSubtitle;
    @SerializedName("priority")
    private int mPriority;
    @SerializedName("startTime")
    private String mStartTime;
    @SerializedName("endTime")
    private String mEndTime;
    @SerializedName("duration")
    private int mDuration;
    private long mTimeOfAdded;

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    public void setSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String time) {
        this.mTimestamp = time;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public long getTimeOfAdded() {
        return mTimeOfAdded;
    }

    public void setTimeOfAdded(long timeOfAdded) {
        this.mTimeOfAdded = timeOfAdded;
    }
}
