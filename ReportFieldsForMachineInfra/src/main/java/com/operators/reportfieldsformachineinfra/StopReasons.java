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

    @SerializedName("EventGroupColorID")
    private String EventGroupColorID;

    @SerializedName("EventGroupIconID")
    private String EventGroupIconID;

    @SerializedName("EventGroupOpAppDisplayOrder")
    private int EventGroupOpAppDisplayOrder;

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

    public String getEventGroupColorID() {
        return EventGroupColorID;
    }

    public String getEventGroupIconID() {
        return EventGroupIconID;
    }

    public int getEventGroupOpAppDisplayOrder() {
        return EventGroupOpAppDisplayOrder;
    }

    public StopReasons() {
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
        dest.writeString(this.EventGroupColorID);
        dest.writeString(this.EventGroupIconID);
        dest.writeInt(this.EventGroupOpAppDisplayOrder);
        dest.writeTypedList(this.subReasons);
    }

    protected StopReasons(Parcel in) {
        this.id = in.readInt();
        this.EName = in.readString();
        this.LName = in.readString();
        this.EventGroupColorID = in.readString();
        this.EventGroupIconID = in.readString();
        this.EventGroupOpAppDisplayOrder = in.readInt();
        this.subReasons = in.createTypedArrayList(SubReasons.CREATOR);
    }

    public static final Creator<StopReasons> CREATOR = new Creator<StopReasons>() {
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