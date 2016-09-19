package com.operators.activejobslistformachinenetworkbridge;

import android.util.Log;

import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachineCallback;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachineNetworkBridgeInterface;
import com.operators.activejobslistformachinenetworkbridge.interfaces.ActiveJobsListForMachineNetworkManagerInterface;
import com.operators.activejobslistformachinenetworkbridge.server.ErrorObject;
import com.operators.activejobslistformachinenetworkbridge.server.requests.GetActiveJobsListForMachineRequest;
import com.operators.activejobslistformachinenetworkbridge.server.responses.ActiveJobsListForMachineResponse;
import com.operators.activejobslistformachinenetworkbridge.server.responses.ErrorResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJobsListForMachineNetworkBridge implements ActiveJobsListForMachineNetworkBridgeInterface {
    private static final String LOG_TAG = ActiveJobsListForMachineNetworkBridge.class.getSimpleName();
    private ActiveJobsListForMachineNetworkManagerInterface mActiveJobsListForMachineNetworkManagerInterface;
    private int mRetryCount = 0;

    public void inject(ActiveJobsListForMachineNetworkManagerInterface activeJobsListForMachineNetworkManagerInterface) {
        Log.i(LOG_TAG, "ActiveJoshListForMachineNetworkBridge inject()");
        mActiveJobsListForMachineNetworkManagerInterface = activeJobsListForMachineNetworkManagerInterface;
    }

    @Override
    public void getActiveJobsForMachine(String siteUrl, String sessionId, int machineId, final ActiveJobsListForMachineCallback callback, final int totalRetries, int specificRequestTimeout) {
        GetActiveJobsListForMachineRequest getActiveJobsListForMachineRequest = new GetActiveJobsListForMachineRequest(sessionId, machineId);
        Call<ActiveJobsListForMachineResponse> call = mActiveJobsListForMachineNetworkManagerInterface.getActiveJobListForMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getActiveJobsForMachine(getActiveJobsListForMachineRequest);
        call.enqueue(new Callback<ActiveJobsListForMachineResponse>() {
            @Override
            public void onResponse(Call<ActiveJobsListForMachineResponse> call, Response<ActiveJobsListForMachineResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        ActiveJobsListForMachine activeJobsListForMachine = response.body().getActiveJobsListForMachine();
                        if (callback != null) {
                            if (activeJobsListForMachine != null && activeJobsListForMachine.getActiveJobs().size() > 0) {
                                callback.onGetActiveJobsListForMachineSuccess(activeJobsListForMachine);
                            } else {
                                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_active_jobs_for_machine_failed, "Response is null Error");
                                callback.onGetActiveJobsListForMachineFailed(errorObject);
                            }
                        } else {
                            Log.w(LOG_TAG, "getActiveJoshForMachine() callback is null");
                            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_active_jobs_for_machine_failed, "Response is null Error");
                            callback.onGetActiveJobsListForMachineFailed(errorObject);
                        }
                    } else {
                        ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                        callback.onGetActiveJobsListForMachineFailed(errorObject);
                    }
                } else {
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_active_jobs_for_machine_failed, "Response is null Error");
                    callback.onGetActiveJobsListForMachineFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<ActiveJobsListForMachineResponse> call, Throwable t) {
                if (mRetryCount++ < totalRetries) {
                    Log.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                } else {
                    mRetryCount = 0;
                    Log.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_active_jobs_for_machine_failed, "Response failure");
                    callback.onGetActiveJobsListForMachineFailed(errorObject);
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
