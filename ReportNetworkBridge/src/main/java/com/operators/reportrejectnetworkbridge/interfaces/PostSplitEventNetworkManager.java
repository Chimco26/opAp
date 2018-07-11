package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 05/07/2018.
 */

public interface PostSplitEventNetworkManager {

    EmeraldPostSplitEvent emeraldPostSplitEvent(String siteUrl, int timeout, TimeUnit timeUnit);

}
