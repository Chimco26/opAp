package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by slava-android on 1/7/2016.
 */
public class BaseRequest
{
    @SerializedName("SessionID")
    private String mSessionId;

    public BaseRequest(String sessionId)
    {
        mSessionId = sessionId;
    }
}
