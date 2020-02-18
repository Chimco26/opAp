package com.operatorsapp.interfaces;

public interface DashboardCentralContainerListener {

    void onOpenNewFragmentInCentralDashboardContainer(String type);
    void onCounterPressedInCentralDashboardContainer(int count);
    void onReportReject(String value, boolean isUnit, int selectedCauseId, int selectedReasonId);
    void onScrollToPosition(int position);
    void onReportCycleUnit(String value);
    void onOpenPendingJobs();
    void onEndSetup();
    void onReportStopEvent();
    void onKeyboardEvent(boolean isOpen);
}
