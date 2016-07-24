package com.operators.jobsinfra;


public interface GetJobsListForMachineCallback<T> {
    void onGetJobsListForMachineSuccess(JobListForMachine jobListForMachine);

    void onGetJobsListForMachineFailed(ErrorObjectInterface reason);
}
