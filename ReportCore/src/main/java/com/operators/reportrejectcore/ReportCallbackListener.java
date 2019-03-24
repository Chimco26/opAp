package com.operators.reportrejectcore;

import com.example.common.callback.ErrorObjectInterface;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface ReportCallbackListener {

    void sendReportSuccess(Object object);

    void sendReportFailure(ErrorObjectInterface reason);
}
