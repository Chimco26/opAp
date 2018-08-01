package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

/**
 * Created by alex on 01/08/2018.
 */

public interface PostUpdateNotesForJobNetworkManager {
    EmeraldPostUpdateNotesForJob emeraldPostUpdateNotesForJob(String siteUrl, int timeout, TimeUnit timeUnit);

}
