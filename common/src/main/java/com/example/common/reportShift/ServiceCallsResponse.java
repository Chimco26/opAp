package com.example.common.reportShift;

import com.example.common.StandardResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceCallsResponse extends StandardResponse {

    @SerializedName("NotificationByDateTime")
    @Expose
    private Object notificationByDateTime;
    @SerializedName("NotificationByType")
    @Expose
    private List<NotificationByType> notificationByType = null;
    @SerializedName("ServiceCallsForMachine")
    @Expose
    private Object serviceCallsForMachine;

    public Object getNotificationByDateTime() {
        return notificationByDateTime;
    }

    public void setNotificationByDateTime(Object notificationByDateTime) {
        this.notificationByDateTime = notificationByDateTime;
    }

    public List<NotificationByType> getNotificationByType() {
        return notificationByType;
    }

    public void setNotificationByType(List<NotificationByType> notificationByType) {
        this.notificationByType = notificationByType;
    }

    public Object getServiceCallsForMachine() {
        return serviceCallsForMachine;
    }

    public void setServiceCallsForMachine(Object serviceCallsForMachine) {
        this.serviceCallsForMachine = serviceCallsForMachine;
    }

}
