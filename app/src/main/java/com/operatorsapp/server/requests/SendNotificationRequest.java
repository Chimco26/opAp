package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;
import com.operatorsapp.managers.PersistenceManager;

public class SendNotificationRequest {

    @SerializedName("sourceMachineID")
    private int sourceMachineID;

    @SerializedName("Text")
    private String text;

    @SerializedName("title")
    private String title;

    @SerializedName("SessionID")
    private String sessionID;

    @SerializedName("workerID")
    private String workerID;

    public SendNotificationRequest(int sourceMachineID, String text, String title, String sessionID) {
        this.sourceMachineID = sourceMachineID;
        this.text = text;
        this.title = title;
        this.sessionID = sessionID;
        workerID= PersistenceManager.getInstance().getOperatorId() + "";
    }

    public void setSourceMachineID(int sourceMachineID){
        this.sourceMachineID = sourceMachineID;
    }

    public int getSourceMachineID(){
        return sourceMachineID;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setSessionID(String sessionID){
        this.sessionID = sessionID;
    }

    public String getSessionID(){
        return sessionID;
    }
}
