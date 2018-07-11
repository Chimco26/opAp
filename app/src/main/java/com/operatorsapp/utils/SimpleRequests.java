package com.operatorsapp.utils;

import android.support.annotation.NonNull;

import com.operators.reportrejectinfra.GetAllRecipeCallback;
import com.operators.reportrejectinfra.GetJobDetailsCallback;
import com.operators.reportrejectinfra.GetPendingJobListCallback;
import com.operators.reportrejectinfra.GetVersionCallback;
import com.operators.reportrejectinfra.PostActivateJobCallback;
import com.operators.reportrejectinfra.PostSplitEventCallback;
import com.operators.reportrejectinfra.PostUpdtaeActionsCallback;
import com.operators.reportrejectnetworkbridge.interfaces.GetAllRecipeNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.GetJobDetailsNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetPendingJobListNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetVersionNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostActivateJobNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostSplitEventNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostUpdtaeActionsNetworkManager;
import com.operators.reportrejectnetworkbridge.server.ErrorObject;
import com.operators.reportrejectnetworkbridge.server.request.GetAllRecipesRequest;
import com.operators.reportrejectnetworkbridge.server.request.SplitEventRequest;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.VersionResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActionsUpdateRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.GetPendingJobListRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobResponse;
import com.zemingo.logrecorder.ZLogger;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleRequests {

    private static final String LOG_TAG = SimpleRequests.class.getSimpleName();

    public SimpleRequests() {

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

    public void getPendingJobList(String siteUrl, final GetPendingJobListCallback callback, GetPendingJobListNetworkManager getPendingJobListNetworkManager,
                                  GetPendingJobListRequest getPendingJobListRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<PendingJobResponse> call = getPendingJobListNetworkManager.emeraldGetPendingJobList(siteUrl, requestTimeout, TimeUnit.SECONDS).getPendingJobListRequest(getPendingJobListRequest);

        call.enqueue(new Callback<PendingJobResponse>() {
            @Override
            public void onResponse(@NonNull Call<PendingJobResponse> call, @NonNull Response<PendingJobResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onGetPendingJobListSuccess(response.body());

                    } else {

                        ZLogger.w(LOG_TAG, "getPendingJobList(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(Call<PendingJobResponse> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "getPendingJobList_Failed Error");
                        callback.onGetPendingJobListFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "getPendingJobList(), onFailure() callback is null");

                }
            }
        });
    }

    public void getJobDetails(String siteUrl, final GetJobDetailsCallback callback, GetJobDetailsNetworkManager getJobDetailsNetworkManager,
                              JobDetailsRequest jobDetailsRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<JobDetailsResponse> call = getJobDetailsNetworkManager.emeraldGetJobDetails(siteUrl, requestTimeout, TimeUnit.SECONDS).getPendingJobListRequest(jobDetailsRequest);

        call.enqueue(new Callback<JobDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<JobDetailsResponse> call, @NonNull Response<JobDetailsResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onGetJobDetailsSuccess(response.body());

                    } else {

                        ZLogger.w(LOG_TAG, "getJobDetails(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(Call<JobDetailsResponse> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "getJobDeatils_Failed Error");
                        callback.onGetJobDetailsFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "getJobDetails(), onFailure() callback is null");

                }
            }
        });
    }

    public void postUpdtaeActions(String siteUrl, final PostUpdtaeActionsCallback callback, PostUpdtaeActionsNetworkManager postUpdateActionsNetworkManager,
                                  ActionsUpdateRequest actionsUpdateRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response> call = postUpdateActionsNetworkManager.emeraldpostUpdateActions(siteUrl, requestTimeout, TimeUnit.SECONDS).postUpdtaeActionsRequest(actionsUpdateRequest);

        call.enqueue(new Callback<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response>() {
            @Override
            public void onResponse(@NonNull Call<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response> call,
                                   @NonNull Response<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onPostUpdtaeActionsSuccess(response.body());

                    } else {

                        ZLogger.w(LOG_TAG, "postUpdtaeActions(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(Call<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "postUpdtaeActions_Failed Error");
                        callback.onPostUpdtaeActionsFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "postUpdtaeActions(), onFailure() callback is null");

                }
            }
        });
    }

    public void postActivateJob(String siteUrl, final PostActivateJobCallback callback, PostActivateJobNetworkManager postActivateJobNetworkManager,
                                ActivateJobRequest activateJobRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response> call = postActivateJobNetworkManager.emeraldPostActivateJob(siteUrl, requestTimeout, TimeUnit.SECONDS).postActivateJobRequest(activateJobRequest);

        call.enqueue(new Callback<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response>() {
            @Override
            public void onResponse(@NonNull Call<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response> call,
                                   @NonNull Response<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onPostActivateJobSuccess(response.body());

                    } else {

                        ZLogger.w(LOG_TAG, "PostActivateJob(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(Call<com.operators.reportrejectnetworkbridge.server.response.activateJob.Response> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "PostActivateJob Failed Error");
                        callback.onPostActivateJobFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "PostActivateJob(), onFailure() callback is null");

                }
            }
        });
    }

    public void postSplitEvent(String siteUrl, final PostSplitEventCallback callback, PostSplitEventNetworkManager postSplitEventNetworkManager,
                               SplitEventRequest splitEventRequest, final int totalRetries, int requestTimeout){

        final int[] retryCount = {0};

        Call<String> call = postSplitEventNetworkManager.emeraldPostSplitEvent(siteUrl, requestTimeout, TimeUnit.SECONDS).postSplitEvent(splitEventRequest);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onPostSplitEventSuccess(response.body());

                    } else {

                        ZLogger.w(LOG_TAG, "PostSplit(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "PostSplitEvent_Failed Error");
                        callback.onPostSplitEventFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "PostSplitEvent(), onFailure() callback is null");

                }
            }
        });

    }
}
