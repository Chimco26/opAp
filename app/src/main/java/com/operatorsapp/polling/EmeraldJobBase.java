package com.operatorsapp.polling;


import com.zemingo.pollingmachanaim.JobBase;

public abstract class EmeraldJobBase extends JobBase{

    @Override
    protected abstract void executeJob(OnJobFinishedListener onJobFinishedListener);

}

