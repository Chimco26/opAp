package com.operators.reportrejectnetworkbridge;

import android.util.Log;

import com.operators.reportrejectinfra.ReportRejectNetworkBridgeInterface;
import com.operators.reportrejectinfra.SendReportRejectCallback;
import com.operators.reportrejectinfra.SendReportStopCallback;
import com.operators.reportrejectnetworkbridge.interfaces.ReportRejectNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.interfaces.ReportStopNetworkManagerInterface;
import com.operators.reportrejectnetworkbridge.server.request.SendReportRejectRequest;
import com.operators.reportrejectnetworkbridge.server.request.SendReportStopRequest;
import com.operators.reportrejectnetworkbridge.server.response.SendReportRejectResponse;
import com.operators.reportrejectnetworkbridge.server.response.SendReportStopResponse;

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

    public void inject(ReportRejectNetworkManagerInterface reportRejectNetworkManagerInterface, ReportStopNetworkManagerInterface reportStopNetworkManagerInterface) {
        mReportRejectNetworkManagerInterface = reportRejectNetworkManagerInterface;
        mReportStopNetworkManagerInterface = reportStopNetworkManagerInterface;
    }


    @Override
    public void sendReportStop(String siteUrl, String sessionId, String machineId, String operatorId, int stopReasonId, int stopSubReasonId, final SendReportStopCallback callback, int totalRetries, int specificRequestTimeout) {
        SendReportStopRequest sendReportStopRequest = new SendReportStopRequest(sessionId, machineId, operatorId, stopReasonId, stopSubReasonId);
        Call<SendReportStopResponse> call = mReportStopNetworkManagerInterface.reportStopRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendStopReport(sendReportStopRequest);
        call.enqueue(new Callback<SendReportStopResponse>() {
            @Override
            public void onResponse(Call<SendReportStopResponse> call, Response<SendReportStopResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (callback != null) {
                            callback.onSendStopReportSuccess();
                        }
                        else {
                            Log.w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SendReportStopResponse> call, Throwable t) {
                if (callback != null) {
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Send_Report_Failed, "Send_Report_Failed Error");
                    callback.onSendStopReportFailed(errorObject);
                }
                else {
                    Log.w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });

    }

    @Override
    public void sendReportReject(String siteUrl, String sessionId, String machineId, String operatorId, int rejectReasonId, int rejectCauseId, double units, Double weight, final SendReportRejectCallback callback, int totalRetries, int specificRequestTimeout) {
        SendReportRejectRequest sendReportRejectRequest = new SendReportRejectRequest(sessionId, machineId, operatorId, rejectReasonId, units, rejectCauseId, weight);
        Call<SendReportRejectResponse> call = mReportRejectNetworkManagerInterface.reportRejectRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).sendReportReject(sendReportRejectRequest);
        call.enqueue(new Callback<SendReportRejectResponse>() {
            @Override
            public void onResponse(Call<SendReportRejectResponse> call, Response<SendReportRejectResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (callback != null) {
                            callback.onSendReportSuccess();
                        }
                        else {
                            Log.w(LOG_TAG, "sendReportReject(), onResponse() callback is null");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SendReportRejectResponse> call, Throwable t) {
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Send_Report_Failed, "General Error");
                if (callback != null) {
                    callback.onSendReportFailed(errorObject);
                }
                else {
                    Log.w(LOG_TAG, "sendReportReject(), onFailure() callback is null");

                }
            }
        });
    }

}
