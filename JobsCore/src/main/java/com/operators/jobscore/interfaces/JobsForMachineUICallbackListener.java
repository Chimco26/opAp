package com.operators.jobscore.interfaces;

import com.operators.jobsinfra.ErrorObjectInterface;
import com.operators.jobsinfra.JobListForMachine;

/**
 * Created by User on 20/07/2016.
 */
public interface JobsForMachineUICallbackListener
{
    void onJobListReceived(JobListForMachine jobListForMachine);
    void onJobListReceiveFailed(ErrorObjectInterface reason);
    void onStartJobSuccess();
    void onStartJobFailed(ErrorObjectInterface reason);
}
