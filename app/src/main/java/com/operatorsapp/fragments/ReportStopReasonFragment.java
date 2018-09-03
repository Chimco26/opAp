package com.operatorsapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.adapters.StopReasonsAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.utils.SendReportUtil;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;
import com.zemingo.logrecorder.ZLogger;

public class ReportStopReasonFragment extends BackStackAwareFragment implements OnStopReasonSelectedCallbackListener, CroutonRootProvider {
    private static final String LOG_TAG = ReportStopReasonFragment.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 5;
    private static final String IS_OPEN = "IS_OPEN";
    private static final String CURRENT_JOB_LIST_FOR_MACHINE = "CURRENT_JOB_LIST_FOR_MACHINE";
    private static final String CURRENT_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    //    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
//    private static final String CURRENT_JOB_ID = "current_job_id";

    private Integer mJobId = 0;

    private RecyclerView mRecyclerView;

    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportFieldsForMachine mReportFieldsForMachine;

    private ReportStopReasonFragmentListener mListener;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsOpen;

    public static ReportStopReasonFragment newInstance(boolean isOpen, ActiveJobsListForMachine activeJobsListForMachine, int selectedPosition) {
        ReportStopReasonFragment reportStopReasonFragment = new ReportStopReasonFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_OPEN, isOpen);
        bundle.putParcelable(CURRENT_JOB_LIST_FOR_MACHINE, activeJobsListForMachine);
        bundle.putInt(CURRENT_SELECTED_POSITION, selectedPosition);
        reportStopReasonFragment.setArguments(bundle);
        return reportStopReasonFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        if (mReportFieldsFragmentCallbackListener != null) {
            mReportFieldsForMachine = mReportFieldsFragmentCallbackListener.getReportForMachine();
        }
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
        mListener = (ReportStopReasonFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            ZLogger.i(LOG_TAG, "Start " + mStart + " end " + mEnd + " duration " + mDuration);
            mIsOpen = getArguments().getBoolean(IS_OPEN, false);
            ActiveJobsListForMachine mActiveJobsListForMachine = getArguments().getParcelable(CURRENT_JOB_LIST_FOR_MACHINE);
            int mSelectedPosition = getArguments().getInt(CURRENT_SELECTED_POSITION);
            if (mActiveJobsListForMachine != null) {
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshID();
            }
        }

        // Analytics
        OperatorApplication application = (OperatorApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName(LOG_TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        getActiveJobs();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_stop_reason_new, container, false);

        setActionBar();
//        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.stop_recycler_view);

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getStopReasons() == null || mReportFieldsForMachine.getStopReasons().size() == 0) {
            ZLogger.i(LOG_TAG, "No Reasons in list");
            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
        } else {

            mGridLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            int spacing = 0;//40

            Configuration config = getResources().getConfiguration();
            if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                mRecyclerView.addItemDecoration(new GridSpacingItemDecorationRTL(NUMBER_OF_COLUMNS, spacing, true, 0));
            } else {
                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS, spacing, true, 0));
            }
            initStopReasons();

            setSpanCount(mIsOpen);

        }

    }

    public void setSpanCount(boolean isOpen) {
        if (mGridLayoutManager != null) {
            if (isOpen) {
                mGridLayoutManager.setSpanCount(NUMBER_OF_COLUMNS - 1);
            } else {
                mGridLayoutManager.setSpanCount(NUMBER_OF_COLUMNS);
            }
            mIsOpen = isOpen;
        }
    }

    private void initStopReasons() {

        StopReasonsAdapter mStopReasonsAdapter = new StopReasonsAdapter(getContext(), mReportFieldsForMachine.getStopReasons(), this);

        mRecyclerView.setAdapter(mStopReasonsAdapter);
    }

//    private List<StopReasons> getFilterReasone(List<StopReasons> stopReasons) {
//        List<StopReasons> stopReasonsList = new ArrayList<>();
//
//        try {
//
//            for (StopReasons reasons : stopReasons) {
//
//                // TODO: DAVID VARDI Sprint 1.5: add if for filter
//                stopReasons.add(reasons);
//            }
//
//        } catch (ConcurrentModificationException e) {
//
//            if (e.getMessage() != null)
//                Log.e(LOG_TAG, e.getMessage());
//        }
//
//
//        return stopReasonsList;
//    }

    @Override
    public void onPause() {
        super.onPause();
        mOnCroutonRequestListener.onHideConnectivityCroutonRequest();
    }

    @Override
    public void onResume() {
        super.onResume();
//        getActiveJobs();
    }

    protected void setActionBar() {

    }

    @Override
    public void onStopReasonSelected(int position) {

        try {

            mListener.onOpenSelectStopReasonFragmentNew(SelectStopReasonFragment.newInstance(position, mJobId,
                    mReportFieldsForMachine.getStopReasons().get(position).getId(),
                    mReportFieldsForMachine.getStopReasons().get(position).getEName(),
                    mReportFieldsForMachine.getStopReasons().get(position).getLName(), mIsOpen));

        } catch (IllegalStateException e) {

            SendReportUtil.sendAcraExeption(e, "onStopReasonSelected");
        }

    }

//    private void disableProgressBar() {
//        mActiveJobsProgressBar.setVisibility(View.GONE);
//    }

    @Override
    public int getCroutonRoot() {
        return R.id.report_stop_reason_crouton_root;
    }

    public interface ReportStopReasonFragmentListener {

        void onOpenSelectStopReasonFragmentNew(SelectStopReasonFragment selectStopReasonFragment);
    }
}