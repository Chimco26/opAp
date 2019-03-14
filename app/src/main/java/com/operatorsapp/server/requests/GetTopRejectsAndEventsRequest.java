package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;
import com.operatorsapp.utils.TimeUtils;

/**
 * Created by alex on 20/12/2018.
 */

public class GetTopRejectsAndEventsRequest {

    @SerializedName("MachineID")
    private String[] mMachineID;

    @SerializedName("SessionID")
    private String mSessionId;

    @SerializedName("StartDate")
    private String mStartDate;

    @SerializedName("EndDate")
    private String mEndDate;

    public GetTopRejectsAndEventsRequest(String[] mMachineID, String mSessionId, String mStartDate, String mEndDate) {
        this.mMachineID = mMachineID;
        this.mSessionId = mSessionId;
        this.mEndDate = mEndDate;
        if (mStartDate != null && mStartDate.length() > 2){
            this.mStartDate = mStartDate.substring(0, mStartDate.length() -2) + "00";
        }else {
            this.mStartDate = mStartDate;
        }
    }

    public String[] getmMachineID() {
        return mMachineID;
    }

    public void setmMachineID(String[] mMachineID) {
        this.mMachineID = mMachineID;
    }

    public String getmSessionId() {
        return mSessionId;
    }

    public void setmSessionId(String mSessionId) {
        this.mSessionId = mSessionId;
    }

    public String getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public String getmEndDate() {
        return mEndDate;
    }

    public void setmEndDate(String mEndDate) {
        this.mEndDate = mEndDate;
    }
}
