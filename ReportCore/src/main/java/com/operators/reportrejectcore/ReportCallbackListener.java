package com.operators.reportrejectcore;

import com.operators.errorobject.ErrorObjectInterface;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface ReportCallbackListener {

    void sendReportSuccess(Object errorResponse);

    void sendReportFailure(ErrorObjectInterface reason);
}
