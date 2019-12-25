package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface GetSimpleNetworkManager {
    EmeraldGetSimple emeraldGetSimple(String siteUrl, int timeout, TimeUnit timeUnit);

}
