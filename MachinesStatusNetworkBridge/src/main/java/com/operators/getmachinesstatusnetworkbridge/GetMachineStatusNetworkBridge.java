package com.operators.getmachinesstatusnetworkbridge;


import android.util.Log;

import com.operators.getmachinesstatusnetworkbridge.interfaces.GetMachineStatusNetworkManagerInterface;
import com.operators.getmachinesstatusnetworkbridge.server.ErrorObject;
import com.operators.getmachinesstatusnetworkbridge.server.requests.GetMachineStatusDataRequest;
import com.operators.getmachinesstatusnetworkbridge.server.responses.ErrorResponse;
import com.operators.getmachinesstatusnetworkbridge.server.responses.MachineStatusDataResponse;

import com.operators.machinestatusinfra.interfaces.GetMachineStatusCallback;
import com.operators.machinestatusinfra.interfaces.GetMachineStatusNetworkBridgeInterface;
import com.operators.machinestatusinfra.models.MachineStatus;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMachineStatusNetworkBridge implements GetMachineStatusNetworkBridgeInterface {
    private static final String LOG_TAG = GetMachineStatusNetworkBridge.class.getSimpleName();
    private GetMachineStatusNetworkManagerInterface mGetMachineStatusNetworkManagerInterface;

    private int mRetryCount = 0;

    public void inject(GetMachineStatusNetworkManagerInterface getMachineStatusNetworkManager) {
        mGetMachineStatusNetworkManagerInterface = getMachineStatusNetworkManager;
        Log.i(LOG_TAG, " GetMachineStatusNetworkBridge inject()");
    }

    @Override
    public void getMachineStatus(String siteUrl, String sessionId, int machineId, final GetMachineStatusCallback getMachineStatusCallback, final int totalRetries, int specificRequestTimeout) {
        GetMachineStatusDataRequest getMachineStatusDataRequest = new GetMachineStatusDataRequest(sessionId, machineId);
        Call<MachineStatusDataResponse> call = mGetMachineStatusNetworkManagerInterface.getMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS)
                .getMachineStatus(getMachineStatusDataRequest);
        call.enqueue(new Callback<MachineStatusDataResponse>() {
            @Override
            public void onResponse(Call<MachineStatusDataResponse> call, Response<MachineStatusDataResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getMachineStatus().getAllMachinesData().size() == 0) {
                        Log.d(LOG_TAG, "getMachineStatus, onResponse(),  " + "getAllMachinesData size = 0");
//                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_machines_failed, response.body().getErrorResponse().getErrorDesc());
//                        getMachineStatusCallback.onGetMachineStatusFailed(errorObject);
                    } else {
                        Log.d(LOG_TAG, "getMachineStatus, onResponse(),  " + "getAllMachinesData size = " + response.body().getMachineStatus().getAllMachinesData().size());
                    }
                    MachineStatus machineStatus = response.body().getMachineStatus();
                    getMachineStatusCallback.onGetMachineStatusSucceeded(machineStatus);
                } else {
                    Log.d(LOG_TAG, "getMachineStatus, onResponse(),  " + "isSuccessful = false");
                    ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                    getMachineStatusCallback.onGetMachineStatusFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<MachineStatusDataResponse> call, Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    Log.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    Log.d(LOG_TAG, "getMachineStatus, onFailure " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_machines_failed, "Get_machines_failed Error");
                    getMachineStatusCallback.onGetMachineStatusFailed(errorObject);
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
