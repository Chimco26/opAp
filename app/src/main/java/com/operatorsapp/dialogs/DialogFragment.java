package com.operatorsapp.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operatorsapp.R;

public class DialogFragment extends android.support.v4.app.DialogFragment {
    public final static String DIALOG = "dialog";
    private final static String DIALOG_TITLE = "AlertDialogFragment.DIALOG_TITLE";
    private final static String DIALOG_SUBTITLE = "AlertDialogFragment.DIALOG_SUBTITLE";
    private final static String DIALOG_MIN = "AlertDialogFragment.DIALOG_MIN";
    private final static String DIALOG_MAX = "AlertDialogFragment.DIALOG_MAX";
    private OnDialogButtonsListener mListener;
    private String mTitle;
    private String mSubtitle;
    private String mMin;
    private String mMax;

    public static DialogFragment newInstance(String title, String subtitle, String min, String max) {
        DialogFragment dialogFragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE, title);
        args.putString(DIALOG_SUBTITLE, subtitle);
        args.putString(DIALOG_MIN, min);
        args.putString(DIALOG_MAX, max);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(DIALOG_TITLE);
            mSubtitle = getArguments().getString(DIALOG_SUBTITLE);
            mMin = getArguments().getString(DIALOG_MIN);
            mMax = getArguments().getString(DIALOG_MAX);
        }
    }

    @Override
    public void onResume() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (width * 0.31);
        getDialog().getWindow().setAttributes(params);

        super.onResume();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_dialog, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        title.setText(mTitle);
        TextView subtitle = (TextView) view.findViewById(R.id.dialog_subtitle);
        subtitle.setText(mSubtitle);

        TextView min = (TextView) view.findViewById(R.id.dialog_min);
        if (mMin != null && !mMin.equals("")) {
            min.setText(new StringBuilder("Start: " + mMin));
        }
        TextView max = (TextView) view.findViewById(R.id.dialog_max);
        if (mMax != null && !mMax.equals("")) {
            max.setText(new StringBuilder("End: " + mMax));
        }
        TextView dismiss = (TextView) view.findViewById(R.id.dialog_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPositiveButtonClick(getDialog(), getTargetRequestCode());
                }
            }
        });
        TextView dismissForShift = (TextView) view.findViewById(R.id.dialog_dismiss_for_shift);
        dismissForShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onNegativeButtonClick(getDialog(), getTargetRequestCode());
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnDialogButtonsListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnDialogButtonsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDialogButtonsListener {
        void onPositiveButtonClick(DialogInterface dialog, int requestCode);

        void onNegativeButtonClick(DialogInterface dialog, int requestCode);
    }
}
