package com.operators.reportrejectnetworkbridge.interfaces;

import java.util.concurrent.TimeUnit;

public interface RecipeNetworkManagerInterface {

    EmeraldGetAllRecipe emeraldGetAllRecipe(String siteUrl, int timeout, TimeUnit timeUnit);

}
