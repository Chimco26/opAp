package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.oppapplog.OppAppLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.reportfieldsformachineinfra.RejectReasons;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponse;
import com.operators.reportrejectnetworkbridge.server.response.ResponseStatus;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.TechnicianSpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.ApproveFirstItemFragmentCallbackListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ApproveFirstItemFragment extends DialogFragment implements View.OnClickListener, CroutonRootProvider {

    private static final String LOG_TAG = ApproveFirstItemFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private static final String CURRENT_JOB_LIST_FOR_MACHINE = "CURRENT_JOB_LIST_FOR_MACHINE";
    private static final String CURRENT_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    private static final int REFRESH_DELAY_MILLIS = 3000;
    private RelativeLayout mCancelButton;
    private Button mNextButton;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private int mSelectedReasonId;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private int mCurrentProductId;
    private Integer mJobId = 0;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private ReportCore mReportCore;
    private int mSelectedTechnicianId;
    private ApproveFirstItemFragmentCallbackListener mCallbackListener;
    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private int mSelectedPosition;

    public ApproveFirstItemFragment() {
    }

    public static ApproveFirstItemFragment newInstance(int currentProductId, ActiveJobsListForMachine activeJobsListForMachine, int selectedPosition) {
        ApproveFirstItemFragment reportRejectsFragment = new ApproveFirstItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        bundle.putParcelable(CURRENT_JOB_LIST_FOR_MACHINE, activeJobsListForMachine);
        bundle.putInt(CURRENT_SELECTED_POSITION, selectedPosition);
        reportRejectsFragment.setArguments(bundle);
        return reportRejectsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ReportFieldsFragmentCallbackListener reportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        if (reportFieldsFragmentCallbackListener != null) {
            mReportFieldsForMachine = reportFieldsFragmentCallbackListener.getReportForMachine();
        }
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();

        if (context instanceof ApproveFirstItemFragmentCallbackListener) {
            mCallbackListener = (ApproveFirstItemFragmentCallbackListener) context;
        }
        if (context instanceof ShowDashboardCroutonListener) {
            mDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbackListener = null;
        mOnCroutonRequestListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
            mActiveJobsListForMachine = getArguments().getParcelable(CURRENT_JOB_LIST_FOR_MACHINE);
            mSelectedPosition = getArguments().getInt(CURRENT_SELECTED_POSITION);
            mJobId = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshID();
        }

        // Analytics
        OperatorApplication application = (OperatorApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        PersistenceManager pm = PersistenceManager.getInstance();
        mTracker.setScreenName(LOG_TAG);
        mTracker.setClientId("machine id: " + pm.getMachineId());
        mTracker.setAppVersion(pm.getVersion() + "");
        mTracker.setHostname(pm.getSiteName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //        setActionBar();

        return inflater.inflate(R.layout.fragment_approve_first_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!PersistenceManager.getInstance().getAddRejectsOnSetupEnd()) {
//            view.findViewById(R.id.reject_reason_tv).setVisibility(View.GONE);
//            view.findViewById(R.id.reject_reason_spinner).setVisibility(View.GONE);
//            view.findViewById(R.id.reject_reason_rl).setVisibility(View.GONE);
        }
//        getActiveJobs();
        mCancelButton = view.findViewById(R.id.button_cancel);
        mNextButton = view.findViewById(R.id.button_approve);

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getRejectCauses() == null || mReportFieldsForMachine.getRejectReasons() == null || mReportFieldsForMachine.getRejectCauses().size() == 0 || mReportFieldsForMachine.getRejectReasons().size() == 0) {
            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            mNextButton.setEnabled(false);
        } else {
//            mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
            mNextButton.setEnabled(true);
        }

        if (mReportFieldsForMachine != null) {

//            sortRejectReasons();
//            if (PersistenceManager.getInstance().getAddRejectsOnSetupEnd() && getActivity() != null) {
//                Spinner rejectReasonSpinner = view.findViewById(R.id.reject_reason_spinner);
//
//                final RejectReasonSpinnerAdapter reasonSpinnerArrayAdapter = new RejectReasonSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectReasons());
//                reasonSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                rejectReasonSpinner.setAdapter(reasonSpinnerArrayAdapter);
//                rejectReasonSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
//
//                rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                        mSelectedReasonId = mReportFieldsForMachine.getRejectReasons().get(position).getId();
//                        reasonSpinnerArrayAdapter.setTitle(position);
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                    }
//                });
//            } else {
//                for (RejectReasons rejectReason : mReportFieldsForMachine.getRejectReasons()) {
//                    String nameByLang = OperatorApplication.isEnglishLang() ? rejectReason.getEName() : rejectReason.getLName();
//                    if (nameByLang.equals(getString(R.string.reject_reason_setup))) {
//                        mSelectedReasonId = rejectReason.getId();
//                        break;
//                    }
//                }
//            }

            Spinner technicianSpinner = view.findViewById(R.id.technician_spinner);
            final TechnicianSpinnerAdapter technicianSpinnerAdapter = new TechnicianSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getTechnicians());
            technicianSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            technicianSpinner.setAdapter(technicianSpinnerAdapter);

            technicianSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            technicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mReportFieldsForMachine.getRejectCauses().size() > 0) {
                        mSelectedTechnicianId = mReportFieldsForMachine.getTechnicians().get(position).getID();
                    }

                    technicianSpinnerAdapter.setTitle(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        mCancelButton = view.findViewById(R.id.button_cancel);
    }

    private void sortRejectReasons() {
        List<RejectReasons> list = mReportFieldsForMachine.getRejectReasons();
        Collections.sort(list, new Comparator<RejectReasons>() {
            public int compare(RejectReasons o1, RejectReasons o2) {
                if (OperatorApplication.isEnglishLang()) {
                    return o1.getEName().compareTo(o2.getEName());
                } else {
                    return o1.getLName().compareTo(o2.getLName());
                }
            }
        });
        for (RejectReasons rr : list) {
            if (rr.getEName().equals("Setup")) {
                list.remove(rr);
                list.add(0, rr);
                break;
            }
        }
        mReportFieldsForMachine.setRejectReasons(list);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCancelButton.setOnClickListener(null);
        mNextButton.setOnClickListener(null);
        mOnCroutonRequestListener.onHideConnectivityCroutonRequest();
    }


    @Override
    public void onResume() {
        super.onResume();
        mCancelButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mCallbackListener.onApproveFirstItemShowFilter(true);
    }

    protected void setActionBar() {
        if (getActivity() != null) {
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
                View view = inflater.inflate(R.layout.report_resects_action_bar, null);

                LinearLayout buttonClose = view.findViewById(R.id.close_image);
                buttonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null) {
                            fragmentManager.popBackStack();
                        }
                    }
                });

                ((TextView) view.findViewById(R.id.new_job_title)).setText(R.string.first_item_approval);

                actionBar.setCustomView(view);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel: {
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    fragmentManager.popBackStack();
                }
                break;
            }
            case R.id.button_approve: {
                sendReport();
                break;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mCallbackListener.onApproveFirstItemShowFilter(false);
    }

    private void sendReport() {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.injectApproveFirstItem(NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        mReportCore.sendApproveFirstItem(mSelectedReasonId, mSelectedTechnicianId, mJobId);
//        SendBroadcast.refreshPolling(getContext());

    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess(Object o) {//TODO crouton error
            ResponseStatus response = objectToNewError(o);
            SendBroadcast.refreshPolling(getContext());
            dismissProgressDialog();

            if (response.isFunctionSucceed()) {
                mDashboardCroutonListener.onShowCrouton(response.getmError().getErrorDesc(), false);
            } else {
                mDashboardCroutonListener.onShowCrouton(response.getmError().getErrorDesc(), true);
            }

            OppAppLogger.getInstance().i(LOG_TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();
            if (mCallbackListener != null) {
                mCallbackListener.onApproveFirstItemComplete();
            }
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason) {
            dismissProgressDialog();
            OppAppLogger.getInstance().w(LOG_TAG, "sendReportFailure()");
            if (reason.getError() == ErrorObjectInterface.ErrorCode.Credentials_mismatch && getActivity() != null) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendReport();
                    }

                    @Override
                    public void onSilentLoginFailed(ErrorObjectInterface reason) {
                        OppAppLogger.getInstance().w(LOG_TAG, "Failed silent login");
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
                        ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
                        dismissProgressDialog();
                    }
                });
            } else {

                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
                ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            }
            SendBroadcast.refreshPolling(getContext());

        }
    };

    private ResponseStatus objectToNewError(Object o) {
        ResponseStatus responseNewVersion;
        if (o instanceof ResponseStatus) {
            responseNewVersion = (ResponseStatus) o;
        } else {
            Gson gson = new GsonBuilder().create();

            ErrorResponse er = gson.fromJson(new Gson().toJson(o), ErrorResponse.class);

            responseNewVersion = new ResponseStatus(true, 0, er);
            if (responseNewVersion.getmError().getErrorCode() != 0) {
                responseNewVersion.setFunctionSucceed(false);
            }
        }
        return responseNewVersion;
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

    @Override
    public int getCroutonRoot() {
        return R.id.parent_layouts;
    }

}
