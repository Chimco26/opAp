package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface SendReportCallback {
    void onSendReportSuccess(StandardResponse o);

    void onSendReportFailed(StandardResponse reason);
}
