package com.operatorsapp.managers;


import android.app.Activity;

import com.example.oppapplog.OppAppLogger;
import com.operatorsapp.dialogs.ProgressDialogFragment;

public class ProgressDialogManager {
    private static final String LOG_TAG = ProgressDialogManager.class.getSimpleName();
    private static ProgressDialogFragment mProgressDialog = null;

    public static void show(final Activity activity) {
        try {
            if (!isShowing()) {
                mProgressDialog = new ProgressDialogFragment();
                mProgressDialog.show(activity.getFragmentManager(), "ProgressDialogManager");
            }
        } catch (Exception e) {
            OppAppLogger.e(LOG_TAG, "failed to create progress dialog, " + e.getMessage());
        }
    }

    public static void dismiss() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mProgressDialog = null;
            OppAppLogger.e(LOG_TAG, "failed to dismiss progress dialog, " + e.getMessage());
        }
    }

    public static boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.isVisible();
    }

}
