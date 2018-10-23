package com.operators.infra;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Machine implements Parcelable {
    @SerializedName("DefaultControllerFieldName")
    private String mDefaultControllerFieldName;
    @SerializedName("Department")
    private int mDepartment;
    @SerializedName("DisplayOrder")
    private int mDisplayOrder;
    @SerializedName("Id")
    private int mId;
    @SerializedName("MachineEName")
    private String mMachineEName;
    @SerializedName("MachineLName")
    private String mMachineLName;
    @SerializedName("MachineName")
    private String mMachineName;
    @SerializedName("MachineStatus")
    private int mMachineStatus;

    public String getDefaultControllerFieldName() {
        return mDefaultControllerFieldName;
    }

    public int getDepartment() {
        return mDepartment;
    }

    public int getDisplayOrder() {
        return mDisplayOrder;
    }

    public int getId() {
        return mId;
    }

    public String getMachineEName()
    {
        return mMachineEName;
    }

    public String getMachineLName() {
        return mMachineLName;
    }

    public String getMachineName() {
        return mMachineName;
    }

    public int getMachineStatus() {
        return mMachineStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDefaultControllerFieldName);
        dest.writeInt(this.mDepartment);
        dest.writeInt(this.mDisplayOrder);
        dest.writeInt(this.mId);
        dest.writeString(this.mMachineEName);
        dest.writeString(this.mMachineLName);
        dest.writeString(this.mMachineName);
        dest.writeInt(this.mMachineStatus);
    }

    public Machine() {
    }

    protected Machine(Parcel in) {
        this.mDefaultControllerFieldName = in.readString();
        this.mDepartment = in.readInt();
        this.mDisplayOrder = in.readInt();
        this.mId = in.readInt();
        this.mMachineEName = in.readString();
        this.mMachineLName = in.readString();
        this.mMachineName = in.readString();
        this.mMachineStatus = in.readInt();
    }

    public static final Parcelable.Creator<Machine> CREATOR = new Parcelable.Creator<Machine>() {
        @Override
        public Machine createFromParcel(Parcel source) {
            return new Machine(source);
        }

        @Override
        public Machine[] newArray(int size) {
            return new Machine[size];
        }
    };
}
