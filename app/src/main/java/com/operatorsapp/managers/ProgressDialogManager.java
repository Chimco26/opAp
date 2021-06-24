package com.operatorsapp.managers;


import android.app.Activity;

import com.example.oppapplog.OppAppLogger;
import com.operatorsapp.dialogs.ProgressDialogFragment;

import java.lang.ref.WeakReference;

public class ProgressDialogManager {
    private static final String LOG_TAG = ProgressDialogManager.class.getSimpleName();
    private static WeakReference<ProgressDialogFragment> mProgressDialog = null;

    public static void show(final Activity activity) {
        try {
            if (!isShowing()) {
                mProgressDialog = new WeakReference<>(new ProgressDialogFragment());
                mProgressDialog.get().show(activity.getFragmentManager(), "ProgressDialogManager");
            }
        } catch (Exception e) {
            OppAppLogger.e(LOG_TAG, "failed to create progress dialog, " + e.getMessage());
        }
    }

    public static void dismiss() {
        try {
            if (mProgressDialog != null && mProgressDialog.get() != null) {
                mProgressDialog.get().dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mProgressDialog = null;
            OppAppLogger.e(LOG_TAG, "failed to dismiss progress dialog, " + e.getMessage());
        }
    }

    public static boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.get() != null && mProgressDialog.get().isVisible();
    }

}
