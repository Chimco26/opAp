package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingJob implements Parcelable{


    public static final String TAG = PendingJob.class.getSimpleName();
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Properties")
    @Expose
    private List<Property> properties = null;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

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

    public PendingJob() {
    }

    protected PendingJob(Parcel in) {
        this.iD = (Integer) in.readValue(Integer.class.getClassLoader());
        this.properties = in.createTypedArrayList(Property.CREATOR);
    }

    public static final Creator<PendingJob> CREATOR = new Creator<PendingJob>() {
        @Override
        public PendingJob createFromParcel(Parcel source) {
            return new PendingJob(source);
        }

        @Override
        public PendingJob[] newArray(int size) {
            return new PendingJob[size];
        }
    };

}
