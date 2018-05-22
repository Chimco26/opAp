package com.operators.reportrejectnetworkbridge.server.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse implements Parcelable {
    @SerializedName("ErrorDescription")
    private String mErrorDesc;

    @SerializedName("ErrorMessage")
    private String mErrorMessage;

    @SerializedName("ErrorCode")
    private int mErrorCode;

    @SerializedName("ErrorLine")
    private int mErrorLine;

    @SerializedName("ErrorFunction")
    private int mErrorFunction;

    public String getErrorDesc() {
        return mErrorDesc;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mErrorDesc);
        dest.writeString(this.mErrorMessage);
        dest.writeInt(this.mErrorCode);
        dest.writeInt(this.mErrorLine);
        dest.writeInt(this.mErrorFunction);
    }

    public ErrorResponse() {
    }

    protected ErrorResponse(Parcel in) {
        this.mErrorDesc = in.readString();
        this.mErrorMessage = in.readString();
        this.mErrorCode = in.readInt();
        this.mErrorLine = in.readInt();
        this.mErrorFunction = in.readInt();
    }

    public static final Parcelable.Creator<ErrorResponse> CREATOR = new Parcelable.Creator<ErrorResponse>() {
        @Override
        public ErrorResponse createFromParcel(Parcel source) {
            return new ErrorResponse(source);
        }

        @Override
        public ErrorResponse[] newArray(int size) {
            return new ErrorResponse[size];
        }
    };
}
