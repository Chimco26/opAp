package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;
import com.operatorsapp.utils.Consts;

/**
 * Created by alex on 10/01/2019.
 */

public class PostDeleteTokenRequest {

    @SerializedName("MachineID")
    private int mMachineID;

    @SerializedName("SessionID")
    private String mSessionId;

    @SerializedName("ApplicationCode")
    private int mApplicationCode;

    @SerializedName("Token")
    private String mToken;

    public PostDeleteTokenRequest(int mMachineID, String mSessionId, String mToken) {
        this.mMachineID = mMachineID;
        this.mSessionId = mSessionId;
        this.mApplicationCode = Consts.APPLICATION_CODE;
        this.mToken = mToken;
    }

    public int getmMachineID() {
        return mMachineID;
    }

    public void setmMachineID(int mMachineID) {
        this.mMachineID = mMachineID;
    }

    public String getmSessionId() {
        return mSessionId;
    }

    public void setmSessionId(String mSessionId) {
        this.mSessionId = mSessionId;
    }

    public int getmApplicationCode() {
        return mApplicationCode;
    }

    public void setmApplicationCode(int mApplicationCode) {
        this.mApplicationCode = mApplicationCode;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }
}
