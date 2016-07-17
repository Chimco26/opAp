package com.operators.getmachinesstatusnetworkbridge;


import android.util.Log;

import com.google.gson.Gson;
import com.operators.getmachinesstatusnetworkbridge.interfaces.GetMachineStatusNetworkManagerInterface;
import com.operators.getmachinesstatusnetworkbridge.server.requests.GetMachineStatusDataRequest;
import com.operators.getmachinesstatusnetworkbridge.server.responses.MachineStatusDataResponse;
import com.operators.infra.GetMachineStatusCallback;
import com.operators.infra.GetMachineStatusNetworkBridgeInterface;
import com.operators.infra.GetMachinesCallback;
import com.operators.infra.Machine;
import com.operators.infra.MachineStatus;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMachineStatusNetworkBridge implements GetMachineStatusNetworkBridgeInterface
{
    private static final String LOG_TAG = GetMachineStatusNetworkBridge.class.getSimpleName();

    GetMachineStatusNetworkManagerInterface mGetMachineStatusNetworkManagerInterface;

    public void inject(GetMachineStatusNetworkManagerInterface getMachineStatusNetworkManagerInterface)
    {
        mGetMachineStatusNetworkManagerInterface = getMachineStatusNetworkManagerInterface;
        Log.i(LOG_TAG, " GetMachineStatusNetworkBridge inject()");
    }

    @Override
    public void getMachineStatus(String siteUrl, String sessionId, String machineId, final GetMachineStatusCallback callback, int totalRetries, int specificRequestTimeout)
    {
        GetMachineStatusDataRequest getMachineStatusDataRequest = new GetMachineStatusDataRequest(sessionId, machineId);
        Call<MachineStatusDataResponse> call = mGetMachineStatusNetworkManagerInterface.getMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getMachineStatus(getMachineStatusDataRequest);
        call.enqueue(new Callback<MachineStatusDataResponse>()
        {
            @Override
            public void onResponse(Call<MachineStatusDataResponse> call, Response<MachineStatusDataResponse> response)
            {
                MachineStatus machineStatus = response.body().getMachineStatus();
                callback.onGetMachineStatusSucceeded(machineStatus);
            }

            @Override
            public void onFailure(Call<MachineStatusDataResponse> call, Throwable t)
            {
                callback.onGetMachineStatusFailed();
            }
        });
    }
}
