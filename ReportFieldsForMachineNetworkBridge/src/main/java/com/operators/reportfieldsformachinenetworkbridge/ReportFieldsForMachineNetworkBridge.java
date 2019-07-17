package com.operators.reportfieldsformachinenetworkbridge;

import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.reportfieldsformachineinfra.GetReportFieldsForMachineCallback;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachineNetworkBridgeInterface;
import com.operators.reportfieldsformachinenetworkbridge.interfaces.GetReportFieldsForMachineNetworkManagerInterface;
import com.operators.reportfieldsformachinenetworkbridge.server.requests.GetReportFieldsForMachineRequest;
import com.operators.reportfieldsformachinenetworkbridge.server.responses.GetReportFieldsForMachineResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sergey on 02/08/2016.
 */
public class ReportFieldsForMachineNetworkBridge implements ReportFieldsForMachineNetworkBridgeInterface
{
    private static final String LOG_TAG = ReportFieldsForMachineNetworkBridge.class.getSimpleName();
    private GetReportFieldsForMachineNetworkManagerInterface mGetReportFieldsForMachineNetworkManagerInterface;

    private int mRetryCount = 0;

    public void inject(GetReportFieldsForMachineNetworkManagerInterface getReportFieldsForMachineNetworkManagerInterface)
    {
        mGetReportFieldsForMachineNetworkManagerInterface = getReportFieldsForMachineNetworkManagerInterface;
    }

    @Override
    public void getReportFieldsForMachine(String siteUrl, String sessionId, int machineId, final GetReportFieldsForMachineCallback callback, final int totalRetries, int specificRequestTimeout)
    {
        GetReportFieldsForMachineRequest getReportFieldsForMachineRequest = new GetReportFieldsForMachineRequest(sessionId, machineId);
        Call<GetReportFieldsForMachineResponse> call = mGetReportFieldsForMachineNetworkManagerInterface.getReportFieldsForMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getReportFieldsForMachine(getReportFieldsForMachineRequest);
        call.enqueue(new Callback<GetReportFieldsForMachineResponse>()
        {
            @Override
            public void onResponse(Call<GetReportFieldsForMachineResponse> call, Response<GetReportFieldsForMachineResponse> response)
            {
                if(callback != null)
                {
                    if(response.isSuccessful())
                    {
                        if(response.body() == null || response.body().getReportFieldsForMachine() == null)
                        {
                            StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.No_data, "Response data is null");
                            callback.onGetReportFieldsForMachineFailed(errorObject);
                        }
                        else
                        {
                            OppAppLogger.getInstance().i(LOG_TAG, "getReportFieldsForMachine onResponse Success");
                            callback.onGetReportFieldsForMachineSuccess(response.body().getReportFieldsForMachine());
                        }
                    }
                    else
                    {
                        if(response.body() != null)
                        {
                            OppAppLogger.getInstance().i(LOG_TAG, "getReportFieldsForMachine onResponse failure");
                            response.body().getError().setDefaultErrorCodeConstant(response.body().getError().getErrorCode());
                            callback.onGetReportFieldsForMachineFailed(response.body());
                        }
                    }
                }
                else
                {
                    OppAppLogger.getInstance().i(LOG_TAG, "getReportFieldsForMachine callback is null");
                }
            }

            @Override
            public void onFailure(Call<GetReportFieldsForMachineResponse> call, Throwable t)
            {
                if(callback != null)
                {
                    if(mRetryCount++ < totalRetries)
                    {
                        OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                        call.clone().enqueue(this);
                    }
                    else
                    {
                        mRetryCount = 0;
                        OppAppLogger.getInstance().d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Response Error");
                        callback.onGetReportFieldsForMachineFailed(errorObject);
                    }
                }
                else
                {
                    OppAppLogger.getInstance().i(LOG_TAG, "getReportFieldsForMachine callback is null");
                }

            }
        });
    }

}
