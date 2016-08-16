package com.operators.shiftloginfra;

import com.google.gson.annotations.SerializedName;

public class Event {

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
    private long mEventDuration;
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

    public String getTitle() {
        return mEventTitle;
    }

    public String getSubtitleE() {
        return mEventSubTitleEname;
    }

    public String getSubtitleL() {
        return mEventSubTitleLname;
    }

    public String getTime() {
        return mEventTime;
    }

    public String getStartTime() {
        return mEventEndTime /*mEventStartTime*/;
    }

    public String getEndTime() {
        return mEventEndTime;
    }

    public long getDuration() {
        return mEventDuration;
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

    public int getEventGroupID() {
        return mEventGroupID;
    }

    public String getEventGroupLname() {
        return mEventGroupLname;
    }

    public int getEventID() {
        return mEventID;
    }

}
