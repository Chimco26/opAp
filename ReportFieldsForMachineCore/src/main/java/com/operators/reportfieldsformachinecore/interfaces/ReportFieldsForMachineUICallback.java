package com.operators.reportfieldsformachinecore.interfaces;

import com.example.common.StandardResponse;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;

/**
 * Created by Sergey on 02/08/2016.
 */
public interface ReportFieldsForMachineUICallback {
    void onReportFieldsReceivedSuccessfully(ReportFieldsForMachine reportFieldsForMachine);

    void onReportFieldsReceivedSFailure(StandardResponse reason);
}
