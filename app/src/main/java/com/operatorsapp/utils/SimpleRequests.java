package com.operatorsapp.utils;

import android.support.annotation.NonNull;

import com.operators.reportrejectinfra.GetAllRecipeCallback;
import com.operators.reportrejectinfra.GetVersionCallback;
import com.operators.reportrejectnetworkbridge.interfaces.GetAllRecipeNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.GetVersionNetworkManager;
import com.operators.reportrejectnetworkbridge.server.ErrorObject;
import com.operators.reportrejectnetworkbridge.server.request.GetAllRecipesRequest;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.VersionResponse;
import com.operatorsapp.server.NetworkManager;
import com.zemingo.logrecorder.ZLogger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleRequests {

    private static final String LOG_TAG = SimpleRequests.class.getSimpleName();

    public SimpleRequests(){

    }

    public void getAllRecipe(String siteUrl, String sessionId, Integer jobId, final GetAllRecipeCallback callback, GetAllRecipeNetworkManagerInterface getAllRecipeNetworkManagerInterface, final int totalRetries, int specificRequestTimeout) {

        GetAllRecipesRequest getAllRecipesRequest = new GetAllRecipesRequest(sessionId, jobId);

        final int[] retryCount = {0};

        Call<RecipeResponse> call = getAllRecipeNetworkManagerInterface.emeraldGetAllRecipe(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getAllRecipesRequest(getAllRecipesRequest);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeResponse> call, @NonNull Response<RecipeResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onGetAllRecipeSuccess(response.body());
                    } else {

                        ZLogger.w(LOG_TAG, "getAllRecipesRequest(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "getAllRecipesRequest_Failed Error");
                        callback.onGetAllRecipeFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "getAllRecipesRequest(), onFailure() callback is null");

                }
            }
        });
    }

    public void getVersion(String siteUrl, final GetVersionCallback callback, GetVersionNetworkManager getVersionNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<List<VersionResponse>> call = getVersionNetworkManager.emeraldGetVersion(siteUrl, requestTimeout, TimeUnit.SECONDS).getAllRecipesRequest();

        call.enqueue(new Callback<List<VersionResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<VersionResponse>> call, @NonNull Response<List<VersionResponse>> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onGetVersionSuccess(response.body());
                    } else {

                        ZLogger.w(LOG_TAG, "getAllRecipesRequest(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(Call<List<VersionResponse>> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "getAllRecipesRequest_Failed Error");
                        callback.onGetVersionFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "getAllRecipesRequest(), onFailure() callback is null");

                }
            }
        });
    }
}
