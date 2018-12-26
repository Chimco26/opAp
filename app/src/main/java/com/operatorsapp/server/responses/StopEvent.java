package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 20/12/2018.
 */

class StopEvent {

    @SerializedName("Name")
    String mName;

    @SerializedName("Color")
    String mColor;

    @SerializedName("Duration")
    String mDuration;

    @SerializedName("MachineID")
    String mMachineID;

    @SerializedName("MachineName")
    String mMachineName;

    @SerializedName("AffectMachineNames")
    String mAffectMachineNames;

    public StopEvent(String mName, String mDuration, String mColor, String mMachineID, String mMachineName, String mAffectMachineNames) {
        this.mName = mName;
        this.mDuration = mDuration;
        this.mColor = mColor;
        this.mMachineID = mMachineID;
        this.mMachineName = mMachineName;
        this.mAffectMachineNames = mAffectMachineNames;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmMachineID() {
        return mMachineID;
    }

    public void setmMachineID(String mMachineID) {
        this.mMachineID = mMachineID;
    }

    public String getmMachineName() {
        return mMachineName;
    }

    public void setmMachineName(String mMachineName) {
        this.mMachineName = mMachineName;
    }

    public String getmAffectMachineNames() {
        return mAffectMachineNames;
    }

    public void setmAffectMachineNames(String mAffectMachineNames) {
        this.mAffectMachineNames = mAffectMachineNames;
    }
}
