package com.operators.reportfieldsformachineinfra;

/**
 * Created by Sergey on 02/08/2016.
 */
public interface ReportFieldsForMachineNetworkBridgeInterface {
    void  getReportFieldsForMachine(String siteUrl, String sessionId, int machineId, GetReportFieldsForMachineCallback callback, int totalRetries, int specificRequestTimeout);
}
