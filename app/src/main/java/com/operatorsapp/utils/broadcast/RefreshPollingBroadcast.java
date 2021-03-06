package com.operatorsapp.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RefreshPollingBroadcast extends BroadcastReceiver {

    public static final String ACTION_REFRESH_POLLING = "ACTION_REFRESH_POLLING";

    private RefreshPollingListener mListener;

    public RefreshPollingBroadcast(RefreshPollingListener listener) {

        mListener = listener;
    }

    public RefreshPollingBroadcast() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.getAction() != null &&
                    intent.getAction().equals(ACTION_REFRESH_POLLING)) {

                mListener.onRefreshPolling();
            }

    }

    public interface RefreshPollingListener {

        void onRefreshPolling();
    }
}


