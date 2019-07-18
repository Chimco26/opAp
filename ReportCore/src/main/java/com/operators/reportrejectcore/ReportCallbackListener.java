package com.operators.reportrejectcore;

import com.example.common.StandardResponse;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface ReportCallbackListener {

    void sendReportSuccess(StandardResponse object);

    void sendReportFailure(StandardResponse reason);
}
