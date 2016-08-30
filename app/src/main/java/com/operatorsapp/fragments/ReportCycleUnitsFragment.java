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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.activejobslistformachinecore.ActiveJobsListForMachineCore;
import com.operators.activejobslistformachinecore.interfaces.ActiveJobsListForMachineUICallbackListener;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachinenetworkbridge.ActiveJobsListForMachineNetworkBridge;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportRejectCore;
import com.operators.reportrejectnetworkbridge.ReportRejectNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;

import java.util.Locale;

public class ReportCycleUnitsFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = ReportCycleUnitsFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private String mCurrentProductName;
    private int mCurrentProductId;

    private ImageView mPlusButton;
    private ImageView mMinusButton;
    private EditText mUnitsCounterTextView;
    private Button mButtonReport;
    private TextView mButtonCancel;

    private double mUnitsCounter = 1;
    private ReportRejectCore mReportRejectCore;
    private Integer mJobId = null;

    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private Spinner mJobsSpinner;
    private ProgressBar mActiveJobsProgressBar;

    public static ReportCycleUnitsFragment newInstance(String currentProductName, int currentProductId) {
        ReportCycleUnitsFragment reportCycleUnitsFragment = new ReportCycleUnitsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_PRODUCT_NAME, currentProductName);
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        reportCycleUnitsFragment.setArguments(bundle);
        return reportCycleUnitsFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActiveJobs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_cycle_unit, container, false);
        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
        getActiveJobs();

        if (getArguments() != null) {
            mCurrentProductName = getArguments().getString(CURRENT_PRODUCT_NAME);
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
        }

        setActionBar();

        TextView mProductTitleTextView = (TextView) view.findViewById(R.id.report_cycle_u_product_name_text_view);
        TextView mProductIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);

        mProductTitleTextView.setText(mCurrentProductName);
        mProductIdTextView.setText(String.valueOf(mCurrentProductId));

        mUnitsCounterTextView = (EditText) view.findViewById(R.id.units_text_view);
        mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
        mPlusButton = (ImageView) view.findViewById(R.id.button_plus);
        mMinusButton = (ImageView) view.findViewById(R.id.button_minus);

        mButtonReport = (Button) view.findViewById(R.id.button_report);
        mButtonCancel = (TextView) view.findViewById(R.id.button_cancel);

        mUnitsCounterTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!mUnitsCounterTextView.getText().toString().equals("")) {
                    if (s.length() > 0) {
                        Character lastCharacter = mUnitsCounterTextView.getText().toString().charAt(mUnitsCounterTextView.getText().toString().length() - 1);
                        if (!lastCharacter.toString().equals(".")) {
                            if (Double.valueOf(s.toString()) > 0) {
                                mButtonReport.setEnabled(true);
                                double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
                                mUnitsCounter = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
                            }
                        } else {
                            mButtonReport.setEnabled(false);
                        }
                    }
                } else {
                    mButtonReport.setEnabled(false);
                }
            }
        });

        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlusButton.setOnClickListener(this);
        mMinusButton.setOnClickListener(this);
        mButtonReport.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
            View view = inflater.inflate(R.layout.report_cycle_unit_action_bar, null);

            ImageView buttonClose = (ImageView) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_plus: {
                increase();
                break;
            }
            case R.id.button_minus: {
                decrease();
                break;
            }
            case R.id.button_cancel: {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.button_report: {
                sendReport();
                break;
            }
        }
    }

    private void increase() {
        double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
        value = value + 1;
        value = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
        mUnitsCounter = value;
        mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
        mMinusButton.setEnabled(true);
        mButtonReport.setEnabled(true);
    }

    private void decrease() {
        if (mUnitsCounter > 0) {
            mUnitsCounter--;
        }
        if (mUnitsCounter <= 0) {
            mUnitsCounterTextView.setText("0.0");
            mButtonReport.setEnabled(false);
            mMinusButton.setEnabled(false);
        } else {
            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
            value = value - 1;
            value = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
            mUnitsCounter = value;
            mButtonReport.setEnabled(true);
            mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
        }
    }

    private void sendReport() {
        ReportRejectNetworkBridge reportRejectNetworkBridge = new ReportRejectNetworkBridge();
        reportRejectNetworkBridge.inject(NetworkManager.getInstance());
        mReportRejectCore = new ReportRejectCore(reportRejectNetworkBridge, PersistenceManager.getInstance());
        mReportRejectCore.registerListener(mReportCallbackListener);
        Log.i(LOG_TAG, "sendReport units value is: " + String.valueOf(mUnitsCounter) + " JobId: " + mJobId);

        mReportRejectCore.sendCycleUnitsReport(mUnitsCounter, mJobId);
    }

    private ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess() {
            Log.i(LOG_TAG, "sendReportSuccess() units value is: " + mUnitsCounter);
            mReportRejectCore.unregisterListener();
            //TODO check
            getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason) {
            Log.i(LOG_TAG, "sendReportFailure() reason: " + reason.getDetailedDescription());
        }
    };

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
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(0).getJobID();
                initJobsSpinner();
                Log.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            } else {
                mJobId = null;
                Log.w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
            }
            disableProgressBar();

        }

        @Override
        public void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason) {
            mJobId = null;
            Log.w(LOG_TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getDetailedDescription());
            disableProgressBar();

        }
    };

    private void disableProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }


    private void initJobsSpinner() {
        mJobsSpinner.setVisibility(View.VISIBLE);
        final ActiveJobsSpinnerAdapter activeJobsSpinnerAdapter = new ActiveJobsSpinnerAdapter(getActivity(), R.layout.active_jobs_spinner_item, mActiveJobsListForMachine.getActiveJobs());
        activeJobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mJobsSpinner.setAdapter(activeJobsSpinnerAdapter);
        mJobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
        mJobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activeJobsSpinnerAdapter.setTitle(position);
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(position).getJobID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
