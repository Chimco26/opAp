package com.operatorsapp.server.responses;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.operatorsapp.R;
import com.operatorsapp.utils.Consts;

/**
 * Created by alex on 14/10/2018.
 */

public class Notification {

    @SerializedName("Text")
    private String mBody;

    @SerializedName("InsightText")
    private NotificationText mInsightBody;

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

    @SerializedName("Title")
    private String mTitle;

    @SerializedName("SenderUserDisplayName")
    private String mOriginalSenderName;

    @SerializedName("SenderUserDisplayHName")
    private String mOriginalSenderHName;

    @SerializedName("NotificationType")
    private int mNotificationType;

    @SerializedName("ID")
    private int mNotificationID;

    @SerializedName("SourceUserID")
    private int mSourceUserID;

    @SerializedName("TargetUserID")
    private int mTargetUserId;

    @SerializedName("SourceMachineID")
    private int mSourceMachineID;

    @SerializedName("AdditionalText")
    private String mAdditionalText;

    public Notification(String mBody, String mTitle,  String mSender, String mSentTime, int mResponseType, String mResponseDate, String mTargetName, int mNotificationID, int mNotificationType) {
        this.mBody = mBody;
        this.mTitle = mTitle;
        this.mSender = mSender;
        this.mSentTime = mSentTime;
        this.mResponseType = mResponseType;
        this.mResponseDate = mResponseDate;
        this.mTargetName = mTargetName;
        this.mNotificationID = mNotificationID;
        this.mNotificationType= mNotificationType;
    }

    public String getmAdditionalText() {
        return mAdditionalText;
    }

    public void setmAdditionalText(String mAdditionalText) {
        this.mAdditionalText = mAdditionalText;
    }

    public int getMachineID() {
        return mSourceMachineID;
    }

    public int getmNotificationType() {
        return mNotificationType;
    }

    public void setmNotificationType(int mNotificationType) {
        this.mNotificationType = mNotificationType;
    }

    public int getmSourceUserID() {
        return mSourceUserID;
    }

    public void setmSourceUserID(int mSourceUserID) {
        this.mSourceUserID = mSourceUserID;
    }

    public int getmTargetUserId() {
        return mTargetUserId;
    }

    public void setmTargetUserId(int mTargetUserId) {
        this.mTargetUserId = mTargetUserId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmBody(Context context) {

        //if (getmResponseType() == Consts.NOTIFICATION_TYPE_REAL_TIME && getmInsightBody() != null){
        if (getmNotificationType() == Consts.NOTIFICATION_TYPE_REAL_TIME && getmInsightBody() != null){
            return getmInsightBody().getFullText(context);
        }else {
            return mBody;
        }
    }

    public NotificationText getmInsightBody() {
        return mInsightBody;
    }

    public void setmInsightBody(NotificationText mInsightBody) {
        this.mInsightBody = mInsightBody;
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
        if (mResponseDate == null || mResponseDate == ""){
            return getmSentTime();
        }
        return mResponseDate;
    }

    public int getmNotificationID() {
        return mNotificationID;
    }

    public String getmOriginalSenderName() {
        if (mOriginalSenderName == null){
            return getmSender();
        }
        return mOriginalSenderName;
    }

    public String getmOriginalSenderHName() {
        if (mOriginalSenderHName == null){
            return getmSender();
        }
        return mOriginalSenderHName;
    }

    public void setmOriginalSenderName(String mOriginalSenderName) {
        this.mOriginalSenderName = mOriginalSenderName;
    }

    public void setmOriginalSenderHName(String mOriginalSenderHName) {
        this.mOriginalSenderHName = mOriginalSenderHName;
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

    public int getResponseIcon(Context context) {

        int icon = 0;
        if (getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){

            switch (getmResponseType()){

                case Consts.NOTIFICATION_RESPONSE_TYPE_UNSET:
                    icon = R.drawable.call_recieved;
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                    icon = R.drawable.call_sent_blue;
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                    icon = R.drawable.call_declined;
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                    icon = R.drawable.at_work_blue;
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                    icon = R.drawable.service_done;
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                    icon = R.drawable.cancel_blue;
                    break;
            }
        }else {

            switch (getmResponseType()){

                case Consts.NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS:
                    icon = R.drawable.question_circle_outline;
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                    icon = R.drawable.baseline_check_circle;
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                    icon = R.drawable.close_circle_outline;
                    break;

            }
        }
        return icon;
    }
}
