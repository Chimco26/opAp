package com.operatorsapp.dialogs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ravtech.david.sqlcore.Event;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.TimeUtils;

import java.lang.reflect.Type;

public class DialogFragment extends android.support.v4.app.DialogFragment {
    private final static String TYPE = "type";
    public final static String DIALOG = "dialog";
    private final static String EVENT = "event";
    //    public static final int DAY_MILLIS = 86400000;
    private OnDialogButtonsListener mListener;
    private boolean mIsStopDialog;
    private Event mEvent;
    private ProgressBar mProgressBar;

    public static DialogFragment newInstance(Event event, boolean isStopDialog) {
        DialogFragment dialogFragment = new DialogFragment();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String eventString = gson.toJson(event);
        args.putString(EVENT, eventString);
        args.putBoolean(TYPE, isStopDialog);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mIsStopDialog = getArguments().getBoolean(TYPE);
            Gson gson = new Gson();

            Type eventType = new TypeToken<Event>() {
            }.getType();

            mEvent = gson.fromJson(getArguments().getString(EVENT), eventType);
        }
    }

    @Override
    public void onResume() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();

        Point size = new Point();

        display.getSize(size);

        int width = size.x;

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = (int) (width * 0.35);

        getDialog().getWindow().setAttributes(params);

        super.onResume();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;
        if (mIsStopDialog) {
            view = inflater.inflate(R.layout.stop_dialog, null);

            TextView title = (TextView) view.findViewById(R.id.dialog_title);
            String titleByLang = OperatorApplication.isEnglishLang() ? mEvent.getEventETitle() : mEvent.getEventLTitle();
            title.setText(titleByLang);

            TextView start = (TextView) view.findViewById(R.id.dialog_start);
            if (mEvent.getTime() != null && !mEvent.getTime().equals("")) {
                start.setText(TimeUtils.getTimeFromString(mEvent.getTime()));
            }
            TextView end = (TextView) view.findViewById(R.id.dialog_end);
            if (mEvent.getEventEndTime() != null && !mEvent.getEventEndTime().equals("")) {
                end.setText(TimeUtils.getTimeFromString(mEvent.getEventEndTime()));
            }

            TextView duration = (TextView) view.findViewById(R.id.dialog_duration);
            duration.setText(TimeUtils.getDurationTime(getActivity(), mEvent.getDuration()));

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
                    if (mListener != null) {
                        //// TODO: 20-Nov-16  check
                        dismiss();
                        mListener.onReportClick(mEvent.getEventID(), mEvent.getTime(), mEvent.getEventEndTime(), mEvent.getDuration());
                    }
                }
            });
        } else {
            view = inflater.inflate(R.layout.parameter_dialog, null);

            setProgressCountDown(view);

            TextView title = (TextView) view.findViewById(R.id.dialog_title);
            String subtitleNameByLang = OperatorApplication.isEnglishLang() ? mEvent.getSubtitleEname() : mEvent.getSubtitleLname();
            title.setText(new StringBuilder(subtitleNameByLang).append(" ").append(getString(R.string.alarm)));

            TextView reason = (TextView) view.findViewById(R.id.dialog_reason);
            String titleByLang = OperatorApplication.isEnglishLang() ? mEvent.getEventETitle() : mEvent.getEventLTitle();
            reason.setText(titleByLang);

            TextView reasonVal = (TextView) view.findViewById(R.id.dialog_reason_val);
            reasonVal.setText(String.valueOf(mEvent.getAlarmValue()));

            TextView standard = (TextView) view.findViewById(R.id.dialog_standard);
            standard.setText(String.valueOf(mEvent.getAlarmStandardValue()));

            TextView low = (TextView) view.findViewById(R.id.dialog_low);
            low.setText(String.valueOf(mEvent.getAlarmLValue()));

            TextView high = (TextView) view.findViewById(R.id.dialog_high);
            high.setText(String.valueOf(mEvent.getAlarmHValue()));

            TextView dismiss = (TextView) view.findViewById(R.id.dialog_dismiss);
            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onDismissClick(getDialog(), getTargetRequestCode());
                    } else {
                        //todo
                    }
                }
            });

            TextView dismissForShift = (TextView) view.findViewById(R.id.dialog_dismiss_all);
            dismissForShift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onDismissAllClick(getDialog(), mEvent.getEventGroupID(), getTargetRequestCode());
                    } else {
                        //todo
                    }
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    private void setProgressCountDown(View view) {

        mProgressBar = (ProgressBar) view.findViewById(R.id.PT_progressbar_time_left);

        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);

        animation.setDuration(PersistenceManager.getInstance().getTimeToDownParameterDialog());

        animation.setInterpolator(new DecelerateInterpolator());

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (mListener != null) {

                    mListener.onDismissClick(getDialog(), getTargetRequestCode());
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animation.start();
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
        setTargetFragment(null, 0);
    }

    public interface OnDialogButtonsListener {

        void onDismissClick(DialogInterface dialog, int requestCode);

        void onDismissAllClick(DialogInterface dialog, int eventGroupId, int requestCode);

        void onReportClick(int eventId, String start, String end, long duration);
    }
}
