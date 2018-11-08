package com.operatorsapp.utils;

/**
 * Created by alex on 11/10/2018.
 */

public class Consts {

    public static final int NOTIFICATION_RESPONSE_TYPE_UNSET = 0;
    public static final int NOTIFICATION_RESPONSE_TYPE_APPROVE = 1;
    public static final int NOTIFICATION_RESPONSE_TYPE_DECLINE = 2;
    public static final int NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS = 3;
    public static final int NOTIFICATION_RESPONSE_TYPE_START_SERVICE = 4;
    public static final int NOTIFICATION_RESPONSE_TYPE_END_SERVICE = 5;

    public static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    public static final int NOTIFICATION_TYPE_FROM_WEB = 1;
    public static final int NOTIFICATION_TYPE_TECHNICIAN = 2;
    public static final int NOTIFICATION_TYPE_REAL_TIME = 3;

    public static final int NOTIFICATION_RESPONSE_TARGET_WEB = 1;
    public static final int NOTIFICATION_RESPONSE_TARGET_TECHNICIAN = 2;
    public static final int NOTIFICATION_RESPONSE_TARGET_REAL_TIME = 3;

    public static final String NOTIFICATION_TECHNICIAN_STATUS = "NOTIFICATION_TECHNICIAN_STATUS";
    public static final int NOTIFICATION_TECHNICIAN_STATUS_DEFAULT = 0;
    public static final int NOTIFICATION_TECHNICIAN_STATUS_CALLED = 1;
    public static final int NOTIFICATION_TECHNICIAN_STATUS_RECEIVED = 2;
    public static final int NOTIFICATION_TECHNICIAN_STATUS_DECLINED = 3;
    public static final String NOTIFICATION_BROADCAST_NAME = "NOTIFICATION_BROADCAST_NAME";
    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String NOTIFICATION_TECHNICIAN_NAME = "NOTIFICATION_TECHNICIAN_NAME";

    public enum NotificationType
    {
        NotificationFromWeb(1),
        CallForTechnicianFromOperatorApp(2),
        NotificationFromRealTime(3);

        public int mType;

        NotificationType(int mType) {
            this.mType = mType;
        }
    }

    public enum ResponseTarget
    {
        WEB(1),
        MANAGER(2),
        OPERATOR(3);

        private final int mType;

        ResponseTarget(int mType) {
            this.mType = mType;
        }
    }
}
