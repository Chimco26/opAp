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

    public NotificationHistoryResponse(ArrayList<Notification> mNotificationsList) {
        this.mNotificationsList = mNotificationsList;
    }

    public NotificationHistoryResponse() {
        mNotificationsList = new ArrayList<>();
    }

    public ArrayList<Notification> getmNotificationsList() {
        return mNotificationsList;
    }

    public void setmNotificationsList(ArrayList<Notification> mNotificationsList) {
        this.mNotificationsList = mNotificationsList;
    }

//    public class Notification {
//
//        @SerializedName("Text")
//        private String mBody;
//
//        @SerializedName("SourceUserName")
//        private String mSender;
//
//        @SerializedName("TargetUserName")
//        private String mTargetName;
//
//        @SerializedName("SentTime")
//        private String mSentTime;
//
//        @SerializedName("ResponseType")
//        private int mResponseType;
//
//        @SerializedName("ResponseDate")
//        private String mResponseDate;
//
//        @SerializedName("ID")
//        private int mNotificationID;
//
//        public Notification(String mBody, String mSender, String mSentTime, int mResponseType, String mResponseDate, String mTargetName, int mNotificationID) {
//            this.mBody = mBody;
//            this.mSender = mSender;
//            this.mSentTime = mSentTime;
//            this.mResponseType = mResponseType;
//            this.mResponseDate = mResponseDate;
//            this.mTargetName = mTargetName;
//            this.mNotificationID = mNotificationID;
//        }
//
//        public String getmBody() {
//            return mBody;
//        }
//
//        public String getmTargetName() {
//            return mTargetName;
//        }
//
//        public String getmSender() {
//            return mSender;
//        }
//
//        public String getmSentTime() {
//            return mSentTime;
//        }
//
//        public int getmResponseType() {
//            return mResponseType;
//        }
//
//        public String getmResponseDate() {
//            return mResponseDate;
//        }
//
//        public int getmNotificationID() {
//            return mNotificationID;
//        }
//    }
//
//    public void demoNotificationList(){
//
//        mNotificationsList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//
//            //mNotificationsList.add(new NotificationHistoryResponse.Notification("blablablbabla blablabla", "Menahel", "2018-10-01T09:45:00.00", 1, "asdasdasd"));
//        }
//
//
//    }
}
