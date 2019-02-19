package com.operatorsapp.server.responses.technicianNotificationHistory;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TechnicianNotificationResponse{

	@SerializedName("TechStatus")
	private Object techStatus;

	@SerializedName("notification")
	private List<NotificationItem> notification;

	@SerializedName("error")
	private Object error;

	@SerializedName("LeaderRecordID")
	private int leaderRecordID;

	@SerializedName("FunctionSucceed")
	private boolean functionSucceed;

	public void setTechStatus(Object techStatus){
		this.techStatus = techStatus;
	}

	public Object getTechStatus(){
		return techStatus;
	}

	public void setNotification(List<NotificationItem> notification){
		this.notification = notification;
	}

	public List<NotificationItem> getNotification(){
		return notification;
	}

	public void setError(Object error){
		this.error = error;
	}

	public Object getError(){
		return error;
	}

	public void setLeaderRecordID(int leaderRecordID){
		this.leaderRecordID = leaderRecordID;
	}

	public int getLeaderRecordID(){
		return leaderRecordID;
	}

	public void setFunctionSucceed(boolean functionSucceed){
		this.functionSucceed = functionSucceed;
	}

	public boolean isFunctionSucceed(){
		return functionSucceed;
	}

}