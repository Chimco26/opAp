package com.operatorsapp.interfaces;

import com.operatorsapp.fragments.interfaces.OnReportFieldsUpdatedCallbackListener;

public interface SettingsInterface {
    void onClearAppDataRequest();
    void onChangeMachineRequest();
    void onRefreshReportFieldsRequest(OnReportFieldsUpdatedCallbackListener onReportFieldsUpdatedCallbackListener);
    void onRefreshApplicationRequest();
    void onRefreshPollingRequest();
    void onIgnoreFromOnPause(boolean ignore);
    void onCheckForAppUpdates();

}
