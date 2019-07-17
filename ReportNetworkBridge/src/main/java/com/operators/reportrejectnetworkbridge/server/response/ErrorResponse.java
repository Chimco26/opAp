package com.operators.reportrejectnetworkbridge.server.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse implements Parcelable {
    private static final String DEFAULT_ERROR_MSG = "Error";
    private static final String DEFAULT_SUCCESS_MSG = "Success";
    @SerializedName("ErrorDescription")
    private String mErrorDesc;

    @SerializedName("ErrorMessage")
    private String mErrorMessage;

    @SerializedName("ErrorCode")
    private int mErrorCode;

    @SerializedName("ErrorLine")
    private int mErrorLine;

    @SerializedName("ErrorFunction")
    private String mErrorFunction;

    public int getmErrorCode() {
        return mErrorCode;
    }

    public int getmErrorLine() {
        return mErrorLine;
    }

    public String getmErrorFunction() {
        return mErrorFunction;
    }

    public String getmErrorMessage() {
        return mErrorMessage;
    }

    public String getErrorDesc() {
        if (mErrorDesc != null && mErrorDesc.length() > 0) {
            return mErrorDesc;
        } else if (getErrorCode() != 0) {
            return DEFAULT_ERROR_MSG;
        } else {
            return DEFAULT_SUCCESS_MSG;
        }

    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setmErrorDesc(String mErrorDesc) {
        this.mErrorDesc = mErrorDesc;
    }

    public void setmErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }

    public void setmErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public void setmErrorLine(int mErrorLine) {
        this.mErrorLine = mErrorLine;
    }

    public void setmErrorFunction(String mErrorFunction) {
        this.mErrorFunction = mErrorFunction;
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
        dest.writeString(this.mErrorFunction);
    }

    public ErrorResponse() {
    }

    protected ErrorResponse(Parcel in) {
        this.mErrorDesc = in.readString();
        this.mErrorMessage = in.readString();
        this.mErrorCode = in.readInt();
        this.mErrorLine = in.readInt();
        this.mErrorFunction = in.readString();
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
