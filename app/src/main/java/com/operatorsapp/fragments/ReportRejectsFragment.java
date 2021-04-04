package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.adapters.RejectCauseSpinnerAdapter;
import com.operatorsapp.adapters.RejectReasonSpinnerAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;

public class ReportRejectsFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider {

    private static final String LOG_TAG = ReportRejectsFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    public static final String DASHBOARD_FRAGMENT = "dashboard_fragment";
    private static final int REFRESH_DELAY_MILLIS = 3000;
    private static final String CURRENT_JOB_LIST_FOR_MACHINE = "CURRENT_JOB_LIST_FOR_MACHINE";
    private static final String CURRENT_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    private static final String REJECT_MESURING = "REJECT_MESURING";
    private TextView mCancelButton;
    private Button mNextButton;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private int mSelectedReasonId;
    private int mSelectedCauseId;
    private ReportFieldsForMachine mReportFieldsForMachine;
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
    private int mSelectedPosition;
    private String mRejectMesuaring;
    private View mUnitsLy;
    private View mWeight_ly;

    public static ReportRejectsFragment newInstance(int currentProductId, ActiveJobsListForMachine activeJobsListForMachine, int selectedPosition, String rejectMesuring) {
        ReportRejectsFragment reportRejectsFragment = new ReportRejectsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        bundle.putParcelable(CURRENT_JOB_LIST_FOR_MACHINE, activeJobsListForMachine);
        bundle.putInt(CURRENT_SELECTED_POSITION, selectedPosition);
        bundle.putString(REJECT_MESURING, rejectMesuring);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
            mActiveJobsListForMachine = getArguments().getParcelable(CURRENT_JOB_LIST_FOR_MACHINE);
            mSelectedPosition = getArguments().getInt(CURRENT_SELECTED_POSITION);
            mRejectMesuaring = getArguments().getString(REJECT_MESURING);

            if (mActiveJobsListForMachine != null) {
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshID();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        setActionBar();
        return inflater.inflate(R.layout.fragment_report_rejects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(mUnitsEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        }

        if (!PersistenceManager.getInstance().getDisplayRejectFactor()) {
            view.findViewById(R.id.cause_tv).setVisibility(View.GONE);
            view.findViewById(R.id.cause_spinner).setVisibility(View.GONE);
            view.findViewById(R.id.cause_rl).setVisibility(View.GONE);
        }

        ((TextView)view.findViewById(R.id.FRR_units_tv)).setText(PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("units"));
        String txt = getResources().getString(R.string.fill_units_or_weight);
        txt = txt.replace(getResources().getString(R.string.placeholder1), PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("units"));
        ((TextView)view.findViewById(R.id.FRR_units_or_weight_tv)).setText(txt);

        mActiveJobsProgressBar = view.findViewById(R.id.active_jobs_progressBar);
        mCancelButton = view.findViewById(R.id.button_cancel);
        mNextButton = view.findViewById(R.id.button_approve);
        mUnitsLy = view.findViewById(R.id.FRR_unit_ly);
        mWeight_ly = view.findViewById(R.id.FRR_weight_ly);
        mUnitsEditText = view.findViewById(R.id.units_edit_text);
        mWeightEditText = view.findViewById(R.id.weight_edit_text);

        initUnitAndWeight();

        mJobsSpinner = view.findViewById(R.id.report_job_spinner);
        TextView productIdTextView = view.findViewById(R.id.report_cycle_id_text_view);
        setWeightTitleView(view);

//        getActiveJobs();

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getRejectCauses() == null || mReportFieldsForMachine.getRejectReasons() == null || mReportFieldsForMachine.getRejectCauses().size() == 0 || mReportFieldsForMachine.getRejectReasons().size() == 0) {
            StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            mNextButton.setEnabled(false);
            mNextButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bg_disabled));
        } else {
            mNextButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttons_selector));
            mNextButton.setEnabled(true);
        }

        if (mReportFieldsForMachine != null) {
            Spinner rejectReasonSpinner = view.findViewById(R.id.reject_reason_spinner);

            final RejectReasonSpinnerAdapter reasonSpinnerArrayAdapter = new RejectReasonSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectReasons());
            reasonSpinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
            rejectReasonSpinner.setAdapter(reasonSpinnerArrayAdapter);
            rejectReasonSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    mSelectedReasonId = mReportFieldsForMachine.getRejectReasons().get(position).getId();
                    reasonSpinnerArrayAdapter.setTitle(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (PersistenceManager.getInstance().getDisplayRejectFactor()) {
                Spinner causeSpinner = view.findViewById(R.id.cause_spinner);
                final RejectCauseSpinnerAdapter causeSpinnerArrayAdapter = new RejectCauseSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectCauses());
                causeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                causeSpinner.setAdapter(causeSpinnerArrayAdapter);
                causeSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

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
            } else {
                mSelectedCauseId = 0;
            }
        }

        productIdTextView.setText(String.valueOf(mCurrentProductId));

        mUnitsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    mUnitsData = Double.parseDouble(s.toString());
                } catch (NumberFormatException e) {
                    mUnitsData = null;
                }
                refreshSendButtonState();
            }
        });

        mWeightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    mWeightData = Double.parseDouble(s.toString());
                } catch (NumberFormatException e) {
                    mWeightData = null;
                }
                refreshSendButtonState();
            }
        });

        initJobsSpinner();
        disableSpinnerProgressBar();

    }

    private void setWeightTitleView(View view) {
        if (mRejectMesuaring.equals("gr")){
            ((TextView)view.findViewById(R.id.FRR_weight_tv)).setText(String.format("%s (%s)", getResources().getString(R.string.weight), getResources().getString(R.string.gr)));
        }else {
            ((TextView)view.findViewById(R.id.FRR_weight_tv)).setText(String.format("%s (%s)", getResources().getString(R.string.weight), getResources().getString(R.string.kg)));
        }
    }

    private void refreshSendButtonState() {

        if (getActivity() != null) {
            if (canSendReport()) {
                mNextButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.buttons_selector));
                mNextButton.setEnabled(true);
            } else {
                mNextButton.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bg_disabled));
                mNextButton.setEnabled(false);
            }
        }
    }

    private boolean canSendReport() {

        //return ((mUnitsData != null || mWeightData != null) && mNextButton.isEnabled());
        return (mUnitsData != null || mWeightData != null);
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

                        getActivity().onBackPressed();
                    }
                });
                actionBar.setCustomView(view);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel: {

                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }

                break;
            }
            case R.id.button_approve: {

                OppAppLogger.d(LOG_TAG, "reason: " + mSelectedReasonId + " cause: " + mSelectedCauseId + " units: " + mUnitsEditText.getText().toString() + " weight: " + mWeightEditText.getText().toString() + " jobId " + mJobId);
                if (canSendReport()) {
                    mNextButton.setEnabled(false);
                    sendReport();
                }

//                mGoToScreenListener.goToFragment(ReportRejectSelectParametersFragment.getInstance(mSelectedReasonId, mSelectedCauseId, mSelectedReasonName, mJobId, mCurrentProductName, mCurrentProductId), true);

                break;
            }
        }
    }

    private void sendReport() {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        mReportCore.sendReportReject(mSelectedReasonId, mSelectedCauseId, mUnitsData, mWeightData, mJobId);
//        SendBroadcast.refreshPolling(getContext());
    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {

        @Override
        public void sendReportSuccess(StandardResponse errorResponse) {
            StandardResponse response = objectToNewError(errorResponse);
            dismissProgressDialog();
            OppAppLogger.i(LOG_TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();

            if (response.getFunctionSucceed()) {
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.REJECT_REPORT, true, "Reject Reason id = " + mSelectedReasonId + ", Reject Ca1use id = " + mSelectedCauseId);
                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
            } else {
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.REJECT_REPORT, false, "Reject Reason id = " + mSelectedReasonId + ", Reject Ca1use id = " + mSelectedCauseId);

                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
            }
            if (getFragmentManager() != null) {

                getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            SendBroadcast.refreshPolling(getContext());

        }

        @Override
        public void sendReportFailure(StandardResponse reason) {
            new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.REJECT_REPORT, true, "Report Reject failed - " + reason.getError().getErrorDesc());
            dismissProgressDialog();
            OppAppLogger.w(LOG_TAG, "sendReportFailure()");
            if (reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Credentials_mismatch && getActivity() != null) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendReport();
                    }

                    @Override
                    public void onSilentLoginFailed(StandardResponse reason) {
                        OppAppLogger.w(LOG_TAG, "Failed silent login");
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
                        ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
                        dismissProgressDialog();
                    }
                });
            } else {

                StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, reason.getError().getErrorDesc());
                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, errorObject.getError().getErrorDesc(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);
                if (getFragmentManager() != null) {

                    getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                //ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            }
            SendBroadcast.refreshPolling(getContext());
        }
    };

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

    private void disableSpinnerProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }

    private void initJobsSpinner() {
        if (getActivity() != null) {
            if (mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null) {
                mJobsSpinner.setVisibility(View.VISIBLE);
                final ActiveJobsSpinnerAdapter activeJobsSpinnerAdapter = new ActiveJobsSpinnerAdapter(getActivity(), R.layout.active_jobs_spinner_item, mActiveJobsListForMachine.getActiveJobs());
                activeJobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mJobsSpinner.setAdapter(activeJobsSpinnerAdapter);
                mJobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
                mJobsSpinner.setSelection(mSelectedPosition);
                mJobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        activeJobsSpinnerAdapter.setTitle(position);
                        mJobId = mActiveJobsListForMachine.getActiveJobs().get(position).getJoshID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }

    private void initUnitAndWeight() {
        switch (PersistenceManager.getInstance().getReportRejectDefaultUnits()) {
            case 1:
                mUnitsLy.setVisibility(View.VISIBLE);
                mUnitsEditText.requestFocus();
                mWeight_ly.setVisibility(View.GONE);
                break;
            case 2:
                mWeight_ly.setVisibility(View.VISIBLE);
                mWeightEditText.requestFocus();
                mUnitsLy.setVisibility(View.GONE);
                break;
            case 3:
                mUnitsLy.setVisibility(View.VISIBLE);
                mWeight_ly.setVisibility(View.VISIBLE);
                mUnitsEditText.requestFocus();
                break;
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

    @Override
    public int getCroutonRoot() {
        return R.id.parent_layouts;
    }
}
