package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;
import com.operatorsapp.managers.PersistenceManager;

/**
 * Created by alex on 15/10/2018.
 */

public class RespondToNotificationRequest {

    @SerializedName("SessionID")
    private String mSessionID;

    @SerializedName("Title")
    private String mTitle;

    @SerializedName("Text")
    private String mText;

    @SerializedName("sourceMachineID")
    private String mMachineID;

    @SerializedName("HistoryMsgID")
    private String mMsgID;

    @SerializedName("ResType")
    private int mResponseType;

    @SerializedName("notificationType")
    private int mNotificationType;

    @SerializedName("ResTarget")
    private int mResponseTarget;

    @SerializedName("sourceUserID")
    private int mSourceUserID;

    @SerializedName("sourceUserName")
    private String mSourceUserName;

    @SerializedName("targetUserName")
    private String mTargetUserName;

    @SerializedName("SourceWorkerID")
    private String mSourceWorkerID;

    @SerializedName("targetUserID")
    private String mTargetUserID;

    public RespondToNotificationRequest(String mSessionID, String mTitle, String mText, String mMachineID, String mMsgID, int mResponseType, int mNotificationType, int mResponseTarget, String mSourceUserID, String mSourceUserName, String mTargetUserName, String mSourceWorkerID, String mTargetUserId) {
        this.mSessionID = mSessionID;
        this.mTitle = mTitle;
        this.mText = mText;
        this.mMachineID = mMachineID;
        this.mMsgID = mMsgID;
        this.mResponseType = mResponseType;
        this.mNotificationType = mNotificationType;
        this.mResponseTarget = mResponseTarget;
        this.mSourceUserName = mSourceUserName;
        this.mTargetUserName = mTargetUserName;
        this.mTargetUserID = mTargetUserId;

        if (!(mSourceWorkerID == null || mSourceWorkerID.equals(""))){
            this.mSourceWorkerID = mSourceWorkerID;
        }

        try{
            this.mSourceUserID = Integer.parseInt(mSourceUserID);
        }catch (Exception e){
            this.mSourceUserID = 0;
        }
    }

    public String getmSourceWorkerID() {
        return mSourceWorkerID;
    }

    public String getmSessionID() {
        return mSessionID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmText() {
        return mText;
    }

    public String getmMachineID() {
        return mMachineID;
    }

    public String getmMsgID() {
        return mMsgID;
    }

    public int getmResponseType() {
        return mResponseType;
    }

    public int getmNotificationType() {
        return mNotificationType;
    }

    public int getmResponseTarget() {
        return mResponseTarget;
    }

    public String getmSourceUserID() {
        return mSourceUserID+"";
    }

    public String getmSourceUserName() {
        return mSourceUserName;
    }

    public String getmTargetUserName() {
        return mTargetUserName;
    }
}
