package com.operatorsapp.model;

import com.operatorsapp.utils.Consts;

/**
 * Created by alex on 20/11/2018.
 */

public class TechCallInfo {

    private int mResponseType;
    private int mNotificationId;
    private int mTechnicianId;
    private String mName;
    private String mStatus;
    private long mCallTime;

    public TechCallInfo(int mResponseType, String mName, String mStatus, long mCallTime) {
        this.mResponseType = mResponseType;
        this.mName = mName;
        this.mStatus = mStatus;
        this.mCallTime = mCallTime;
    }

    public TechCallInfo(int mResponseType, String mName, String mStatus, long mCallTime, int mNotificationId, int mTechnicianId) {
        this.mResponseType = mResponseType;
        this.mName = mName;
        this.mStatus = mStatus;
        this.mCallTime = mCallTime;
        this.mNotificationId = mNotificationId;
        this.mTechnicianId= mTechnicianId;
    }

    public int getmTechnicianId() {
        return mTechnicianId;
    }

    public void setmTechnicianId(int mTechnicianId) {
        this.mTechnicianId = mTechnicianId;
    }

    public int getmNotificationId() {
        return mNotificationId;
    }

    public void setmNotificationId(int mNotificationId) {
        this.mNotificationId = mNotificationId;
    }

    public int getmResponseType() {
        return mResponseType;
    }

    public void setmResponseType(int mResponseType) {
        this.mResponseType = mResponseType;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public long getmCallTime() {
        return mCallTime;
    }

    public void setmCallTime(long mCallTime) {
        this.mCallTime = mCallTime;
    }

    public boolean isOpenCall(){
        switch (mResponseType){

            case Consts.NOTIFICATION_RESPONSE_TYPE_UNSET:
                return true;
            case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                return true;
            case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                return false;
            case Consts.NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS:
                return false;
            case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                return true;
            case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                return false;
            case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                return false;
            default:return false;
        }
    }


}
