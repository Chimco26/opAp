package com.operators.reportrejectinfra;

import com.operators.errorobject.ErrorObjectInterface;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface SendReportRejectCallback {

    void onSendReportSuccess();

    void onSendReportFailed(ErrorObjectInterface reason);
}
