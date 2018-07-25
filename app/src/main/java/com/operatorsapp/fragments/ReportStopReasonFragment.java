package com.operatorsapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.operators.activejobslistformachinecore.ActiveJobsListForMachineCore;
import com.operators.activejobslistformachinecore.interfaces.ActiveJobsListForMachineUICallbackListener;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachinenetworkbridge.ActiveJobsListForMachineNetworkBridge;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.StopReasons;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.StopReasonsAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.SendReportUtil;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;
import com.ravtech.david.sqlcore.Event;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ReportStopReasonFragment extends BackStackAwareFragment implements OnStopReasonSelectedCallbackListener, CroutonRootProvider, DashboardUICallbackListener {
    private static final String LOG_TAG = ReportStopReasonFragment.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 5;
    private static final String IS_OPEN = "IS_OPEN";
    //    private static final String SELECTED_STOP_REASON_POSITION = "selected_stop_reason_position";
//    private static final String CURRENT_JOB_ID = "current_job_id";

    private Integer mJobId = 0;

    private RecyclerView mRecyclerView;

    private GoToScreenListener mGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportFieldsForMachine mReportFieldsForMachine;

    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private ProgressBar mActiveJobsProgressBar;
    private ReportStopReasonFragmentListener mListener;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsOpen;
    private StopReasonsAdapter mStopReasonsAdapter;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;

    public static ReportStopReasonFragment newInstance(boolean isOpen) {
        ReportStopReasonFragment reportStopReasonFragment = new ReportStopReasonFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_OPEN, isOpen);
        reportStopReasonFragment.setArguments(bundle);
        return reportStopReasonFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
        mOnActivityCallbackRegistered.onFragmentAttached(this);
        mGoToScreenListener = (GoToScreenListener) context;
        ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = mReportFieldsFragmentCallbackListener.getReportForMachine();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
        mListener = (ReportStopReasonFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnActivityCallbackRegistered.onFragmentDetached(this);
        mOnActivityCallbackRegistered = null;

        mGoToScreenListener = null;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            ZLogger.i(LOG_TAG, "Start " + mStart + " end " + mEnd + " duration " + mDuration);
            mIsOpen = getArguments().getBoolean(IS_OPEN, false);
        }
//        getActiveJobs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_stop_reason_new, container, false);

        setActionBar();
//        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
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

        mStopReasonsAdapter = new StopReasonsAdapter(getContext(), mReportFieldsForMachine.getStopReasons(), this);

        mRecyclerView.setAdapter(mStopReasonsAdapter);
    }

    private List<StopReasons> getFilterReasone(List<StopReasons> stopReasons) {
        List<StopReasons> stopReasonsList = new ArrayList<>();

        try {

            for (StopReasons reasons : stopReasons) {

                // TODO: DAVID VARDI Sprint 1.5: add if for filter
                stopReasons.add(reasons);
            }

        } catch (ConcurrentModificationException e) {

            if (e.getMessage() != null)
                Log.e(LOG_TAG, e.getMessage());
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
                ZLogger.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            } else {
                mJobId = 0;
                ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
            }
//            disableProgressBar();
        }

        @Override
        public void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason) {
            mJobId = 0;
            ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getDetailedDescription());
//            disableProgressBar();
        }
    };

    @Override
    public void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine activeJobsListForMachine) {
        if (activeJobsListForMachine != null) {
            mActiveJobsListForMachine = activeJobsListForMachine;
            mJobId = mActiveJobsListForMachine.getActiveJobs().get(0).getJoshID();
            ZLogger.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
        } else {
            mJobId = 0;
            ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
        }
    }

    private void disableProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public int getCroutonRoot() {
        return R.id.report_stop_reason_crouton_root;
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {

    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {

    }

    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events) {

    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {

    }

    @Override
    public void onDataFailure(ErrorObjectInterface reason, CallType callType) {

    }

    @Override
    public void onShiftForMachineEnded() {

    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {

    }

    public interface ReportStopReasonFragmentListener {

        void onOpenSelectStopReasonFragmentNew(SelectStopReasonFragment selectStopReasonFragment);
    }
}