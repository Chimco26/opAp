package com.operators.machinestatusinfra.models;

import com.google.gson.annotations.SerializedName;

public class TaskStatusCount {

    @SerializedName("NumOfTasks")
    private int numOfTasks;
    @SerializedName("TaskStatusID")
    private int taskStatusId;

    public int getNumOfTasks() {
        return numOfTasks;
    }

    public void setNumOfTasks(int numOfTasks) {
        this.numOfTasks = numOfTasks;
    }

    public int getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(int taskStatusId) {
        this.taskStatusId = taskStatusId;
    }
}
