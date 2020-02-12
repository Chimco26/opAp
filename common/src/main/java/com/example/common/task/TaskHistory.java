package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskHistory {

    @SerializedName("TaskID")
    @Expose
    private Integer taskID;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Assignee")
    @Expose
    private Integer assignee;
    @SerializedName("SourceTaskCreationPlatform")
    @Expose
    private final Integer sourceTaskCreationPlatform = 3;

    public TaskHistory(Integer taskID, Integer status, Integer assignee) {
        this.taskID = taskID;
        this.status = status;
        this.assignee = assignee;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAssignee() {
        return assignee;
    }

    public void setAssignee(Integer assignee) {
        this.assignee = assignee;
    }

    public Integer getSourceTaskCreationPlatform() {
        return sourceTaskCreationPlatform;
    }

}
