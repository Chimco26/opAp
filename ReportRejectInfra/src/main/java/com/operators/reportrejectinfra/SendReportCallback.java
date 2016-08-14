package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface SendReportCallback {
    void onSendReportSuccess();

    void onSendReportFailed(ErrorObjectInterface reason);
}
