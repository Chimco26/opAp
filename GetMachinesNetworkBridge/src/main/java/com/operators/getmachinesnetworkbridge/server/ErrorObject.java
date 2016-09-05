package com.operators.getmachinesnetworkbridge.server;

import com.operators.errorobject.ErrorObjectInterface;

public class ErrorObject implements ErrorObjectInterface {
    private ErrorCode mError;
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
