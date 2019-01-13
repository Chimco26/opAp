package com.operators.reportfieldsformachineinfra;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 02/08/2016.
 */
public class RejectReasons implements Parcelable {

    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String EName;
    @SerializedName("LName")
    private String LName;
    @SerializedName("SubReason")
    private SubReasons[] subReasons;

    public int getId() {
        return id;
    }

    public String getEName() {
        return EName;
    }

    public String getLName()
    {
        return LName;
    }

    public SubReasons[] getSubReasons()
    {
        return subReasons;
    }

    public RejectReasons(int id, String name) {
        this.id = id;
        this.EName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.EName);
        dest.writeString(this.LName);
        dest.writeTypedArray(this.subReasons, flags);
    }

    protected RejectReasons(Parcel in) {
        this.id = in.readInt();
        this.EName = in.readString();
        this.LName = in.readString();
        this.subReasons = in.createTypedArray(SubReasons.CREATOR);
    }

    public static final Parcelable.Creator<RejectReasons> CREATOR = new Parcelable.Creator<RejectReasons>() {
        @Override
        public RejectReasons createFromParcel(Parcel source) {
            return new RejectReasons(source);
        }

        @Override
        public RejectReasons[] newArray(int size) {
            return new RejectReasons[size];
        }
    };
}

