package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;
import com.operatorsapp.utils.Consts;

public class GetTaskNoteRequest {

    @SerializedName("SessionID")
    private String mSessionId;

    @SerializedName("TaskID")
    private int taskId;

    @SerializedName("SourceTaskCreationPlatform")
    private int mPlatform = Consts.APPLICATION_CODE;

    public GetTaskNoteRequest(String sessionId, int taskId) {
        this.mSessionId = sessionId;
        this.taskId = taskId;

    }
}
