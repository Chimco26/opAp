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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponse;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.adapters.RejectCauseSpinnerAdapter;
import com.operatorsapp.adapters.RejectReasonSpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.zemingo.logrecorder.ZLogger;

public class ReportRejectsFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider
{

    private static final String LOG_TAG = ReportRejectsFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    public static final String DASHBOARD_FRAGMENT = "dashboard_fragment";
    private TextView mCancelButton;
    private Button mNextButton;
    //    private boolean mIsFirstReasonSpinnerSelection = true;
//    private boolean mIsReasonSelected;
    private GoToScreenListener mGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private int mSelectedReasonId;
    private int mSelectedCauseId;
    private String mSelectedReasonName;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private String mCurrentProductName;
    private int mCurrentProductId;
    private Integer mJobId = 0;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private Spinner mJobsSpinner;
    private ProgressBar mActiveJobsProgressBar;
    private EditText mUnitsEditText;
    private EditText mWeightEditText;
    private Double mUnitsData = null;
    private Double mWeightData = null;
    private ReportCore mReportCore;

    public static ReportRejectsFragment newInstance(String currentProductName, int currentProductId) {
        ReportRejectsFragment reportRejectsFragment = new ReportRejectsFragment();
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
        final View view = inflater.inflate(R.layout.fragment_report_rejects, container, false);
        setActionBar();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mUnitsEditText, InputMethodManager.SHOW_IMPLICIT);

        if (!PersistenceManager.getInstance().getDisplayRejectFactor()){
            view.findViewById(R.id.cause_tv).setVisibility(View.GONE);
            view.findViewById(R.id.cause_spinner).setVisibility(View.GONE);
            view.findViewById(R.id.cause_rl).setVisibility(View.GONE);
        }
        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);
        mNextButton = (Button) view.findViewById(R.id.button_approve);
        mUnitsEditText = (EditText) view.findViewById(R.id.units_edit_text);
        mWeightEditText = (EditText) view.findViewById(R.id.weight_edit_text);
        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);
        TextView productNameTextView = (TextView) view.findViewById(R.id.report_cycle_u_product_name_text_view);
        TextView productIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);

        getActiveJobs();

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getRejectCauses() == null || mReportFieldsForMachine.getRejectReasons() == null || mReportFieldsForMachine.getRejectCauses().size() == 0 || mReportFieldsForMachine.getRejectReasons().size() == 0) {
            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            mNextButton.setEnabled(false);
            mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_disabled));
        } else {
            mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
            mNextButton.setEnabled(true);
        }

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
                    mSelectedReasonName = nameByLang;
                    reasonSpinnerArrayAdapter.setTitle(position);
//                        mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
//                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (PersistenceManager.getInstance().getDisplayRejectFactor()) {
                Spinner causeSpinner = (Spinner) view.findViewById(R.id.cause_spinner);
                final RejectCauseSpinnerAdapter causeSpinnerArrayAdapter = new RejectCauseSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectCauses());
                causeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                causeSpinner.setAdapter(causeSpinnerArrayAdapter);
                causeSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

                causeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (mReportFieldsForMachine.getRejectCauses().size() > 0) {
                            mSelectedCauseId = mReportFieldsForMachine.getRejectCauses().get(position).getId();
                        }
                        causeSpinnerArrayAdapter.setTitle(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }else {
                mSelectedCauseId = 0;
            }
        }

        productNameTextView.setText(new StringBuilder(mCurrentProductName).append(","));
        productIdTextView.setText(String.valueOf(mCurrentProductId));

        mUnitsEditText.setFocusableInTouchMode(true);
        mUnitsEditText.requestFocus();

        mUnitsEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                try
                {
                    mUnitsData= Double.parseDouble(s.toString());
                }
                catch (NumberFormatException e)
                {
                    mUnitsData = null;
                }
                refreshSendButtonState();
            }
        });

        mWeightEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                try
                {
                    mWeightData = Double.parseDouble(s.toString());
                }
                catch (NumberFormatException e)
                {
                    mWeightData = null;
                }
                refreshSendButtonState();
            }
        });

    }

    private void refreshSendButtonState()
    {
        if(canSendReport())
        {
            mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
            mNextButton.setEnabled(true);
        }
        else
        {
            mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_disabled));
            mNextButton.setEnabled(false);
        }
    }

    private boolean canSendReport()
    {

        return ((mUnitsData != null || mWeightData != null) && mNextButton.isEnabled());
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

    public void setActionBar() {
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
//                    FragmentManager fragmentManager = getFragmentManager();
//                    if(fragmentManager != null)
//                    {
//                        fragmentManager.popBackStack();
//                    }
                    getActivity().onBackPressed();
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel: {
//                getFragmentManager().popBackStack();
                getActivity().onBackPressed();

                break;
            }
            case R.id.button_approve: {

                ZLogger.d(LOG_TAG, "reason: " + mSelectedReasonId + " cause: " + mSelectedCauseId + " units: " + mUnitsEditText.getText().toString() + " weight: " + mWeightEditText.getText().toString() + " jobId " + mJobId);
                if(canSendReport())
                {
                    sendReport();
                }

//                mGoToScreenListener.goToFragment(ReportRejectSelectParametersFragment.newInstance(mSelectedReasonId, mSelectedCauseId, mSelectedReasonName, mJobId, mCurrentProductName, mCurrentProductId), true);

                break;
            }
        }
    }

    private void sendReport()
    {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        mReportCore.sendReportReject(mSelectedReasonId, mSelectedCauseId, mUnitsData, mWeightData, mJobId);
        // TODO: 25/06/2018 check all instances and make the call after response
//        SendBroadcast.refreshPolling(getContext());
    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener()
    {

        @Override
        public void sendReportSuccess(Object errorResponse) {
            ErrorResponseNewVersion response = objectToNewError(errorResponse);
            SendBroadcast.refreshPolling(getContext());
            dismissProgressDialog();
            ZLogger.i(LOG_TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();

            if (response.isFunctionSucceed()){
                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getmError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
            }else {
                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getmError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
            }
            if (getFragmentManager() != null){

                getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    @Override
    public int getCroutonRoot()
    {
        return R.id.parent_layouts;
    }
}
