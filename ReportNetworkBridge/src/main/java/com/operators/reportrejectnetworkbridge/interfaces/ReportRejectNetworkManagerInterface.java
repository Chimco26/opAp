package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sergey on 08/08/2016.
 */
public interface ReportRejectNetworkManagerInterface {
    EmeraldSendReportReject reportRejectRetroFitServiceRequests(String siteUrl);

    EmeraldSendReportReject reportRejectRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
