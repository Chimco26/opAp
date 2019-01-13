package com.operators.reportfieldsformachineinfra;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey on 02/08/2016.
 */
public class RejectCauses implements Parcelable {

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

    public RejectCauses() {
    }

    protected RejectCauses(Parcel in) {
        this.id = in.readInt();
        this.EName = in.readString();
        this.LName = in.readString();
        this.subReasons = in.createTypedArray(SubReasons.CREATOR);
    }

    public static final Parcelable.Creator<RejectCauses> CREATOR = new Parcelable.Creator<RejectCauses>() {
        @Override
        public RejectCauses createFromParcel(Parcel source) {
            return new RejectCauses(source);
        }

        @Override
        public RejectCauses[] newArray(int size) {
            return new RejectCauses[size];
        }
    };
}