package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActionsByJob implements Parcelable {

    @SerializedName("JobID")
    @Expose
    private String jobID;
    @SerializedName("OperatorID")
    @Expose
    private String operatorID;
    @SerializedName("Actions")
    @Expose
    private List<Action> actions = null;

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jobID);
        dest.writeString(this.operatorID);
        dest.writeTypedList(this.actions);
    }

    public ActionsByJob() {
    }

    public ActionsByJob(String jobID, String operatorID, List<Action> actions) {
        this.jobID = jobID;
        this.operatorID = operatorID;
        this.actions = actions;
    }

    protected ActionsByJob(Parcel in) {
        this.jobID = in.readString();
        this.operatorID = in.readString();
        this.actions = in.createTypedArrayList(Action.CREATOR);
    }

    public static final Parcelable.Creator<ActionsByJob> CREATOR = new Parcelable.Creator<ActionsByJob>() {
        @Override
        public ActionsByJob createFromParcel(Parcel source) {
            return new ActionsByJob(source);
        }

        @Override
        public ActionsByJob[] newArray(int size) {
            return new ActionsByJob[size];
        }
    };
}
