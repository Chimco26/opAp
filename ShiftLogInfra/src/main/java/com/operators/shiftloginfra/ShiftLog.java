package com.operators.shiftloginfra;

import com.google.gson.annotations.SerializedName;

public class ShiftLog {

    @SerializedName("timestamp")
    private long mTimestamp;
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
    private boolean mDialogShown;

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

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long time) {
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

    public boolean isDialogShown() {
        return mDialogShown;
    }

    public void setDialogShown(boolean dialogShown) {
        this.mDialogShown = dialogShown;
    }
}
