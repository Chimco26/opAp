package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface SendReportCallback {
    void onSendReportSuccess(Object o);

    void onSendReportFailed(ErrorObjectInterface reason);
}
