package com.operators.reportfieldsformachineinfra;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 02/08/2016.
 */
public class StopReasons implements Parcelable {

    @SerializedName("ID")
    private int id;
    @SerializedName("EName")
    private String EName;
    @SerializedName("LName")
    private String LName;
    @SerializedName("SubReason")
    private List<SubReasons> subReasons = new ArrayList<SubReasons>();

    public int getId() {
        return id;
    }

    public String getEName() {
        return EName;
    }

    public List<SubReasons> getSubReasons() {
        return subReasons;
    }

    public String getLName()
    {
        return LName;
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
        dest.writeList(this.subReasons);
    }

    public StopReasons() {
    }

    protected StopReasons(Parcel in) {
        this.id = in.readInt();
        this.EName = in.readString();
        this.LName = in.readString();
        this.subReasons = new ArrayList<SubReasons>();
        in.readList(this.subReasons, SubReasons.class.getClassLoader());
    }

    public static final Parcelable.Creator<StopReasons> CREATOR = new Parcelable.Creator<StopReasons>() {
        @Override
        public StopReasons createFromParcel(Parcel source) {
            return new StopReasons(source);
        }

        @Override
        public StopReasons[] newArray(int size) {
            return new StopReasons[size];
        }
    };
}