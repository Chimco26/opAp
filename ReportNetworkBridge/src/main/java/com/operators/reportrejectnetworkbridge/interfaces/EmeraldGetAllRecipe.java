package com.operators.reportrejectnetworkbridge.interfaces;

import com.example.common.StandardResponse;
import com.example.common.request.BaseRequest;
import com.example.common.request.RecipeUpdateRequest;
import com.operators.reportrejectnetworkbridge.server.request.GetAllRecipesRequest;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetAllRecipe {

    @POST("/LeaderMESApi/GetJobRecipeWithEdit")
    Call<RecipeResponse> getAllRecipesRequest(@Body GetAllRecipesRequest getAllRecipesRequest);

    @POST("/LeaderMESApi/UpdateProductRecipeJobOpApp")
    Call<StandardResponse> updateRecipe(@Body RecipeUpdateRequest recipeUpdateRequest);

    @POST("/LeaderMESApi/GetRecipeTranslation")
    Call<StandardResponse> updateRecipe(@Body BaseRequest request);

}
