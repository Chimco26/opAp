package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by slava-android on 1/13/2016.
 */
public class ErrorBaseRespone
{
    @SerializedName("error")
    ErrorResponse mErrorResponse;

    public ErrorResponse getErrorResponse()
    {
        return mErrorResponse;
    }
}
