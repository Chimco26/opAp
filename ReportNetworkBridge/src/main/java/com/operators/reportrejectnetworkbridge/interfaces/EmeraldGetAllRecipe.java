package com.operators.reportrejectnetworkbridge.interfaces;

import com.operators.reportrejectnetworkbridge.server.request.GetAllRecipesRequest;
import com.operators.reportrejectnetworkbridge.server.response.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EmeraldGetAllRecipe {

    @POST("/LeaderMESApi/GetJobRecipe")
    Call<RecipeResponse> getAllRecipesRequest(@Body GetAllRecipesRequest getAllRecipesRequest);

}
