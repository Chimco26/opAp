package com.operators.machinedatanetworkbridge;


import android.support.annotation.NonNull;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.operators.machinedatainfra.interfaces.GetMachineDataCallback;
import com.operators.machinedatainfra.interfaces.GetMachineDataNetworkBridgeInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinedatanetworkbridge.interfaces.GetMachineDataNetworkManagerInterface;
import com.operators.machinedatanetworkbridge.server.requests.GetMachineDataDataRequest;
import com.operators.machinedatanetworkbridge.server.responses.MachineDataDataResponse;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMachineDataNetworkBridge implements GetMachineDataNetworkBridgeInterface
{
    private static final String LOG_TAG = GetMachineDataNetworkBridge.class.getSimpleName();
    private GetMachineDataNetworkManagerInterface mGetMachineDataNetworkManagerInterface;

    private int mRetryCount = 0;

    public void inject(GetMachineDataNetworkManagerInterface getMachineDataNetworkManagerInterface)
    {
        mGetMachineDataNetworkManagerInterface = getMachineDataNetworkManagerInterface;
        OppAppLogger.getInstance().i(LOG_TAG, " GetMachineStatusNetworkBridge inject()");
    }

    @Override
    public void getMachineData(String siteUrl, String sessionId, int machineId, String startingFrom, final GetMachineDataCallback getMachineDataCallback, final int totalRetries, int specificRequestTimeout, Integer joshID)
    {
        GetMachineDataDataRequest getMachineDataDataRequest = new GetMachineDataDataRequest(sessionId, machineId, startingFrom, String.valueOf(joshID));
        Call<MachineDataDataResponse> call = mGetMachineDataNetworkManagerInterface.getMachineDataRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getMachineData(getMachineDataDataRequest);
        call.enqueue(new Callback<MachineDataDataResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<MachineDataDataResponse> call, @NonNull Response<MachineDataDataResponse> response)
            {

                if(response.body() != null && response.body().getMachineParams() != null && response.body().getError().getErrorDesc() == null)
                {
                    ArrayList<Widget> widgets = response.body().getMachineParams();
                    OppAppLogger.getInstance().d(LOG_TAG, "getMachineData, onResponse " + widgets.size() + " widgets");
                    getMachineDataCallback.onGetMachineDataSucceeded(response.body().getMachineParams());
                }
                else
                {
                    OppAppLogger.getInstance().d(LOG_TAG, "getShiftLog , onResponse - getMachineData failed Error");
                    StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "getMachineData failed Error");
                    getMachineDataCallback.onGetMachineDataFailed(errorObject);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MachineDataDataResponse> call, @NonNull Throwable t)
            {
                if(mRetryCount++ < totalRetries)
                {
                    OppAppLogger.getInstance().d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                }
                else
                {
                    mRetryCount = 0;
                    OppAppLogger.getInstance().d(LOG_TAG, "getMachineData, onFailure " + t.getMessage());
                    StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, t.getMessage());
                    getMachineDataCallback.onGetMachineDataFailed(errorObject);

                }
            }
        });
    }

//    private ErrorObject errorObjectWithErrorCode(ErrorResponse errorResponse)
//    {
//        ErrorObject.ErrorCode code = toCode(errorResponse.getErrorCode());
//        return new ErrorObject(code, errorResponse.getErrorDesc());
//    }
//
//    private ErrorObject.ErrorCode toCode(int errorCode)
//    {
//        switch(errorCode)
//        {
//            case 101:
//                return ErrorObject.ErrorCode.Credentials_mismatch;
//            case 500:
//                return ErrorObject.ErrorCode.Server;
//        }
//        return ErrorObject.ErrorCode.Unknown;
//    }

}
