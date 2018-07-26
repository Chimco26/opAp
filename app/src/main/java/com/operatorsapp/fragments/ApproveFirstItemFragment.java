package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.adapters.RejectReasonSpinnerAdapter;
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
import com.zemingo.logrecorder.ZLogger;

public class ApproveFirstItemFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider {

    private static final String LOG_TAG = ApproveFirstItemFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private static final String CURRENT_JOB_LIST_FOR_MACHINE = "CURRENT_JOB_LIST_FOR_MACHINE";
    private static final String CURRENT_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    private TextView mCancelButton;
    private Button mNextButton;
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
    private ApproveFirstItemFragmentCallbackListener mCallbackListener;
    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private int mSelectedPosition;

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
        mReportFieldsForMachine = reportFieldsFragmentCallbackListener.getReportForMachine();
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();

        if(context instanceof ApproveFirstItemFragmentCallbackListener)
        {
            mCallbackListener = (ApproveFirstItemFragmentCallbackListener) context;
        }
        if (context instanceof ShowDashboardCroutonListener) {
            mDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        }
    }

    @Override
    public void onDetach()
    {
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
        if (!PersistenceManager.getInstance().getAddRejectsOnSetupEnd()){
            view.findViewById(R.id.reject_reason_tv).setVisibility(View.GONE);
            view.findViewById(R.id.reject_reason_spinner).setVisibility(View.GONE);
            view.findViewById(R.id.reject_reason_rl).setVisibility(View.GONE);
        }
        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
//        getActiveJobs();
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

        productNameTextView.setText(new StringBuilder(mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshName()).append(","));
        productIdTextView.setText(String.valueOf(mCurrentProductId));

        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);


        if (mReportFieldsForMachine != null) {

            if (PersistenceManager.getInstance().getAddRejectsOnSetupEnd()) {
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
                        String nameByLang = OperatorApplication.isEnglishLang() ? mReportFieldsForMachine.getRejectReasons().get(position).getEName() : mReportFieldsForMachine.getRejectReasons().get(position).getLName();
                        reasonSpinnerArrayAdapter.setTitle(position);
//                        mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
//                    }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }else {
                for (RejectReasons rejectReason : mReportFieldsForMachine.getRejectReasons()) {
                    String nameByLang = OperatorApplication.isEnglishLang() ? rejectReason.getEName() : rejectReason.getLName();
                    if (nameByLang.equals(R.string.reject_reason_setup)){
                        mSelectedReasonId = rejectReason.getId();
                        break;
                    }
                }
            }

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

        initJobsSpinner();

        disableSpinnerProgressBar();
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
            View view = inflater.inflate(R.layout.report_resects_action_bar, null);

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

            ((TextView)view.findViewById(R.id.new_job_title)).setText(R.string.first_item_approval);

            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel: {
                FragmentManager fragmentManager = getFragmentManager();
                if(fragmentManager != null)
                {
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

    private void sendReport()
    {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.injectApproveFirstItem(NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        mReportCore.sendApproveFirstItem(mSelectedReasonId, mSelectedTechnicianId, mJobId);
//        SendBroadcast.refreshPolling(getContext());

    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener()
    {
        @Override
        public void sendReportSuccess(Object o)
        {//TODO crouton error
            ErrorResponseNewVersion response = objectToNewError(o);
            SendBroadcast.refreshPolling(getContext());
            dismissProgressDialog();

            if (response.isFunctionSucceed()){
                // TODO: 17/07/2018 add crouton for success
                // ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getmError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
                mDashboardCroutonListener.onShowCrouton(response.getmError().getErrorDesc());
            }else {
                mDashboardCroutonListener.onShowCrouton(response.getmError().getErrorDesc());
            }


//            if (((SendApproveFirstItemResponse) o).getErrorResponse() != null){
//
//                mDashboardCroutonListener.onShowCrouton(((SendApproveFirstItemResponse) o).getErrorResponse().getErrorDesc());
//            }else{
//                // TODO: 15/07/2018 add another constructor for success
//                mDashboardCroutonListener.onShowCrouton(getString(R.string.end_setup_success));
//            }
            ZLogger.i(LOG_TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();
            if(mCallbackListener != null)
            {
                mCallbackListener.onApproveFirstItemComplete();
            }
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager != null)
            {
                fragmentManager.popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

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

    private ErrorResponseNewVersion objectToNewError(Object o) {
        ErrorResponseNewVersion responseNewVersion;
        if (o instanceof ErrorResponseNewVersion){
            responseNewVersion = (ErrorResponseNewVersion)o;
        }else {
            Gson gson = new GsonBuilder().create();

            ErrorResponse er = gson.fromJson(new Gson().toJson(o), ErrorResponse.class);

            responseNewVersion = new ErrorResponseNewVersion(true, 0, er);
            if (responseNewVersion.getmError().getErrorCode() != 0){
                responseNewVersion.setFunctionSucceed(false);
            }
        }
        return responseNewVersion;
    }

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

    private void disableSpinnerProgressBar() {
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
                mJobsSpinner.setSelection(mSelectedPosition);
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
