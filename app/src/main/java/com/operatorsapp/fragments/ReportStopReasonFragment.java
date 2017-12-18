package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.activejobslistformachinecore.ActiveJobsListForMachineCore;
import com.operators.activejobslistformachinecore.interfaces.ActiveJobsListForMachineUICallbackListener;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachinenetworkbridge.ActiveJobsListForMachineNetworkBridge;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.StopReasons;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.adapters.StopReasonsAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SendReportUtil;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ReportStopReasonFragment extends BackStackAwareFragment implements OnStopReasonSelectedCallbackListener, CroutonRootProvider
{
    private static final String LOG_TAG = ReportStopReasonFragment.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 5;
//    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
//    private static final String CURRENT_JOB_ID = "current_job_id";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String DURATION = "duration";
    private static final String EVENT_ID = "stop_report_event_id";

    private int mEventId;
    private String mStart;
    private String mEnd;
    private long mDuration;
    private Integer mJobId = 0;

    private RecyclerView mRecyclerView;

    private GoToScreenListener mGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportFieldsForMachine mReportFieldsForMachine;

    private Spinner mJobsSpinner;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
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
        mGoToScreenListener = (GoToScreenListener) context;
        ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = mReportFieldsFragmentCallbackListener.getReportForMachine();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mGoToScreenListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStart = getArguments().getString(START_TIME);
            mEnd = getArguments().getString(END_TIME);
            mDuration = getArguments().getLong(DURATION);
            mEventId = getArguments().getInt(EVENT_ID);
            ZLogger.i(LOG_TAG, "Start " + mStart + " end " + mEnd + " duration " + mDuration);
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
            ZLogger.i(LOG_TAG, "No Reasons in list");
            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
        }
        else {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
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
            productTextView.setText(new StringBuilder(getActivity().getString(R.string.report_stop_start)).append(TimeUtils.getTimeFromString(mStart)).append(", ").append(getActivity().getString(R.string.report_stop_resume)).append(" ").append(TimeUtils.getTimeFromString(mEnd)));
        }

        long durationInMillis = mDuration * 60 * 1000;
        durationTextView.setText(TimeUtils.getDurationTime(getActivity(), durationInMillis));
        eventIdTextView.setText(String.valueOf(mEventId));

        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);

    }

    private void initStopReasons() {

        StopReasonsAdapter mStopReasonsAdapter = new StopReasonsAdapter(getContext(), mReportFieldsForMachine.getStopReasons(), this);

        mRecyclerView.setAdapter(mStopReasonsAdapter);
    }

    private List<StopReasons> getFilterReasone(List<StopReasons> stopReasons) {
        List<StopReasons> stopReasonsList = new ArrayList<>();

        try {

            for(StopReasons reasons: stopReasons){

                // TODO: DAVID VARDI Sprint 1.5: add if for filter
                stopReasons.add(reasons);
            }

        }catch (ConcurrentModificationException e){

            e.printStackTrace();
        }



        return stopReasonsList;
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

    protected void setActionBar() {
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

            LinearLayout buttonClose = (LinearLayout) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    if(fragmentManager != null)
                    {
                        fragmentManager.popBackStack();
                    }
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onStopReasonSelected(int position) {

        try {
            mGoToScreenListener.goToFragment(SelectedStopReasonFragment.newInstance(position,
                    mJobId,
                    mStart,
                    mEnd,
                    mDuration,
                    mEventId,
                    mReportFieldsForMachine.getStopReasons().get(position).getId(),
                    mReportFieldsForMachine.getStopReasons().get(position).getEName(),
                    mReportFieldsForMachine.getStopReasons().get(position).getLName()), true);
        }catch (IllegalStateException e){

            SendReportUtil.sendAcraExeption(e,"onStopReasonSelected");
        }


    }

    private void initJobsSpinner() {
        mJobsSpinner.setVisibility(View.VISIBLE);
        if(getActivity()!=null)
        {
            if (mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null)
            {
                final ActiveJobsSpinnerAdapter activeJobsSpinnerAdapter = new ActiveJobsSpinnerAdapter(getActivity(), R.layout.active_jobs_spinner_item, mActiveJobsListForMachine.getActiveJobs());
                activeJobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mJobsSpinner.setAdapter(activeJobsSpinnerAdapter);
                mJobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
                mJobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        activeJobsSpinnerAdapter.setTitle(position);
                        mJobId = mActiveJobsListForMachine.getActiveJobs().get(position).getJoshID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });
            }
        }
    }

    private void getActiveJobs() {
        ActiveJobsListForMachineNetworkBridge activeJobsListForMachineNetworkBridge = new ActiveJobsListForMachineNetworkBridge();
        activeJobsListForMachineNetworkBridge.inject(NetworkManager.getInstance());
        ActiveJobsListForMachineCore mActiveJobsListForMachineCore = new ActiveJobsListForMachineCore(PersistenceManager.getInstance(), activeJobsListForMachineNetworkBridge);
        mActiveJobsListForMachineCore.registerListener(mActiveJobsListForMachineUICallbackListener);
        mActiveJobsListForMachineCore.getActiveJobsListForMachine();
    }

    private ActiveJobsListForMachineUICallbackListener mActiveJobsListForMachineUICallbackListener = new ActiveJobsListForMachineUICallbackListener() {
        @Override
        public void onActiveJobsListForMachineReceived(ActiveJobsListForMachine activeJobsListForMachine) {
            if (activeJobsListForMachine != null) {
                mActiveJobsListForMachine = activeJobsListForMachine;
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(0).getJoshID();
                initJobsSpinner();
                ZLogger.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            }
            else {
                mJobId = 0;
                ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
            }
            disableProgressBar();
        }

        @Override
        public void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason) {
            mJobId = 0;
            ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getDetailedDescription());
            disableProgressBar();
        }
    };

    private void disableProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public int getCroutonRoot()
    {
        return R.id.report_stop_reason_crouton_root;
    }
}
