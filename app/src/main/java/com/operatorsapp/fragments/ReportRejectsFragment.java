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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.activejobslistformachinecore.ActiveJobsListForMachineCore;
import com.operators.activejobslistformachinecore.interfaces.ActiveJobsListForMachineUICallbackListener;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachinenetworkbridge.ActiveJobsListForMachineNetworkBridge;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.adapters.RejectCauseSpinnerAdapter;
import com.operatorsapp.adapters.RejectReasonSpinnerAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;


/**
 * Created by Sergey on 31/07/2016.
 */
public class ReportRejectsFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = ReportRejectsFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private TextView mCancelButton;
    private Button mNextButton;
    private boolean mIsFirstReasonSpinnerSelection = true;
    private boolean mIsReasonSelected;
    private GoToScreenListener mGoToScreenListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private int mSelectedReasonId;
    private int mSelectedCauseId;
    private String mSelectedReasonName;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private String mCurrentProductName;
    private int mCurrentProductId;
    private Integer mJobId = null;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private Spinner mJobsSpinner;
    private ProgressBar mActiveJobsProgressBar;

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
        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
        getActiveJobs();
        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);
        mNextButton = (Button) view.findViewById(R.id.button_report);

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getRejectCauses() == null || mReportFieldsForMachine.getRejectReasons() == null || mReportFieldsForMachine.getRejectCauses().size() == 0 || mReportFieldsForMachine.getRejectReasons().size() == 0) {
            ShowCrouton.noDataCrouton(mOnCroutonRequestListener, R.id.report_reject_screen);
            mNextButton.setEnabled(false);
        }else{
//            mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
            mNextButton.setEnabled(true);
        }

        TextView productNameTextView = (TextView) view.findViewById(R.id.report_cycle_u_product_name_text_view);
        TextView productIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);

        productNameTextView.setText(new StringBuilder(mCurrentProductName + ","));
        productIdTextView.setText(String.valueOf(mCurrentProductId));

        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);


        if (mReportFieldsForMachine!=null) {
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
                        mSelectedReasonName = mReportFieldsForMachine.getRejectReasons().get(position).getName();
                        reasonSpinnerArrayAdapter.setTitle(position);
//                        mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
//                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            Spinner causeSpinner = (Spinner) view.findViewById(R.id.cause_spinner);
            final RejectCauseSpinnerAdapter causeSpinnerArrayAdapter = new RejectCauseSpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getRejectCauses());
            causeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            causeSpinner.setAdapter(causeSpinnerArrayAdapter);
            causeSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            causeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mSelectedCauseId = mReportFieldsForMachine.getRejectCauses().get(position).getId();
                    causeSpinnerArrayAdapter.setTitle(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);
        mNextButton = (Button) view.findViewById(R.id.button_report);

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

                    mGoToScreenListener.goToFragment(ReportRejectSelectParametersFragment.newInstance(mSelectedReasonId, mSelectedCauseId, mSelectedReasonName, mJobId, mCurrentProductName, mCurrentProductId), true);

                break;
            }
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
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(0).getJobID();
                initJobsSpinner();
                Log.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            }
            else {
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
