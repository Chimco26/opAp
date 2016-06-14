package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SessionResponse
{
    @SerializedName("JGetUserSessionIDResult")
    private GetUserSessionIDResult mGetUserSessionIDResult;

    public GetUserSessionIDResult getGetUserSessionIDResult()
    {
        return mGetUserSessionIDResult;
    }

    public class GetUserSessionIDResult extends ErrorBaseRespone
    {
        @SerializedName("session")
        private ArrayList<SessionId> mSessionIds;

        public ArrayList<SessionId> getSessionIds()
        {
            return mSessionIds;
        }
    }

}