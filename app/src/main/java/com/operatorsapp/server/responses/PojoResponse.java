package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 11/10/2018.
 */

public class PojoResponse {

    @SerializedName("FunctionSucceed")
    boolean isFunctionSucceed;

    @SerializedName("LeaderRecordID")
    int mLeaderRecordID;

    @SerializedName("error")
    ErrorResponse mError;

    public boolean isFunctionSucceed() {
        return isFunctionSucceed;
    }

    public void setFunctionSucceed(boolean functionSucceed) {
        isFunctionSucceed = functionSucceed;
    }

    public int getmLeaderRecordID() {
        return mLeaderRecordID;
    }

    public void setmLeaderRecordID(int mLeaderRecordID) {
        this.mLeaderRecordID = mLeaderRecordID;
    }

    public ErrorResponse getmError() {
        return mError;
    }

    public void setmError(ErrorResponse mError) {
        this.mError = mError;
    }
}
