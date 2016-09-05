package com.operatorsapp.interfaces;

import com.operators.jobsinfra.JobListForMachine;

public interface DashboardActivityToJobsFragmentCallback {

    void onJobsListReceived(JobListForMachine jobListForMachine);

    void onJobsListReceiveFailed();

}
