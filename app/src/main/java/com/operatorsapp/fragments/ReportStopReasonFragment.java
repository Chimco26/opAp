package com.operatorsapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.SubReasons;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.NewStopReasonsAdapter;
import com.operatorsapp.adapters.StopReasonsAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.SendReportUtil;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.operatorsapp.view.GridSpacingItemDecoration;
import com.operatorsapp.view.GridSpacingItemDecorationRTL;

import java.util.ArrayList;

public class ReportStopReasonFragment extends BackStackAwareFragment implements OnStopReasonSelectedCallbackListener {
    private static final String LOG_TAG = ReportStopReasonFragment.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 5;
    private static final String IS_OPEN = "IS_OPEN";
    private static final String CURRENT_JOB_LIST_FOR_MACHINE = "CURRENT_JOB_LIST_FOR_MACHINE";
    private static final String CURRENT_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    private static final float MINIMUM_VERSION_TO_NEW_API = 1.7f;

    private Integer mJoshId = 0;

    private RecyclerView mRecyclerView;

    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportFieldsForMachine mReportFieldsForMachine;

    private ReportStopReasonFragmentListener mListener;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsOpen;
    private ReportCore mReportCore;
    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private SubReasons mSelectedSubreason;
    private ArrayList<Float> mSelectedEvents;
    private int mSelectedPosition;
    private int mFlavorSpanDif;
    private Switch mSwitch;

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
        if (context instanceof ShowDashboardCroutonListener) {
            mDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            OppAppLogger.getInstance().i(LOG_TAG, "Start " + mStart + " end " + mEnd + " duration " + mDuration);
            mIsOpen = getArguments().getBoolean(IS_OPEN, false);
            ActiveJobsListForMachine mActiveJobsListForMachine = getArguments().getParcelable(CURRENT_JOB_LIST_FOR_MACHINE);
            int mSelectedPosition = getArguments().getInt(CURRENT_SELECTED_POSITION);
            if (mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null
                    && mActiveJobsListForMachine.getActiveJobs().size() > mSelectedPosition) {
                mJoshId = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshID();
            }
        }

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
        mSwitch = view.findViewById(R.id.stop_switch);

        if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
            view.findViewById(R.id.powered_by_leadermess_txt).setVisibility(View.VISIBLE);
            mFlavorSpanDif = -2;
            mRecyclerView.setPadding(200, 0, 0, 0);
        }

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getStopReasons() == null || mReportFieldsForMachine.getStopReasons().size() == 0) {
            OppAppLogger.getInstance().i(LOG_TAG, "No Reasons in list");
            StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
        } else {

            mGridLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS + mFlavorSpanDif);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            int spacing = 0;//40

            Configuration config = getResources().getConfiguration();
            if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                mRecyclerView.addItemDecoration(new GridSpacingItemDecorationRTL(NUMBER_OF_COLUMNS + mFlavorSpanDif, spacing, true, 0));
            } else {
                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(NUMBER_OF_COLUMNS + mFlavorSpanDif, spacing, true, 0));
            }
            initNewStopReasons();

            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PersistenceManager.getInstance().setisNewStopReasonDesign(isChecked);
                    if (isChecked) {
                        initNewStopReasons();
                    } else {
                        initStopReasons();
                    }
                }
            });

            setSpanCount(mIsOpen);

        }

        mSwitch.setChecked(PersistenceManager.getInstance().isNewStopReasonDesign());

        view.findViewById(R.id.FRSRN_close_select_events).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                close();
            }
        });
    }

    public void setSpanCount(boolean isOpen) {
        if (mGridLayoutManager != null) {
            if (isOpen) {
                mGridLayoutManager.setSpanCount(NUMBER_OF_COLUMNS - 1 + mFlavorSpanDif);
            } else {
                mGridLayoutManager.setSpanCount(NUMBER_OF_COLUMNS + mFlavorSpanDif);
            }
            mIsOpen = isOpen;
        }
    }

    private void initStopReasons() {
        //Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Report Stop Reason- circle level 1");

        StopReasonsAdapter mStopReasonsAdapter = new StopReasonsAdapter(getContext(), mReportFieldsForMachine.getStopReasons(), this);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mStopReasonsAdapter);
    }

    private void initNewStopReasons() {
        //Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Report Stop Reason- table");

        NewStopReasonsAdapter newStopReasonsAdapter = new NewStopReasonsAdapter(getActivity(), mReportFieldsForMachine.getStopReasons(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(newStopReasonsAdapter);
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

            if (mReportFieldsForMachine.getStopReasons().get(position).getSubReasons().size() == 1) {//&& BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))

                mSelectedPosition = position;

                mSelectedSubreason = mReportFieldsForMachine.getStopReasons().get(position).getSubReasons().get(0);
                sendReport();

            } else {
                mListener.onOpenSelectStopReasonFragmentNew(SelectStopReasonFragment.newInstance(position, mJoshId,
                        mReportFieldsForMachine.getStopReasons().get(position).getId(),
                        mReportFieldsForMachine.getStopReasons().get(position).getEName(),
                        mReportFieldsForMachine.getStopReasons().get(position).getLName(), mIsOpen));
            }
        } catch (IllegalStateException e) {

            SendReportUtil.sendAcraExeption(e, "onStopReasonSelected");
        }

    }

    @Override
    public void onUpdateStopReasonSelected(int position) {
        mSelectedPosition = position;
    }

    @Override
    public void onSubReasonSelected(SubReasons subReason) {
        if (mSelectedEvents != null && mSelectedEvents.size() > 0) {

            OppAppLogger.getInstance().i(LOG_TAG, "Selected sub reason id: " + subReason.getId());

            mSelectedSubreason = subReason;

            sendReport();

//            SendBroadcast.refreshPolling(getContext());

        } else {

            Toast.makeText(getActivity(), "you need to choice at least one Event", Toast.LENGTH_SHORT).show();
        }

    }

    public void setSelectedEvents(ArrayList<Float> selectedEvents) {

        mSelectedEvents = selectedEvents;
    }

//    private void disableProgressBar() {
//        mActiveJobsProgressBar.setVisibility(View.GONE);
//    }

//    @Override
//    public int getCroutonRoot() {
//        return R.id.report_stop_reason_crouton_root;
//    }

    private void sendReport() {

        ProgressDialogManager.show(getActivity());

        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();

        reportNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());

        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());

        mReportCore.registerListener(mReportCallbackListener);

        long[] eventsId = new long[mSelectedEvents.size()];

        for (int i = 0; i < mSelectedEvents.size(); i++) {

            eventsId[i] = mSelectedEvents.get(i).intValue();

//            SendBroadcast.sendReason(getContext(), mSelectedEvents.get(i), mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getId(),
//                    mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getEName(),
//                    mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getLName(),
//                    mSelectedSubreason.getEName(), mSelectedSubreason.getLName());

        }

        if (PersistenceManager.getInstance().getVersion() < MINIMUM_VERSION_TO_NEW_API) {

            for (int i = 0; i < mSelectedEvents.size(); i++) {

                mReportCore.sendStopReport(mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getId()
                        , mSelectedSubreason.getId(), mSelectedEvents.get(i).intValue(), mJoshId);

            }

        } else {

            mReportCore.sendMultipleStopReport(mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getId(),
                    mSelectedSubreason.getId(), eventsId, mJoshId);

        }


    }


    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess(StandardResponse response) {
            if (!isAdded()){
                return;
            }
//            StandardResponse response = objectToNewError(response);
            SendBroadcast.refreshPolling(getContext());
            dismissProgressDialog();

            if (response.getFunctionSucceed()) {
                // TODO: 17/07/2018 add crouton for success
                if (response.getError().getErrorDesc() == null || response.getError().getErrorDesc().length() < 1) {
                    response.getError().setErrorDesc(getString(R.string.success));
                }
                // ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
                mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc(), false);
                OppAppLogger.getInstance().i(LOG_TAG, "sendReportSuccess()");
                Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "sendReportSuccess");

                for (int i = 0; i < mSelectedEvents.size(); i++) {

                    SendBroadcast.sendReason(getContext(), mSelectedEvents.get(i).intValue(), mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getId(),
                            mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getEName(),
                            mReportFieldsForMachine.getStopReasons().get(mSelectedPosition).getLName(),
                            mSelectedSubreason.getEName(), mSelectedSubreason.getLName());

                }


            } else {
                mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc(), true);
            }

            try {

                mReportCore.unregisterListener();

                close();

            } catch (NullPointerException e) {

                if (getFragmentManager() == null)
                    SendReportUtil.sendAcraExeption(e, "mReportCallbackListener getFragmentManager = null");

                if (mReportCore == null)
                    SendReportUtil.sendAcraExeption(e, "mReportCallbackListener mReportCore = null");
            }
        }

        @Override
        public void sendReportFailure(StandardResponse reason) {
            if (!isAdded()){
                return;
            }
            dismissProgressDialog();
            OppAppLogger.getInstance().w(LOG_TAG, "sendReportFailure()");
            if (reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Credentials_mismatch && getActivity() != null) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendReport();
                    }

                    @Override
                    public void onSilentLoginFailed(StandardResponse reason) {
                        OppAppLogger.getInstance().w(LOG_TAG, "Failed silent login");
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
                        ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
                    }
                });
            } else {
                String msg = "missing reports";
                if (reason != null && reason.getError().getErrorDesc() != null) {
                    msg = reason.getError().getErrorDesc();
                }
                StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, msg);
                ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            }

        }
    };

    public void close() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    private void dismissProgressDialog() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogManager.dismiss();
                }
            });
        }
    }

    private StandardResponse objectToNewError(Object o) {
        StandardResponse responseNewVersion;
        if (o instanceof StandardResponse) {
            responseNewVersion = (StandardResponse) o;
        } else {
            Gson gson = new GsonBuilder().create();

            ErrorResponse er = gson.fromJson(new Gson().toJson(o), ErrorResponse.class);

            responseNewVersion = new StandardResponse(true, 0, er);
            if (responseNewVersion.getError().getErrorCode() != 0) {
                responseNewVersion.setFunctionSucceed(false);
            }
        }
        return responseNewVersion;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mReportCallbackListener = null;
        mDashboardCroutonListener= null;
        mOnCroutonRequestListener= null;
        mListener= null;
    }

    public interface ReportStopReasonFragmentListener {

        void onOpenSelectStopReasonFragmentNew(SelectStopReasonFragment selectStopReasonFragment);
    }
}