package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface PostActivateJobNetworkManager {

    EmeraldPostActivateJob  emeraldPostActivateJob(String siteUrl, int timeout, TimeUnit timeUnit);

}
