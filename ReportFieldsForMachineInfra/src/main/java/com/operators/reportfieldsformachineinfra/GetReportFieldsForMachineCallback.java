package com.operators.reportfieldsformachineinfra;

/**
 * Created by Sergey on 02/08/2016.
 */
public interface GetReportFieldsForMachineCallback {
    void onGetReportFieldsForMachineSuccess(ReportFieldsForMachine reportFieldsForMachine);

    void onGetReportFieldsForMachineFailed(ErrorObjectInterface reason);
}
