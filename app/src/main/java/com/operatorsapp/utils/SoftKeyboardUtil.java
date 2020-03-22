package com.operatorsapp.utils;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.oppapplog.OppAppLogger;

public class SoftKeyboardUtil {
    protected static final String LOG_TAG = SoftKeyboardUtil.class.getSimpleName();

    @SuppressWarnings("unused")
    public static void hideKeyboard(Activity activity) {
        try {
            if (activity != null && activity.getCurrentFocus() != null) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            OppAppLogger.e(LOG_TAG, "error hiding keyboard, " + e.getMessage());
        }
    }

    public static void hideKeyboard(Fragment fragment) {

        if (fragment.getActivity() != null) {
            try {
                InputMethodManager inputManager = (InputMethodManager) fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null && fragment.getView() != null) {
                    inputManager.hideSoftInputFromWindow(fragment.getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            } catch (Exception e) {
                OppAppLogger.e(LOG_TAG, "error hiding keyboard, " + e.getMessage());
            }
        }
    }

    @SuppressWarnings("unused")
    public static void hideKeyboard(android.app.Fragment fragment) {
        try {
            InputMethodManager inputManager = (InputMethodManager) fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null && fragment.getView() != null) {
                inputManager.hideSoftInputFromWindow(fragment.getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            OppAppLogger.e(LOG_TAG, "error hiding keyboard, " + e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public static void hideKeyboard(View view) {
        if (view != null) {
            try {
                InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            } catch (Exception e) {
                OppAppLogger.e(LOG_TAG, "error hiding keyboard, " + e.getMessage());
            }
        }
    }

    public static void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }
}
