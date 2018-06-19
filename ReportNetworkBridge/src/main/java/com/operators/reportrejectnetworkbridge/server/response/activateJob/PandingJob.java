package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PandingJob implements Parcelable {


    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Properties")
    @Expose
    private List<Property> properties = null;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.iD);
        dest.writeTypedList(this.properties);
    }

    public PandingJob() {
    }

    protected PandingJob(Parcel in) {
        this.iD = (Integer) in.readValue(Integer.class.getClassLoader());
        this.properties = in.createTypedArrayList(Property.CREATOR);
    }

    public static final Creator<PandingJob> CREATOR = new Creator<PandingJob>() {
        @Override
        public PandingJob createFromParcel(Parcel source) {
            return new PandingJob(source);
        }

        @Override
        public PandingJob[] newArray(int size) {
            return new PandingJob[size];
        }
    };
}
