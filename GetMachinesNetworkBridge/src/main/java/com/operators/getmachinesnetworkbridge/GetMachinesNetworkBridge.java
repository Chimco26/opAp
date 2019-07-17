package com.operators.getmachinesnetworkbridge;


import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.getmachinesnetworkbridge.interfaces.GetMachineNetworkManagerInterface;
import com.operators.getmachinesnetworkbridge.server.requests.GetMachinesRequest;
import com.operators.getmachinesnetworkbridge.server.responses.MachinesResponse;
import com.operators.infra.GetMachinesCallback;
import com.operators.infra.GetMachinesNetworkBridgeInterface;
import com.operators.infra.Machine;

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
                if (response.body() != null) {
                    ArrayList<Machine> machines = null;
                    if (response.body().getMachines() != null) {
                        machines = response.body().getMachines();
                    }
                    if (response.body().getError().getErrorDesc() == null) {
                        if (machines != null && machines.size() > 0) {
                            OppAppLogger.getInstance().d(LOG_TAG, "onRequestSucceed(), " + machines.size() + " machines");

                            getMachinesCallback.onGetMachinesSucceeded(machines);

                        } else {
                            OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), list null or empty");
                            StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.No_data, "list null or empty");
                            getMachinesCallback.onGetMachinesFailed(errorObject);
                        }
                    } else {
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequest(), getMachines failed");
                        StandardResponse errorObject = new StandardResponse();
                        errorObject.getError().setDefaultErrorCodeConstant(response.body().getError().getErrorCode());
                        getMachinesCallback.onGetMachinesFailed(errorObject);
                    }
                }
            }

            @Override
            public void onFailure(Call<MachinesResponse> call, Throwable t) {
                if (retryCount++ < totalRetries) {
                    OppAppLogger.getInstance().v(LOG_TAG, "Retrying... (" + retryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "General Error");
                    getMachinesCallback.onGetMachinesFailed(errorObject);
                }
            }
        });
    }

    public void inject(GetMachineNetworkManagerInterface getMachineNetworkManagerInterface) {
        mGetMachineNetworkManagerInterface = getMachineNetworkManagerInterface;
    }
}
