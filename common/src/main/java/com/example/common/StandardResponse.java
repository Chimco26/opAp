package com.example.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.common.callback.ErrorObjectInterface;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StandardResponse implements Parcelable {

    @SerializedName("FunctionSucceed")
    @Expose
    private Boolean isSucceed;
    @SerializedName("error")
    @Expose
    protected ErrorResponse error;
    @SerializedName("LeaderRecordID")
    @Expose
    private Integer leaderRecordID;

    public StandardResponse(boolean isSucceed, int leaderRecordID, ErrorResponse error) {
        this.error = error;
        this.leaderRecordID = leaderRecordID;
        this.isSucceed = isSucceed;
    }

    public StandardResponse(ErrorObjectInterface.ErrorCode enumCode, String desc) {
        if (error == null){
            error = new ErrorResponse();
        }
        this.error.setErrorCodeConstant(enumCode);
        this.getError().setErrorDesc(desc);
    }

    public Boolean getSucceed() {
        return isSucceed;
    }

    public Boolean getFunctionSucceed() {
        return isSucceed;
    }

    public void setFunctionSucceed(Boolean functionSucceed) {
        this.isSucceed = functionSucceed;
    }
    public ErrorResponse getError() {
        if (error == null){
            error = new ErrorResponse();
        }
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

    public StandardResponse() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.isSucceed);
        dest.writeParcelable(this.error, flags);
        dest.writeValue(this.leaderRecordID);
    }

    protected StandardResponse(Parcel in) {
        this.isSucceed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.error = in.readParcelable(ErrorResponse.class.getClassLoader());
        this.leaderRecordID = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<StandardResponse> CREATOR = new Creator<StandardResponse>() {
        @Override
        public StandardResponse createFromParcel(Parcel source) {
            return new StandardResponse(source);
        }

        @Override
        public StandardResponse[] newArray(int size) {
            return new StandardResponse[size];
        }
    };
}
