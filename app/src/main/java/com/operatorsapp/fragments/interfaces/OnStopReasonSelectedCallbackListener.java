package com.operatorsapp.fragments.interfaces;

import com.operatorsapp.server.responses.StopReasonsGroup;

public interface OnStopReasonSelectedCallbackListener {
    void onStopReasonSelected(int position);
    void onUpdateStopReasonSelected(int position);
    void onSubReasonSelected(StopReasonsGroup subReasons);
}
