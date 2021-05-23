package com.operatorsapp.interfaces;

public interface OnActivityCallbackRegistered
{
    void onFragmentAttached(String tag, DashboardUICallbackListener dashboardUICallbackListener);

    void onFragmentDetached(String tag, DashboardUICallbackListener dashboardUICallbackListener);
}
