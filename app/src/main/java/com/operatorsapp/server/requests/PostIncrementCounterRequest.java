package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 17/12/2018.
 */

public class PostIncrementCounterRequest {

    @SerializedName("MachineID")
    private int mMachineID;

    @SerializedName("")
    private String mSessionId;


    public PostIncrementCounterRequest(int mMachineID, String mSessionId) {
        this.mMachineID = mMachineID;
        this.mSessionId= mSessionId;
    }


    public String getmSessionId() {
        return mSessionId;
    }

    public void setmSessionId(String mSessionId) {
        this.mSessionId = mSessionId;
    }

    public int getmMachineID() {
        return mMachineID;
    }

    public void setmMachineID(int mMachineID) {
        this.mMachineID = mMachineID;
    }
}
