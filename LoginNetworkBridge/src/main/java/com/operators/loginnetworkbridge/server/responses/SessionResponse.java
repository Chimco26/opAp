package com.operators.loginnetworkbridge.server.responses;

import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SessionResponse extends StandardResponse {
    @SerializedName("JGetUserSessionIDResult")
    private UserSessionIDResult mUserSessionIDResult;

    public UserSessionIDResult getUserSessionIDResult() {
        return mUserSessionIDResult;
    }

    public class UserSessionIDResult extends StandardResponse {
        @SerializedName("session")
        private ArrayList<SessionId> mSessionIds;

        public ArrayList<SessionId> getSessionIds() {
            return mSessionIds;
        }
    }

}