package com.operators.reportrejectnetworkbridge.server.response.activateJob;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponse;

public class Response implements Parcelable {

    @SerializedName("FunctionSucceed")
    @Expose
    private Boolean functionSucceed;
    @SerializedName("error")
    @Expose
    private ErrorResponse error;
    @SerializedName("LeaderRecordID")
    @Expose
    private Integer leaderRecordID;

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    public Boolean getFunctionSucceed() {
        return functionSucceed;
    }

    public void setFunctionSucceed(Boolean functionSucceed) {
        this.functionSucceed = functionSucceed;
    }
    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
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
        dest.writeParcelable(this.error, 0);
        dest.writeValue(this.leaderRecordID);
    }

    public Response() {
    }

    protected Response(Parcel in) {
        this.functionSucceed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.error = in.readParcelable(ErrorResponse.class.getClassLoader());
        this.leaderRecordID = (Integer) in.readValue(Integer.class.getClassLoader());
    }

}
