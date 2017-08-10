package com.operatorsapp.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RefreshPollingBroadcast extends BroadcastReceiver {

    public static final String ACTION_REFRESH_POLLING = "ACTION_REFRESH_POLLING";

    private RefreshPollingListener mListener;

    public RefreshPollingBroadcast() {
    }

    public RefreshPollingBroadcast(RefreshPollingListener listener) {

        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ACTION_REFRESH_POLLING)) {

                mListener.onRefreshPolling();
            }

    }

    public interface RefreshPollingListener {

        void onRefreshPolling();
    }
}


