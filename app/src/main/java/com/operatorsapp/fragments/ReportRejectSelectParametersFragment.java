package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportRejectCore;
import com.operators.reportrejectnetworkbridge.ReportRejectNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;

/**
 * Created by Sergey on 31/07/2016.
 */
public class ReportRejectSelectParametersFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = ReportRejectSelectParametersFragment.class.getSimpleName();
    private static final String REJECT_REASON = "reject_reason";
    private static final String REJECT_CAUSE = "reject_cause";
    private static final String REJECT_REASON_TITLE = "reject_reason_title";
    private static final String SELECTED_JOB_ID = "selected_job_id";
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";

    private int mSelectedReasonId;
    private int mSelectedCauseId;
    private String mSelectedReasonName;
    private Integer mJobId = null;
    private EditText mUnitsEditText;
    private EditText mWeightEditText;
    private Button mReportButton;
    private TextView mCancelButton;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportRejectCore mReportRejectCore;
    private String mCurrentProductName;
    private int mCurrentProductId;


    public static ReportRejectSelectParametersFragment newInstance(int selectedReasonId, int selectedCauseId, String selectedReasonName, Integer jobId, String currentProductName, int currentProductId) {
        ReportRejectSelectParametersFragment reportRejectSelectParametersFragment = new ReportRejectSelectParametersFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(REJECT_REASON, selectedReasonId);
        bundle.putInt(REJECT_CAUSE, selectedCauseId);
        bundle.putString(REJECT_REASON_TITLE, selectedReasonName);
        bundle.putInt(SELECTED_JOB_ID, jobId);
        bundle.putString(CURRENT_PRODUCT_NAME, currentProductName);
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        reportRejectSelectParametersFragment.setArguments(bundle);
        return reportRejectSelectParametersFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedReasonId = getArguments().getInt(REJECT_REASON);
            mSelectedCauseId = getArguments().getInt(REJECT_CAUSE);
            mSelectedReasonName = getArguments().getString(REJECT_REASON_TITLE);
            mCurrentProductName = getArguments().getString(CURRENT_PRODUCT_NAME);
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
            mJobId = getArguments().getInt(SELECTED_JOB_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_rejects_selected_parameters, container, false);

        setActionBar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mUnitsEditText, InputMethodManager.SHOW_IMPLICIT);

        TextView productNameTextView = (TextView) view.findViewById(R.id.report_rejects_product_name_text_view);
        TextView productIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);
        TextView jobIdTextView = (TextView) view.findViewById(R.id.report_rejects_selected_job_id_text_view);

        mReportButton = (Button) view.findViewById(R.id.button_report);
        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);

        mUnitsEditText = (EditText) view.findViewById(R.id.units_edit_text);
        mWeightEditText = (EditText) view.findViewById(R.id.weight_edit_text);

        mUnitsEditText.setFocusableInTouchMode(true);
        mUnitsEditText.requestFocus();

        if (mCurrentProductName != null) {
            productNameTextView.setText(mCurrentProductName + ",");
        }
        else {
            productNameTextView.setText(getActivity().getString(R.string.dashes));
        }

        productIdTextView.setText(String.valueOf(mCurrentProductId));

        if (mJobId != null) {
            jobIdTextView.setText(String.valueOf(mJobId));
        }
        else {
            jobIdTextView.setText(getActivity().getString(R.string.dashes));

        }


        mUnitsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mReportButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
                    mReportButton.setClickable(true);
                }
                else {
                    mReportButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_disabled));
                    mReportButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mCancelButton.setOnClickListener(null);
        mReportButton.setOnClickListener(null);
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCancelButton.setOnClickListener(this);
        mReportButton.setOnClickListener(this);

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
            View view = inflater.inflate(R.layout.report_reject_parameters_action_bar, null);

            ImageView buttonClose = (ImageView) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO check
                    getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });

            TextView reasonTextView = (TextView) view.findViewById(R.id.reason_text_view);
            reasonTextView.setText(mSelectedReasonName);
            actionBar.setCustomView(view);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_report: {
                Log.d(LOG_TAG, "reason: " + mSelectedReasonId + " cause: " + mSelectedCauseId + " units: " + mUnitsEditText.getText().toString() + " weight: " + mWeightEditText.getText().toString() + " jobId " + mJobId);
                sendReport();
                break;
            }
            case R.id.button_cancel: {
                //TODO check
                getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            }
        }
    }

    private void sendReport() {

        ReportRejectNetworkBridge reportRejectNetworkBridge = new ReportRejectNetworkBridge();
        reportRejectNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());
        mReportRejectCore = new ReportRejectCore(reportRejectNetworkBridge, PersistenceManager.getInstance());
        mReportRejectCore.registerListener(mReportCallbackListener);
        Double weight = null;
        if (!mWeightEditText.getText().toString().equals("")) {
            weight = Double.parseDouble(mWeightEditText.getText().toString());
        }
        mReportRejectCore.sendReportReject(mSelectedReasonId, mSelectedCauseId, Double.parseDouble(mUnitsEditText.getText().toString()), weight, mJobId);
    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess() {
            Log.i(LOG_TAG, "sendReportSuccess()");
            mReportRejectCore.unregisterListener();
            //TODO check
            getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason) {
            Log.w(LOG_TAG, "sendReportFailure()");
            if (reason.getError() == ErrorObjectInterface.ErrorCode.Credentials_mismatch) {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendReport();
                    }

                    @Override
                    public void onSilentLoginFailed(ErrorObjectInterface reason) {
                        Log.w(LOG_TAG, "Failed silent login");
                        ShowCrouton.reportRejectCrouton(mOnCroutonRequestListener);
                    }
                });
            }
            else {

                ShowCrouton.reportRejectCrouton(mOnCroutonRequestListener);
            }
        }
    };
}
