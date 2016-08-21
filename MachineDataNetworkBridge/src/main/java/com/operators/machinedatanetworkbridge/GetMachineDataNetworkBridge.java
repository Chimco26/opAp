package com.operators.machinedatanetworkbridge;


import android.util.Log;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatainfra.interfaces.GetMachineDataCallback;
import com.operators.machinedatainfra.interfaces.GetMachineDataNetworkBridgeInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinedatanetworkbridge.interfaces.GetMachineDataNetworkManagerInterface;
import com.operators.machinedatanetworkbridge.server.ErrorObject;
import com.operators.machinedatanetworkbridge.server.requests.GetMachineDataDataRequest;
import com.operators.machinedatanetworkbridge.server.responses.ErrorResponse;
import com.operators.machinedatanetworkbridge.server.responses.MachineDataDataResponse;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMachineDataNetworkBridge implements GetMachineDataNetworkBridgeInterface {
    private static final String LOG_TAG = GetMachineDataNetworkBridge.class.getSimpleName();
    private GetMachineDataNetworkManagerInterface mGetMachineDataNetworkManagerInterface;

    private int mRetryCount = 0;

    public void inject(GetMachineDataNetworkManagerInterface getMachineDataNetworkManagerInterface) {
        mGetMachineDataNetworkManagerInterface = getMachineDataNetworkManagerInterface;
        Log.i(LOG_TAG, " GetMachineStatusNetworkBridge inject()");
    }

    @Override
    public void getMachineData(String siteUrl, String sessionId, int machineId, String startingFrom, final GetMachineDataCallback getMachineDataCallback, final int totalRetries, int specificRequestTimeout) {
        GetMachineDataDataRequest getMachineDataDataRequest = new GetMachineDataDataRequest(sessionId, machineId, null);
        Call<MachineDataDataResponse> call = mGetMachineDataNetworkManagerInterface.getMachineDataRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getMachineData(getMachineDataDataRequest);
        call.enqueue(new Callback<MachineDataDataResponse>() {
            @Override
            public void onResponse(Call<MachineDataDataResponse> call, Response<MachineDataDataResponse> response) {
                ArrayList<Widget> widgets = response.body().getMachineParams();
                if (response.body().getErrorResponse() == null) {
                    if (widgets != null && widgets.size() > 0) {
                        ZLogger.d(LOG_TAG, "onRequestSucceed(), " + widgets.size() + " widgets");
                        getMachineDataCallback.onGetMachineDataSucceeded(response.body().getMachineParams());
                    } else {
                        ZLogger.d(LOG_TAG, "onRequestFailed(), list null or empty");
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_machines_failed, "list null or empty");
                        getMachineDataCallback.onGetMachineDataFailed(errorObject);
                    }
                } else {
                    ZLogger.d(LOG_TAG, "onRequest(), getMachineData failed");
                    ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                    getMachineDataCallback.onGetMachineDataFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<MachineDataDataResponse> call, Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    ZLogger.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_machine_parameters_failed, t.getMessage());
                    getMachineDataCallback.onGetMachineDataFailed(errorObject);
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
            case 500:
                return ErrorObject.ErrorCode.Error_rest;
        }
        return ErrorObject.ErrorCode.Unknown;
    }

}
