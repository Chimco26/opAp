package com.operatorsapp.server.responses;

import com.google.gson.annotations.SerializedName;

public class TaskNote {
    @SerializedName("ID")
    public int mNoteId;

    @SerializedName("TaskID")
    public int mTaskId;

    @SerializedName("TaskHistoryID")
    public int mTaskHistoryID;

    @SerializedName("Note")
    public String mNoteText;

    @SerializedName("CreateDate")
    public String mCreateDate;

    @SerializedName("UserName")
    public String mCreatorName;

    public TaskNote(int mNoteId, int mTaskId, int mTaskHistoryID, String mNoteText, String mCreateDate, String mCreatorName) {
        this.mNoteId = mNoteId;
        this.mTaskId = mTaskId;
        this.mTaskHistoryID = mTaskHistoryID;
        this.mNoteText = mNoteText;
        this.mCreateDate = mCreateDate;
        this.mCreatorName = mCreatorName;
    }

    public String getCreatorName() {
        return mCreatorName;
    }

    public int getmNoteId() {
        return mNoteId;
    }

    public int getmTaskId() {
        return mTaskId;
    }

    public int getmTaskHistoryID() {
        return mTaskHistoryID;
    }

    public String getmNoteText() {
        return mNoteText;
    }

    public String getmCreateDate() {
        return mCreateDate;
    }

    public void setNoteText(String mNoteText) {
        this.mNoteText = mNoteText;
    }
}
