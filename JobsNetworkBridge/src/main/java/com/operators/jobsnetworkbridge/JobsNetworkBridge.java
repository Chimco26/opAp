package com.operators.jobsnetworkbridge;

import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.jobsinfra.GetJobsListForMachineCallback;
import com.operators.jobsinfra.JobListForMachine;
import com.operators.jobsinfra.JobsListForMachineNetworkBridgeInterface;
import com.operators.jobsinfra.StartJobForMachineCallback;
import com.operators.jobsnetworkbridge.interfaces.GetJobsListForMachineNetworkManagerInterface;
import com.operators.jobsnetworkbridge.interfaces.StartJobForMachineNetworkManagerInterface;
import com.operators.jobsnetworkbridge.server.requests.GetJobsListForMachineDataRequest;
import com.operators.jobsnetworkbridge.server.requests.StartJobForMachineRequest;
import com.operators.jobsnetworkbridge.server.responses.JobsListForMachineResponse;
import com.operators.jobsnetworkbridge.server.responses.StartJobForMachineResponse;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobsNetworkBridge implements JobsListForMachineNetworkBridgeInterface
{
    private static final String LOG_TAG = JobsNetworkBridge.class.getSimpleName();

    private GetJobsListForMachineNetworkManagerInterface mGetJobsListForMachineNetworkManagerInterface;
    private StartJobForMachineNetworkManagerInterface mStartJobForMachineNetworkManagerInterface;

    private int mRetryCount = 0;

    public void inject(GetJobsListForMachineNetworkManagerInterface getJobsListForMachineNetworkManager, StartJobForMachineNetworkManagerInterface startJobForMachineNetworkManager)
    {
        mGetJobsListForMachineNetworkManagerInterface = getJobsListForMachineNetworkManager;
        mStartJobForMachineNetworkManagerInterface = startJobForMachineNetworkManager;
        OppAppLogger.i(LOG_TAG, "JobsNetworkBridge inject()");
    }

    @Override
    public void getJobsForMachine(String siteUrl, String sessionId, int machineId, final GetJobsListForMachineCallback getJobsListForMachineCallback, final int totalRetries, int specificRequestTimeout)
    {
        GetJobsListForMachineDataRequest getJobsListForMachineDataRequest = new GetJobsListForMachineDataRequest(sessionId, machineId);
        Call<JobsListForMachineResponse> call = mGetJobsListForMachineNetworkManagerInterface.getJobListForMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).getJobsForMachine(getJobsListForMachineDataRequest);
        call.enqueue(new Callback<JobsListForMachineResponse>()
        {
            @Override
            public void onResponse(Call<JobsListForMachineResponse> call, Response<JobsListForMachineResponse> response)
            {
                if(response.isSuccessful() && response.body() != null)
                {
                    JobListForMachine jobListForMachine = response.body().getJobListForMachine();
                    if(jobListForMachine.getData() != null)
                    {
                        if(jobListForMachine.getData().size() > 0)
                        {
                            getJobsListForMachineCallback.onGetJobsListForMachineSuccess(jobListForMachine);
                        }
                        else
                        {
                            StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.No_data, "Jobs_list_Is_Empty Error");
                            getJobsListForMachineCallback.onGetJobsListForMachineFailed(errorObject);
                        }
                    }
                    else
                    {
                        OppAppLogger.w(LOG_TAG, "jobListForMachine.getData() is null");
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.No_data, "Get_jobs_list_failed Error");
                        getJobsListForMachineCallback.onGetJobsListForMachineFailed(errorObject);
                    }
                }
                else
                {
                    if(response.body() != null)
                    {
                        response.body().getError().setDefaultErrorCodeConstant(response.body().getError().getErrorCode());
                        getJobsListForMachineCallback.onGetJobsListForMachineFailed(response.body());
                    }
                    else
                    {
                        OppAppLogger.w(LOG_TAG, "response.body() is null");
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.No_data, "Get_jobs_list_failed Error");
                        getJobsListForMachineCallback.onGetJobsListForMachineFailed(errorObject);
                    }
                }
            }

            @Override
            public void onFailure(Call<JobsListForMachineResponse> call, Throwable t)
            {
                if(mRetryCount++ < totalRetries)
                {
                    OppAppLogger.d(LOG_TAG, "Retrying... (" + mRetryCount + " out of " + totalRetries + ")");
                    call.clone().enqueue(this);
                }
                else
                {
                    mRetryCount = 0;
                    OppAppLogger.d(LOG_TAG, "onRequestFailed(), " + t.getMessage());
                    StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "Get_jobs_list_failed Error");
                    getJobsListForMachineCallback.onGetJobsListForMachineFailed(errorObject);
                }
            }
        });
    }

    @Override
    public void startJobsForMachine(String siteUrl, String sessionId, int machineId, int jobId, final StartJobForMachineCallback startJobForMachineCallback, int totalRetries, int specificRequestTimeout)
    {
        StartJobForMachineRequest startJobForMachineRequest = new StartJobForMachineRequest(sessionId, machineId, jobId);
        Call<StartJobForMachineResponse> call = mStartJobForMachineNetworkManagerInterface.startJobForMachineStatusRetroFitServiceRequests(siteUrl, specificRequestTimeout, TimeUnit.SECONDS).startJobForMachine(startJobForMachineRequest);
        call.enqueue(new Callback<StartJobForMachineResponse>()
        {
            @Override
            public void onResponse(Call<StartJobForMachineResponse> call, Response<StartJobForMachineResponse> response)
            {
                if(response.isSuccessful())
                {
                    startJobForMachineCallback.onStartJobForMachineSuccess();
                }
                else
                {
                    if(response.body() != null)
                    {
                        response.body().getError().setDefaultErrorCodeConstant(response.body().getError().getErrorCode());
                        startJobForMachineCallback.onStartJobForMachineFailed(response.body());
                    }
                    else
                    {
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.No_data, "Response is empty");
                        startJobForMachineCallback.onStartJobForMachineFailed(errorObject);
                    }
                }
            }

            @Override
            public void onFailure(Call<StartJobForMachineResponse> call, Throwable t)
            {
                StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Retrofit, "General Error");
                startJobForMachineCallback.onStartJobForMachineFailed(errorObject);
            }
        });
    }
}

