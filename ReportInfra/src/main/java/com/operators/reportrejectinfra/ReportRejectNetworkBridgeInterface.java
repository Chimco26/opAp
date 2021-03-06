package com.operators.reportrejectinfra;

public interface ReportRejectNetworkBridgeInterface {
    void sendReportReject(String siteUrl, String sessionId, String machineId, String operatorId, int rejectReasonId, int RejectCauseId, Double units, Double weight, Integer jobId,
                          SendReportRejectCallback callback, int totalRetries, int specificRequestTimeout);

    void sendApproveFirstItem(String siteUrl, String sessionId, String machineId, String operatorId, int rejectReasonId, int aprovingTechnicianId, Integer jobId,
                              SendReportCallback callback, int totalRetries, int specificRequestTimeout);

    void sendReportStop(String siteUrl, String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, long eventId, Integer jobId,
                        SendReportStopCallback callback, int totalRetries, int specificRequestTimeout);

    void sendMultipleReportStop(String siteUrl, String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, long[] eventId, Integer jobId,
                                SendReportStopCallback callback, int totalRetries, int specificRequestTimeout, boolean byRootEvent, String notes);

    void sendReportCycleUnits(String siteUrl, String sessionId, String machineId, String operatorId, double unitsPerCycle, Integer jobId,
                              SendReportCallback callback, int totalRetries, int specificRequestTimeout);

    void sendReportFixUnits(String siteUrl, String sessionId, String machineId, String operatorId, double amount, Integer joshId,
                              SendReportCallback callback, int totalRetries, int specificRequestTimeout);

    void sendReportInventory(String siteUrl, String sessionId, String machineId, String operatorId, int packageTypeId, int units, Integer jobId, Integer numOfBatch,
                             SendReportCallback callback, int totalRetries, int specificRequestTimeout);


}
