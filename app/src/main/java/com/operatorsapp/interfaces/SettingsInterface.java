package com.operatorsapp.interfaces;

import com.operatorsapp.fragments.interfaces.OnReportFieldsUpdatedCallbackListener;

/**
 * Created by Sergey on 17/08/2016.
 */
public interface SettingsInterface {
    void onClearAppDataRequest();
    void onRefreshReportFieldsRequest(OnReportFieldsUpdatedCallbackListener onReportFieldsUpdatedCallbackListener);
    void onRefreshApplicationRequest();
    void onRefreshPollingRequest();
}
