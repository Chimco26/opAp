package com.operators.reportfieldsformachineinfra;

import com.example.common.StandardResponse;

/**
 * Created by Sergey on 02/08/2016.
 */
public interface GetReportFieldsForMachineCallback {
    void onGetReportFieldsForMachineSuccess(ReportFieldsForMachine reportFieldsForMachine);

    void onGetReportFieldsForMachineFailed(StandardResponse reason);
}
