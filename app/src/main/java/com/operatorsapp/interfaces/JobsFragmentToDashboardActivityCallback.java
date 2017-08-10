package com.operatorsapp.interfaces;

public interface JobsFragmentToDashboardActivityCallback {
    void onJobFragmentAttached(DashboardActivityToJobsFragmentCallback dashboardActivityToJobsFragmentCallback);

    void onSelectedJobFragmentAttached(DashBoardActivityToSelectedJobFragmentCallback dashboardActivityToSelectedJobFragmentCallback);

    void getJobsForMachineList();

    void startJobForMachine(int jobId);

    void initJobsCore();

    void unregisterListeners();

    void updateReportRejectFields();
}
