package com.operatorsapp.server.responses;

import com.example.common.StandardResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by alex on 08/10/2018.
 */

public class NotificationHistoryResponse extends StandardResponse {

    @SerializedName("notification")
    private ArrayList<Notification> mNotificationsList;
//
//    @SerializedName("MachineNotificaation")
//    private ArrayList<Notification> mNotificationsListForLine;

    public NotificationHistoryResponse(ArrayList<Notification> mNotificationsList) {
        this.mNotificationsList = mNotificationsList;
    }

    public NotificationHistoryResponse() {
        mNotificationsList = new ArrayList<>();
    }

    public ArrayList<Notification> getmNotificationsList() {
        return mNotificationsList != null ? mNotificationsList : new ArrayList<Notification>();
    }

    public void setmNotificationsList(ArrayList<Notification> mNotificationsList) {
        this.mNotificationsList = mNotificationsList;
    }


}
