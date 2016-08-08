package com.operators.reportrejectcore;

import com.operators.reportrejectinfra.ErrorObjectInterface;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface ReportRejectCallbackListener {

    void sendReportRejectSuccess();

    void sendReportRejectFailure(ErrorObjectInterface reason);
}
