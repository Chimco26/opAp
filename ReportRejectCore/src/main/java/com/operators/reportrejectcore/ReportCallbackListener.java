package com.operators.reportrejectcore;

import com.operators.reportrejectinfra.ErrorObjectInterface;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface ReportCallbackListener {

    void sendReportSuccess();

    void sendReportFailure(ErrorObjectInterface reason);
}
