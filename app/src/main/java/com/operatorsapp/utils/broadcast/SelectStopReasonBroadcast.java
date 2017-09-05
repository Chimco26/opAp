package com.operatorsapp.utils.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SelectStopReasonBroadcast extends BroadcastReceiver {

    public static final String ACTION_SELECT_REASON = "ACTION_SELECT_REASON";

    public static final String EVENT_ID = "EVENT_ID";

    public static final String REASON_ID = "REASON_ID";

    public static final String EN_NAME = "EN_NAME";

    public static final String IL_NAME = "IL_NAME";

    private SelectStopReasonListener mListener;

    public SelectStopReasonBroadcast(SelectStopReasonListener listener) {

        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {

            if (intent.getAction().equals(ACTION_SELECT_REASON)) {

                mListener.onSelectStopReason(intent.getExtras().getInt(EVENT_ID),
                        intent.getExtras().getInt(REASON_ID),
                        intent.getExtras().getString(EN_NAME),
                        intent.getExtras().getString(IL_NAME));
            }
        }
    }


    public interface SelectStopReasonListener {

        void onSelectStopReason(int eventId, int reasonId,String enName,String ilName);
    }
}
