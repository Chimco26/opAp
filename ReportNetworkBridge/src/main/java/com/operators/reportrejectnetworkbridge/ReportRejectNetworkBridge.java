package com.operators.reportrejectnetworkbridge;

import com.operators.reportrejectinfra.ReportRejectNetworkBridgeInterface;
import com.operators.reportrejectinfra.SendReportCallback;
import com.operators.reportrejectinfra.SendReportRejectCallback;
import com.operators.reportrejectinfra.SendReportStopCallback;
import com.operators.reportrejectnetworkbridge.interfaces.ReportCycleUnitsNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportInventoryNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportRejectNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportStopNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.server.ErrorObject;
import com.operators.reportrejectnetworkbridge.server.request.SendReportCycleUnitsRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportInventoryRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportRejectRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportStopRequest;
import com.operators.reportrejectnetworkbridge.server.response.SendReportCycleUnitsResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportInventoryResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportRejectResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportStopResponse;
import com.zemingo.logrecorder.ZLogger;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sergey on 08/08/2016.
 */
public class ReportRejectNetworkBridge implements ReportRejectNetworkBridgeInterface {
    private static final String LOG_TAG = ReportRejectNetworkBridge.class.getSimpleName();
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


    @Override
    public void sendReportStop(String siteUrl, String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, int eventId, Integer jobId, final SendReportStopCallback callback, final int totalRetries, int specificRequestTimeout) {
        SendReportStopRequest sendReportStopRequest = new SendReportStopRequest(sessionId, machineId, operatorId, stopReasonId, stopSubReasonId, eventId, jobId);
        final int[] retryCount = {0};
        Call<SendReportStopResponse> call = mReportStopNetworkManagerInterface.reportStopRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendStopReport(sendReportStopRequest);
        call.enqueue(new Callback<SendReportStopResponse>() {
            @Override
            public void onResponse(Call<SendReportStopResponse> call, Response<SendReportStopResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (callback != null) {
                            callback.onSendStopReportSuccess();
                        } else {
                            ZLogger.w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                        }
                    }
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
                            callback.onSendReportSuccess();
                        } else {
                            ZLogger.w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                        }
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
                            callback.onSendReportSuccess();
                        } else {
                            ZLogger.w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                        }
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
                            callback.onSendReportSuccess();
                        } else {
                            ZLogger.w(LOG_TAG, "sendReportInventory(), onResponse() callback is null");
                        }
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
