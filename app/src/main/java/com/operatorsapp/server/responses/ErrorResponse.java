package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Slava on 1/29/2015.
 */
public class ErrorResponse
{
    @SerializedName("ErrorDescription")
    private String mErrorDesc;

    @SerializedName("ErrorMessage")
    private String mErrorMessage;

    @SerializedName("ErrorCode")
    private int mErrorCode;

    public String getErrorDesc()
    {
        return mErrorDesc;
    }

    public void setErrorDesc(String errorDesc)
    {
        mErrorDesc = errorDesc;
    }

    public String getErrorMessage()
    {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        mErrorMessage = errorMessage;
    }

    public int getErrorCode()
    {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode)
    {
        mErrorCode = errorCode;
    }
}
