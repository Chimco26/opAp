package com.operators.reportfieldsformachinenetworkbridge;

import android.util.Log;

import com.operators.reportfieldsformachineinfra.GetReportFieldsForMachineCallback;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachineNetworkBridgeInterface;
import com.operators.reportfieldsformachinenetworkbridge.interfaces.GetReportFieldsForMachineNetworkManagerInterface;
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

    public void inject(GetReportFieldsForMachineNetworkManagerInterface getReportFieldsForMachineNetworkManagerInterface) {
        mGetReportFieldsForMachineNetworkManagerInterface = getReportFieldsForMachineNetworkManagerInterface;
    }

    @Override
    public void getReportFieldsForMachine(String siteUrl, String sessionId, int machineId, final GetReportFieldsForMachineCallback callback, int totalRetries, int specificRequestTimeout) {
        GetReportFieldsForMachineRequest getReportFieldsForMachineRequest = new GetReportFieldsForMachineRequest(sessionId, machineId);
        Call<GetReportFieldsForMachineResponse> call = mGetReportFieldsForMachineNetworkManagerInterface.getReportFieldsForMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout,
                TimeUnit.SECONDS).getReportFieldsForMachine(getReportFieldsForMachineRequest);
        call.enqueue(new Callback<GetReportFieldsForMachineResponse>() {
            @Override
            public void onResponse(Call<GetReportFieldsForMachineResponse> call, Response<GetReportFieldsForMachineResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getReportFieldsForMachine() == null) {
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_report_fields_failed, "Response data is null");
                        callback.onGetReportFieldsForMachineFailed(errorObject);
                    }
                    else {
                        Log.i(LOG_TAG,"getReportFieldsForMachine onResponse Success");
                        callback.onGetReportFieldsForMachineSuccess(response.body().getReportFieldsForMachine());
                    }
                }
                else {
                    ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                    callback.onGetReportFieldsForMachineFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<GetReportFieldsForMachineResponse> call, Throwable t) {
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_report_fields_failed, "General Error");
                callback.onGetReportFieldsForMachineFailed(errorObject);
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
