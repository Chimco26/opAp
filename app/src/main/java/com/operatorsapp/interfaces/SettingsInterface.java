package com.operatorsapp.interfaces;

import com.operatorsapp.fragments.interfaces.OnReportFieldsUpdatedCallbackListener;

/**
 * Created by Sergey on 17/08/2016.
 */
public interface SettingsInterface {
    void clearAppData();
    void refreshReportFields(OnReportFieldsUpdatedCallbackListener onReportFieldsUpdatedCallbackListener);
    void refreshApplication();
}
