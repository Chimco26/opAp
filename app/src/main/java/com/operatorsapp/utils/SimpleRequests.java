package com.operatorsapp.utils;

import androidx.annotation.NonNull;

import com.example.common.ErrorResponse;
import com.example.common.MultipleRejectRequestModel;
import com.example.common.StandardResponse;
import com.example.common.StopLogs.StopLogsResponse;
import com.example.common.callback.CreateTaskCallback;
import com.example.common.callback.ErrorObjectInterface;
import com.example.common.callback.GetDepartmentCallback;
import com.example.common.callback.GetMachineLineCallback;
import com.example.common.callback.GetShiftWorkersCallback;
import com.example.common.callback.GetStopLogCallback;
import com.example.common.callback.GetTaskListCallback;
import com.example.common.department.DepartmentsMachinesResponse;
import com.example.common.department.MachineLineRequest;
import com.example.common.department.MachineLineResponse;
import com.example.common.machineData.ShiftOperatorResponse;
import com.example.common.operator.SaveShiftWorkersRequest;
import com.example.common.request.BaseRequest;
import com.example.common.request.MachineIdRequest;
import com.example.common.request.RecipeUpdateRequest;
import com.example.common.task.CreateTaskHistoryRequest;
import com.example.common.task.CreateTaskRequest;
import com.example.common.task.GetTaskFilesRequest;
import com.example.common.task.Task;
import com.example.common.task.TaskDefaultRequest;
import com.example.common.task.TaskFilesResponse;
import com.example.common.task.TaskHistory;
import com.example.common.task.TaskListResponse;
import com.example.common.task.TaskObjectsForCreateOrEditResponse;
import com.example.oppapplog.OppAppLogger;
import com.operators.getmachinesstatusnetworkbridge.interfaces.GetMachineStatusNetworkManagerInterface;
import com.operators.getmachinesstatusnetworkbridge.server.requests.SetProductionModeForMachineRequest;
import com.operators.reportrejectinfra.GetAllRecipeCallback;
import com.operators.reportrejectinfra.GetJobDetailsCallback;
import com.operators.reportrejectinfra.GetPendingJobListCallback;
import com.operators.reportrejectinfra.GetVersionCallback;
import com.operators.reportrejectinfra.PostActivateJobCallback;
import com.operators.reportrejectinfra.PostSplitEventCallback;
import com.operators.reportrejectinfra.PostUpdtaeActionsCallback;
import com.operators.reportrejectinfra.SimpleCallback;
import com.operators.reportrejectnetworkbridge.interfaces.GetIntervalAndTimeOutNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetJobDetailsNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetPendingJobListNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetReportMultipleRequestNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetSimpleNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.GetTaskFilesCallback;
import com.operators.reportrejectnetworkbridge.interfaces.GetTaskObjectsForCreateCallback;
import com.operators.reportrejectnetworkbridge.interfaces.GetVersionNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostActivateJobNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostSplitEventNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostUpdateNotesForJobNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.PostUpdtaeActionsNetworkManager;
import com.operators.reportrejectnetworkbridge.interfaces.RecipeNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.UpdateTaskStatusCallback;
import com.operators.reportrejectnetworkbridge.server.request.GetAllRecipesRequest;
import com.operators.reportrejectnetworkbridge.server.request.PostUpdateNotesForJobRequest;
import com.operators.reportrejectnetworkbridge.server.request.SessionIdModel;
import com.operators.reportrejectnetworkbridge.server.request.SplitEventRequest;
import com.operators.reportrejectnetworkbridge.server.response.IntervalAndTimeOutResponse;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.VersionResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActionsUpdateRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.GetPendingJobListRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsRequest;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.JobDetailsStandardResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJobStandardResponse;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.callback.PostProductionModeCallback;
import com.operatorsapp.server.callback.PostUpdateNotesForJobCallback;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleRequests {

    private static final String LOG_TAG = SimpleRequests.class.getSimpleName();

    public SimpleRequests() {

    }

    public void getAllRecipe(String siteUrl, String sessionId, Integer jobId, final GetAllRecipeCallback callback, RecipeNetworkManagerInterface recipeNetworkManagerInterface, final int totalRetries, int specificRequestTimeout) {

        GetAllRecipesRequest getAllRecipesRequest = new GetAllRecipesRequest(sessionId, jobId);

        final int[] retryCount = {0};

        Call<RecipeResponse> call = recipeNetworkManagerInterface.emeraldGetAllRecipe(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getAllRecipesRequest(getAllRecipesRequest);

        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeResponse> call, @NonNull Response<RecipeResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onGetAllRecipeSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getAllRecipesRequest(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<RecipeResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getAllRecipesRequest_Failed Error");
                        callback.onGetAllRecipeFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getAllRecipesRequest(), onFailure() callback is null");

                }
            }
        });
    }

    public void updateRecipe(String siteUrl, RecipeUpdateRequest recipeUpdateRequest, final SimpleCallback callback, RecipeNetworkManagerInterface recipeNetworkManagerInterface, final int totalRetries, int specificRequestTimeout) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = recipeNetworkManagerInterface.emeraldGetAllRecipe(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).updateRecipe(recipeUpdateRequest);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                if (response.body() != null && response.body().getError().getErrorDesc() == null) {
                    if (callback != null) {

                        callback.onRequestSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "updateRecipe(), onResponse() callback is null");
                    }
                } else {

                    if (response.body() != null && response.body().getError().getErrorDesc() != null) {
                        onFailure(call, new Exception(response.body().getError().getErrorDesc()));
                    } else {
                        onFailure(call, new Exception("response not successful"));
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, t.getMessage());
                        callback.onRequestFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "updateRecipe(), onFailure() callback is null");

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

                        OppAppLogger.getInstance().w(LOG_TAG, "getAllRecipesRequest(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<VersionResponse>> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getAllRecipesRequest_Failed Error");
                        callback.onGetVersionFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getAllRecipesRequest(), onFailure() callback is null");

                }
            }
        });
    }

    public static void getDepartmentsMachines(String siteUrl, final GetDepartmentCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<DepartmentsMachinesResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl, requestTimeout, TimeUnit.SECONDS).getAllDepartmentsRequest(new BaseRequest(PersistenceManager.getInstance().getSessionId()));

        call.enqueue(new Callback<DepartmentsMachinesResponse>() {
            @Override
            public void onResponse(@NonNull Call<DepartmentsMachinesResponse> call, @NonNull Response<DepartmentsMachinesResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onGetDepartmentSuccess((DepartmentsMachinesResponse) response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getDepartmentsMachines(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<DepartmentsMachinesResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getDepartmentsMachines_Failed Error");
                        callback.onGetDepartmentFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getDepartmentsMachines(), onFailure() callback is null");

                }
            }
        });
    }

    public static void getMachineLine(String siteUrl, final GetMachineLineCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<MachineLineResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl,
                requestTimeout, TimeUnit.SECONDS).getMachineLineRequest(new MachineLineRequest(PersistenceManager.getInstance().getSessionId(), PersistenceManager.getInstance().getMachineLineId()));

        call.enqueue(new Callback<MachineLineResponse>() {
            @Override
            public void onResponse(@NonNull Call<MachineLineResponse> call, @NonNull Response<MachineLineResponse> response) {

                if (response.body() != null && (response.body().getError().getErrorDesc() == null || response.body().getError().getErrorDesc().isEmpty())) {
                    if (callback != null) {

                        callback.onGetMachineLineSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getMachineLine(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<MachineLineResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getMachineLine Error");
                        callback.onGetMachineLineFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getMachineLine(), onFailure() callback is null");

                }
            }
        });
    }

    public static void getLineShiftLog(String siteUrl, final GetStopLogCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<StopLogsResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl,
                requestTimeout, TimeUnit.SECONDS).GetLineShiftLog(new MachineLineRequest(PersistenceManager.getInstance().getSessionId(), PersistenceManager.getInstance().getMachineLineId()));

        call.enqueue(new Callback<StopLogsResponse>() {
            @Override
            public void onResponse(@NonNull Call<StopLogsResponse> call, @NonNull Response<StopLogsResponse> response) {

                if (response.body().getError().getErrorDesc() == null || response.body().getError().getErrorDesc().isEmpty()) {
                    if (callback != null) {

                        callback.onGetStopLogSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getLineShiftLog(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StopLogsResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getLineShiftLog Error");
                        callback.onGetStopLogFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getLineShiftLog(), onFailure() callback is null");

                }
            }
        });
    }


    public static void getShiftWorkers(String siteUrl, final GetShiftWorkersCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<ShiftOperatorResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl,
                requestTimeout, TimeUnit.SECONDS).GetShiftWorkers(new MachineIdRequest(String.valueOf(PersistenceManager.getInstance().getMachineId()), PersistenceManager.getInstance().getSessionId()));

        call.enqueue(new Callback<ShiftOperatorResponse>() {
            @Override
            public void onResponse(@NonNull Call<ShiftOperatorResponse> call, @NonNull Response<ShiftOperatorResponse> response) {

                if (response.body().getError().getErrorDesc() == null || response.body().getError().getErrorDesc().isEmpty()) {
                    if (callback != null) {

                        callback.onGetShiftWorkersSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getShiftWorkers(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<ShiftOperatorResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getShiftWorkers Error");
                        callback.onGetShiftWorkersFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getShiftWorkers(), onFailure() callback is null");

                }
            }
        });
    }

    public static void saveShiftWorkers(SaveShiftWorkersRequest request, String siteUrl, final com.example.common.callback.SimpleCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl,
                requestTimeout, TimeUnit.SECONDS).saveShiftWorkers(request);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                if (response.body() != null && response.body().getError().getErrorDesc() == null || response.body().getError().getErrorDesc().isEmpty()) {
                    if (callback != null) {

                        callback.onRequestSuccess(response.body());
                    } else {

                        onFailure(call, new Exception("response not successful"));
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "saveShiftWorkers Error");
                        callback.onRequestFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "saveShiftWorkers(), onFailure() callback is null");

                }
            }
        });
    }

    public void getIntervalAndTimeout(String siteUrl, String sessionId, final GetVersionCallback callback, GetIntervalAndTimeOutNetworkManager getIntervalAndTimeOutNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<IntervalAndTimeOutResponse> call = getIntervalAndTimeOutNetworkManager.emeraldGetIntervalAndTimeOut(siteUrl, requestTimeout, TimeUnit.SECONDS).getIntervalAndTimeOutRequest(new SessionIdModel(Double.valueOf(sessionId)));

        call.enqueue(new Callback<IntervalAndTimeOutResponse>() {
            @Override
            public void onResponse(@NonNull Call<IntervalAndTimeOutResponse> call, @NonNull Response<IntervalAndTimeOutResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onGetVersionSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getAllRecipesRequest(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<IntervalAndTimeOutResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getAllRecipesRequest_Failed Error");
                        callback.onGetVersionFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getAllRecipesRequest(), onFailure() callback is null");

                }
            }
        });
    }

    public void postUpdateNotesForJob(String siteUrl, final PostUpdateNotesForJobCallback callback, PostUpdateNotesForJobNetworkManager postUpdateNotesForJobNetworkManager,
                                      PostUpdateNotesForJobRequest postUpdateNotesForJobRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = postUpdateNotesForJobNetworkManager.emeraldPostUpdateNotesForJob(siteUrl, requestTimeout, TimeUnit.SECONDS).postUpdateNotesForJobRequest(postUpdateNotesForJobRequest);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                if (response.isSuccessful() && response.body() != null &&
                        response.body().getFunctionSucceed()) {
                    if (callback != null) {

                        callback.onUpdateNotesSuccess(response.body());

                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "postUpdateNotesForJob(), onResponse() callback is null");
                    }
                } else {

                    if (callback != null) {
                        StandardResponse errorObject = null;
                        if (response.body() != null) {
                            errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, response.body().getError().getErrorDesc());
                        }

                        callback.onUpdateNotesFailed(errorObject);
                    }

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "postUpdateNotesForJob Error");
                        callback.onUpdateNotesFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "postUpdateNotesForJob(), onFailure() callback is null");

                }
            }
        });


    }

    public void getPendingJobList(String siteUrl, final GetPendingJobListCallback callback, GetPendingJobListNetworkManager getPendingJobListNetworkManager,
                                  GetPendingJobListRequest getPendingJobListRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<PendingJobStandardResponse> call = getPendingJobListNetworkManager.emeraldGetPendingJobList(siteUrl, requestTimeout, TimeUnit.SECONDS).getPendingJobListRequest(getPendingJobListRequest);

        call.enqueue(new Callback<PendingJobStandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<PendingJobStandardResponse> call, @NonNull Response<PendingJobStandardResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onGetPendingJobListSuccess(response.body());

                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getPendingJobList(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<PendingJobStandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getPendingJobList_Failed Error");
                        callback.onGetPendingJobListFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getPendingJobList(), onFailure() callback is null");

                }
            }
        });
    }

    public void reportMultipleRejects(String siteUrl, final SimpleCallback callback, GetReportMultipleRequestNetworkManager getReportMultipleRequestNetworkManager,
                                      MultipleRejectRequestModel multipleRejectRequestModel, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = getReportMultipleRequestNetworkManager.emeraldReportMultipleRejects(siteUrl, requestTimeout, TimeUnit.SECONDS).reportMultipleRejects(multipleRejectRequestModel);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onRequestSuccess(response.body());

                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "reportMultipleRejects(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "reportMultipleRejects Error");
                        callback.onRequestFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "reportMultipleRejects(), onFailure() callback is null");

                }
            }
        });
    }

    public void getJobDetails(String siteUrl, final GetJobDetailsCallback callback, GetJobDetailsNetworkManager getJobDetailsNetworkManager,
                              JobDetailsRequest jobDetailsRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<JobDetailsStandardResponse> call = getJobDetailsNetworkManager.emeraldGetJobDetails(siteUrl, requestTimeout, TimeUnit.SECONDS).getPendingJobListRequest(jobDetailsRequest);

        call.enqueue(new Callback<JobDetailsStandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<JobDetailsStandardResponse> call, @NonNull Response<JobDetailsStandardResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onGetJobDetailsSuccess(response.body());

                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getJobDetails(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<JobDetailsStandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getJobDeatils_Failed Error");
                        callback.onGetJobDetailsFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getJobDetails(), onFailure() callback is null");

                }
            }
        });
    }

    public void postUpdateActions(String siteUrl, final PostUpdtaeActionsCallback callback, PostUpdtaeActionsNetworkManager postUpdateActionsNetworkManager,
                                  ActionsUpdateRequest actionsUpdateRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = postUpdateActionsNetworkManager.emeraldpostUpdateActions(siteUrl, requestTimeout, TimeUnit.SECONDS).postUpdtaeActionsRequest(actionsUpdateRequest);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call,
                                   @NonNull Response<StandardResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onPostUpdtaeActionsSuccess(response.body());

                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "postUpdateActions(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "postUpdtaeActions_Failed Error");
                        callback.onPostUpdtaeActionsFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "postUpdateActions(), onFailure() callback is null");

                }
            }
        });
    }

    public void postActivateJob(String siteUrl, final PostActivateJobCallback callback, PostActivateJobNetworkManager postActivateJobNetworkManager,
                                ActivateJobRequest activateJobRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = postActivateJobNetworkManager.emeraldPostActivateJob(siteUrl, requestTimeout, TimeUnit.SECONDS).postActivateJobRequest(activateJobRequest);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call,
                                   @NonNull Response<StandardResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onPostActivateJobSuccess(response.body());

                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "PostActivateJob(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "PostActivateJob Failed Error");
                        callback.onPostActivateJobFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "PostActivateJob(), onFailure() callback is null");

                }
            }
        });
    }

    public void postSplitEvent(String siteUrl, final PostSplitEventCallback callback, PostSplitEventNetworkManager postSplitEventNetworkManager,
                               SplitEventRequest splitEventRequest, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = postSplitEventNetworkManager.emeraldPostSplitEvent(siteUrl, requestTimeout, TimeUnit.SECONDS).postSplitEvent(splitEventRequest);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response != null && response.body() != null && response.body().getFunctionSucceed()) {
                    if (callback != null) {

                        callback.onPostSplitEventSuccess(response.body());

                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "PostSplit(), onResponse() callback is null");
                    }
                } else {
                    String msg = "";
                    if (response != null && response.body() != null && response.body().getError() != null) {
                        msg = response.body().getError().getErrorDesc();
                    }
                    onFailure(call, new Throwable(msg));
                    //onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, t.getMessage());
                        callback.onPostSplitEventFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "PostSplitEvent(), onFailure() callback is null");

                }
            }
        });

    }

    public void postProductionMode(String siteUrl, final PostProductionModeCallback callback, GetMachineStatusNetworkManagerInterface machineStatusNetworkBridge,
                                   SetProductionModeForMachineRequest productionModeForMachineRequest, final int totalRetries) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = machineStatusNetworkBridge.postProductionModeForMachineRetroFitServiceRequests(siteUrl).postProductionModeForMachine(productionModeForMachineRequest);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getFunctionSucceed()) {
                    if (callback != null) {

                        callback.onPostProductionModeSuccess(response.body());

                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "PostProductionMode(), onResponse() callback is null");
                    }
                } else {

                    String msg = "Production Mode Update Failed";
                    if (response.body() != null && response.body().getError() != null) {
                        msg = response.body().getError().getErrorDesc();
                    }
                    StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, msg);
//                    callback.onPostProductionModeFailed(errorObject);
                    onFailure(call, new Exception(errorObject.getError().getErrorDesc()));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, t.getMessage());
                        callback.onPostProductionModeFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "PostProductionMode(), onFailure() callback is null");

                }
            }
        });

    }

    public static void getTaskList(String siteUrl, final GetTaskListCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<TaskListResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl,
                requestTimeout, TimeUnit.SECONDS).getTaskList(new TaskDefaultRequest(PersistenceManager.getInstance().getSessionId()));

        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(@NonNull Call<TaskListResponse> call, @NonNull Response<TaskListResponse> response) {

                if (response.body() != null && (response.body().getError().getErrorDesc() == null || response.body().getError().getErrorDesc().isEmpty())) {
                    if (callback != null) {

                        callback.onGetTaskListCallbackSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getTaskList(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<TaskListResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getTaskList Error");
                        callback.onGetTaskListCallbackFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getTaskList(), onFailure() callback is null");

                }
            }
        });
    }

    public static void createOrUpdateTask(Task task, String siteUrl, final CreateTaskCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl,
                requestTimeout, TimeUnit.SECONDS).createOrdUpdateTask(new CreateTaskRequest(PersistenceManager.getInstance().getSessionId(), task));

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                if (response.body() != null && response.body().getError().getErrorDesc() == null || response.body() != null && response.body().getError().getErrorDesc().isEmpty()) {
                    if (callback != null) {

                        callback.onCreateTaskCallbackSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "createOrUpdateTask(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "createOrUpdateTask Error");
                        callback.onCreateTaskCallbackFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "createOrUpdateTask(), onFailure() callback is null");

                }
            }
        });
    }

    public static void getTaskObjectsForCreateOrEdit(String siteUrl, final GetTaskObjectsForCreateCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<TaskObjectsForCreateOrEditResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl,
                requestTimeout, TimeUnit.SECONDS).getTaskObjectsForCreateOrEdit(new TaskDefaultRequest(PersistenceManager.getInstance().getSessionId()));

        call.enqueue(new Callback<TaskObjectsForCreateOrEditResponse>() {
            @Override
            public void onResponse(@NonNull Call<TaskObjectsForCreateOrEditResponse> call, @NonNull Response<TaskObjectsForCreateOrEditResponse> response) {

                if (response.body().getError().getErrorDesc() == null || response.body().getError().getErrorDesc().isEmpty()) {
                    if (callback != null) {

                        callback.onGetTaskObjectsForCreateCallbackSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "getTaskObjectsForCreateOrEdit(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<TaskObjectsForCreateOrEditResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getTaskObjectsForCreateOrEdit Error");
                        callback.onGetTaskObjectsForCreateCallbackFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "getTaskObjectsForCreateOrEdit(), onFailure() callback is null");

                }
            }
        });
    }

    public static void updateTaskStatus(TaskHistory task, String siteUrl, final UpdateTaskStatusCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<StandardResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl,
                requestTimeout, TimeUnit.SECONDS).updateTaskStatus(new CreateTaskHistoryRequest(PersistenceManager.getInstance().getSessionId(), task));

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                if (response.body().getError().getErrorDesc() == null || response.body().getError().getErrorDesc().isEmpty()) {
                    if (callback != null) {

                        callback.onUpdateTaskStatusCallbackSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "updateTaskStatus(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "updateTaskStatus Error");
                        callback.onUpdateTaskStatusCallbackFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "updateTaskStatus(), onFailure() callback is null");

                }
            }
        });
    }

    public static void getTaskFiles(int taskId, String siteUrl, final GetTaskFilesCallback callback, GetSimpleNetworkManager getSimpleNetworkManager, final int totalRetries, int requestTimeout) {

        final int[] retryCount = {0};

        Call<TaskFilesResponse> call = getSimpleNetworkManager.emeraldGetSimple(siteUrl,
                requestTimeout, TimeUnit.SECONDS).getTaskFiles(new GetTaskFilesRequest(PersistenceManager.getInstance().getSessionId(), taskId));

        call.enqueue(new Callback<TaskFilesResponse>() {
            @Override
            public void onResponse(@NonNull Call<TaskFilesResponse> call, @NonNull Response<TaskFilesResponse> response) {

                if (response.body() != null && (response.body().getError().getErrorDesc() == null || response.body().getError().getErrorDesc().isEmpty())) {
                    if (callback != null) {

                        callback.onGetTaskFilesCallbackSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "updateTaskStatus(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(@NonNull Call<TaskFilesResponse> call, @NonNull Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "updateTaskStatus Error");
                        callback.onGetTaskFilesCallbackFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "updateTaskStatus(), onFailure() callback is null");

                }
            }
        });
    }


}
