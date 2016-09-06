package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportRejectCore;
import com.operators.reportrejectnetworkbridge.ReportRejectNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.StopSubReasonAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnSelectedSubReasonListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;

public class SelectedStopReasonFragment extends Fragment implements OnSelectedSubReasonListener, View.OnClickListener {

    public static final String LOG_TAG = SelectedStopReasonFragment.class.getSimpleName();
    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
    private static final String CURRENT_JOB_ID = "current_job_id";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String DURATION = "duration";
    private static final String EVENT_ID = "stop_report_event_id";

    private static final int NUMBER_OF_COLUMNS = 5;
    public static final String SAMSUNG = "samsung";

    private int mSelectedPosition;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private Integer mJobId = 0;

    private int mSelectedSubreasonId = -1;
    private int mSelectedReason;

    private RecyclerView mRecyclerView;
    private StopSubReasonAdapter mStopReasonsAdapter;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportRejectCore mReportRejectCore;


    private Button mButtonNext;
    private TextView mButtonCancel;
    private String mStart;
    private String mEnd;
    private long mDuration;
    private int mEventId;

    public static SelectedStopReasonFragment newInstance(int selectedPosition, Integer jobId, String start, String end, long duration, int eventId) {
        SelectedStopReasonFragment selectedStopReasonFragment = new SelectedStopReasonFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SELECTED_STOP_REASON_POSITION, selectedPosition);
        bundle.putInt(CURRENT_JOB_ID, jobId);
        bundle.putString(END_TIME, end);
        bundle.putString(START_TIME, start);
        bundle.putLong(DURATION, duration);
        bundle.putInt(EVENT_ID, eventId);
        selectedStopReasonFragment.setArguments(bundle);
        return selectedStopReasonFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedPosition = getArguments().getInt(SELECTED_STOP_REASON_POSITION);
            mJobId = getArguments().getInt(CURRENT_JOB_ID);
            mStart = getArguments().getString(START_TIME);
            mEnd = getArguments().getString(END_TIME);
            mDuration = getArguments().getLong(DURATION);
            mEventId = getArguments().getInt(EVENT_ID);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ReportFieldsFragmentCallbackListener reportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = reportFieldsFragmentCallbackListener.getReportForMachine();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_selected_stop_report, container, false);
        mSelectedReason = mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getId();
        setActionBar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView jobIdTextView = (TextView) view.findViewById(R.id.report_job_spinner);
        jobIdTextView.setText((String.valueOf(mJobId)));

        mButtonNext = (Button) view.findViewById(R.id.button_next);
        mButtonCancel = (TextView) view.findViewById(R.id.button_cancel);

        TextView eventIdTextView = (TextView) view.findViewById(R.id.date_text_view);
        TextView productTextView = (TextView) view.findViewById(R.id.prodct_Text_View);
        TextView durationTextView = (TextView) view.findViewById(R.id.duration_text_view);

        if (mStart == null || mEnd == null) {
            productTextView.setText("- -");
        } else {
            productTextView.setText(new StringBuilder("Stop ").append(TimeUtils.getTimeFromString(mStart)).append(", Resume ").append(TimeUtils.getTimeFromString(mEnd)));
        }

        durationTextView.setText(TimeUtils.getDurationTime(getActivity(), mDuration));

        eventIdTextView.setText(String.valueOf(mEventId));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.selected_stop_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        mRecyclerView.setLayoutManager(layoutManager);
        int spacing;
        String strManufacturer = android.os.Build.MANUFACTURER;

        if (strManufacturer.equals(SAMSUNG)) {
            spacing = 80;
        } else {
            spacing = 40;
        }
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            mRecyclerView.addItemDecoration(new GridSpacingItemDecorationRTL(NUMBER_OF_COLUMNS, spacing, true, 0));
        } else {

            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS, spacing, true, 0));
        }


        initSubReasons();
    }

    private void initSubReasons() {
        if (mReportFieldsForMachine != null) {
            mStopReasonsAdapter = new StopSubReasonAdapter(this, getContext(), mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getSubReasons());
            mRecyclerView.setAdapter(mStopReasonsAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mButtonCancel.setOnClickListener(null);
        mButtonNext.setOnClickListener(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mButtonCancel.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // rootView null
            @SuppressLint("InflateParams")
            View view = inflater.inflate(R.layout.action_bar_report_stop_selected, null);

            ImageView buttonClose = (ImageView) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });

            TextView reasonTextView = (TextView) view.findViewById(R.id.stop_reason_selected_title);
            reasonTextView.setText(mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getName());
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onSubReasonSelected(int subReasonId) {
        mButtonNext.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
        Log.i(LOG_TAG, "Selected sub reason id: " + subReasonId);
        mSelectedSubreasonId = subReasonId;
        mStopReasonsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next: {
                if (mSelectedSubreasonId != -1) {
                    sendReport();
                }
                break;
            }
            case R.id.button_cancel: {
                getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            }
        }
    }

    private void sendReport() {
        ReportRejectNetworkBridge reportRejectNetworkBridge = new ReportRejectNetworkBridge();
        reportRejectNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());
        mReportRejectCore = new ReportRejectCore(reportRejectNetworkBridge, PersistenceManager.getInstance());
        mReportRejectCore.registerListener(mReportCallbackListener);
        mReportRejectCore.sendStopReport(mSelectedReason, mSelectedSubreasonId, mJobId);
    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess() {
            Log.i(LOG_TAG, "sendReportSuccess()");
            mReportRejectCore.unregisterListener();
            //TODO check
            getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason) {
            Log.w(LOG_TAG, "sendReportFailure()");
            if (reason.getError() == ErrorObjectInterface.ErrorCode.Credentials_mismatch) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendReport();
                    }

                    @Override
                    public void onSilentLoginFailed(ErrorObjectInterface reason) {
                        Log.w(LOG_TAG, "Failed silent login");
                        ShowCrouton.reportRejectCrouton(mOnCroutonRequestListener);
                    }
                });
            } else {

                ShowCrouton.reportRejectCrouton(mOnCroutonRequestListener);
            }
        }
    };
}
