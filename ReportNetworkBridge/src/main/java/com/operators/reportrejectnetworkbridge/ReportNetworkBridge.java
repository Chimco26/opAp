package com.operators.reportrejectnetworkbridge;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.operators.reportrejectinfra.ReportRejectNetworkBridgeInterface;
import com.operators.reportrejectinfra.SendReportCallback;
import com.operators.reportrejectinfra.SendReportRejectCallback;
import com.operators.reportrejectinfra.SendReportStopCallback;
import com.operators.reportrejectnetworkbridge.interfaces.ApproveFirstItemNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportCycleUnitsNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportInventoryNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportRejectNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportStopNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.server.ErrorObject;
import com.operators.reportrejectnetworkbridge.server.request.GetAllRecipesRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendApproveFirstItemRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendMultipleStopRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportCycleUnitsRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportInventoryRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportRejectRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportStopRequest;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponse;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendApproveFirstItemResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportCycleUnitsResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportInventoryResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportRejectResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportStopResponse;
import com.zemingo.logrecorder.ZLogger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sergey on 08/08/2016.
 */
public class ReportNetworkBridge implements ReportRejectNetworkBridgeInterface {
    private static final String LOG_TAG = ReportNetworkBridge.class.getSimpleName();
    private ApproveFirstItemNetworkManagerInterface mApproveFirstItemNetworkManagerInterface;
    private ReportRejectNetworkManagerInterface mReportRejectNetworkManagerInterface;
    private ReportStopNetworkManagerInterface mReportStopNetworkManagerInterface;
    private ReportCycleUnitsNetworkManagerInterface mReportCycleUnitsNetworkManagerInterface;
    private ReportInventoryNetworkManagerInterface mReportInventoryNetworkManagerInterface;

    public void inject(ReportRejectNetworkManagerInterface reportRejectNetworkManagerInterface, ReportStopNetworkManagerInterface reportStopNetworkManagerInterface) {
        mReportRejectNetworkManagerInterface = reportRejectNetworkManagerInterface;
        mReportStopNetworkManagerInterface = reportStopNetworkManagerInterface;
    }

    public void inject(ReportCycleUnitsNetworkManagerInterface reportCycleUnitsNetworkManagerInterface) {
        mReportCycleUnitsNetworkManagerInterface = reportCycleUnitsNetworkManagerInterface;
    }

    public void injectInventory(ReportInventoryNetworkManagerInterface reportInventoryNetworkManagerInterface) {
        mReportInventoryNetworkManagerInterface = reportInventoryNetworkManagerInterface;
    }

    public void injectApproveFirstItem(ApproveFirstItemNetworkManagerInterface approveFirstItemNetworkManagerInterface) {
        mApproveFirstItemNetworkManagerInterface = approveFirstItemNetworkManagerInterface;
    }


    @Override
    public void sendReportStop(String siteUrl, String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, int eventId, Integer jobId, final SendReportStopCallback callback, final int totalRetries, int specificRequestTimeout) {

        SendReportStopRequest sendReportStopRequest = new SendReportStopRequest(sessionId, machineId, operatorId, stopReasonId, stopSubReasonId, jobId, eventId);

        final int[] retryCount = {0};

        Call<SendReportStopResponse> call = mReportStopNetworkManagerInterface.reportStopRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendStopReport(sendReportStopRequest);

        call.enqueue(new Callback<SendReportStopResponse>() {
            @Override
            public void onResponse(@NonNull Call<SendReportStopResponse> call, @NonNull Response<SendReportStopResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onSendStopReportSuccess(response.body());
                    } else {

                        ZLogger.w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(Call<SendReportStopResponse> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendStopReportFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });

    }

    public void sendMultipleReportStop(String siteUrl, String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, long[] eventId, Integer jobId, final SendReportStopCallback callback, final int totalRetries, int specificRequestTimeout) {

        SendMultipleStopRequest sendMultipleStopRequest = new SendMultipleStopRequest(sessionId, machineId, operatorId, stopReasonId, stopSubReasonId, jobId, eventId);

        final int[] retryCount = {0};

        Call<SendMultipleStopRequest> call = mReportStopNetworkManagerInterface.reportStopRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendMultipleStopReport(sendMultipleStopRequest);

        call.enqueue(new Callback<SendMultipleStopRequest>() {
            @Override
            public void onResponse(@NonNull Call<SendMultipleStopRequest> call, @NonNull Response<SendMultipleStopRequest> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onSendStopReportSuccess(response.body());
                    } else {

                        ZLogger.w(LOG_TAG, "sendMultipleReportReject(), onResponse() callback is null");
                    }
                } else {

                    onFailure(call, new Exception("response not successful"));
                }

            }

            @Override
            public void onFailure(Call<SendMultipleStopRequest> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendStopReportFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "sendMultipleReportReject(), onFailure() callback is null");

                }
            }
        });
    }


    @Override
    public void sendReportReject(String siteUrl, String sessionId, String machineId, String operatorId, int rejectReasonId, int rejectCauseId, Double units, Double weight, Integer joshId, final SendReportRejectCallback callback, final int totalRetries, int specificRequestTimeout) {
        SendReportRejectRequest sendReportRejectRequest = new SendReportRejectRequest(sessionId, machineId, operatorId, rejectReasonId, units, rejectCauseId, weight, joshId);
        final int[] retryCount = {0};
        Call<SendReportRejectResponse> call = mReportRejectNetworkManagerInterface.reportRejectRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendReportReject(sendReportRejectRequest);
        call.enqueue(new Callback<SendReportRejectResponse>() {
            @Override
            public void onResponse(Call<SendReportRejectResponse> call, Response<SendReportRejectResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (callback != null) {
                            callback.onSendReportSuccess(response.body());
                        } else {
                            ZLogger.w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                        }
                    } else {
                        onFailure(call, new Exception("response not successful"));
                    }
                }
            }

            @Override
            public void onFailure(Call<SendReportRejectResponse> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendReportFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });
    }

    @Override
    public void sendApproveFirstItem(String siteUrl, String sessionId, String machineId, String operatorId, int rejectReasonId, int aprovingTechnicianId, Integer jobId, final SendReportCallback callback, final int totalRetries, int specificRequestTimeout) {
        SendApproveFirstItemRequest sendApproveFirstItemRequest = new SendApproveFirstItemRequest(sessionId, machineId, operatorId, rejectReasonId, aprovingTechnicianId, jobId);
        final int[] retryCount = {0};
        Call<SendApproveFirstItemResponse> call = mApproveFirstItemNetworkManagerInterface.approveFirstItemRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendApproveFirstItem(sendApproveFirstItemRequest);
        call.enqueue(new Callback<SendApproveFirstItemResponse>() {
            @Override
            public void onResponse(Call<SendApproveFirstItemResponse> call, Response<SendApproveFirstItemResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (callback != null) {
                            callback.onSendReportSuccess(response.body());
                        } else {
                            ZLogger.w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                        }
                    } else {
                        onFailure(call, new Exception("response not successful"));
                    }
                }
            }

            @Override
            public void onFailure(Call<SendApproveFirstItemResponse> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendReportFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });
    }


    @Override
    public void sendReportCycleUnits(String siteUrl, String sessionId, String machineId, String operatorId, double unitsPerCycle, Integer jobId, final SendReportCallback callback, final int totalRetries, int specificRequestTimeout) {
        SendReportCycleUnitsRequest sendReportCycleUnitsRequest = new SendReportCycleUnitsRequest(sessionId, machineId, operatorId, unitsPerCycle, jobId);
        final int[] retryCount = {0};
        Call<SendReportCycleUnitsResponse> call = mReportCycleUnitsNetworkManagerInterface.reportCycleUnitsRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendReportCycleUnits(sendReportCycleUnitsRequest);
        call.enqueue(new Callback<SendReportCycleUnitsResponse>() {
            @Override
            public void onResponse(Call<SendReportCycleUnitsResponse> call, Response<SendReportCycleUnitsResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (callback != null) {
                            callback.onSendReportSuccess(response.body());
                        } else {
                            ZLogger.w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                        }
                    } else {
                        onFailure(call, new Exception("response not successful"));
                    }
                }
            }

            @Override
            public void onFailure(Call<SendReportCycleUnitsResponse> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendReportFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });
    }

    @Override
    public void sendReportInventory(String siteUrl, String sessionId, String machineId, String operatorId, int packageTypeId, int units, Integer jobId, final SendReportCallback callback, final int totalRetries, int specificRequestTimeout) {
        SendReportInventoryRequest sendReportInventoryRequest = new SendReportInventoryRequest(sessionId, machineId, operatorId, packageTypeId, units, jobId);
        final int[] retryCount = {0};
        Call<SendReportInventoryResponse> call = mReportInventoryNetworkManagerInterface.reportInventoryRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendReportInventory(sendReportInventoryRequest);
        call.enqueue(new Callback<SendReportInventoryResponse>() {
            @Override
            public void onResponse(Call<SendReportInventoryResponse> call, Response<SendReportInventoryResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (callback != null) {
                            callback.onSendReportSuccess(response.body());
                        } else {
                            ZLogger.w(LOG_TAG, "sendReportInventory(), onResponse() callback is null");
                        }
                    } else {
                        onFailure(call, new Exception("response not successful"));
                    }
                }
            }

            @Override
            public void onFailure(Call<SendReportInventoryResponse> call, Throwable t) {
                if (callback != null) {
                    if (retryCount[0]++ < totalRetries) {
                        ZLogger.d(LOG_TAG, "Retrying... (" + retryCount[0] + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        retryCount[0] = 0;
                        ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendReportFailed(errorObject);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "sendReportInventory(), onFailure() callback is null");

                }
            }
        });
    }

}
