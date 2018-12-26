package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 20/12/2018.
 */

public class TopRejectReason {

    @SerializedName("Name")
    String mName;

    @SerializedName("Amount")
    String mAmount;

    @SerializedName("MachineID")
    String mMachineID;

    @SerializedName("MachineName")
    String mMachineName;

    @SerializedName("AffectMachineNames")
    String mAffectMachineNames;

    public TopRejectReason(String mName, String mAmount, String mMachineID, String mMachineName, String mAffectMachineNames) {
        this.mName = mName;
        this.mAmount = mAmount;
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

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
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
