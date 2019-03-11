package com.example.common.actualBarExtraResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkingEvent implements Parcelable {

    @SerializedName("StartTime")
    @Expose
    private String startTime;
    @SerializedName("EndTime")
    @Expose
    private String endTime;
    @SerializedName("Duration")
    @Expose
    private Integer duration;
    @SerializedName("EventDistributionID")
    @Expose
    private Integer eventDistributionID;
    @SerializedName("EventReason")
    @Expose
    private String eventReason;
    @SerializedName("EventReasonID")
    @Expose
    private Integer eventReasonID;
    @SerializedName("EventGroup")
    @Expose
    private String eventGroup;
    @SerializedName("EventGroupID")
    @Expose
    private Integer eventGroupID;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Color")
    @Expose
    private String color;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getEventDistributionID() {
        return eventDistributionID;
    }

    public void setEventDistributionID(Integer eventDistributionID) {
        this.eventDistributionID = eventDistributionID;
    }

    public String getEventReason() {
        return eventReason;
    }

    public void setEventReason(String eventReason) {
        this.eventReason = eventReason;
    }

    public Integer getEventReasonID() {
        return eventReasonID;
    }

    public void setEventReasonID(Integer eventReasonID) {
        this.eventReasonID = eventReasonID;
    }

    public String getEventGroup() {
        return eventGroup;
    }

    public void setEventGroup(String eventGroup) {
        this.eventGroup = eventGroup;
    }

    public Integer getEventGroupID() {
        return eventGroupID;
    }

    public void setEventGroupID(Integer eventGroupID) {
        this.eventGroupID = eventGroupID;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeValue(this.duration);
        dest.writeValue(this.eventDistributionID);
        dest.writeString(this.eventReason);
        dest.writeValue(this.eventReasonID);
        dest.writeString(this.eventGroup);
        dest.writeValue(this.eventGroupID);
        dest.writeValue(this.iD);
        dest.writeString(this.name);
        dest.writeString(this.color);
    }

    public WorkingEvent() {
    }

    protected WorkingEvent(Parcel in) {
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.duration = (Integer) in.readValue(Integer.class.getClassLoader());
        this.eventDistributionID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.eventReason = in.readString();
        this.eventReasonID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.eventGroup = in.readString();
        this.eventGroupID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.iD = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.color = in.readString();
    }

    public static final Parcelable.Creator<WorkingEvent> CREATOR = new Parcelable.Creator<WorkingEvent>() {
        @Override
        public WorkingEvent createFromParcel(Parcel source) {
            return new WorkingEvent(source);
        }

        @Override
        public WorkingEvent[] newArray(int size) {
            return new WorkingEvent[size];
        }
    };
}
