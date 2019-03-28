package com.operators.jobsinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface GetJobsListForMachineCallback<T> {
    void onGetJobsListForMachineSuccess(JobListForMachine jobListForMachine);

    void onGetJobsListForMachineFailed(ErrorObjectInterface reason);
}
