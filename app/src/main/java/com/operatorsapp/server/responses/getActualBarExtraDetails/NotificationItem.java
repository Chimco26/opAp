package com.operatorsapp.server.responses.getActualBarExtraDetails;

import com.google.gson.annotations.SerializedName;

public class NotificationItem{

	@SerializedName("ResponseDate")
	private String responseDate;

	@SerializedName("NotificationID")
	private int notificationID;

	@SerializedName("SenderUserDisplayHName")
	private Object senderUserDisplayHName;

	@SerializedName("TextKeysValues")
	private Object textKeysValues;

	@SerializedName("MinutesPassedFromResponse")
	private int minutesPassedFromResponse;

	@SerializedName("SourceUserID")
	private int sourceUserID;

	@SerializedName("Title")
	private Object title;

	@SerializedName("Text")
	private String text;

	@SerializedName("SourceUserName")
	private Object sourceUserName;

	@SerializedName("ResponseType")
	private Object responseType;

	@SerializedName("SenderUserID")
	private int senderUserID;

	@SerializedName("SentTime")
	private String sentTime;

	@SerializedName("SenderUserDisplayName")
	private Object senderUserDisplayName;

	@SerializedName("NotificationType")
	private int notificationType;

	@SerializedName("ID")
	private int iD;

	@SerializedName("TargetUserName")
	private Object targetUserName;

	@SerializedName("Topic")
	private Object topic;

	@SerializedName("SourceMachineID")
	private int sourceMachineID;

	@SerializedName("ResponseTypeID")
	private int responseTypeID;

	@SerializedName("TargetUserID")
	private int targetUserID;

	public String getResponseDate(){
		return responseDate;
	}

	public int getNotificationID(){
		return notificationID;
	}

	public Object getSenderUserDisplayHName(){
		return senderUserDisplayHName;
	}

	public Object getTextKeysValues(){
		return textKeysValues;
	}

	public int getMinutesPassedFromResponse(){
		return minutesPassedFromResponse;
	}

	public int getSourceUserID(){
		return sourceUserID;
	}

	public Object getTitle(){
		return title;
	}

	public String getText(){
		return text;
	}

	public Object getSourceUserName(){
		return sourceUserName;
	}

	public Object getResponseType(){
		return responseType;
	}

	public int getSenderUserID(){
		return senderUserID;
	}

	public String getSentTime(){
		return sentTime;
	}

	public Object getSenderUserDisplayName(){
		return senderUserDisplayName;
	}

	public int getNotificationType(){
		return notificationType;
	}

	public int getID(){
		return iD;
	}

	public Object getTargetUserName(){
		return targetUserName;
	}

	public Object getTopic(){
		return topic;
	}

	public int getSourceMachineID(){
		return sourceMachineID;
	}

	public int getResponseTypeID(){
		return responseTypeID;
	}

	public int getTargetUserID(){
		return targetUserID;
	}
}