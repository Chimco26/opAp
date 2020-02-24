package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTaskFilesRequest {
    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("SourceTaskCreationPlatform")
    @Expose
    private int sourceTaskCreationPlatform = 3;
    @SerializedName("TaskID")
    @Expose
    private long taskId;

    public GetTaskFilesRequest(String sessionID, long taskId) {
        this.sessionID = sessionID;
        this.taskId = taskId;
    }
}
