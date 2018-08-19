package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.getmachinesnetworkbridge.server.ErrorObject;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponse;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.adapters.RejectInventorySpinnerAdapter;
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

import static com.operatorsapp.application.OperatorApplication.isEnglishLang;

public class ReportInventoryFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider, KeyboardUtils.KeyboardListener {

    public static final String LOG_TAG = ReportInventoryFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private static final String CURRENT_JOB_LIST_FOR_MACHINE = "CURRENT_JOB_LIST_FOR_MACHINE";
    private static final String CURRENT_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    private int mCurrentProductId;
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
    private Spinner mJobsSpinner;
    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private int mSelectedPosition;


    public static ReportInventoryFragment newInstance(int currentProductId, ActiveJobsListForMachine activeJobsListForMachine, int selectedPosition) {
        ReportInventoryFragment reportInventoryFragment = new ReportInventoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        bundle.putParcelable(CURRENT_JOB_LIST_FOR_MACHINE, activeJobsListForMachine);
        bundle.putInt(CURRENT_SELECTED_POSITION, selectedPosition);
        reportInventoryFragment.setArguments(bundle);
        return reportInventoryFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
        ReportFieldsFragmentCallbackListener mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
        if (mReportFieldsFragmentCallbackListener != null) {
            mReportFieldsForMachine = mReportFieldsFragmentCallbackListener.getReportForMachine();
        }
        if (context instanceof ShowDashboardCroutonListener) {
            mDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        }
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
            mJobId = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshID();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //        setActionBar();
        return inflater.inflate(R.layout.fragment_report_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActiveJobsProgressBar = view.findViewById(R.id.active_jobs_progressBar);

//        getActiveJobs();

        mButtonReport = view.findViewById(R.id.button_report);

        mPlusButton = view.findViewById(R.id.button_plus);

        mMinusButton = view.findViewById(R.id.button_minus);

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
        TextView productTitleTextView = view.findViewById(R.id.report_cycle_u_product_name_text_view);
        TextView productIdTextView = view.findViewById(R.id.report_cycle_id_text_view);

        productTitleTextView.setText(mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshName());
        productIdTextView.setText(String.valueOf(mCurrentProductId));

        mUnitsCounterTextView = view.findViewById(R.id.units_text_view);

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

        mButtonCancel = view.findViewById(R.id.button_cancel);

        mJobsSpinner = view.findViewById(R.id.report_job_spinner);

        Spinner rejectReasonSpinner = view.findViewById(R.id.package_type_spinner);
        if (mReportFieldsForMachine != null && getActivity() != null) {
            final RejectInventorySpinnerAdapter reasonSpinnerArrayAdapter = new RejectInventorySpinnerAdapter(getActivity(), R.layout.base_spinner_item, mReportFieldsForMachine.getPackageTypes());
            reasonSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rejectReasonSpinner.setAdapter(reasonSpinnerArrayAdapter);
            rejectReasonSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            rejectReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    reasonSpinnerArrayAdapter.setTitle(position);
                    mSelectedPackageTypeName = isEnglishLang() ? mReportFieldsForMachine.getPackageTypes().get(position).getEName() : mReportFieldsForMachine.getPackageTypes().get(position).getLName();
                    mSelectedPackageTypeId = mReportFieldsForMachine.getPackageTypes().get(position).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5,"keyboardIsShown");

        if (getActivity() != null) {
            KeyboardUtils.keyboardIsShownC(getActivity(), this);
        }

        initJobsSpinner();

        disableSpinnerProgressBar();
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
                View view = inflater.inflate(R.layout.action_bar_report_inventory, null);

                LinearLayout buttonClose = view.findViewById(R.id.close_image);
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

                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }

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
        ReportCore mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        ZLogger.i(LOG_TAG, "sendReport units value is: " + String.valueOf(mUnitsCounter) + " type value: " + mSelectedPackageTypeId + " type name: " + mSelectedPackageTypeName + " JobId: " + mJobId);

        mReportCore.sendInventoryReport(mSelectedPackageTypeId, mUnitsCounter, mJobId);

//        SendBroadcast.refreshPolling(getContext());
    }

    private ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override//TODO crouton error
        public void sendReportSuccess(Object o) {


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

            if (getFragmentManager() != null){

                getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

//
//            SendBroadcast.refreshPolling(getContext());
//            dismissProgressDialog();
//            ZLogger.i(LOG_TAG, "sendReportSuccess()");
//            mReportCore.unregisterListener();
//
//            if (o != null){
//
//                mDashboardCroutonListener.onShowCrouton(((ErrorResponse) o).getErrorDesc());
//            }
//            if (getFragmentManager() != null){
//
//                getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            }

        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason) {
            dismissProgressDialog();
            ZLogger.i(LOG_TAG, "sendReportFailure() reason: " + reason.getDetailedDescription());
            mDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + reason.getDetailedDescription());

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


    private void disableSpinnerProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
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
                mJobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
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
