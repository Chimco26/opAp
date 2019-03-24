package com.operators.reportrejectinfra;

import com.example.common.callback.ErrorObjectInterface;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface SendReportRejectCallback {

    void onSendReportSuccess(Object errorResponse);

    void onSendReportFailed(ErrorObjectInterface reason);
}
