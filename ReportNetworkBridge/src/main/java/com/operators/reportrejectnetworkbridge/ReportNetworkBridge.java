package com.operators.reportrejectnetworkbridge;

import androidx.annotation.NonNull;

import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.reportrejectinfra.ReportRejectNetworkBridgeInterface;
import com.operators.reportrejectinfra.SendReportCallback;
import com.operators.reportrejectinfra.SendReportRejectCallback;
import com.operators.reportrejectinfra.SendReportStopCallback;
import com.operators.reportrejectnetworkbridge.interfaces.ApproveFirstItemNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportCycleUnitsNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportInventoryNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportRejectNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportStopNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.server.request.SendApproveFirstItemRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendMultipleStopRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportCycleUnitsRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportInventoryRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportRejectRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportStopRequest;

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
    public void sendReportStop(String siteUrl, String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, long eventId, Integer jobId, final SendReportStopCallback callback, final int totalRetries, int specificRequestTimeout) {

        SendReportStopRequest sendReportStopRequest = new SendReportStopRequest(sessionId, machineId, operatorId, stopReasonId, stopSubReasonId, jobId, eventId);

        final int[] retryCount = {0};

        Call<StandardResponse> call = mReportStopNetworkManagerInterface.reportStopRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendStopReport(sendReportStopRequest);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                if (response.isSuccessful()) {
                    if (callback != null) {

                        callback.onSendStopReportSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
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
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendStopReportFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });

    }

    public void sendMultipleReportStop(String siteUrl, String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, long[] eventId, Integer jobId, final SendReportStopCallback callback, final int totalRetries, int specificRequestTimeout) {

        SendMultipleStopRequest sendMultipleStopRequest = new SendMultipleStopRequest(sessionId, machineId, operatorId, stopReasonId, stopSubReasonId, jobId, eventId, false, false);

        final int[] retryCount = {0};

        Call<StandardResponse> call = mReportStopNetworkManagerInterface.reportStopRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendMultipleStopReport(sendMultipleStopRequest);

        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                if (response.isSuccessful() && response.body() != null && response.body().getError().getErrorCode() == 0) {
                    if (callback != null) {

                        callback.onSendStopReportSuccess(response.body());
                    } else {

                        OppAppLogger.getInstance().w(LOG_TAG, "sendMultipleReportReject(), onResponse() callback is null");
                    }
                } else {
                    String msg = "response not successful";
                    if (response!=null && response.body() != null){
                        msg = response.body().getError().getErrorDesc();
                    }

                    onFailure(call, new Throwable(msg));
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
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, t.getMessage());
                        callback.onSendStopReportFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "sendMultipleReportReject(), onFailure() callback is null");

                }
            }
        });
    }


    @Override
    public void sendReportReject(String siteUrl, String sessionId, String machineId, String operatorId, int rejectReasonId, int rejectCauseId, Double units, Double weight, Integer joshId, final SendReportRejectCallback callback, final int totalRetries, int specificRequestTimeout) {
        SendReportRejectRequest sendReportRejectRequest = new SendReportRejectRequest(sessionId, machineId, operatorId, rejectReasonId, units, rejectCauseId, weight, joshId);
        final int[] retryCount = {0};
        Call<StandardResponse> call = mReportRejectNetworkManagerInterface.reportRejectRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendReportReject(sendReportRejectRequest);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getFunctionSucceed()
                        && (response.body().getError().getErrorDesc() == null || response.body().getError().getErrorCode() == 0)) {
                    if (callback != null) {
                        callback.onSendReportSuccess(response.body());
                    } else {
                        OppAppLogger.getInstance().w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                    }
                } else {
                    String error = "";
                    if (response != null && response.body() != null){
                        error = response.body().getError().getErrorDesc();
                    }
                    onFailure(call, new Exception(error));
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
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, t.getMessage());
                        callback.onSendReportFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });
    }

    @Override
    public void sendApproveFirstItem(String siteUrl, String sessionId, String machineId, String operatorId, int rejectReasonId, int aprovingTechnicianId, Integer jobId, final SendReportCallback callback, final int totalRetries, int specificRequestTimeout) {
        SendApproveFirstItemRequest sendApproveFirstItemRequest = new SendApproveFirstItemRequest(sessionId, machineId, operatorId, rejectReasonId, aprovingTechnicianId, jobId);
        final int[] retryCount = {0};
        Call<StandardResponse> call = mApproveFirstItemNetworkManagerInterface.approveFirstItemRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendApproveFirstItem(sendApproveFirstItemRequest);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        callback.onSendReportSuccess(response.body());
                    } else {
                        OppAppLogger.getInstance().w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
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
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendReportFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });
    }


    @Override
    public void sendReportCycleUnits(String siteUrl, String sessionId, String machineId, String operatorId, double unitsPerCycle, Integer jobId, final SendReportCallback callback, final int totalRetries, int specificRequestTimeout) {
        SendReportCycleUnitsRequest sendReportCycleUnitsRequest = new SendReportCycleUnitsRequest(sessionId, machineId, operatorId, unitsPerCycle, jobId);
        final int[] retryCount = {0};
        Call<StandardResponse> call = mReportCycleUnitsNetworkManagerInterface.reportCycleUnitsRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendReportCycleUnits(sendReportCycleUnitsRequest);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        callback.onSendReportSuccess(response.body());
                    } else {
                        OppAppLogger.getInstance().w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
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
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendReportFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });
    }

    @Override
    public void sendReportInventory(String siteUrl, String sessionId, String machineId, String operatorId, int packageTypeId, int units, Integer jobId, final SendReportCallback callback, final int totalRetries, int specificRequestTimeout) {
        SendReportInventoryRequest sendReportInventoryRequest = new SendReportInventoryRequest(sessionId, machineId, operatorId, packageTypeId, units, jobId);
        final int[] retryCount = {0};
        Call<StandardResponse> call = mReportInventoryNetworkManagerInterface.reportInventoryRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendReportInventory(sendReportInventoryRequest);
        call.enqueue(new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        callback.onSendReportSuccess(response.body());
                    } else {
                        OppAppLogger.getInstance().w(LOG_TAG, "sendReportInventory(), onResponse() callback is null");
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
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Send_Report_Failed Error");
                        callback.onSendReportFailed(errorObject);
                    }
                } else {
                    OppAppLogger.getInstance().w(LOG_TAG, "sendReportInventory(), onFailure() callback is null");

                }
            }
        });
    }

}
