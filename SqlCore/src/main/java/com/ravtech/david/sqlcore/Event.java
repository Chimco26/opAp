package com.ravtech.david.sqlcore;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class Event extends DataSupport implements Parcelable {

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
    @SerializedName("Color")
    private String color;

    private long mTimeOfAdded;

    private boolean mTreated;

    private boolean mIsDismiss;

    private boolean mChecked;

    private long mEventTimeInMillis;

    public Event() {
    }

    public Event(int mEventID) {

        this.mEventID = mEventID ;

    }

    public Event(String title, String eTitle, String lTitle, String subETitle, String time,
                 String endTime, int groupId, int id, int priority, String groupEname, String groupLname, int duration,
                 boolean treated, float alarmValue, float alarmHValue, float alarmLValue, float alarmStandardValue, int reasonId) {

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
        mEventReasonID = reasonId;

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mPriority);
        dest.writeString(this.mEventTime);
        dest.writeString(this.mEventTitle);
        dest.writeString(this.mEventETitle);
        dest.writeString(this.mEventLTitle);
        dest.writeString(this.mEventSubTitleEname);
        dest.writeString(this.mEventSubTitleLname);
        dest.writeString(this.mEventEndTime);
        dest.writeLong(this.mEventDuration);
        dest.writeInt(this.mEventGroupID);
        dest.writeString(this.mEventGroupLname);
        dest.writeString(this.mEventGroupEname);
        dest.writeInt(this.mEventID);
        dest.writeByte(this.mAlarmDismissed ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.mAlarmHValue);
        dest.writeFloat(this.mAlarmLValue);
        dest.writeFloat(this.mAlarmStandardValue);
        dest.writeFloat(this.mAlarmValue);
        dest.writeInt(this.mEventReasonID);
        dest.writeLong(this.mTimeOfAdded);
        dest.writeByte(this.mTreated ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsDismiss ? (byte) 1 : (byte) 0);
    }

    protected Event(Parcel in) {
        this.mPriority = in.readInt();
        this.mEventTime = in.readString();
        this.mEventTitle = in.readString();
        this.mEventETitle = in.readString();
        this.mEventLTitle = in.readString();
        this.mEventSubTitleEname = in.readString();
        this.mEventSubTitleLname = in.readString();
        this.mEventEndTime = in.readString();
        this.mEventDuration = in.readLong();
        this.mEventGroupID = in.readInt();
        this.mEventGroupLname = in.readString();
        this.mEventGroupEname = in.readString();
        this.mEventID = in.readInt();
        this.mAlarmDismissed = in.readByte() != 0;
        this.mAlarmHValue = in.readFloat();
        this.mAlarmLValue = in.readFloat();
        this.mAlarmStandardValue = in.readFloat();
        this.mAlarmValue = in.readFloat();
        this.mEventReasonID = in.readInt();
        this.mTimeOfAdded = in.readLong();
        this.mTreated = in.readByte() != 0;
        this.mIsDismiss = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setEventTimeInMillis(long eventTimeInMillis) {
        mEventTimeInMillis = eventTimeInMillis;
    }

    public long getEventTimeInMillis() {
        return mEventTimeInMillis;
    }
}
