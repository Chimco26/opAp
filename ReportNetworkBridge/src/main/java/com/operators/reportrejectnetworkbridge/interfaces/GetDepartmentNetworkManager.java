package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface GetDepartmentNetworkManager {
    EmeraldGetDepartment emeraldGetDepartment(String siteUrl, int timeout, TimeUnit timeUnit);

}
