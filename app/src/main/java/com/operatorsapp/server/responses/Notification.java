package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 14/10/2018.
 */

public class Notification {

    @SerializedName("Text")
    private String mBody;

    @SerializedName("SourceUserName")
    private String mSender;

    @SerializedName("TargetUserName")
    private String mTargetName;

    @SerializedName("SentTime")
    private String mSentTime;

    @SerializedName("ResponseType")
    private int mResponseType;

    @SerializedName("ResponseDate")
    private String mResponseDate;

    @SerializedName("ID")
    private int mNotificationID;

    public Notification(String mBody, String mSender, String mSentTime, int mResponseType, String mResponseDate, String mTargetName, int mNotificationID) {
        this.mBody = mBody;
        this.mSender = mSender;
        this.mSentTime = mSentTime;
        this.mResponseType = mResponseType;
        this.mResponseDate = mResponseDate;
        this.mTargetName = mTargetName;
        this.mNotificationID = mNotificationID;
    }

    public String getmBody() {
        return mBody;
    }

    public String getmTargetName() {
        return mTargetName;
    }

    public String getmSender() {
        return mSender;
    }

    public String getmSentTime() {
        return mSentTime;
    }

    public int getmResponseType() {
        return mResponseType;
    }

    public String getmResponseDate() {
        return mResponseDate;
    }

    public int getmNotificationID() {
        return mNotificationID;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    public void setmTargetName(String mTargetName) {
        this.mTargetName = mTargetName;
    }

    public void setmSentTime(String mSentTime) {
        this.mSentTime = mSentTime;
    }

    public void setmResponseType(int mResponseType) {
        this.mResponseType = mResponseType;
    }

    public void setmResponseDate(String mResponseDate) {
        this.mResponseDate = mResponseDate;
    }

    public void setmNotificationID(int mNotificationID) {
        this.mNotificationID = mNotificationID;
    }
}