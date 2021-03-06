package com.operatorsapp.server.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 08/10/2018.
 */

public class NotificationHistoryRequest {

    //{"":43384.42508,"sourceMachineID":1,"applicationID":2}

    public static final int APPLICATION_CODE_FOR_NOTIFICATIONS = 2;

    @SerializedName("SessionID")
    private String sessionID;

    @SerializedName("sourceMachineID")
    private int machineId;

    @SerializedName("sourceMachines")
    private int[] machinesIdArray;

    @SerializedName("applicationID")
    private int mAppCode;

    public NotificationHistoryRequest(String sessionID, int machineId) {
        this.sessionID = sessionID;
        this.machineId = machineId;
        mAppCode = APPLICATION_CODE_FOR_NOTIFICATIONS;
    }

    public NotificationHistoryRequest(String sessionId, int[] machinesId) {
        this.sessionID = sessionId;
        this.machinesIdArray = machinesId;
        mAppCode = APPLICATION_CODE_FOR_NOTIFICATIONS;
        this.machineId = -1;
    }
}
