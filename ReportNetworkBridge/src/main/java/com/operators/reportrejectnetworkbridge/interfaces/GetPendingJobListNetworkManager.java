package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface GetPendingJobListNetworkManager {

    EmeraldGetPendingJobList  emeraldGetPendingJobList(String siteUrl, int timeout, TimeUnit timeUnit);

}
