package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface ReportInventoryNetworkManagerInterface {
    EmeraldSendReportInventory reportInventoryRetroFitServiceRequests(String siteUrl);

    EmeraldSendReportInventory reportInventoryRetroFitServiceRequests(String siteUrl, int timeout, TimeUnit timeUnit);
}
