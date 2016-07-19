package com.operators.jobscore;

import com.google.gson.internal.Primitives;
import com.operators.jobsinfra.GetJobsListForMachineCallback;
import com.operators.jobsinfra.JobsListForMachineNetworkBridgeInterface;
import com.operators.jobsinfra.JobsPersistenceManagerInterface;

public class JobsCore {
    private JobsListForMachineNetworkBridgeInterface mJobsListForMachineNetworkBridgeInterface;
    private JobsPersistenceManagerInterface mJobsPersistenceManagerInterface;


    public JobsCore(JobsListForMachineNetworkBridgeInterface jobsListForMachineNetworkBridgeInterface, JobsPersistenceManagerInterface jobsPersistenceManagerInterface) {
        mJobsListForMachineNetworkBridgeInterface = jobsListForMachineNetworkBridgeInterface;
        mJobsPersistenceManagerInterface = jobsPersistenceManagerInterface;
    }

    private void GetJobsListForMachiine(){
//        mJobsListForMachineNetworkBridgeInterface.getJobsForMachine();
    }
}
