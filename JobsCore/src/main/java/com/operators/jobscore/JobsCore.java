package com.operators.jobscore;

import android.util.Log;

import com.google.gson.internal.Primitives;
import com.operators.jobscore.interfaces.JobsForMachineUICallbackListener;
import com.operators.jobsinfra.ErrorObjectInterface;
import com.operators.jobsinfra.GetJobsListForMachineCallback;
import com.operators.jobsinfra.Job;
import com.operators.jobsinfra.JobListForMachine;
import com.operators.jobsinfra.JobsListForMachineNetworkBridgeInterface;
import com.operators.jobsinfra.JobsPersistenceManagerInterface;
import com.operators.jobsinfra.StartJobForMachineCallback;

public class JobsCore
{
    public static final String LOG_TAG = JobsCore.class.getSimpleName();
    private JobsListForMachineNetworkBridgeInterface mJobsListForMachineNetworkBridgeInterface;
    private JobsPersistenceManagerInterface mJobsPersistenceManagerInterface;
    private JobsForMachineUICallbackListener mJobsForMachineUICallbackListener;


    public JobsCore(JobsListForMachineNetworkBridgeInterface jobsListForMachineNetworkBridgeInterface, JobsPersistenceManagerInterface jobsPersistenceManagerInterface)
    {
        mJobsListForMachineNetworkBridgeInterface = jobsListForMachineNetworkBridgeInterface;
        mJobsPersistenceManagerInterface = jobsPersistenceManagerInterface;
    }

    public void registerListener(JobsForMachineUICallbackListener jobsForMachineUICallbackListener)
    {
        mJobsForMachineUICallbackListener = jobsForMachineUICallbackListener;
    }

    public void unregisterListener(JobsForMachineUICallbackListener jobsForMachineUICallbackListener)
    {
        if (mJobsForMachineUICallbackListener != null)
        {
            mJobsForMachineUICallbackListener = null;
        }
    }

    public void getJobsListForMachine()
    {
        mJobsListForMachineNetworkBridgeInterface.getJobsForMachine(mJobsPersistenceManagerInterface.getSiteUrl(), mJobsPersistenceManagerInterface.getSessionId(),
                mJobsPersistenceManagerInterface.getMachineId(), new GetJobsListForMachineCallback()
                {
                    @Override
                    public void onGetJobsListForMachineSuccess(JobListForMachine jobListForMachine)
                    {
                        mJobsForMachineUICallbackListener.onJobListReceived(jobListForMachine);
                    }

                    @Override
                    public void onGetJobsListForMachineFailed(ErrorObjectInterface reason)
                    {
                        mJobsForMachineUICallbackListener.onJobListReceiveFailed(reason);
                        Log.w(LOG_TAG, "Error getting job list");
                    }
                }, mJobsPersistenceManagerInterface.getTotalRetries(), mJobsPersistenceManagerInterface.getRequestTimeout());
    }

    public void startJobForMachine(int jobId){
        mJobsListForMachineNetworkBridgeInterface.startJobsForMachine(mJobsPersistenceManagerInterface.getSiteUrl(), mJobsPersistenceManagerInterface.getSessionId(),
                mJobsPersistenceManagerInterface.getMachineId(), jobId, new StartJobForMachineCallback()
                {
                    @Override
                    public void onStartJobForMachineSuccess()
                    {
                        Log.i(LOG_TAG, "Starting job success");
                        mJobsForMachineUICallbackListener.onStartJobSuccess();
                    }

                    @Override
                    public void onStartJobForMachineFailed(ErrorObjectInterface reason)
                    {
                        mJobsForMachineUICallbackListener.onStartJobFailed(reason);
                        Log.w(LOG_TAG, "Error starting job");
                    }
                },mJobsPersistenceManagerInterface.getTotalRetries(), mJobsPersistenceManagerInterface.getRequestTimeout() );
    }
}
