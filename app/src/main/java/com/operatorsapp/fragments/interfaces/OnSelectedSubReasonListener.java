package com.operatorsapp.fragments.interfaces;

import com.operators.reportfieldsformachineinfra.SubReasons;
import com.operatorsapp.server.responses.StopReasonsGroup;

public interface OnSelectedSubReasonListener {
    void onSubReasonSelected(StopReasonsGroup subReasonId);
}
