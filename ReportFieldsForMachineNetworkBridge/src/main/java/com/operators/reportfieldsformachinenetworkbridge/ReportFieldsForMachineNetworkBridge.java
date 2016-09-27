package com.operators.reportfieldsformachinenetworkbridge;

import android.util.Log;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportfieldsformachineinfra.GetReportFieldsForMachineCallback;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachineNetworkBridgeInterface;
import com.operators.reportfieldsformachinenetworkbridge.interfaces.GetReportFieldsForMachineNetworkManagerInterface;
import com.operators.reportfieldsformachinenetworkbridge.server.ErrorObject;
import com.operators.reportfieldsformachinenetworkbridge.server.requests.GetReportFieldsForMachineRequest;
import com.operators.reportfieldsformachinenetworkbridge.server.responses.ErrorResponse;
import com.operators.reportfieldsformachinenetworkbridge.server.responses.GetReportFieldsForMachineResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sergey on 02/08/2016.
 */
public class ReportFieldsForMachineNetworkBridge implements ReportFieldsForMachineNetworkBridgeInterface {
    private static final String LOG_TAG = ReportFieldsForMachineNetworkBridge.class.getSimpleName();
    private GetReportFieldsForMachineNetworkManagerInterface mGetReportFieldsForMachineNetworkManagerInterface;

    private int mRetryCount = 0;

    public void inject(GetReportFieldsForMachineNetworkManagerInterface getReportFieldsForMachineNetworkManagerInterface) {
        mGetReportFieldsForMachineNetworkManagerInterface = getReportFieldsForMachineNetworkManagerInterface;
    }

    @Override
    public void getReportFieldsForMachine(String siteUrl, String sessionId, int machineId, final GetReportFieldsForMachineCallback callback, final int totalRetries, int specificRequestTimeout) {
        GetReportFieldsForMachineRequest getReportFieldsForMachineRequest = new GetReportFieldsForMachineRequest(sessionId, machineId);
        Call<GetReportFieldsForMachineResponse> call = mGetReportFieldsForMachineNetworkManagerInterface.getReportFieldsForMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout,
                TimeUnit.SECONDS).getReportFieldsForMachine(getReportFieldsForMachineRequest);
        call.enqueue(new Callback<GetReportFieldsForMachineResponse>() {
            @Override
            public void onResponse(Call<GetReportFieldsForMachineResponse> call, Response<GetReportFieldsForMachineResponse> response) {
                if (callback != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getReportFieldsForMachine() == null) {
                            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.No_data, "Response data is null");
                            callback.onGetReportFieldsForMachineFailed(errorObject);
                        } else {
                            Log.i(LOG_TAG, "getReportFieldsForMachine onResponse Success");
                            callback.onGetReportFieldsForMachineSuccess(response.body().getReportFieldsForMachine());
                        }
                    } else {
                        ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                        callback.onGetReportFieldsForMachineFailed(errorObject);
                    }
                } else {
                    Log.i(LOG_TAG, "getReportFieldsForMachine callback is null");
                }
            }

            @Override
            public void onFailure(Call<GetReportFieldsForMachineResponse> call, Throwable t) {
                if (callback != null) {
                    if (mRetryCount++ < totalRetries) {
                        Log.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    } else {
                        mRetryCount = 0;
                        Log.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "Response Error");
                        callback.onGetReportFieldsForMachineFailed(errorObject);
                    }
                } else {
                    Log.i(LOG_TAG, "getReportFieldsForMachine callback is null");
                }
            }
        });
    }

    private ErrorObject errorObjectWithErrorCode(ErrorResponse errorResponse) {
        ErrorObject.ErrorCode code = toCode(errorResponse.getErrorCode());
        return new ErrorObject(code, errorResponse.getErrorDesc());
    }

    private ErrorObject.ErrorCode toCode(int errorCode) {
        switch (errorCode) {
            case 101:
                return ErrorObject.ErrorCode.Credentials_mismatch;
        }
        return ErrorObject.ErrorCode.Unknown;
    }
}
