package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by slava-android on 1/13/2016.
 */
public class SessionId
{
    @SerializedName("session")
    private String mSessionId;

    public String getSessionId()
    {
        return mSessionId;
    }
}
