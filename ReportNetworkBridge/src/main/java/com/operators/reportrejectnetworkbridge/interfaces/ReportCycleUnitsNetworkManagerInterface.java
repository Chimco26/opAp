package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface ReportCycleUnitsNetworkManagerInterface {
    EmeraldSendReportCycleUnits reportCycleUnitsRetroFitServiceRequests(String siteUrl);

    EmeraldSendReportCycleUnits reportCycleUnitsRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
    EmeraldSendReportCycleUnits reportFixUnitsProduced(String siteUrl, int timeout, TimeUnit timeUnit);
}
