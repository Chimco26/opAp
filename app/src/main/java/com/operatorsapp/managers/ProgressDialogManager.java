package com.operatorsapp.managers;

import android.app.Activity;

import com.operatorsapp.dialogs.ProgressDialogFragment;
import com.zemingo.logrecorder.ZLogger;

public class ProgressDialogManager {
    private static final String LOG_TAG = ProgressDialogManager.class.getSimpleName();
    private static ProgressDialogFragment mProgressDialog = null;

    public static void show(final Activity activity) {
        try {
            if (!isShowing()) {
                mProgressDialog = new ProgressDialogFragment();
                mProgressDialog.show(activity.getFragmentManager(), "");
            }
        } catch (Exception e) {
            OppAppLogger.getInstance().e(LOG_TAG, "failed to create progress dialog, " + e.getMessage());
        }
    }

    public static void dismiss() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            mProgressDialog = null;
            OppAppLogger.getInstance().e(LOG_TAG, "failed to dismiss progress dialog, " + e.getMessage());
        }
    }

    public static boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.isVisible();
    }

}
