package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskListContentResponse {

    @SerializedName("Tasks")
    @Expose
    private List<TaskProgress> taskProgress = null;

    public List<TaskProgress> getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(List<TaskProgress> taskProgress) {
        this.taskProgress = taskProgress;
    }
}
