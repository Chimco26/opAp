package com.operators.loginnetworkbridge.server.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SessionResponse {
    @SerializedName("JGetUserSessionIDResult")
    private UserSessionIDResult mUserSessionIDResult;

    public UserSessionIDResult getUserSessionIDResult() {
        return mUserSessionIDResult;
    }

    public class UserSessionIDResult extends ErrorBaseRespone {
        @SerializedName("session")
        private ArrayList<SessionId> mSessionIds;

        public ArrayList<SessionId> getSessionIds() {
            return mSessionIds;
        }
    }

}