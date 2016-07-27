package com.operatorsapp.interfaces;

import com.operators.jobsinfra.ErrorObjectInterface;
import com.operators.jobsinfra.JobListForMachine;

/**
 * Created by User on 20/07/2016.
 */
public interface DashboardActivityToJobsFragmentCallback {

   void onJobReceived(JobListForMachine jobListForMachine);
    void onJobReceiveFailed();

}
