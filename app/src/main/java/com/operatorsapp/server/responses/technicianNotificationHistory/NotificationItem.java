package com.operatorsapp.server.responses.technicianNotificationHistory;

import com.google.gson.annotations.SerializedName;

public class NotificationItem{

	@SerializedName("ResponseDate")
	private String responseDate;

	@SerializedName("MinutesPassedFromResponse")
	private int minutesPassedFromResponse;

	@SerializedName("Title")
	private String title;

	@SerializedName("Text")
	private String text;

	@SerializedName("SourceUserName")
	private String sourceUserName;

	@SerializedName("ResponseType")
	private String responseType;

	@SerializedName("SentTime")
	private String sentTime;

	@SerializedName("NotificationType")
	private int notificationType;

	@SerializedName("ID")
	private int iD;

	@SerializedName("TargetUserName")
	private String targetUserName;

	@SerializedName("Topic")
	private Object topic;

	@SerializedName("SourceMachineID")
	private int sourceMachineID;

	@SerializedName("ResponseTypeID")
	private int responseTypeID;

	public void setResponseDate(String responseDate){
		this.responseDate = responseDate;
	}

	public String getResponseDate(){
		return responseDate;
	}

	public void setMinutesPassedFromResponse(int minutesPassedFromResponse){
		this.minutesPassedFromResponse = minutesPassedFromResponse;
	}

	public int getMinutesPassedFromResponse(){
		return minutesPassedFromResponse;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setSourceUserName(String sourceUserName){
		this.sourceUserName = sourceUserName;
	}

	public String getSourceUserName(){
		return sourceUserName;
	}

	public void setResponseType(String responseType){
		this.responseType = responseType;
	}

	public String getResponseType(){
		return responseType;
	}

	public void setSentTime(String sentTime){
		this.sentTime = sentTime;
	}

	public String getSentTime(){
		return sentTime;
	}

	public void setNotificationType(int notificationType){
		this.notificationType = notificationType;
	}

	public int getNotificationType(){
		return notificationType;
	}

	public void setID(int iD){
		this.iD = iD;
	}

	public int getID(){
		return iD;
	}

	public void setTargetUserName(String targetUserName){
		this.targetUserName = targetUserName;
	}

	public String getTargetUserName(){
		return targetUserName;
	}

	public void setTopic(Object topic){
		this.topic = topic;
	}

	public Object getTopic(){
		return topic;
	}

	public void setSourceMachineID(int sourceMachineID){
		this.sourceMachineID = sourceMachineID;
	}

	public int getSourceMachineID(){
		return sourceMachineID;
	}

	public void setResponseTypeID(int responseTypeID){
		this.responseTypeID = responseTypeID;
	}

	public int getResponseTypeID(){
		return responseTypeID;
	}

}