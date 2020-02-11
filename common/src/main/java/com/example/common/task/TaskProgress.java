package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskProgress {

    @SerializedName("TaskID")
    @Expose
    private int taskID;
    @SerializedName("HistoryID")
    @Expose
    private int historyID;
    @SerializedName("TaskStatus")
    @Expose
    private int taskStatus;
    @SerializedName("TaskCreateUser")
    @Expose
    private int taskCreateUser;
    @SerializedName("Assignee")
    @Expose
    private int assignee;
    @SerializedName("TaskCreateDate")
    @Expose
    private String taskCreateDate;
    @SerializedName("TaskStartTimeTarget")
    @Expose
    private String taskStartTimeTarget;
    @SerializedName("TaskEndTimeTarget")
    @Expose
    private String taskEndTimeTarget;
    @SerializedName("SubjectTrans")
    @Expose
    private String subjectTrans;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("TaskPriorityTrans")
    @Expose
    private String taskPriorityTrans;
    @SerializedName("TaskPriorityID")
    @Expose
    private int taskPriorityID;
    @SerializedName("HistoryDisplayHName")
    @Expose
    private String historyDisplayHName;
    @SerializedName("HistoryDisplayName")
    @Expose
    private String historyDisplayName;
    @SerializedName("HistoryUser")
    @Expose
    private String historyUser;
    @SerializedName("HistoryCreateDate")
    @Expose
    private String historyCreateDate;
    @SerializedName("TaskLevel")
    @Expose
    private int taskLevel;
    @SerializedName("StringID")
    @Expose
    private int StringID;
    @SerializedName("LName")
    @Expose
    private String lName;
    @SerializedName("EName")
    @Expose
    private String eName;

    public TaskProgress(int taskID) {
        this.taskID = taskID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getHistoryID() {
        return historyID;
    }

    public void setHistoryID(int historyID) {
        this.historyID = historyID;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskCreateUser() {
        return taskCreateUser;
    }

    public void setTaskCreateUser(int taskCreateUser) {
        this.taskCreateUser = taskCreateUser;
    }

    public int getAssignee() {
        return assignee;
    }

    public void setAssignee(int assignee) {
        this.assignee = assignee;
    }

    public String getTaskCreateDate() {
        return taskCreateDate;
    }

    public void setTaskCreateDate(String taskCreateDate) {
        this.taskCreateDate = taskCreateDate;
    }

    public String getTaskStartTimeTarget() {
        return taskStartTimeTarget;
    }

    public void setTaskStartTimeTarget(String taskStartTimeTarget) {
        this.taskStartTimeTarget = taskStartTimeTarget;
    }

    public String getTaskEndTimeTarget() {
        return taskEndTimeTarget;
    }

    public void setTaskEndTimeTarget(String taskEndTimeTarget) {
        this.taskEndTimeTarget = taskEndTimeTarget;
    }

    public String getSubjectTrans() {
        return subjectTrans;
    }

    public void setSubjectTrans(String subjectTrans) {
        this.subjectTrans = subjectTrans;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTaskPriorityTrans() {
        return taskPriorityTrans;
    }

    public void setTaskPriorityTrans(String taskPriorityTrans) {
        this.taskPriorityTrans = taskPriorityTrans;
    }

    public int getTaskPriorityID() {
        return taskPriorityID;
    }

    public void setTaskPriorityID(int taskPriorityID) {
        this.taskPriorityID = taskPriorityID;
    }

    public String getHistoryDisplayHName() {
        return historyDisplayHName;
    }

    public void setHistoryDisplayHName(String historyDisplayHName) {
        this.historyDisplayHName = historyDisplayHName;
    }

    public String getHistoryDisplayName() {
        return historyDisplayName;
    }

    public void setHistoryDisplayName(String historyDisplayName) {
        this.historyDisplayName = historyDisplayName;
    }

    public String getHistoryUser() {
        return historyUser;
    }

    public void setHistoryUser(String historyUser) {
        this.historyUser = historyUser;
    }

    public String getHistoryCreateDate() {
        return historyCreateDate;
    }

    public void setHistoryCreateDate(String historyCreateDate) {
        this.historyCreateDate = historyCreateDate;
    }

    public int getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(int taskLevel) {
        this.taskLevel = taskLevel;
    }

    public int getStringID() {
        return StringID;
    }

    public void setStringID(int StringID) {
        this.StringID = StringID;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getEName() {
        return eName;
    }

    public void setEName(String eName) {
        this.eName = eName;
    }
}
