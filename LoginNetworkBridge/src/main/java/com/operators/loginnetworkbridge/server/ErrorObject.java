package com.operators.loginnetworkbridge.server;

import com.example.common.callback.ErrorObjectInterface;

import okhttp3.internal.http2.ErrorCode;

public class ErrorObject implements ErrorObjectInterface {
    private ErrorObjectInterface.ErrorCode mError;
    private String mDetailedDescription;

    public ErrorObject(ErrorCode errorCode, String detailedDescription) {
        mError = errorCode;
        mDetailedDescription = detailedDescription;
    }

    @Override
    public ErrorCode getError() {
        return mError;
    }

    @Override
    public String getDetailedDescription() {
        return mDetailedDescription;
    }


    @Override
    public String toString() {
        return "ErrorObject{" +
                "mError=" + mError +
                ", mDetailedDescription='" + mDetailedDescription + '\'' +
                '}';
    }
}
