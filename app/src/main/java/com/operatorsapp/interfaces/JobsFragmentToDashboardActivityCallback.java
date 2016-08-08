package com.operatorsapp.interfaces;

/**
 * Created by User on 20/07/2016.
 */
public interface JobsFragmentToDashboardActivityCallback {
    void onJobFragmentAttached(DashboardActivityToJobsFragmentCallback dashboardActivityToJobsFragmentCallback);

    void onSelectedJobFragmentAttached(DashboardActivityToSelectedJobFragmentCallback dashboardActivityToSelectedJobFragmentCallback);

    void getJobsForMachineList();

    void startJobForMachine(int jobId);

    void initJobsCore();

    void unregisterListeners();

    void updateReportRejectFields();
}
