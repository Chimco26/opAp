package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface PostUpdtaeActionsNetworkManager {

    EmeraldPostUpdateActions  emeraldpostUpdateActions(String siteUrl, int timeout, TimeUnit timeUnit);

}
