package com.operators.jobsinfra;


import com.operators.errorobject.ErrorObjectInterface;

public interface GetJobsListForMachineCallback<T> {
    void onGetJobsListForMachineSuccess(JobListForMachine jobListForMachine);

    void onGetJobsListForMachineFailed(ErrorObjectInterface reason);
}
