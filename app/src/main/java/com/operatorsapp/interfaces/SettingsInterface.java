package com.operatorsapp.interfaces;

import com.operatorsapp.fragments.interfaces.OnReportFieldsUpdatedCallbackListener;

public interface SettingsInterface {
    void onClearAppDataRequest();
    void onRefreshReportFieldsRequest(OnReportFieldsUpdatedCallbackListener onReportFieldsUpdatedCallbackListener);
    void onRefreshApplicationRequest();
    void onRefreshPollingRequest();
}
