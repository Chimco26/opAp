package com.example.common.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskInfoObject {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;

    public TaskInfoObject(String displayName) {
        this.displayName = displayName;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
