package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface GetReportMultipleRequestNetworkManager {

    EmeraldReportMultipleRejects  emeraldReportMultipleRejects(String siteUrl, int timeout, TimeUnit timeUnit);

}
