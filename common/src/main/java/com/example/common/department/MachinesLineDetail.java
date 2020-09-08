package com.example.common.department;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachinesLineDetail implements Parcelable {

    @SerializedName("CurrentStatusTimeMin")
    @Expose
    private Integer currentStatusTimeMin;
    @SerializedName("JobColor")
    @Expose
    private String jobColor;
    @SerializedName("JobColoredShadow")
    @Expose
    private Boolean jobColoredShadow;
    @SerializedName("MachineID")
    @Expose
    private Integer machineID;
    @SerializedName("MachineName")
    @Expose
    private String machineName;
    @SerializedName("MachineStatusID")
    @Expose
    private Integer machineStatusID;
    @SerializedName("MachineStatusName")
    @Expose
    private String machineStatusName;
    @SerializedName("Row_Counter")
    @Expose
    private Integer rowCounter;
    @SerializedName("MachineStatusColor")
    @Expose
    private String statusColor;
    @SerializedName("RootStop")
    @Expose
    private boolean rootStop;

    public Integer getCurrentStatusTimeMin() {
        return currentStatusTimeMin;
    }

    public void setCurrentStatusTimeMin(Integer currentStatusTimeMin) {
        this.currentStatusTimeMin = currentStatusTimeMin;
    }

    public String getJobColor() {
        return jobColor;
    }

    public void setJobColor(String jobColor) {
        this.jobColor = jobColor;
    }

    public Boolean getJobColoredShadow() {
        return jobColoredShadow;
    }

    public void setJobColoredShadow(Boolean jobColoredShadow) {
        this.jobColoredShadow = jobColoredShadow;
    }

    public Integer getMachineID() {
        return machineID;
    }

    public void setMachineID(Integer machineID) {
        this.machineID = machineID;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Integer getMachineStatusID() {
        return machineStatusID;
    }

    public void setMachineStatusID(Integer machineStatusID) {
        this.machineStatusID = machineStatusID;
    }

    public String getMachineStatusName() {
        return machineStatusName;
    }

    public void setMachineStatusName(String machineStatusName) {
        this.machineStatusName = machineStatusName;
    }

    public Integer getRowCounter() {
        return rowCounter;
    }

    public void setRowCounter(Integer rowCounter) {
        this.rowCounter = rowCounter;
    }

    public String getStatusColor() {
        if (statusColor != null && !statusColor.isEmpty())
            return statusColor;
        else return "#ffffff";
    }

    public boolean isRootStop() {
        return rootStop;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.currentStatusTimeMin);
        dest.writeString(this.jobColor);
        dest.writeValue(this.jobColoredShadow);
        dest.writeValue(this.machineID);
        dest.writeString(this.machineName);
        dest.writeValue(this.machineStatusID);
        dest.writeString(this.machineStatusName);
        dest.writeValue(this.rowCounter);
        dest.writeString(this.statusColor);
        dest.writeByte(this.rootStop ? (byte) 1 : (byte) 0);
    }

    public MachinesLineDetail() {
    }

    protected MachinesLineDetail(Parcel in) {
        this.currentStatusTimeMin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobColor = in.readString();
        this.jobColoredShadow = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.machineID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.machineName = in.readString();
        this.machineStatusID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.machineStatusName = in.readString();
        this.rowCounter = (Integer) in.readValue(Integer.class.getClassLoader());
        this.statusColor = in.readString();
        this.rootStop = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MachinesLineDetail> CREATOR = new Parcelable.Creator<MachinesLineDetail>() {
        @Override
        public MachinesLineDetail createFromParcel(Parcel source) {
            return new MachinesLineDetail(source);
        }

        @Override
        public MachinesLineDetail[] newArray(int size) {
            return new MachinesLineDetail[size];
        }
    };
}
