package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponse;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.adapters.RejectInventorySpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.KeyboardUtils;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.zemingo.logrecorder.ZLogger;

public class ReportInventoryFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider, KeyboardUtils.KeyboardListener {

    public static final String LOG_TAG = ReportInventoryFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private String mCurrentProductName;
    private int mCurrentProductId;
    private ReportCore mReportCore;
    private ImageView mPlusButton;
    private ImageView mMinusButton;
    private EditText mUnitsCounterTextView;
    private Button mButtonReport;
    private TextView mButtonCancel;
    private ProgressBar mActiveJobsProgressBar;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private int mUnitsCounter = 1;
    private Integer mJobId = null;
    private int mSelectedPackageTypeId;
    private String mSelectedPackageTypeName;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private ActiveJobsListForMachineCore mActiveJobsListForMachineCore;
    private Spinner mJobsSpinner;
    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private View mTopView;


    public static ReportInventoryFragment newInstance(String currentProductName, int currentProductId) {
        ReportInventoryFragment reportInventoryFragment = new ReportInventoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_PRODUCT_NAME, currentProductName);
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        reportInventoryFragment.setArguments(bundle);
        return reportInventoryFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
        ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        mReportFieldsForMachine = mReportFieldsFragmentCallbackListener.getReportForMachine();
        if (context instanceof ShowDashboardCroutonListener) {
            mDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        }
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
        final View view = inflater.inflate(R.layout.fragment_report_inventory, container, false);
        setActionBar();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);

        getActiveJobs();

        mButtonReport = (Button) view.findViewById(R.id.button_report);

        mPlusButton = (ImageView) view.findViewById(R.id.button_plus);

        mMinusButton = (ImageView) view.findViewById(R.id.button_minus);

        mTopView = view.findViewById(R.id.FRI_top_view);

        if (mReportFieldsForMachine == null || mReportFieldsForMachine.getPackageTypes() == null || mReportFieldsForMachine.getPackageTypes().size() == 0) {

            ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Missing_reports, "missing reports");
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            mButtonReport.setEnabled(false);
            mMinusButton.setEnabled(false);
            mPlusButton.setEnabled(false);
        }
        else {
            mButtonReport.setEnabled(true);
            mMinusButton.setEnabled(true);
            mPlusButton.setEnabled(true);
        }
        TextView productTitleTextView = (TextView) view.findViewById(R.id.report_cycle_u_product_name_text_view);
        TextView productIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);

        productTitleTextView.setText(mCurrentProductName);
        productIdTextView.setText(String.valueOf(mCurrentProductId));

        mUnitsCounterTextView = (EditText) view.findViewById(R.id.units_text_view);

        mUnitsCounterTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>0){

                    mUnitsCounter = Integer.valueOf(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));

        mButtonCancel = (TextView) view.findViewById(R.id.button_cancel);

        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);

        Spinner rejectReasonSpinner = (Spinner) view.findViewById(R.id.package_type_spinner);
        if (mReportFieldsForMachine != null) {
            final RejectInventorySpinnerAdapter reasonSpinnerArrayAdapter = new RejectInventorySpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getPackageTypes());
            reasonSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rejectReasonSpinner.setAdapter(reasonSpinnerArrayAdapter);
            rejectReasonSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    reasonSpinnerArrayAdapter.setTitle(position);
                    String nameByLang = OperatorApplication.isEnglishLang() ? mReportFieldsForMachine.getPackageTypes().get(position).getEName() : mReportFieldsForMachine.getPackageTypes().get(position).getLName();
                    mSelectedPackageTypeName = nameByLang;
                    mSelectedPackageTypeId = mReportFieldsForMachine.getPackageTypes().get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5,"keyboardIsShown");

        KeyboardUtils.keyboardIsShownC(getActivity(),this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPlusButton.setOnClickListener(this);
        mMinusButton.setOnClickListener(this);
        mButtonReport.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);

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
            View view = inflater.inflate(R.layout.action_bar_report_inventory, null);

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
            case R.id.button_plus: {
                increase();
                break;
            }
            case R.id.button_minus: {
                decrease();
                break;
            }
            case R.id.button_cancel: {
//                getFragmentManager().popBackStack();

                getActivity().onBackPressed();
                break;
            }
            case R.id.button_report: {
                sendReport();
                break;
            }
        }
    }

    private void increase() {
        mUnitsCounter++;
        mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
    }

    private void decrease() {
        if (mUnitsCounter > 1) {
            mUnitsCounter--;
        }
        if (mUnitsCounter == 1) {
            mUnitsCounterTextView.setText("1");
        }
        else {
            mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
        }
    }

    private void sendReport() {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.injectInventory(NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        ZLogger.i(LOG_TAG, "sendReport units value is: " + String.valueOf(mUnitsCounter) + " type value: " + mSelectedPackageTypeId + " type name: " + mSelectedPackageTypeName + " JobId: " + mJobId);

        mReportCore.sendInventoryReport(mSelectedPackageTypeId, mUnitsCounter, mJobId);

        SendBroadcast.refreshPolling(getContext());
    }

    private ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override//TODO crouton error
        public void sendReportSuccess(Object o) {
            dismissProgressDialog();
            ZLogger.i(LOG_TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();

            if (o != null){

                mDashboardCroutonListener.onShowCrouton(((ErrorResponse) o).getErrorDesc());
            }
            if (getFragmentManager() != null){

                getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason) {
            dismissProgressDialog();
            ZLogger.i(LOG_TAG, "sendReportFailure() reason: " + reason.getDetailedDescription());
            mDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + reason.getDetailedDescription());

        }
    };

    private void dismissProgressDialog()
    {
        if (getActivity() != null)
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
        mActiveJobsListForMachineCore = new ActiveJobsListForMachineCore(PersistenceManager.getInstance(), activeJobsListForMachineNetworkBridge);
        mActiveJobsListForMachineCore.registerListener(mActiveJobsListForMachineUICallbackListener);
        mActiveJobsListForMachineCore.getActiveJobsListForMachine();
    }

    private ActiveJobsListForMachineUICallbackListener mActiveJobsListForMachineUICallbackListener = new ActiveJobsListForMachineUICallbackListener() {
        @Override
        public void onActiveJobsListForMachineReceived(ActiveJobsListForMachine activeJobsListForMachine) {
            if (activeJobsListForMachine != null) {
                mActiveJobsListForMachine = activeJobsListForMachine;
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(0).getJoshID();
                initJobsSpinner();
                ZLogger.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            }
            else {
                mJobId = null;
                ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
            }
            disableProgressBar();
        }

        @Override
        public void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason) {
            mJobId = null;
            ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getDetailedDescription());
            disableProgressBar();
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener);
        }
    };

    private void disableProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mActiveJobsListForMachineCore.unregisterListener();
        mOnCroutonRequestListener.onHideConnectivityCroutonRequest();
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
        return R.id.report_inventory_crouton_root;
    }

    @Override
    public void onKeyboardShown() {

    //    mTopView.setVisibility(View.GONE);

        Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5,"onKeyboardShown");


    }

    @Override
    public void onKeyboardHidden() {

     //   mTopView.setVisibility(View.VISIBLE);

        Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5,"onKeyboardHidden");


    }
}
