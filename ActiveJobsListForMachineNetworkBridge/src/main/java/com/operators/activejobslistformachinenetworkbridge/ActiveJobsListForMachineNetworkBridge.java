package com.operators.activejobslistformachinenetworkbridge;

import androidx.annotation.NonNull;

import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachineCallback;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachineNetworkBridgeInterface;
import com.operators.activejobslistformachinenetworkbridge.interfaces.ActiveJobsListForMachineNetworkManagerInterface;
import com.operators.activejobslistformachinenetworkbridge.server.requests.GetActiveJobsListForMachineRequest;
import com.operators.activejobslistformachinenetworkbridge.server.responses.ActiveJobsListForMachineResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sergey on 14/08/2016
 */
public class ActiveJobsListForMachineNetworkBridge implements ActiveJobsListForMachineNetworkBridgeInterface {
    private static final String LOG_TAG = ActiveJobsListForMachineNetworkBridge.class.getSimpleName();
    private ActiveJobsListForMachineNetworkManagerInterface mActiveJobsListForMachineNetworkManagerInterface;
    private int mRetryCount = 0;

    public void inject(ActiveJobsListForMachineNetworkManagerInterface activeJobsListForMachineNetworkManagerInterface) {
        OppAppLogger.i(LOG_TAG, "ActiveJoshListForMachineNetworkBridge inject()");
        mActiveJobsListForMachineNetworkManagerInterface = activeJobsListForMachineNetworkManagerInterface;
    }

    @Override
    public void getActiveJobsForMachine(String siteUrl, String sessionId, int machineId, final ActiveJobsListForMachineCallback callback, final int totalRetries, int specificRequestTimeout) {
        GetActiveJobsListForMachineRequest getActiveJobsListForMachineRequest = new GetActiveJobsListForMachineRequest(sessionId, machineId);
        Call<ActiveJobsListForMachineResponse> call = mActiveJobsListForMachineNetworkManagerInterface.getActiveJobListForMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getActiveJobsForMachine(getActiveJobsListForMachineRequest);
        call.enqueue(new Callback<ActiveJobsListForMachineResponse>() {
            @Override
            public void onResponse(@NonNull Call<ActiveJobsListForMachineResponse> call, @NonNull Response<ActiveJobsListForMachineResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getActiveJobsListForMachine() != null) {
                        ActiveJobsListForMachine activeJobsListForMachine = response.body().getActiveJobsListForMachine();
                        if (callback != null) {
                            if (activeJobsListForMachine != null && activeJobsListForMachine.getActiveJobs().size() > 0) {
                                callback.onGetActiveJobsListForMachineSuccess(activeJobsListForMachine);
                            } else {
                                StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.No_data, "Response is null Error");
                                callback.onGetActiveJobsListForMachineFailed(errorObject);
                            }
                        } else {
                            OppAppLogger.w(LOG_TAG, "getActiveJoshForMachine() callback is null");

                        }
                    } else {
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.No_data, "Response is null Error");
                        callback.onGetActiveJobsListForMachineFailed(errorObject);
                    }
                } else {

                    StandardResponse errorObject = new StandardResponse();

                    if (response.body() != null) {
                        errorObject.getError().setDefaultErrorCodeConstant(response.body().getError().getErrorCode());
                    } else if (response.raw() != null && response.raw().code() == 401) {
                        errorObject.getError().setErrorCodeConstant(ErrorObjectInterface.ErrorCode.SessionInvalid);
                        errorObject.getError().setErrorDesc(response.raw().message() + "");
                    } else {
                        errorObject.getError().setErrorCodeConstant(ErrorObjectInterface.ErrorCode.No_data);
                        errorObject.getError().setErrorDesc("Response is null Error");
                    }

                    callback.onGetActiveJobsListForMachineFailed(errorObject);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ActiveJobsListForMachineResponse> call, @NonNull Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    OppAppLogger.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    OppAppLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Response failure");
                    callback.onGetActiveJobsListForMachineFailed(errorObject);
                }
            }
        });
    }
}
