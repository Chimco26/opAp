package com.operators.jobscore;

import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.operators.jobscore.interfaces.JobsForMachineUICallbackListener;
import com.operators.jobsinfra.GetJobsListForMachineCallback;
import com.operators.jobsinfra.JobListForMachine;
import com.operators.jobsinfra.JobsListForMachineNetworkBridgeInterface;
import com.operators.jobsinfra.JobsPersistenceManagerInterface;
import com.operators.jobsinfra.StartJobForMachineCallback;

public class JobsCore {
    public static final String LOG_TAG = JobsCore.class.getSimpleName();
    private JobsListForMachineNetworkBridgeInterface mJobsListForMachineNetworkBridgeInterface;
    private JobsPersistenceManagerInterface mJobsPersistenceManagerInterface;
    private JobsForMachineUICallbackListener mJobsForMachineUICallbackListener;

    public JobsCore(JobsListForMachineNetworkBridgeInterface jobsListForMachineNetworkBridge, JobsPersistenceManagerInterface jobsPersistenceManager) {
        mJobsListForMachineNetworkBridgeInterface = jobsListForMachineNetworkBridge;
        mJobsPersistenceManagerInterface = jobsPersistenceManager;
    }

    public void registerListener(JobsForMachineUICallbackListener jobsForMachineUICallbackListener) {
        mJobsForMachineUICallbackListener = jobsForMachineUICallbackListener;
    }

    public void unregisterListener() {
        mJobsForMachineUICallbackListener = null;
    }

    public void getJobsListForMachine() {
        mJobsListForMachineNetworkBridgeInterface.getJobsForMachine(mJobsPersistenceManagerInterface.getSiteUrl(), mJobsPersistenceManagerInterface.getSessionId(),
                mJobsPersistenceManagerInterface.getMachineId(), new GetJobsListForMachineCallback() {
                    @Override
                    public void onGetJobsListForMachineSuccess(JobListForMachine jobListForMachine) {
                        if (mJobsForMachineUICallbackListener != null) {
                            if (jobListForMachine != null && jobListForMachine.getData().size()>0) {
                                mJobsForMachineUICallbackListener.onJobListReceived(jobListForMachine);
                                OppAppLogger.w(LOG_TAG, "onGetJobsListForMachineSuccess() ");
                            } else{
                                OppAppLogger.w(LOG_TAG, "onGetJobsListForMachineSuccess(), jobListForMachine null or empty");
                            }
                        }
                        else {
                            OppAppLogger.w(LOG_TAG, "mJobsForMachineUICallbackListener is nul");
                        }
                    }

                    @Override
                    public void onGetJobsListForMachineFailed(StandardResponse reason) {
                        OppAppLogger.w(LOG_TAG, "Error getting job list");
                        if (mJobsForMachineUICallbackListener != null) {
                            if (reason != null) {
                                mJobsForMachineUICallbackListener.onJobListReceiveFailed(reason);
                            }
                            else {
                                OppAppLogger.w(LOG_TAG, "reason is nul");
                            }
                        }
                        else {
                            OppAppLogger.w(LOG_TAG, "onGetJobsListForMachineFailed() UI Callback is null ");
                        }
                    }
                }, mJobsPersistenceManagerInterface.getTotalRetries(), mJobsPersistenceManagerInterface.getRequestTimeout());
    }

    public void startJobForMachine(int jobId, boolean isMachineLine) {
        int machineId = isMachineLine ? 0 : mJobsPersistenceManagerInterface.getMachineId();
        mJobsListForMachineNetworkBridgeInterface.startJobsForMachine(mJobsPersistenceManagerInterface.getSiteUrl(), mJobsPersistenceManagerInterface.getSessionId(),
                machineId, jobId, new StartJobForMachineCallback() {
                    @Override
                    public void onStartJobForMachineSuccess() {
                        OppAppLogger.i(LOG_TAG, "Starting job success");
                        if (mJobsForMachineUICallbackListener != null) {
                            mJobsForMachineUICallbackListener.onStartJobSuccess();
                        }
                        else {
                            OppAppLogger.w(LOG_TAG, "mJobsForMachineUICallbackListener is nul");
                        }
                    }

                    @Override
                    public void onStartJobForMachineFailed(StandardResponse reason) {
                        if (mJobsForMachineUICallbackListener != null) {
                            mJobsForMachineUICallbackListener.onStartJobFailed(reason);
                        }
                        else {
                            OppAppLogger.w(LOG_TAG, "mJobsForMachineUICallbackListener is nul");
                        }
                        OppAppLogger.w(LOG_TAG, "Error starting job");
                    }
                }, mJobsPersistenceManagerInterface.getTotalRetries(), mJobsPersistenceManagerInterface.getRequestTimeout());
    }
}
