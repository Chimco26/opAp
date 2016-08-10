package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sergey on 09/08/2016.
 */
public interface ReportStopNetworkManagerInterface {
    EmeraldSendStopReport reportStopRetroFitServiceRequests(String siteUrl);

    EmeraldSendStopReport reportStopRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
