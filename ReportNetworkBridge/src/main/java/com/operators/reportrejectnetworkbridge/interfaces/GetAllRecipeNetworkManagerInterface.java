package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface GetAllRecipeNetworkManagerInterface {

    EmeraldGetAllRecipe emeraldGetAllRecipe(String siteUrl, int timeout, TimeUnit timeUnit);

}
