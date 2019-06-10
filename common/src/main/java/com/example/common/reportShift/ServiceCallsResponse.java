package com.example.common.reportShift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceCallsResponse {

    @SerializedName("FunctionSucceed")
    @Expose
    private Boolean functionSucceed;
    @SerializedName("LeaderRecordID")
    @Expose
    private Integer leaderRecordID;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("NotificationByDateTime")
    @Expose
    private Object notificationByDateTime;
    @SerializedName("NotificationByType")
    @Expose
    private List<NotificationByType> notificationByType = null;
    @SerializedName("ServiceCallsForMachine")
    @Expose
    private Object serviceCallsForMachine;

    public Boolean getFunctionSucceed() {
        return functionSucceed;
    }

    public void setFunctionSucceed(Boolean functionSucceed) {
        this.functionSucceed = functionSucceed;
    }

    public Integer getLeaderRecordID() {
        return leaderRecordID;
    }

    public void setLeaderRecordID(Integer leaderRecordID) {
        this.leaderRecordID = leaderRecordID;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

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
