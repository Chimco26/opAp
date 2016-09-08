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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.operators.shiftloginfra.Event;
import com.operatorsapp.R;
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
            title.setText(mEvent.getTitle());

            TextView start = (TextView) view.findViewById(R.id.dialog_start);
            if (mEvent.getTime() != null && !mEvent.getTime().equals("")) {
                start.setText(TimeUtils.getTimeFromString(mEvent.getTime()));
            }
            TextView end = (TextView) view.findViewById(R.id.dialog_end);
            if (mEvent.getEndTime() != null && !mEvent.getEndTime().equals("")) {
                end.setText(TimeUtils.getTimeFromString(mEvent.getEndTime()));
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
                        mListener.onReportClick(mEvent.getEventID(), mEvent.getTime(), mEvent.getEndTime(), mEvent.getDuration());
                    }
                }
            });
        } else {
            view = inflater.inflate(R.layout.parameter_dialog, null);

            TextView title = (TextView) view.findViewById(R.id.dialog_title);
            title.setText(new StringBuilder(mEvent.getSubtitleLname()).append(" Alarm"));

            TextView reason = (TextView) view.findViewById(R.id.dialog_reason);
            reason.setText(mEvent.getTitle());

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
                    } else{
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
        void onDismissClick(DialogInterface dialog, int requestCode);

        void onDismissAllClick(DialogInterface dialog, int eventGroupId, int requestCode);

        void onReportClick(int eventId, String start, String end, long duration);
    }
}
