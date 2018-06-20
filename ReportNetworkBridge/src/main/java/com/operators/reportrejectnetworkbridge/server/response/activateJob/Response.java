package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Parcelable {

    @SerializedName("FunctionSucceed")
    @Expose
    private Boolean functionSucceed;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("LeaderRecordID")
    @Expose
    private Integer leaderRecordID;

    public Boolean getFunctionSucceed() {
        return functionSucceed;
    }

    public void setFunctionSucceed(Boolean functionSucceed) {
        this.functionSucceed = functionSucceed;
    }
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getLeaderRecordID() {
        return leaderRecordID;
    }

    public void setLeaderRecordID(Integer leaderRecordID) {
        this.leaderRecordID = leaderRecordID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.functionSucceed);
        dest.writeString(this.error);
        dest.writeValue(this.leaderRecordID);
    }

    public Response() {
    }

    protected Response(Parcel in) {
        this.functionSucceed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.error = in.readString();
        this.leaderRecordID = (Integer) in.readValue(Integer.class.getClassLoader());
    }

}
