package com.operatorsapp.interfaces;

public interface DashboardCentralContainerListener {

    void onOpenNewFragmentInCentralDashboardContainer(String type);
    void onCounterPressedInCentralDashboardContainer(int count);

    void onReportReject(String value, boolean isUnit, int selectedCauseId, int selectedReasonId);
}
