package com.operators.getmachinesnetworkbridge;


import com.operators.errorobject.ErrorObjectInterface;
import com.operators.getmachinesnetworkbridge.interfaces.GetMachineNetworkManagerInterface;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.getmachinesnetworkbridge.server.requests.GetMachinesRequest;
import com.operators.getmachinesnetworkbridge.server.responses.ErrorResponse;
import com.operators.getmachinesnetworkbridge.server.responses.MachinesResponse;
import com.operators.infra.GetMachinesCallback;
import com.operators.infra.GetMachinesNetworkBridgeInterface;
import com.operators.infra.Machine;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMachinesNetworkBridge implements GetMachinesNetworkBridgeInterface {
    private static final String LOG_TAG = GetMachinesNetworkBridge.class.getSimpleName();

    private int retryCount = 0;

    private GetMachineNetworkManagerInterface mGetMachineNetworkManagerInterface;

    @Override
    public void getMachines(String siteUrl, String sessionId, final GetMachinesCallback<Machine> getMachinesCallback, final int totalRetries, int specificRequestTimeout) {
        GetMachinesRequest getMachinesRequest = new GetMachinesRequest(sessionId);
        Call<MachinesResponse> call = mGetMachineNetworkManagerInterface.getMachinesRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getMachinesForFactory(getMachinesRequest);
        call.enqueue(new Callback<MachinesResponse>() {
            @Override
            public void onResponse(Call<MachinesResponse> call, Response<MachinesResponse> response) {
                ArrayList<Machine> machines = null;
                if (response.body().getMachines() != null) {
                    machines = response.body().getMachines();
                }
                if (response.body().getErrorResponse() == null) {
                    if (machines != null && machines.size() > 0) {
                        ZLogger.d(LOG_TAG, "onRequestSucceed(), " + machines.size() + " machines");
                        getMachinesCallback.onGetMachinesSucceeded(machines);
                    } else {
                        ZLogger.d(LOG_TAG, "onRequestFailed(), list null or empty");
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.No_data, "list null or empty");
                        getMachinesCallback.onGetMachinesFailed(errorObject);
                    }
                } else {
                    ZLogger.d(LOG_TAG, "onRequest(), getMachines failed");
                    ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                    getMachinesCallback.onGetMachinesFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<MachinesResponse> call, Throwable t) {
                if (retryCount++ < totalRetries) {
                    ZLogger.v(LOG_TAG, "Retrying... (" + retryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    ZLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, "General Error");
                    getMachinesCallback.onGetMachinesFailed(errorObject);
                }
            }
        });
    }

    public void inject(GetMachineNetworkManagerInterface getMachineNetworkManagerInterface) {
        mGetMachineNetworkManagerInterface = getMachineNetworkManagerInterface;
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
