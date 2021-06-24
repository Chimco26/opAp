package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivateJobRequest implements Parcelable {


    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("MachineID")
    @Expose
    private String machineID;
    @SerializedName("JobID")
    @Expose
    private String jobID;
    @SerializedName("ERPJobID")
    @Expose
    private String eRPJobID;
    @SerializedName("WorkerID")
    @Expose
    private String workerID;
    @SerializedName("EndSetup")
    @Expose
    private boolean isToEndSetup;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getWorkerID() {
        return workerID;
    }

    public void setWorkerID(String workerID) {
        this.workerID = workerID;
    }

    public boolean isToEndSetup() {
        return isToEndSetup;
    }

    public void setToEndSetup(boolean toEndSetup) {
        isToEndSetup = toEndSetup;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.sessionID);
        dest.writeValue(this.machineID);
        dest.writeValue(this.jobID);
        dest.writeValue(this.eRPJobID);
        dest.writeString(this.workerID);
        dest.writeByte(this.isToEndSetup ? (byte) 1 : (byte) 0);
    }

    public ActivateJobRequest() {
    }

    public ActivateJobRequest(String sessionID, String machineID, String jobID, String workerID, boolean isToEndSetup) {
        this.sessionID = sessionID;
        this.machineID = machineID;
        this.jobID = jobID;
        this.workerID = workerID;
        this.isToEndSetup = isToEndSetup;
    }

    public ActivateJobRequest(String sessionID, String machineID, String jobID, String eRPJobID, String workerID, boolean isToEndSetup) {
        this.sessionID = sessionID;
        this.machineID = machineID;
        this.jobID = jobID;
        this.eRPJobID = eRPJobID;
        this.workerID = workerID;
        this.isToEndSetup = isToEndSetup;
    }

    protected ActivateJobRequest(Parcel in) {
        this.sessionID = in.readString();
        this.machineID = in.readString();
        this.jobID = in.readString();
        this.eRPJobID = in.readString();
        this.workerID = in.readString();
        this.isToEndSetup = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ActivateJobRequest> CREATOR = new Parcelable.Creator<ActivateJobRequest>() {
        @Override
        public ActivateJobRequest createFromParcel(Parcel source) {
            return new ActivateJobRequest(source);
        }

        @Override
        public ActivateJobRequest[] newArray(int size) {
            return new ActivateJobRequest[size];
        }
    };
}
