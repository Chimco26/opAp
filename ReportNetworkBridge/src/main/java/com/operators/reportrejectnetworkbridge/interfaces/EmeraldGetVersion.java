package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.Recipe.VersionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EmeraldGetVersion {

    @GET("/LeaderMESApi/ApiVersion")
    Call<List<VersionResponse>> getAllRecipesRequest();

}
