package com.operators.reportrejectinfra;

/**
 * Created by Sergey on 09/08/2016.
 */
public interface SendReportStopCallback {
    void onSendStopReportSuccess();

    void onSendStopReportFailed(ErrorObjectInterface reason);
}
