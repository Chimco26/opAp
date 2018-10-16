package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 10/10/2018.
 */

public class PostTechnicianCallRequest {

    @SerializedName("SessionID")
    private String sessionID;

    @SerializedName("sourceMachineID")
    private int machineId;

    @SerializedName("Title")
    private String title;

    @SerializedName("targetUserID")
    private int technicianId;

    @SerializedName("Text")
    private String text;

    @SerializedName("sourceUserName")
    private String userName;

    @SerializedName("targetUserName")
    private String technicianName;

    public PostTechnicianCallRequest(String sessionID, int machineId, String title, int technicianId, String text, String userName, String technicianName) {
        this.sessionID = sessionID;
        this.machineId = machineId;
        this.title = title;
        this.technicianId = technicianId;
        this.text = text;
        this.userName = userName;
        this.technicianName = technicianName;
    }

    public String getUserName() {
        return userName;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
