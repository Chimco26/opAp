package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public class TaskObjectForCreateOrEditContent {

    @SerializedName("Subjects")
    @Expose
    private List<TaskInfoObject> subjects;
    @SerializedName("Level")
    @Expose
    private List<TaskInfoObject> level;
    @SerializedName("Priority")
    @Expose
    private List<TaskInfoObject> priority;
    @SerializedName("Status")
    @Expose
    private List<TaskInfoObject> status;

    public List<TaskInfoObject> getSubjects(int currentSubject) {
        if (subjects != null && !subjects.isEmpty()) {
            Iterator<TaskInfoObject> iterator = subjects.iterator();
            while (iterator.hasNext()){
                TaskInfoObject item = iterator.next();
                if (!item.isActive() && item.getID() != currentSubject){
                    iterator.remove();
                }
            }
        }
        return subjects;
    }

    public void setSubjects(List<TaskInfoObject> subjects) {
        this.subjects = subjects;
    }

    public List<TaskInfoObject> getLevel() {
        return level;
    }

    public void setLevel(List<TaskInfoObject> level) {
        this.level = level;
    }

    public List<TaskInfoObject> getPriority() {
        return priority;
    }

    public void setPriority(List<TaskInfoObject> priority) {
        this.priority = priority;
    }

    public List<TaskInfoObject> getStatus() {
        return status;
    }

    public void setStatus(List<TaskInfoObject> status) {
        this.status = status;
    }
}
