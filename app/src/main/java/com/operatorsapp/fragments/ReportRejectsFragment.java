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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.RejectCauseSpinnerAdapter;
import com.operatorsapp.adapters.RejectReasonSpinnerAdapter;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;


/**
 * Created by Sergey on 31/07/2016.
 */
public class ReportRejectsFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = ReportRejectsFragment.class.getSimpleName();
    private static final String CURRENT_MACHINE_STATUS = "current_machine_status";
    public static final String REJECT_REASON = "reject_reason";
    public static final String REJECT_CAUSE = "reject_cause";
    private MachineStatus mMachineStatus;
    private Spinner mRejectReasonSpinner;
    private Spinner mCauseSpinner;
    private TextView mCancelButton;
    private Button mNextButton;
    boolean mIsFirstReasonSpinnerSelection = true;
    boolean mIsReasonSelected;
    private GoToScreenListener mGoToScreenListener;


    private TextView mProductNameTextView;
    private TextView mProductIdTextView;
    private TextView mJobIdTextView;

    private String mSelectedReason;
    private String mSelectedCause;
    private ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener;
    private ReportFieldsForMachine mReportFieldsForMachine;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGoToScreenListener = (GoToScreenListener) getActivity();
        mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = mReportFieldsFragmentCallbackListener.getReportForMachine();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_report_rejects, container, false);

        Bundle bundle = this.getArguments();
        Gson gson = new Gson();
        mMachineStatus = gson.fromJson(bundle.getString(CURRENT_MACHINE_STATUS), MachineStatus.class);
        setActionBar();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProductNameTextView = (TextView) view.findViewById(R.id.report_rejects_product_name_text_view);
        mProductIdTextView = (TextView) view.findViewById(R.id.report_rejects_product_id_text_view);
        mJobIdTextView = (TextView) view.findViewById(R.id.report_rejects_job_id__text_view);

        mProductNameTextView.setText(new StringBuilder(mMachineStatus.getAllMachinesData().get(0).getCurrentProductName() + ","));
        mProductIdTextView.setText(String.valueOf(mMachineStatus.getAllMachinesData().get(0).getCurrentProductID()));
        mJobIdTextView.setText((String.valueOf(mMachineStatus.getAllMachinesData().get(0).getCurrentJobID())));

        mRejectReasonSpinner = (Spinner) view.findViewById(R.id.reject_reason_spinner);

        final RejectReasonSpinnerAdapter reasonSpinnerArrayAdapter = new RejectReasonSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectReasons());
        reasonSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRejectReasonSpinner.setAdapter(reasonSpinnerArrayAdapter);
        mRejectReasonSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

        mRejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mIsFirstReasonSpinnerSelection) {
                    mIsFirstReasonSpinnerSelection = false;
                    mIsReasonSelected = false;

                }
                else {
                    mIsReasonSelected = true;
                    mSelectedReason = mReportFieldsForMachine.getRejectReasons().get(position).getName();
                    reasonSpinnerArrayAdapter.setTitle(position);
                    mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mCauseSpinner = (Spinner) view.findViewById(R.id.cause_spinner);
        final RejectCauseSpinnerAdapter causeSpinnerArrayAdapter = new RejectCauseSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectCauses());
        causeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCauseSpinner.setAdapter(causeSpinnerArrayAdapter);
        mCauseSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

        mCauseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedCause = mReportFieldsForMachine.getRejectCauses().get(position).getName();
                causeSpinnerArrayAdapter.setTitle(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);
        mNextButton = (Button) view.findViewById(R.id.button_report);

    }

    @Override
    public void onPause() {
        super.onPause();
        mCancelButton.setOnClickListener(null);
        mNextButton.setOnClickListener(null);
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
            case R.id.button_cancel: {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.button_report: {
                if (!mIsReasonSelected) {
                    Log.i(LOG_TAG, "reason not Selected");
                }
                else {
                    Log.i(LOG_TAG, "reason Selected");
                    ReportRejectSelectParametersFragment reportRejectSelectParametersFragment = new ReportRejectSelectParametersFragment();

                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String jobString = gson.toJson(mMachineStatus, MachineStatus.class);
                    bundle.putString(CURRENT_MACHINE_STATUS, jobString);
                    bundle.putString(REJECT_REASON, mSelectedReason);
                    bundle.putString(REJECT_CAUSE, mSelectedCause);

                    reportRejectSelectParametersFragment.setArguments(bundle);

                    mGoToScreenListener.goToFragment(reportRejectSelectParametersFragment, true);
                }
                break;
            }
        }
    }
}
