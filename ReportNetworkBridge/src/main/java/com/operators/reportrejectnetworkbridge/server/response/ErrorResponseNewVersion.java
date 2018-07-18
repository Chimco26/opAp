package com.operators.reportrejectnetworkbridge.server.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 17/07/2018.
 */

public class ErrorResponseNewVersion implements Parcelable{

    @SerializedName("FunctionSucceed")
    boolean isFunctionSucceed;

    @SerializedName("LeaderRecordID")
    int mLeaderRecordID;

    @SerializedName("error")
    ErrorResponse mError;

    public ErrorResponseNewVersion(boolean isFunctionSucceed, int mLeaderRecordID, ErrorResponse mError) {
        this.isFunctionSucceed = isFunctionSucceed;
        this.mLeaderRecordID = mLeaderRecordID;
        this.mError = mError;
    }

    public boolean isFunctionSucceed() {
        return isFunctionSucceed;
    }

    public void setFunctionSucceed(boolean functionSucceed) {
        isFunctionSucceed = functionSucceed;
    }

    public int getmLeaderRecordID() {
        return mLeaderRecordID;
    }

    public void setmLeaderRecordID(int mLeaderRecordID) {
        this.mLeaderRecordID = mLeaderRecordID;
    }

    public ErrorResponse getmError() {
        return mError;
    }

    public void setmError(ErrorResponse mError) {
        this.mError = mError;
    }

    protected ErrorResponseNewVersion(Parcel in) {
        isFunctionSucceed = in.readByte() != 0;
        mLeaderRecordID = in.readInt();
        mError = in.readParcelable(ErrorResponse.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isFunctionSucceed ? 1 : 0));
        dest.writeInt(mLeaderRecordID);
        dest.writeParcelable(mError, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ErrorResponseNewVersion> CREATOR = new Creator<ErrorResponseNewVersion>() {
        @Override
        public ErrorResponseNewVersion createFromParcel(Parcel in) {
            return new ErrorResponseNewVersion(in);
        }

        @Override
        public ErrorResponseNewVersion[] newArray(int size) {
            return new ErrorResponseNewVersion[size];
        }
    };
}
