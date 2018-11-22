package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface GetIntervalAndTimeOutNetworkManager {

    EmeraldGetIntervalAndTimeOut emeraldGetIntervalAndTimeOut(String siteUrl, int timeout, TimeUnit timeUnit);

}
