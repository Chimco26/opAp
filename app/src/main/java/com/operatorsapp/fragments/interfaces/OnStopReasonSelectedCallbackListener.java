package com.operatorsapp.fragments.interfaces;

import com.operators.reportfieldsformachineinfra.SubReasons;

public interface OnStopReasonSelectedCallbackListener {
    void onStopReasonSelected(int position);
    void onUpdateStopReasonSelected(int position);
    void onSubReasonSelected(SubReasons subReasons);
}
