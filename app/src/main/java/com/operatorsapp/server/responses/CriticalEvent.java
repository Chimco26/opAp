package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 20/12/2018.
 */

public class CriticalEvent {

    @SerializedName("CriticalEvents")
    String mCriticalEvents;

    @SerializedName("Duration")
    String mDuration;

    @SerializedName("EventsCount")
    String mEventsCount;

    @SerializedName("MachineID")
    String mMachineID;

    @SerializedName("Name")
    String mName;

    @SerializedName("PercentageDuration")
    String mPercentageDuration;

    @SerializedName("TotalDuration")
    String mTotalDuration;


    public CriticalEvent(String mCriticalEvents, String mDuration, String mEventsCount, String mMachineID, String mName, String mPercentageDuration, String mTotalDuration) {
        this.mCriticalEvents = mCriticalEvents;
        this.mDuration = mDuration;
        this.mEventsCount = mEventsCount;
        this.mMachineID = mMachineID;
        this.mName = mName;
        this.mPercentageDuration = mPercentageDuration;
        this.mTotalDuration = mTotalDuration;
    }

    public String getmCriticalEvents() {
        return mCriticalEvents;
    }

    public void setmCriticalEvents(String mCriticalEvents) {
        this.mCriticalEvents = mCriticalEvents;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmEventsCount() {
        return mEventsCount;
    }

    public void setmEventsCount(String mEventsCount) {
        this.mEventsCount = mEventsCount;
    }

    public String getmMachineID() {
        return mMachineID;
    }

    public void setmMachineID(String mMachineID) {
        this.mMachineID = mMachineID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPercentageDuration() {
        return mPercentageDuration;
    }

    public void setmPercentageDuration(String mPercentageDuration) {
        this.mPercentageDuration = mPercentageDuration;
    }

    public String getmTotalDuration() {
        return mTotalDuration;
    }

    public void setmTotalDuration(String mTotalDuration) {
        this.mTotalDuration = mTotalDuration;
    }
}
