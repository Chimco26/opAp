package com.operators.reportrejectnetworkbridge;


import com.operators.errorobject.ErrorObjectInterface;

public class ErrorObject implements ErrorObjectInterface {
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
