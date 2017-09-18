package com.operatorsapp.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SendLogsBroadcast extends BroadcastReceiver {

    public static final String ACTION_SEND_LOGS = "ACTION_SEND_LOGS";

    private SendLogsListener mListener;


    public SendLogsBroadcast() {
    }

    public SendLogsBroadcast(SendLogsListener listener) {

        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_SEND_LOGS)) {

            mListener.onPermissionGranted();
        }

    }

    public interface SendLogsListener {

        void onPermissionGranted();
    }
}
