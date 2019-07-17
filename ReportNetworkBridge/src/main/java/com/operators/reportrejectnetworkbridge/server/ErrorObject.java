package com.operators.reportrejectnetworkbridge.server;


import com.example.common.callback.ErrorObjectInterface;

public class ErrorObject implements ErrorObjectInterface {
    private static final String DEFAULT_ERROR_MSG = "Error";
    private ErrorCode mError;
    private String mDetailedDescription;

    public ErrorObject(ErrorObjectInterface.ErrorCode errorCode, String detailedDescription) {
        mError = errorCode;
        mDetailedDescription = detailedDescription;
    }

    @Override
    public ErrorObjectInterface.ErrorCode getError() {
        return mError;
    }

    @Override
    public String getDetailedDescription() {
        if (mDetailedDescription != null && mDetailedDescription.length() > 0) {
            return mDetailedDescription;
        }else {
            return DEFAULT_ERROR_MSG;
        }
    }

    @Override
    public String toString() {
        return "ErrorObject{" +
                "mError=" + mError +
                ", mDetailedDescription='" + mDetailedDescription + '\'' +
                '}';
    }
}
