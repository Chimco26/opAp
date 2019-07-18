package com.operators.jobsinfra;

import com.example.common.StandardResponse;

public interface GetJobsListForMachineCallback<T> {
    void onGetJobsListForMachineSuccess(JobListForMachine jobListForMachine);

    void onGetJobsListForMachineFailed(StandardResponse reason);
}
