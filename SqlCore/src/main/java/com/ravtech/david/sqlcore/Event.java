package com.ravtech.david.sqlcore;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class Event extends DataSupport {

    @SerializedName("priority")
    private int mPriority;
    @SerializedName("EventTime")
    @Column()
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
    @Column(unique = true)
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
    @SerializedName("EventReasonID")
    private int mEventReasonID;

    private long mTimeOfAdded;

    private boolean mTreated;

    private boolean mIsDismiss;

    public Event() {
    }

    public Event(int mEventID) {

        this.mEventID = mEventID ;

    }

    public Event(String title, String eTitle, String lTitle, String subETitle, String time,
                 String endTime, int groupId, int id, int priority, String groupEname, String groupLname, int duration,
                 boolean treated, float alarmValue, float alarmHValue, float alarmLValue, float alarmStandardValue) {

        mEventTitle = title;
        mEventETitle = eTitle;
        mEventLTitle = lTitle;
        mEventSubTitleEname = subETitle;
        mEventTime = time;
        mEventEndTime = endTime;
        mEventGroupID = groupId;
        mEventID = id;
        mPriority = priority;
        mEventGroupEname = groupEname;
        mEventGroupLname = groupLname;
        mEventDuration = duration;
        mTreated = treated;
        mAlarmValue = alarmValue;
        mAlarmHValue = alarmHValue;
        mAlarmLValue = alarmLValue;
        mAlarmStandardValue = alarmStandardValue;


    }

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

    public String getEventETitle() {
        return mEventETitle;
    }

    public String getEventLTitle() {
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

    public void setDuration(long eventDuration) {
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

    public String getEventGroupEname() {
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

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public void setEventTime(String eventTime) {
        mEventTime = eventTime;
    }

    public void setEventTitle(String eventTitle) {
        mEventTitle = eventTitle;
    }

    public void setEventSubTitleEname(String eventSubTitleEname) {
        mEventSubTitleEname = eventSubTitleEname;
    }

    public void setEventSubTitleLname(String eventSubTitleLname) {
        mEventSubTitleLname = eventSubTitleLname;
    }

    public String getEventTime() {
        return mEventTime;
    }

    public int getEventReasonID() {
        return mEventReasonID;
    }

    public String getEventTitle() {
        return mEventTitle;
    }

    public String getEventSubTitleEname() {
        return mEventSubTitleEname;
    }

    public String getEventSubTitleLname() {
        return mEventSubTitleLname;
    }

    public long getEventDuration() {
        return mEventDuration;
    }

    public void setEventEndTime(String eventEndTime) {
        mEventEndTime = eventEndTime;
    }

    public void setEventGroupID(int eventGroupID) {
        mEventGroupID = eventGroupID;
    }

    public void setEventGroupLname(String eventGroupLname) {
        mEventGroupLname = eventGroupLname;
    }

    public void setEventID(int eventID) {
        mEventID = eventID;
    }

    public void setAlarmHValue(float alarmHValue) {
        mAlarmHValue = alarmHValue;
    }

    public void setAlarmLValue(float alarmLValue) {
        mAlarmLValue = alarmLValue;
    }

    public void setAlarmStandardValue(float alarmStandardValue) {
        mAlarmStandardValue = alarmStandardValue;
    }

    public void setEventReasonID(int mEventReasonID) {
        this.mEventReasonID = mEventReasonID;
    }

    public void setAlarmValue(float alarmValue) {
        mAlarmValue = alarmValue;
    }

    public Event(String mEventTitle, String mEventSubTitleEname, String mEventTime, String mEventEndTime, int mEventGroupID, int mEventID) {
        this.mEventTitle = mEventTitle;
        this.mEventSubTitleEname = mEventSubTitleEname;
        this.mEventTime = mEventTime;
        this.mEventEndTime = mEventEndTime;
        this.mEventGroupID = mEventGroupID;
        this.mEventID = mEventID;
    }

    public void setmEventGroupEname(String mEventGroupEname) {
        this.mEventGroupEname = mEventGroupEname;
    }

    public void setmEventETitle(String mEventETitle) {
        this.mEventETitle = mEventETitle;
    }

    public void setmEventLTitle(String mEventLTitle) {
        this.mEventLTitle = mEventLTitle;
    }
}
