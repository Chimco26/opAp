package com.operatorsapp.server.pulling.polling;


import ravtech.co.il.publicutils.JobBase;
public abstract class EmeraldJobBase extends JobBase{

    @Override
    protected abstract void executeJob(JobBase.OnJobFinishedListener onJobFinishedListener);

}

