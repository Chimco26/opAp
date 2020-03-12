package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateTaskRequest {

    @SerializedName("SessionID")
    @Expose
    private String sessionID;
    @SerializedName("task")
    @Expose
    private Task task;


    public CreateTaskRequest(String sessionID, Task task) {
        this.sessionID = sessionID;
        this.task = task;
    }
}
