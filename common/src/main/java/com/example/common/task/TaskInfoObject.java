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
    /*  only in use for "Subject"  */
    @SerializedName("IsActive")
    private boolean isActive = true;

    public TaskInfoObject(String displayName) {
        this.displayName = displayName;
    }

    public TaskInfoObject(Integer iD, String name, String displayName) {
        this.iD = iD;
        this.name = name;
        this.displayName = displayName;
    }

    public TaskInfoObject(int id) {
        this.iD = id;
    }

    public int getID() {
        if (iD == null){
            return 0;
        }
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

    public boolean isActive() {
        return isActive;
    }
}
