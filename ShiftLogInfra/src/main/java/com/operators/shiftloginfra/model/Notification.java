package com.operators.shiftloginfra.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification implements Parcelable {
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("NotificationID")
    @Expose
    private Integer notificationID;
    @SerializedName("SourceUserName")
    @Expose
    private String sourceUserName;
    @SerializedName("TargetUserName")
    @Expose
    private String targetUserName;
    @SerializedName("SentTime")
    @Expose
    private String sentTime;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("ResponseType")
    @Expose
    private String responseType;
    @SerializedName("ResponseDate")
    @Expose
    private String responseDate;
    @SerializedName("NotificationType")
    @Expose
    private Integer notificationType;
    @SerializedName("SourceMachineID")
    @Expose
    private Integer sourceMachineID;
    @SerializedName("ResponseTypeID")
    @Expose
    private Integer responseTypeID;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("MinutesPassedFromResponse")
    @Expose
    private Integer minutesPassedFromResponse;
    @SerializedName("Topic")
    @Expose
    private String topic;
    @SerializedName("SourceUserID")
    @Expose
    private Integer sourceUserID;
    @SerializedName("TargetUserID")
    @Expose
    private Integer targetUserID;
    @SerializedName("SenderUserID")
    @Expose
    private Integer senderUserID;
    @SerializedName("SenderUserDisplayName")
    @Expose
    private String senderUserDisplayName;
    @SerializedName("SenderUserDisplayHName")
    @Expose
    private String senderUserDisplayHName;
    @SerializedName("TextKeysValues")
    @Expose
    private String textKeysValues;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }

    public String getSourceUserName() {
        return sourceUserName;
    }

    public void setSourceUserName(String sourceUserName) {
        this.sourceUserName = sourceUserName;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    public Integer getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    public Integer getSourceMachineID() {
        return sourceMachineID;
    }

    public void setSourceMachineID(Integer sourceMachineID) {
        this.sourceMachineID = sourceMachineID;
    }

    public Integer getResponseTypeID() {
        return responseTypeID;
    }

    public void setResponseTypeID(Integer responseTypeID) {
        this.responseTypeID = responseTypeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMinutesPassedFromResponse() {
        return minutesPassedFromResponse;
    }

    public void setMinutesPassedFromResponse(Integer minutesPassedFromResponse) {
        this.minutesPassedFromResponse = minutesPassedFromResponse;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getSourceUserID() {
        return sourceUserID;
    }

    public void setSourceUserID(Integer sourceUserID) {
        this.sourceUserID = sourceUserID;
    }

    public Integer getTargetUserID() {
        return targetUserID;
    }

    public void setTargetUserID(Integer targetUserID) {
        this.targetUserID = targetUserID;
    }

    public Integer getSenderUserID() {
        return senderUserID;
    }

    public void setSenderUserID(Integer senderUserID) {
        this.senderUserID = senderUserID;
    }

    public String getSenderUserDisplayName() {
        return senderUserDisplayName;
    }

    public void setSenderUserDisplayName(String senderUserDisplayName) {
        this.senderUserDisplayName = senderUserDisplayName;
    }

    public String getSenderUserDisplayHName() {
        return senderUserDisplayHName;
    }

    public void setSenderUserDisplayHName(String senderUserDisplayHName) {
        this.senderUserDisplayHName = senderUserDisplayHName;
    }

    public String getTextKeysValues() {
        return textKeysValues;
    }

    public void setTextKeysValues(String textKeysValues) {
        this.textKeysValues = textKeysValues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.iD);
        dest.writeValue(this.notificationID);
        dest.writeString(this.sourceUserName);
        dest.writeString(this.targetUserName);
        dest.writeString(this.sentTime);
        dest.writeString(this.text);
        dest.writeString(this.responseType);
        dest.writeString(this.responseDate);
        dest.writeValue(this.notificationType);
        dest.writeValue(this.sourceMachineID);
        dest.writeValue(this.responseTypeID);
        dest.writeString(this.title);
        dest.writeValue(this.minutesPassedFromResponse);
        dest.writeString(this.topic);
        dest.writeValue(this.sourceUserID);
        dest.writeValue(this.targetUserID);
        dest.writeValue(this.senderUserID);
        dest.writeString(this.senderUserDisplayName);
        dest.writeString(this.senderUserDisplayHName);
        dest.writeString(this.textKeysValues);
    }

    public Notification() {
    }

    protected Notification(Parcel in) {
        this.iD = (Integer) in.readValue(Integer.class.getClassLoader());
        this.notificationID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sourceUserName = in.readString();
        this.targetUserName = in.readString();
        this.sentTime = in.readString();
        this.text = in.readString();
        this.responseType = in.readString();
        this.responseDate = in.readString();
        this.notificationType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sourceMachineID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.responseTypeID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.minutesPassedFromResponse = (Integer) in.readValue(Integer.class.getClassLoader());
        this.topic = in.readString();
        this.sourceUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.targetUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.senderUserID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.senderUserDisplayName = in.readString();
        this.senderUserDisplayHName = in.readString();
        this.textKeysValues = in.readString();
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
