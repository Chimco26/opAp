package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateTask {

    @SerializedName("SessionID")
    @Expose
    private Double sessionID;
    @SerializedName("task")
    @Expose
    private Task task;

    public Double getSessionID() {
        return sessionID;
    }

    public void setSessionID(Double sessionID) {
        this.sessionID = sessionID;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

}
