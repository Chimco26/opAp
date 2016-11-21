package com.operators.alldashboarddatacore.polling;


import com.zemingo.pollingmachanaim.JobBase;

public abstract class EmeraldJobBase extends JobBase{

    @Override
    protected abstract void executeJob(JobBase.OnJobFinishedListener onJobFinishedListener);

}

