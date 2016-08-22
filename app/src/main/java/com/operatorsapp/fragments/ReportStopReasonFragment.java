package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.operators.activejobslistformachinecore.ActiveJobsListForMachineCore;
import com.operators.activejobslistformachinecore.interfaces.ActiveJobsListForMachineUICallbackListener;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachinenetworkbridge.ActiveJobsListForMachineNetworkBridge;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.adapters.StopReasonsAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;

import java.util.Locale;

/**
 * Created by Sergey on 08/08/2016.
 */
public class ReportStopReasonFragment extends Fragment implements OnStopReasonSelectedCallbackListener {
    private static final String LOG_TAG = ReportStopReasonFragment.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 5;
    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
    private static final String CURRENT_JOB_ID = "current_job_id";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String DURATION = "duration";
    private static final String EVENT_ID = "stop_report_event_id";

    private int mEventId;
    private String mStart;
    private String mEnd;
    private long mDuration;
    private Integer mJobId = null;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private StopReasonsAdapter mStopReasonsAdapter;

    private GoToScreenListener mGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener;
    private ReportFieldsForMachine mReportFieldsForMachine;

    private Spinner mJobsSpinner;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private ActiveJobsListForMachineCore mActiveJobsListForMachineCore;
    private ProgressBar mActiveJobsProgressBar;

    public static ReportStopReasonFragment newInstance(String start, String end, long duration, int eventId) {
        ReportStopReasonFragment reportStopReasonFragment = new ReportStopReasonFragment();
        Bundle bundle = new Bundle();
        bundle.putString(START_TIME, start);
        bundle.putString(END_TIME, end);
        bundle.putLong(DURATION, duration);
        bundle.putInt(EVENT_ID, eventId);
        reportStopReasonFragment.setArguments(bundle);
        return reportStopReasonFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGoToScreenListener = (GoToScreenListener) getActivity();
        mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = mReportFieldsFragmentCallbackListener.getReportForMachine();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStart = getArguments().getString(START_TIME);
            mEnd = getArguments().getString(END_TIME);
            mDuration = getArguments().getLong(DURATION);
            mEventId = getArguments().getInt(EVENT_ID);
            Log.i(LOG_TAG, "Start " + mStart + " end " + mEnd + " duration " + mDuration);
        }
//        getActiveJobs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_stop_reason, container, false);
        setActionBar();
        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.stop_recycler_view);

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getStopReasons() == null || mReportFieldsForMachine.getStopReasons().size() == 0) {
            Log.i(LOG_TAG, "No Reasons in list");
            ShowCrouton.noDataCrouton(mOnCroutonRequestListener, R.id.report_stop_screen);
        }
        else {
            mLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
            mRecyclerView.setLayoutManager(mLayoutManager);
            int spacing = 40;

            Configuration config = getResources().getConfiguration();
            if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                mRecyclerView.addItemDecoration(new GridSpacingItemDecorationRTL(NUMBER_OF_COLUMNS, spacing, true, 0));
            }else {
                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS, spacing, true, 0));

            }
            initStopReasons();
        }

        TextView eventIdTextView = (TextView) view.findViewById(R.id.date_text_view);
        TextView productTextView = (TextView) view.findViewById(R.id.prodct_Text_View);
        TextView durationTextView = (TextView) view.findViewById(R.id.duration_text_view);

        if (mStart == null || mEnd == null) {
            productTextView.setText("- -");
        }
        else {
            productTextView.setText(getActivity().getString(R.string.stop) + TimeUtils.getTimeFromString(mStart) + ", Resume " + TimeUtils.getTimeFromString(mEnd));
        }

        durationTextView.setText(TimeUtils.getDurationTime(getActivity(), mDuration));
        eventIdTextView.setText(String.valueOf(mEventId));

        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);

    }

    private void initStopReasons() {
        mStopReasonsAdapter = new StopReasonsAdapter(getContext(), mReportFieldsForMachine.getStopReasons(), this);
        mRecyclerView.setAdapter(mStopReasonsAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mOnCroutonRequestListener.onHideConnectivityCroutonRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActiveJobs();
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
            View view = inflater.inflate(R.layout.action_bar_report_stop, null);

            ImageView buttonClose = (ImageView) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onStopReasonSelected(int position) {
        mGoToScreenListener.goToFragment(SelectedStopReasonFragment.newInstance(position, mJobId, mStart, mEnd, mDuration, mEventId), true);
    }

    private void initJobsSpinner() {
        mJobsSpinner.setVisibility(View.VISIBLE);
        final ActiveJobsSpinnerAdapter activeJobsSpinnerAdapter = new ActiveJobsSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mActiveJobsListForMachine.getActiveJobs());
        activeJobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mJobsSpinner.setAdapter(activeJobsSpinnerAdapter);
        mJobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
        mJobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activeJobsSpinnerAdapter.setTitle(position);
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(position).getJobID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getActiveJobs() {
        ActiveJobsListForMachineNetworkBridge activeJobsListForMachineNetworkBridge = new ActiveJobsListForMachineNetworkBridge();
        activeJobsListForMachineNetworkBridge.inject(NetworkManager.getInstance());
        mActiveJobsListForMachineCore = new ActiveJobsListForMachineCore(PersistenceManager.getInstance(), activeJobsListForMachineNetworkBridge);
        mActiveJobsListForMachineCore.registerListener(mActiveJobsListForMachineUICallbackListener);
        mActiveJobsListForMachineCore.getActiveJobsListForMachine();
    }

    private ActiveJobsListForMachineUICallbackListener mActiveJobsListForMachineUICallbackListener = new ActiveJobsListForMachineUICallbackListener() {
        @Override
        public void onActiveJobsListForMachineReceived(ActiveJobsListForMachine activeJobsListForMachine) {
            if (activeJobsListForMachine != null) {
                mActiveJobsListForMachine = activeJobsListForMachine;
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(0).getJobID();
                initJobsSpinner();
                Log.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            }
            else {
                mJobId = null;
                Log.w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
            }
            disableProgressBar();
        }

        @Override
        public void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason) {
            mJobId = null;
            Log.w(LOG_TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getDetailedDescription());
            disableProgressBar();
        }
    };

    private void disableProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }
}
