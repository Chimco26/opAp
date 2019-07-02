package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionsUpdateRequest implements Parcelable {

    @SerializedName("")
    @Expose
    private String sessionID;
    @SerializedName("Actions")
    @Expose
    private ActionsByJob actions;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public ActionsByJob getActions() {
        return actions;
    }

    public void setActions(ActionsByJob actions) {
        this.actions = actions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.sessionID);
        dest.writeParcelable(this.actions, flags);
    }

    public ActionsUpdateRequest() {
    }

    public ActionsUpdateRequest(String sessionID, ActionsByJob actions) {
        this.sessionID = sessionID;
        this.actions = actions;
    }

    protected ActionsUpdateRequest(Parcel in) {
        this.sessionID = in.readString();
        this.actions = in.readParcelable(ActionsByJob.class.getClassLoader());
    }

    public static final Parcelable.Creator<ActionsUpdateRequest> CREATOR = new Parcelable.Creator<ActionsUpdateRequest>() {
        @Override
        public ActionsUpdateRequest createFromParcel(Parcel source) {
            return new ActionsUpdateRequest(source);
        }

        @Override
        public ActionsUpdateRequest[] newArray(int size) {
            return new ActionsUpdateRequest[size];
        }
    };
}
