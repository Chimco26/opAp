package com.operators.reportfieldsformachineinfra;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Oren on 1/30/2017.
 */
public class Technician implements Parcelable {
    @SerializedName("EName")
    private String mEName;
    @SerializedName("ID")
    private int mID;
    @SerializedName("LName")
    private String mLName;

    public String getEName()
    {
        return mEName;
    }

    public int getID()
    {
        return mID;
    }

    public String getLName()
    {
        return mLName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mEName);
        dest.writeInt(this.mID);
        dest.writeString(this.mLName);
    }

    public Technician() {
    }

    protected Technician(Parcel in) {
        this.mEName = in.readString();
        this.mID = in.readInt();
        this.mLName = in.readString();
    }

    public static final Parcelable.Creator<Technician> CREATOR = new Parcelable.Creator<Technician>() {
        @Override
        public Technician createFromParcel(Parcel source) {
            return new Technician(source);
        }

        @Override
        public Technician[] newArray(int size) {
            return new Technician[size];
        }
    };
}
