package com.operatorsapp.dialogs;

import android.annotation.SuppressLint;
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
import android.view.WindowManager;
import android.widget.TextView;

import com.operatorsapp.R;

public class DialogFragment extends android.support.v4.app.DialogFragment {
    public final static String DIALOG = "dialog";
    private final static String DIALOG_START = "AlertDialogFragment.DIALOG_START";
    private final static String DIALOG_END = "AlertDialogFragment.DIALOG_END";
    private final static String DIALOG_DURATION = "AlertDialogFragment.DIALOG_DURATION";
    private final static String DIALOG_WEIGHT = "AlertDialogFragment.DIALOG_WEIGHT";
    private final static String DIALOG_STANDARD = "AlertDialogFragment.DIALOG_STANDARD";
    private final static String DIALOG_MIN = "AlertDialogFragment.DIALOG_MIN";
    private final static String DIALOG_MAX = "AlertDialogFragment.DIALOG_MAX";
    private final static String DIALOG_TYPE = "AlertDialogFragment.DIALOG_TYPE";
    private OnDialogButtonsListener mListener;
    private String mStart;
    private String mEnd;
    private int mDuration;
    private int mWeight;
    private int mStandard;
    private int mMin;
    private int mMax;
    private boolean mIsStopDialog;


    public static DialogFragment newInstance(String start, String end, int duration) {
        DialogFragment dialogFragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(DIALOG_TYPE, true);
        args.putString(DIALOG_START, start);
        args.putString(DIALOG_END, end);
        args.putInt(DIALOG_DURATION, duration);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    public static DialogFragment newInstance(int weight, int standard, int min, int max) {
        DialogFragment dialogFragment = new DialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(DIALOG_TYPE, false);
        args.putInt(DIALOG_WEIGHT, weight);
        args.putInt(DIALOG_STANDARD, standard);
        args.putInt(DIALOG_MIN, min);
        args.putInt(DIALOG_MAX, max);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIsStopDialog = getArguments().getBoolean(DIALOG_TYPE);
            mStart = getArguments().getString(DIALOG_START);
            mEnd = getArguments().getString(DIALOG_END);
            mDuration = getArguments().getInt(DIALOG_DURATION);
            mWeight = getArguments().getInt(DIALOG_WEIGHT);
            mStandard = getArguments().getInt(DIALOG_STANDARD);
            mMin = getArguments().getInt(DIALOG_MIN);
            mMax = getArguments().getInt(DIALOG_MAX);
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

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = null;
        if (mIsStopDialog) {
            view = inflater.inflate(R.layout.stop_dialog, null);

            TextView start = (TextView) view.findViewById(R.id.dialog_start);
            if (mStart != null && !mStart.equals("")) {
                start.setText(mStart);
            }
            TextView end = (TextView) view.findViewById(R.id.dialog_end);
            if (mEnd != null && !mEnd.equals("")) {
                end.setText(mEnd);
            }

            TextView duration = (TextView) view.findViewById(R.id.dialog_duration);
            duration.setText(String.valueOf(mDuration));

            TextView dismiss = (TextView) view.findViewById(R.id.dialog_dismiss);
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onDismissClick(getDialog(), getTargetRequestCode());
                    }
                }
            });

            TextView report = (TextView) view.findViewById(R.id.dialog_report);
            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo current method
                    if (mListener != null) {
                        mListener.onReportClick(mStart, mEnd, mDuration);
//     mListener.onDismissAllClick(getDialog(), getTargetRequestCode());
                    }
                }
            });
        }
        else {
            view = inflater.inflate(R.layout.parameter_dialog, null);

            TextView weight = (TextView) view.findViewById(R.id.dialog_weight);
            weight.setText(String.valueOf(mWeight));

            TextView standard = (TextView) view.findViewById(R.id.dialog_standard);
            standard.setText(String.valueOf(mStandard));

            TextView min = (TextView) view.findViewById(R.id.dialog_min);
            min.setText(String.valueOf(mMin));

            TextView max = (TextView) view.findViewById(R.id.dialog_max);
            max.setText(String.valueOf(mMax));

            TextView dismiss = (TextView) view.findViewById(R.id.dialog_dismiss);
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onDismissClick(getDialog(), getTargetRequestCode());
                    }
                }
            });

            TextView dismissForShift = (TextView) view.findViewById(R.id.dialog_dismiss_all);
            dismissForShift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onDismissAllClick(getDialog(), getTargetRequestCode());
                    }
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnDialogButtonsListener) getTargetFragment();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnDialogButtonsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDialogButtonsListener {
        void onDismissClick(DialogInterface dialog, int requestCode);

        void onDismissAllClick(DialogInterface dialog, int requestCode);

        void onReportClick(String start, String end, int duration);
    }
}
