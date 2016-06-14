package com.operatorsapp.server;

public class ErrorObject
{
    private ErrorCode mError;
    private String mDetailedDescription;

    public ErrorObject(ErrorCode errorCode, String detailedDescription)
    {
        mError = errorCode;
        mDetailedDescription = detailedDescription;
    }

    public ErrorCode getError()
    {
        return mError;
    }

    public String getDetailedDescription()
    {
        return mDetailedDescription;
    }

    public enum ErrorCode
    {
        Unknown,
        Retrofit,
        SessionInvalid, Credentials_mismatch,
    }

    @Override
    public String toString()
    {
        return "ErrorObject{" +
                "mError=" + mError +
                ", mDetailedDescription='" + mDetailedDescription + '\'' +
                '}';
    }
}
