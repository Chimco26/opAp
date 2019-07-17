package com.operators.jobscore.interfaces;

import com.example.common.StandardResponse;
import com.operators.jobsinfra.JobListForMachine;

/**
 * Created by Sergey on 20/07/2016.
 */
public interface JobsForMachineUICallbackListener
{
    void onJobListReceived(JobListForMachine jobListForMachine);
    void onJobListReceiveFailed(StandardResponse reason);
    void onStartJobSuccess();
    void onStartJobFailed(StandardResponse reason);
}
