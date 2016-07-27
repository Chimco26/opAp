package com.operators.shiftloginfra;

import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("type")
    private int mType;
    @SerializedName("priority")
    private int mPriority;
    @SerializedName("EventTime")
    private String mEventTime;
    @SerializedName("EventTitle")
    private String mEventTitle;
    @SerializedName("EventSubTitleEname")
    private String mEventSubTitleEname;
    @SerializedName("EventSubTitleLname")
    private String mEventSubTitleLname;
    @SerializedName("EventStartTime")
    private String mEventStartTime;
    @SerializedName("EventEndTime")
    private String mEventEndTime;
    @SerializedName("EventDuration")
    private int mEventDuration;
    @SerializedName("EventGroupID")
    private int mEventGroupID;
    @SerializedName("EventGroupLname")
    private String mEventGroupLname;
    @SerializedName("EventID")
    private int mEventID;

    private long mTimeOfAdded;

    private boolean mTreated;

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    public String getTitle() {
        return mEventTitle;
    }

    public void setTitle(String title) {
        this.mEventTitle = title;
    }

    public String getSubtitleE() {
        return mEventSubTitleEname;
    }

    public void setSubtitleE(String subtitleE) {
        this.mEventSubTitleEname = subtitleE;
    }

    public String getSubtitleL() {
        return mEventSubTitleLname;
    }

    public void setSubtitleL(String subtitleE) {
        this.mEventSubTitleLname = subtitleE;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getTime() {
        return mEventTime;
    }

    public void setTime(String time) {
        this.mEventTime = time;
    }

    public String getStartTime() {
        return mEventEndTime /*mEventStartTime*/;
    }

    public void setStartTime(String startTime) {
        this.mEventStartTime = startTime;
    }

    public String getEndTime() {
        return mEventEndTime;
    }

    public void setEndTime(String endTime) {
        this.mEventEndTime = endTime;
    }

    public int getDuration() {
        return mEventDuration;
    }

    public void setDuration(int duration) {
        this.mEventDuration = duration;
    }

    public long getTimeOfAdded() {
        return mTimeOfAdded;
    }

    public void setTimeOfAdded(long timeOfAdded) {
        this.mTimeOfAdded = timeOfAdded;
    }

    public boolean isTreated() {
        return mTreated;
    }

    public void setTreated(boolean treated) {
        this.mTreated = treated;
    }
}
