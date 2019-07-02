package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 07/10/2018.
 */

public class PostNotificationTokenRequest {

    public static final int APPLICATION_CODE_FOR_NOTIFICATIONS = 2;

    @SerializedName("")
    private String sessionID;

    @SerializedName("MachineID")
    private int machineId;

    @SerializedName("Token")
    private String token;

    @SerializedName("DeviceID")
    private String deviceId;

    @SerializedName("ApplicationCode")
    private int mAppCode;

    public PostNotificationTokenRequest(String sessionID, int machineId, String token, String deviceId) {
        this.mAppCode = APPLICATION_CODE_FOR_NOTIFICATIONS;
        this.sessionID = sessionID;
        this.machineId = machineId;
        this.token = token;
        this.deviceId = deviceId;
    }
}
