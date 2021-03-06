package com.operatorsapp.model;

import com.operatorsapp.utils.Consts;

/**
 * Created by alex on 20/11/2018.
 */

public class TechCallInfo {

    private String mEventName;
    private int mEventId;
    private int mResponseType;
    private int mNotificationId;
    private int mTechnicianId;
    private int mMachineId;
    private String mName;
    private String mStatus;
    private String mAdditionalText;
    private long mCallTime;

    public TechCallInfo(int mResponseType, String mName, String mStatus, long mCallTime) {
        this.mResponseType = mResponseType;
        this.mName = mName;
        this.mStatus = mStatus;
        this.mCallTime = mCallTime;
    }

    public TechCallInfo(int machineId, int mResponseType, String mName, String mStatus, String additionalText,
                        long mCallTime, int mNotificationId, int mTechnicianId, int eventId, String eventName) {
        this.mResponseType = mResponseType;
        this.mName = mName;
        this.mStatus = mStatus;
        this.mCallTime = mCallTime;
        this.mNotificationId = mNotificationId;
        this.mTechnicianId= mTechnicianId;
        this.mMachineId = machineId;
        this.mAdditionalText = additionalText;
        this.mEventId = eventId;
        mEventName = eventName;
    }

    public int getEventId() {
        return mEventId;
    }

    public String getmEventName() {
        return mEventName;
    }

    public void setmEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public void setEventId(int mEventId) {
        this.mEventId = mEventId;
    }

    public int getmMachineId() {
        return mMachineId;
    }

    public String getmAdditionalText() {
        return mAdditionalText;
    }

    public void setmAdditionalText(String mAdditionalText) {
        this.mAdditionalText = mAdditionalText;
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

    public void setmMachineId(int machineID) {
        mMachineId = machineID;
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

    @Override
    public String toString() {
        return "TechCallInfo{" +
                "mResponseType=" + mResponseType +
                ", mNotificationId=" + mNotificationId +
                ", mTechnicianId=" + mTechnicianId +
                ", mName='" + mName + '\'' +
                ", mStatus='" + mStatus + '\'' +
                ", mEventName='" + mEventName + '\'' +
                ", mCallTime=" + mCallTime +
                '}';
    }
}
