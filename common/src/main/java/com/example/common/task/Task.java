package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {

    @SerializedName("ID")
    @Expose
    private Integer ID;
    @SerializedName("HistoryID")
    @Expose
    private Integer historyID;
    @SerializedName("CreateUser")
    @Expose
    private Integer createUser;
    @SerializedName("Subject")
    @Expose
    private Integer subject;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("TaskLevel")
    @Expose
    private Integer taskLevel;
    @SerializedName("TaskLevelObjectID")
    @Expose
    private Integer taskLevelObjectID;
    @SerializedName("Priority")
    @Expose
    private Integer priority;
    @SerializedName("Assignee")
    @Expose
    private Integer assignee;
    @SerializedName("StartTimeTarget")
    @Expose
    private String startTimeTarget;
    @SerializedName("EndTimeTarget")
    @Expose
    private String endTimeTarget;
    @SerializedName("EstimatedExecutionTime")
    @Expose
    private Double estimatedExecutionTime;
    @SerializedName("SourceTaskCreationPlatform")
    @Expose
    private Integer sourceTaskCreationPlatform = 3;
    @SerializedName("Status")
    @Expose
    private Integer status;

    public Task(String text) {
        this.text = text;
    }

    public Task() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getHistoryID() {
        return historyID;
    }

    public void setHistoryID(Integer historyID) {
        this.historyID = historyID;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    public Integer getTaskLevelObjectID() {
        return taskLevelObjectID;
    }

    public void setTaskLevelObjectID(Integer taskLevelObjectID) {
        this.taskLevelObjectID = taskLevelObjectID;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getAssignee() {
        return assignee;
    }

    public void setAssignee(Integer assignee) {
        this.assignee = assignee;
    }

    public String getStartTimeTarget() {
        return startTimeTarget;
    }

    public void setStartTimeTarget(String startTimeTarget) {
        this.startTimeTarget = startTimeTarget;
    }

    public String getEndTimeTarget() {
        return endTimeTarget;
    }

    public void setEndTimeTarget(String endTimeTarget) {
        this.endTimeTarget = endTimeTarget;
    }

    public Double getEstimatedExecutionTime() {
        return estimatedExecutionTime;
    }

    public void setEstimatedExecutionTime(Double estimatedExecutionTime) {
        this.estimatedExecutionTime = estimatedExecutionTime;
    }

    public Integer getSourceTaskCreationPlatform() {
        return sourceTaskCreationPlatform;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
