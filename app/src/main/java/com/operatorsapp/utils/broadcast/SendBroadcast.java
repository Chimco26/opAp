package com.operatorsapp.utils.broadcast;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import static com.operatorsapp.utils.broadcast.RefreshPollingBroadcast.ACTION_REFRESH_POLLING;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.ACTION_SELECT_REASON;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.EN_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.EVENT_ID;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.E_SUB_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.IL_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.L_SUB_NAME;
import static com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast.REASON_ID;
import static com.operatorsapp.utils.broadcast.SendLogsBroadcast.ACTION_SEND_LOGS;

/**
 * Created by david on 16 יולי 2017.
 */

public class SendBroadcast {

    public static void sendReason(Context context, long eventId, int reasonId, String mEnName, String mILName, String eSubName, String lSubName) {

        Intent intent = new Intent();

        intent.putExtra(EVENT_ID, eventId);

        intent.putExtra(REASON_ID, reasonId);

        intent.putExtra(EN_NAME, mEnName);

        intent.putExtra(IL_NAME, mILName);

        intent.putExtra(L_SUB_NAME, lSubName);

        intent.putExtra(E_SUB_NAME, eSubName);

        intent.setAction(ACTION_SELECT_REASON);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void refreshPolling(Context context) {

        Intent intent = new Intent();

        intent.setAction(ACTION_REFRESH_POLLING);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void SendEmail(Context context) {

        Intent intent = new Intent();

        intent.setAction(ACTION_SEND_LOGS);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }
}
