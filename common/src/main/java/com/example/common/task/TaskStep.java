package com.example.common.task;

import com.google.gson.annotations.SerializedName;

public class TaskStep {
    @SerializedName("ID")
    public int mStepId;

    @SerializedName("TaskID")
    public int mTaskId;

    @SerializedName("TaskHistoryID")
    public int mTaskHistoryID;

    @SerializedName("Text")
    public String mText;
//
//    @SerializedName("CreateDate")
//    public String mCreateDate;

    @SerializedName("IsOpen")
    public boolean IsOpen;

    public TaskStep(int mStepId, int mTaskId, int mTaskHistoryID, String mText, boolean IsOpen) {
        this.mStepId = mStepId;
        this.mTaskId = mTaskId;
        this.mTaskHistoryID = mTaskHistoryID;
        this.mText = mText;
        this.IsOpen = IsOpen;
    }

    public int getStepId() {
        return mStepId;
    }

    public int getTaskId() {
        return mTaskId;
    }

    public int getTaskHistoryID() {
        return mTaskHistoryID;
    }

    public String getText() {
        return mText;
    }
//
//    public String getCreateDate() {
//        return mCreateDate;
//    }

    public boolean isOpen() {
        return IsOpen;
    }

    public void setOpen(boolean open) {
        IsOpen = open;
    }

    public void setText(String mText) {
        this.mText = mText;
    }
}
