package com.operators.shiftloginfra;

import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("priority")
    private int mPriority;
    @SerializedName("EventTime")
    private String mEventTime;
    @SerializedName("EventTitle")
    private String mEventTitle;
    @SerializedName("EventETitle")
    private String mEventETitle;
    @SerializedName("EventLTitle")
    private String mEventLTitle;
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
    @SerializedName("EventGroupEname")
    private String mEventGroupEname;
    @SerializedName("EventID")
    private int mEventID;
    @SerializedName("AlarmDismissed")
    private boolean mAlarmDismissed;
    @SerializedName("AlarmHValue")
    private float mAlarmHValue;
    @SerializedName("AlarmLValue")
    private float mAlarmLValue;
    @SerializedName("AlarmStandardValue")
    private float mAlarmStandardValue;
    @SerializedName("AlarmValue")
    private float mAlarmValue;

    private long mTimeOfAdded;

    private boolean mTreated;

    private boolean mIsDismiss;

    public boolean isIsDismiss() {
        return mIsDismiss;
    }

    public void setIsDismiss(boolean mIsDismiss) {
        this.mIsDismiss = mIsDismiss;
    }

    public int getPriority() {
        return mPriority;
    }

    public String getTitle() {
        return mEventTitle;
    }

    public String getEventETitle()
    {
        return mEventETitle;
    }

    public String getEventLTitle()
    {
        return mEventLTitle;
    }

    public String getSubtitleEname() {
        return mEventSubTitleEname;
    }

    public String getSubtitleLname() {
        return mEventSubTitleLname;
    }

    public String getTime() {
        return mEventTime;
    }

    public String getEventEndTime() {
        return mEventEndTime;
    }

    public long getDuration() {
        return mEventDuration;
    }

    public void setDuration(long eventDuration)
    {
        mEventDuration = eventDuration;
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

    public String getEventGroupEname()
    {
        return mEventGroupEname;
    }

    public int getEventID() {
        return mEventID;
    }

    public boolean isAlarmDismissed() {
        return mAlarmDismissed;
    }

    public void setAlarmDismissed(boolean mAlarmDismissed) {
        this.mAlarmDismissed = mAlarmDismissed;
    }

    public float getAlarmHValue() {
        return mAlarmHValue;
    }

    public float getAlarmLValue() {
        return mAlarmLValue;
    }

    public float getAlarmStandardValue() {
        return mAlarmStandardValue;
    }

    public float getAlarmValue() {
        return mAlarmValue;
    }

    public void setPriority(int priority)
    {
        mPriority = priority;
    }

    public void setEventTime(String eventTime)
    {
        mEventTime = eventTime;
    }

    public void setEventTitle(String eventTitle)
    {
        mEventTitle = eventTitle;
    }

    public void setEventSubTitleEname(String eventSubTitleEname)
    {
        mEventSubTitleEname = eventSubTitleEname;
    }

    public void setEventSubTitleLname(String eventSubTitleLname)
    {
        mEventSubTitleLname = eventSubTitleLname;
    }

    public void setEventStartTime(String eventStartTime)
    {
        mEventStartTime = eventStartTime;
    }

    public String getEventTime()
    {
        return mEventTime;
    }

    public String getEventTitle()
    {
        return mEventTitle;
    }

    public String getEventSubTitleEname()
    {
        return mEventSubTitleEname;
    }

    public String getEventSubTitleLname()
    {
        return mEventSubTitleLname;
    }

    public String getEventStartTime()
    {
        return mEventStartTime;
    }

    public long getEventDuration()
    {
        return mEventDuration;
    }

    public void setEventEndTime(String eventEndTime)
    {
        mEventEndTime = eventEndTime;
    }

    public void setEventGroupID(int eventGroupID)
    {
        mEventGroupID = eventGroupID;
    }

    public void setEventGroupLname(String eventGroupLname)
    {
        mEventGroupLname = eventGroupLname;
    }

    public void setEventID(int eventID)
    {
        mEventID = eventID;
    }

    public void setAlarmHValue(float alarmHValue)
    {
        mAlarmHValue = alarmHValue;
    }

    public void setAlarmLValue(float alarmLValue)
    {
        mAlarmLValue = alarmLValue;
    }

    public void setAlarmStandardValue(float alarmStandardValue)
    {
        mAlarmStandardValue = alarmStandardValue;
    }

    public void setAlarmValue(float alarmValue)
    {
        mAlarmValue = alarmValue;
    }

    public Event(String mEventTitle, String mEventSubTitleEname, String mEventStartTime, String mEventEndTime, int mEventGroupID, int mEventID)
    {
        this.mEventTitle = mEventTitle;
        this.mEventSubTitleEname = mEventSubTitleEname;
        this.mEventStartTime = mEventStartTime;
        this.mEventEndTime = mEventEndTime;
        this.mEventGroupID = mEventGroupID;
        this.mEventID = mEventID;
    }
}
