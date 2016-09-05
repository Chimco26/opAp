package com.operatorsapp.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.operatorsapp.R;
import com.zemingo.logrecorder.ZLogger;

public class ProgressDialogFragment extends DialogFragment {
    private static String LOG_TAG = ProgressDialogFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_fragment);
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ProgressBar progressBar = (ProgressBar) inflater.inflate(R.layout.custom_progress_bar, container, false);
        progressBar.setIndeterminate(true);
        return progressBar;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            ZLogger.d(LOG_TAG, "onResume():" + e.getMessage());
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().onBackPressed();
    }
}
