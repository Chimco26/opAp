package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

/**
 * Created by Sergey on 09/08/2016.
 */
public interface SendReportStopCallback {
    void onSendStopReportSuccess(Object o);

    void onSendStopReportFailed(ErrorObjectInterface reason);
}
