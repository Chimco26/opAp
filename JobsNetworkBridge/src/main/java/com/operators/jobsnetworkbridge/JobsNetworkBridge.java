package com.operators.jobsnetworkbridge;

import android.util.Log;

import com.operators.jobsinfra.GetJobsListForMachineCallback;
import com.operators.jobsinfra.JobsListForMachineNetworkBridgeInterface;
import com.operators.jobsinfra.JobListForMachine;
import com.operators.jobsinfra.StartJobForMachineCallback;
import com.operators.jobsnetworkbridge.interfaces.GetJobsListForMachineNetworkManagerInterface;
import com.operators.jobsnetworkbridge.interfaces.StartJobForMachineNetworkManagerInterface;
import com.operators.jobsnetworkbridge.server.requests.GetJobsListForMachineDataRequest;
import com.operators.jobsnetworkbridge.server.requests.StartJobForMachineRequest;
import com.operators.jobsnetworkbridge.server.responses.ErrorResponse;
import com.operators.jobsnetworkbridge.server.responses.JobsListForMachineResponse;
import com.operators.jobsnetworkbridge.server.responses.StartJobForMachineResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobsNetworkBridge implements JobsListForMachineNetworkBridgeInterface {
    private static final String LOG_TAG = JobsNetworkBridge.class.getSimpleName();

    private GetJobsListForMachineNetworkManagerInterface mGetJobsListForMachineNetworkManagerInterface;
    private StartJobForMachineNetworkManagerInterface mStartJobForMachineNetworkManagerInterface;

    public void inject(GetJobsListForMachineNetworkManagerInterface getJobsListForMachineNetworkManagerInterface, StartJobForMachineNetworkManagerInterface startJobForMachineNetworkManagerInterface) {
        mGetJobsListForMachineNetworkManagerInterface = getJobsListForMachineNetworkManagerInterface;
        mStartJobForMachineNetworkManagerInterface = startJobForMachineNetworkManagerInterface;
        Log.i(LOG_TAG, "JobsNetworkBridge inject()");
    }

    @Override
    public void getJobsForMachine(String siteUrl, String sessionId, int machineId, final GetJobsListForMachineCallback getJobsListForMachineCallback, int totalRetries, int specificRequestTimeout) {
        GetJobsListForMachineDataRequest getJobsListForMachineDataRequest = new GetJobsListForMachineDataRequest(sessionId, machineId);
        Call<JobsListForMachineResponse> call = mGetJobsListForMachineNetworkManagerInterface.getJobListForMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getJobsForMachine(getJobsListForMachineDataRequest);
        call.enqueue(new Callback<JobsListForMachineResponse>() {
            @Override
            public void onResponse(Call<JobsListForMachineResponse> call, Response<JobsListForMachineResponse> response) {
                if (response.isSuccessful()) {

                    JobListForMachine jobListForMachine = response.body().getJobListForMachine();
                    getJobsListForMachineCallback.onGetJobsListForMachineSuccess(jobListForMachine);
                }
                else {
                    if(response.body()!=null){
                        ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                        getJobsListForMachineCallback.onGetJobsListForMachineFailed(errorObject);
                    }
                    else{
                        Log.w(LOG_TAG,"response.body() is null");
                    }
                }
            }

            @Override
            public void onFailure(Call<JobsListForMachineResponse> call, Throwable t) {
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_jobs_list_failed, "General Error");
                getJobsListForMachineCallback.onGetJobsListForMachineFailed(errorObject);
            }
        });
    }

    @Override
    public void startJobsForMachine(String siteUrl, String sessionId, int machineId, int jobId, final StartJobForMachineCallback startJobForMachineCallback, int totalRetries, int specificRequestTimeout) {
        StartJobForMachineRequest startJobForMachineRequest = new StartJobForMachineRequest(sessionId, machineId, jobId);
        Call<StartJobForMachineResponse> call = mStartJobForMachineNetworkManagerInterface.startJobForMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).startJobForMachine(startJobForMachineRequest);
        call.enqueue(new Callback<StartJobForMachineResponse>() {
            @Override
            public void onResponse(Call<StartJobForMachineResponse> call, Response<StartJobForMachineResponse> response) {
                if (response.isSuccessful()) {
                    startJobForMachineCallback.onStartJobForMachineSuccess();
                }
                else {
                    ErrorObject errorObject = errorObjectWithErrorCode(response.body().getErrorResponse());
                    startJobForMachineCallback.onStartJobForMachineFailed(errorObject);
                }
            }

            @Override
            public void onFailure(Call<StartJobForMachineResponse> call, Throwable t) {
                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Get_jobs_list_failed, "General Error");
                startJobForMachineCallback.onStartJobForMachineFailed(errorObject);
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

