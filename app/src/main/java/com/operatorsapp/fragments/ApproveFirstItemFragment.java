package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.adapters.RejectReasonSpinnerAdapter;
import com.operatorsapp.adapters.TechnicianSpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;

public class ApproveFirstItemFragment extends Fragment implements View.OnClickListener, CroutonRootProvider
{

    private static final String LOG_TAG = ApproveFirstItemFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private TextView mCancelButton;
    private Button mNextButton;
    private GoToScreenListener mGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private int mSelectedReasonId;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private String mCurrentProductName;
    private int mCurrentProductId;
    private Integer mJobId = 0;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private Spinner mJobsSpinner;
    private ProgressBar mActiveJobsProgressBar;
    private ReportCore mReportCore;
    private int mSelectedTechnicianId;

    public static ApproveFirstItemFragment newInstance(String currentProductName, int currentProductId) {
        ApproveFirstItemFragment reportRejectsFragment = new ApproveFirstItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_PRODUCT_NAME, currentProductName);
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        reportRejectsFragment.setArguments(bundle);
        return reportRejectsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGoToScreenListener = (GoToScreenListener) getActivity();
        ReportFieldsFragmentCallbackListener reportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = reportFieldsFragmentCallbackListener.getReportForMachine();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentProductName = getArguments().getString(CURRENT_PRODUCT_NAME);
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_approve_first_item, container, false);
        setActionBar();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
        getActiveJobs();
        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);
        mNextButton = (Button) view.findViewById(R.id.button_approve);

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getRejectCauses() == null || mReportFieldsForMachine.getRejectReasons() == null || mReportFieldsForMachine.getRejectCauses().size() == 0 || mReportFieldsForMachine.getRejectReasons().size() == 0) {
            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            mNextButton.setEnabled(false);
        } else {
//            mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
            mNextButton.setEnabled(true);
        }

        TextView productNameTextView = (TextView) view.findViewById(R.id.report_cycle_u_product_name_text_view);
        TextView productIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);

        productNameTextView.setText(new StringBuilder(mCurrentProductName).append(","));
        productIdTextView.setText(String.valueOf(mCurrentProductId));

        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);


        if (mReportFieldsForMachine != null) {
            Spinner rejectReasonSpinner = (Spinner) view.findViewById(R.id.reject_reason_spinner);

            final RejectReasonSpinnerAdapter reasonSpinnerArrayAdapter = new RejectReasonSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectReasons());
            reasonSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rejectReasonSpinner.setAdapter(reasonSpinnerArrayAdapter);
            rejectReasonSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if (mIsFirstReasonSpinnerSelection) {
//                        mIsFirstReasonSpinnerSelection = false;
//                        mIsReasonSelected = false;
//
//                    }
//                    else {
//                        mIsReasonSelected = true;
                    mSelectedReasonId = mReportFieldsForMachine.getRejectReasons().get(position).getId();
                    String nameByLang = OperatorApplication.isEnglishLang() ? mReportFieldsForMachine.getRejectReasons().get(position).getEName() :  mReportFieldsForMachine.getRejectReasons().get(position).getLName();
                    reasonSpinnerArrayAdapter.setTitle(position);
//                        mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
//                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            Spinner technicianSpinner = (Spinner) view.findViewById(R.id.technician_spinner);
            final TechnicianSpinnerAdapter technicianSpinnerAdapter = new TechnicianSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getTechnicians());
            technicianSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            technicianSpinner.setAdapter(technicianSpinnerAdapter);
            technicianSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            technicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mReportFieldsForMachine.getRejectCauses().size() > 0)
                    {
                        mSelectedTechnicianId = mReportFieldsForMachine.getTechnicians().get(position).getID();
                    }

                    technicianSpinnerAdapter.setTitle(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);
        //mNextButton = (Button) view.findViewById(R.id.button_next);

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
            View view = inflater.inflate(R.layout.report_resects_action_bar, null);

            LinearLayout buttonClose = (LinearLayout) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });

            ((TextView)view.findViewById(R.id.new_job_title)).setText(R.string.first_item_approval);

            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel: {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.button_approve: {
                sendReport();
                break;
            }
        }
    }

    private void sendReport()
    {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.injectApproveFirstItem(NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        mReportCore.sendApproveFirstItem(mSelectedReasonId, mSelectedTechnicianId, mJobId);
    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener()
    {
        @Override
        public void sendReportSuccess()
        {
            dismissProgressDialog();
            ZLogger.i(LOG_TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();
            getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason)
        {
            dismissProgressDialog();
            ZLogger.w(LOG_TAG, "sendReportFailure()");
            if(reason.getError() == ErrorObjectInterface.ErrorCode.Credentials_mismatch)
            {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback()
                {
                    @Override
                    public void onSilentLoginSucceeded()
                    {
                        sendReport();
                    }

                    @Override
                    public void onSilentLoginFailed(ErrorObjectInterface reason)
                    {
                        ZLogger.w(LOG_TAG, "Failed silent login");
                        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
                        ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
                        dismissProgressDialog();
                    }
                });
            }
            else
            {

                ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
                ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            }
        }
    };

    private void dismissProgressDialog()
    {
        if(getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    ProgressDialogManager.dismiss();
                }
            });
        }
    }

    private void getActiveJobs() {
        ActiveJobsListForMachineNetworkBridge activeJobsListForMachineNetworkBridge = new ActiveJobsListForMachineNetworkBridge();
        activeJobsListForMachineNetworkBridge.inject(NetworkManager.getInstance());
        ActiveJobsListForMachineCore activeJobsListForMachineCore = new ActiveJobsListForMachineCore(PersistenceManager.getInstance(), activeJobsListForMachineNetworkBridge);
        activeJobsListForMachineCore.registerListener(mActiveJobsListForMachineUICallbackListener);
        activeJobsListForMachineCore.getActiveJobsListForMachine();
    }

    private ActiveJobsListForMachineUICallbackListener mActiveJobsListForMachineUICallbackListener = new ActiveJobsListForMachineUICallbackListener() {
        @Override
        public void onActiveJobsListForMachineReceived(ActiveJobsListForMachine activeJobsListForMachine) {
            if (activeJobsListForMachine != null) {
                mActiveJobsListForMachine = activeJobsListForMachine;
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(0).getJoshID();
                initJobsSpinner();
                ZLogger.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            } else {
                mJobId = 0;
                ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
            }
            disableProgressBar();
        }

        @Override
        public void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason) {
            mJobId = 0;
            ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getDetailedDescription());
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener);
            disableProgressBar();
        }
    };

    private void disableProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }

    private void initJobsSpinner() {
        if(getActivity() != null)
        {
            if (mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null)
            {
                mJobsSpinner.setVisibility(View.VISIBLE);
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

    @Override
    public int getCroutonRoot()
    {
        return R.id.parent_layouts;
    }
}
