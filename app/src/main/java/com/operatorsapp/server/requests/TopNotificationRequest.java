package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

public class TopNotificationRequest {

    @SerializedName("NumberOfNotificationToDisplay")
    private int numberOfNotificationToDisplay;

    @SerializedName("SessionID")
    private String sessionID;

    public TopNotificationRequest(String sessionId, int number) {
        this.numberOfNotificationToDisplay = number;
        this.sessionID = sessionId;
    }

    public void setNumberOfNotificationToDisplay(int numberOfNotificationToDisplay){
        this.numberOfNotificationToDisplay = numberOfNotificationToDisplay;
    }

    public int getNumberOfNotificationToDisplay(){
        return numberOfNotificationToDisplay;
    }

    public void setSessionID(String sessionID){
        this.sessionID = sessionID;
    }

    public String getSessionID(){
        return sessionID;
    }

}
