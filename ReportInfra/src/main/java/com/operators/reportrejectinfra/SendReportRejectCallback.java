package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface SendReportRejectCallback {

    void onSendReportSuccess(StandardResponse errorResponse);

    void onSendReportFailed(StandardResponse reason);
}
