package com.operatorsapp.utils.broadcast;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import static com.operatorsapp.utils.broadcast.RefreshPollingBroadcast.ACTION_REFRESH_POLLING;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.ACTION_SELECT_REASON;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.EVENT_ID;

/**
 * Created by david on 16 יולי 2017.
 */

public class SendBroadcast {

    public static void sendReason(Context context, int eventId) {

        Intent intent = new Intent();

        intent.putExtra(EVENT_ID, eventId);

        intent.setAction(ACTION_SELECT_REASON);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void refreshPolling(Context context) {

        Intent intent = new Intent();

        intent.setAction(ACTION_REFRESH_POLLING);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
