package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface GetJobDetailsNetworkManager {

    EmeraldGetJobDetails  emeraldGetJobDetails(String siteUrl, int timeout, TimeUnit timeUnit);

}
