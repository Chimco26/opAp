package com.operators.getmachinesnetworkbridge;


import com.operators.getmachinesnetworkbridge.interfaces.GetMachineNetworkManagerInterface;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.getmachinesnetworkbridge.server.requests.GetMachinesRequest;
import com.operators.getmachinesnetworkbridge.server.responses.ErrorResponse;
import com.operators.getmachinesnetworkbridge.server.responses.Machine;
import com.operators.getmachinesnetworkbridge.server.responses.MachinesResponse;
import com.operators.infra.GetMachinesCallback;
import com.operators.infra.GetMachinesNetworkBridgeInterface;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMachinesNetworkBridge implements GetMachinesNetworkBridgeInterface {
    private static final String LOG_TAG = GetMachinesNetworkBridge.class.getSimpleName();

    private GetMachineNetworkManagerInterface mGetMachineNetworkManagerInterface;

    public GetMachinesNetworkBridge() {

    }

    @Override
    public void getMachinesForFactory(String siteUrl, String sessionId, final GetMachinesCallback callback) {
        int specificRequestTimeout = 17;
        GetMachinesRequest getMachinesRequest = new GetMachinesRequest(sessionId);
        Call<MachinesResponse> call = mGetMachineNetworkManagerInterface.getMachinesRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getMachinesForFactory(getMachinesRequest);
        call.enqueue(new Callback<MachinesResponse>() {
            @Override
            public void onResponse(Call<MachinesResponse> call, Response<MachinesResponse> response) {
                ArrayList<Machine> machines = new ArrayList<>();
                for (Machine machine : response.body().getMachines()) {
                    machines.add(machine);
                }
                callback.onLoginSucceeded(machines);
            }

            @Override
            public void onFailure(Call<MachinesResponse> call, Throwable t) {

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
