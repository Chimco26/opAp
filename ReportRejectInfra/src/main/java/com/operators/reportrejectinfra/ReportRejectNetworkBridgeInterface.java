package com.operators.reportrejectinfra;

import java.lang.annotation.Documented;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface ReportRejectNetworkBridgeInterface {
    void sendReportReject(String siteUrl, String sessionId, String machineId, String operatorId, int rejectReasonId, int RejectCauseId, double units, Double weight,
                          SendReportRejectCallback callback, int totalRetries, int specificRequestTimeout);
}
