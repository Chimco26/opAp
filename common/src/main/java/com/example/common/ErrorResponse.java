package com.example.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.common.callback.ErrorObjectInterface;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse implements Parcelable, ErrorObjectInterface {
    public static final int CREDENTIAL_MISMATCH_CODE = 101;
    @SerializedName("ErrorDescription")
    private String mErrorDesc;

    @SerializedName("ErrorMessage")
    private String mErrorMessage;

    @SerializedName("ErrorCode")
    private int mErrorCode;


    private ErrorCode errorCodeConstant;

    public String getErrorDesc() {
        return mErrorDesc;
    }

    public void setErrorDesc(String mErrorDesc) {
        this.mErrorDesc = mErrorDesc;
    }

    public ErrorCode getErrorCodeConstant() {
        return errorCodeConstant;
    }

    public void setErrorCodeConstant(ErrorCode errorCodeConstant) {
        this.errorCodeConstant = errorCodeConstant;
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
    }

    public ErrorResponse() {
    }

    protected ErrorResponse(Parcel in) {
        this.mErrorDesc = in.readString();
        this.mErrorMessage = in.readString();
        this.mErrorCode = in.readInt();
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

    public void setDefaultErrorCodeConstant(int errorCode) {
        this.mErrorCode = errorCode;
        switch (errorCode) {
            case CREDENTIAL_MISMATCH_CODE:
                this.errorCodeConstant = ErrorResponse.ErrorCode.Credentials_mismatch;
            case 0:
                this.errorCodeConstant = ErrorResponse.ErrorCode.No_data;
            case 500:
                this.errorCodeConstant = ErrorResponse.ErrorCode.Server;
        }
        this.errorCodeConstant = ErrorResponse.ErrorCode.Unknown;
    }
}
