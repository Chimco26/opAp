package com.operatorsapp.interfaces;

import java.lang.ref.WeakReference;

public interface OnActivityCallbackRegistered
{
    void onFragmentAttached(WeakReference<DashboardUICallbackListener> dashboardUICallbackListener);

    void onFragmentDetached(WeakReference<DashboardUICallbackListener> dashboardUICallbackListener);
}
