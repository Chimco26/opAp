package com.example.common.request;

import com.google.gson.annotations.SerializedName;

public class UpdateServiceCallDescriptionRequest {

    @SerializedName("SessionID")
    private String mSessionId;

    @SerializedName("NotificationID")
    private String notificationID;

    @SerializedName("Description")
    private String description;

    public UpdateServiceCallDescriptionRequest(String mSessionId, String notificationID, String description) {
        this.mSessionId = mSessionId;
        this.notificationID = notificationID;
        this.description = description;
    }
}
