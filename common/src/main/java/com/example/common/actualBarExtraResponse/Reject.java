package com.example.common.actualBarExtraResponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reject implements Parcelable {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("ShiftID")
    @Expose
    private Integer shiftID;
    @SerializedName("EName")
    @Expose
    private String eName;
    @SerializedName("LName")
    @Expose
    private String lName;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("Amount")
    @Expose
    private Float amount;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getShiftID() {
        return shiftID;
    }

    public void setShiftID(Integer shiftID) {
        this.shiftID = shiftID;
    }

    public String getEName() {
        return eName;
    }

    public void setEName(String eName) {
        this.eName = eName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.iD);
        dest.writeValue(this.shiftID);
        dest.writeString(this.eName);
        dest.writeString(this.lName);
        dest.writeString(this.time);
        dest.writeValue(this.amount);
    }

    public Reject() {
    }

    protected Reject(Parcel in) {
        this.iD = (Integer) in.readValue(Integer.class.getClassLoader());
        this.shiftID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.eName = in.readString();
        this.lName = in.readString();
        this.time = in.readString();
        this.amount = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Parcelable.Creator<Reject> CREATOR = new Parcelable.Creator<Reject>() {
        @Override
        public Reject createFromParcel(Parcel source) {
            return new Reject(source);
        }

        @Override
        public Reject[] newArray(int size) {
            return new Reject[size];
        }
    };
}