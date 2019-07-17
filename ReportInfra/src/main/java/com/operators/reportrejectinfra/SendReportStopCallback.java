package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

/**
 * Created by Sergey on 09/08/2016.
 */
public interface SendReportStopCallback {
    void onSendStopReportSuccess(StandardResponse o);

    void onSendStopReportFailed(StandardResponse reason);
}
