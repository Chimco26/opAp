package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateTaskHistoryRequest {

    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("task")
    @Expose
    private TaskHistory task;


    public CreateTaskHistoryRequest(String sessionID, TaskHistory task) {
        this.sessionID = sessionID;
        this.task = task;
    }
}
