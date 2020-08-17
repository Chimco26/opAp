package com.operators.getmachinesstatusnetworkbridge;


import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.getmachinesstatusnetworkbridge.interfaces.GetMachineStatusNetworkManagerInterface;
import com.operators.getmachinesstatusnetworkbridge.server.requests.GetMachineStatusDataRequest;
import com.operators.getmachinesstatusnetworkbridge.server.requests.SetProductionModeForMachineRequest;
import com.operators.getmachinesstatusnetworkbridge.server.responses.MachineStatusDataResponse;
import com.operators.machinestatusinfra.interfaces.GetMachineStatusCallback;
import com.operators.machinestatusinfra.interfaces.GetMachineStatusNetworkBridgeInterface;
import com.operators.machinestatusinfra.models.MachineStatus;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMachineStatusNetworkBridge implements GetMachineStatusNetworkBridgeInterface
{
    private static final String LOG_TAG = GetMachineStatusNetworkBridge.class.getSimpleName();
    private GetMachineStatusNetworkManagerInterface mGetMachineStatusNetworkManagerInterface;

    private int mRetryCount = 0;

    public void inject(GetMachineStatusNetworkManagerInterface getMachineStatusNetworkManager)
    {
        mGetMachineStatusNetworkManagerInterface = getMachineStatusNetworkManager;
        OppAppLogger.i(LOG_TAG, " GetMachineStatusNetworkBridge inject()");
    }

    @Override
    public void getMachineStatus(String siteUrl, String sessionId, int machineId, final GetMachineStatusCallback getMachineStatusCallback, final int totalRetries, int specificRequestTimeout, Integer joshID)
    {
        GetMachineStatusDataRequest getMachineStatusDataRequest = new GetMachineStatusDataRequest(sessionId, machineId, joshID);
        Call<MachineStatusDataResponse> call = mGetMachineStatusNetworkManagerInterface.getMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getMachineStatus(getMachineStatusDataRequest);
        call.enqueue(new Callback<MachineStatusDataResponse>()
        {
            @Override
            public void onResponse(Call<MachineStatusDataResponse> call, Response<MachineStatusDataResponse> response)
            {

                if(response.isSuccessful())
                {
                    if(response.body() != null && response.body().getMachineStatus() != null)
                    {
                        MachineStatus machineStatus = response.body().getMachineStatus();
                        machineStatus.setTaskCountObject(response.body().getTaskCountObject());

                        if(response.body().getMachineStatus().getAllMachinesData().size() == 0)
                        {
                            OppAppLogger.d(LOG_TAG, "getMachineStatus, onResponse(),  " + "getAllMachinesData size = 0");
                            //                        StandardResponse errorObject = new StandardResponse(ErrorObject.ErrorCode.Get_machines_failed, response.body().getErrorResponse().getErrorDesc());
                            //                        getMachineStatusCallback.onGetMachineStatusFailed(errorObject);
                        }
                        else
                        {
                            machineStatus.setAllowReportingOnSetupEvents(response.body().getmAllMachinesData().get(0).isAllowReportingOnSetupEvents());
                            machineStatus.setAllowReportingSetupAfterSetupEnd(response.body().getmAllMachinesData().get(0).ismAllowReportingSetupAfterSetupEnd());
                            OppAppLogger.d(LOG_TAG, "getMachineStatus, onResponse(),  " + "getAllMachinesData size = " + response.body().getMachineStatus().getAllMachinesData().size());
                        }
                        getMachineStatusCallback.onGetMachineStatusSucceeded(machineStatus);
                    }
                    else
                    {
                        OppAppLogger.d(LOG_TAG, "getShiftLog , onResponse - getMachineStatus failed Error");
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "getMachineData failed Error");
                        getMachineStatusCallback.onGetMachineStatusFailed(errorObject);
                    }
                }
                else
                {
                    OppAppLogger.d(LOG_TAG, "getMachineStatus, onResponse(),  " + "isSuccessful = false");
                    if(response.body() != null && response.body().getError() != null)
                    {
                        StandardResponse errorObject = new StandardResponse();
                        errorObject.getError().setDefaultErrorCodeConstant(response.body().getError().getErrorCode());
                        getMachineStatusCallback.onGetMachineStatusFailed(errorObject);
                    }
                }
            }

            @Override
            public void onFailure(Call<MachineStatusDataResponse> call, Throwable t)
            {
                if(mRetryCount++ < totalRetries)
                {
                    OppAppLogger.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                }
                else
                {
                    mRetryCount = 0;
                    OppAppLogger.d(LOG_TAG, "getMachineStatus, onFailure " + t.getMessage());
                    StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Get_machines_failed Error");
                    getMachineStatusCallback.onGetMachineStatusFailed(errorObject);
                }
            }
        });
    }

    @Override
    public void setProductionModeForMachine(String siteUrl,int specificRequestTimeout, String sessionId, int machineId, int productionModeId)
    {
        SetProductionModeForMachineRequest setProductionModeForMachineRequest = new SetProductionModeForMachineRequest(sessionId, machineId, productionModeId);
        Call<StandardResponse> call = mGetMachineStatusNetworkManagerInterface.postProductionModeForMachineRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).postProductionModeForMachine(setProductionModeForMachineRequest);
        call.enqueue(new Callback<StandardResponse>(){

            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {

            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {

            }
        });
    }

}
