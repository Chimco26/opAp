package com.operators.shiftlogcore.polling;


import ravtech.co.il.publicutils.JobBase;

public abstract class EmeraldJobBase extends JobBase {

    @Override
    protected abstract void executeJob(OnJobFinishedListener onJobFinishedListener);

}

