package com.example.common.request;

import com.google.gson.annotations.SerializedName;

public class ServiceCallFileRequest {

    @SerializedName("NotificationID")
    private int notificationID;

    @SerializedName("data")
    private String data;

    @SerializedName("FileID")
    private int fileID;

    @SerializedName("FileName")
    private String fileName;

    @SerializedName("FileExt")
    private String fileExt;

    @SerializedName("quickDisplay")
    private int quickDisplay;

    @SerializedName("SessionID")
    private String sessionID;

    public ServiceCallFileRequest(String sessionID, int notificationID, String data, int fileID, String fileName, String fileExt, int quickDisplay) {
        this.sessionID = sessionID;
        this.notificationID = notificationID;
        this.data = data;
        this.fileID = fileID;
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.quickDisplay = quickDisplay;
    }

    public int getNotificationID(){
        return notificationID;
    }

    public String getData(){
        return data;
    }

    public String getFileExt(){
        return fileExt;
    }

    public int getQuickDisplay(){
        return quickDisplay;
    }

    public String getFileName(){
        return fileName;
    }

    public int getFileID(){
        return fileID;
    }

    public String getSessionID(){
        return sessionID;
    }
}

