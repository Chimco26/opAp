package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponse;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.zemingo.logrecorder.ZLogger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ReportCycleUnitsFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider {

    public static final String LOG_TAG = ReportCycleUnitsFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private static final String CURRENT_JOB_LIST_FOR_MACHINE = "CURRENT_JOB_LIST_FOR_MACHINE";
    private static final String CURRENT_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    private int mCurrentProductId;

    private ImageView mPlusButton;
    private ImageView mMinusButton;
    private TextView mUnitsCounterTextView;
    private Button mButtonReport;
    private TextView mButtonCancel;
    private OnCroutonRequestListener mOnCroutonRequestListener;

    private double mUnitsCounter = 1;
    private ReportCore mReportCore;
    private Integer mJobId = null;
    private int mMaxUnits = 0;

    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private Spinner mJobsSpinner;
    private ProgressBar mActiveJobsProgressBar;
    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private int mSelectedPosition;

    public static ReportCycleUnitsFragment newInstance(int currentProductId, ActiveJobsListForMachine activeJobsListForMachine, int selectedPosition) {
        ReportCycleUnitsFragment reportCycleUnitsFragment = new ReportCycleUnitsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        bundle.putParcelable(CURRENT_JOB_LIST_FOR_MACHINE, activeJobsListForMachine);
        bundle.putInt(CURRENT_SELECTED_POSITION, selectedPosition);
        reportCycleUnitsFragment.setArguments(bundle);
        return reportCycleUnitsFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();

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
//        getActiveJobs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_cycle_unit, container, false);

        setActionBar();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //        inputMethodManager.showSoftInput(mUnitsCounterTextView, InputMethodManager.SHOW_IMPLICIT);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
//        getActiveJobs();

        if (getArguments() != null) {
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
            mActiveJobsListForMachine = getArguments().getParcelable(CURRENT_JOB_LIST_FOR_MACHINE);
            mSelectedPosition = getArguments().getInt(CURRENT_SELECTED_POSITION);
            mJobId = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshID();
        }


        TextView mProductTitleTextView = (TextView) view.findViewById(R.id.report_cycle_u_product_name_text_view);
        TextView mProductIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);

        mProductTitleTextView.setText(mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshName());
        mProductIdTextView.setText(String.valueOf(mCurrentProductId));

        mUnitsCounterTextView = (TextView) view.findViewById(R.id.units_text_view);
        mUnitsCounterTextView.setFocusableInTouchMode(true);
        mUnitsCounterTextView.requestFocus();
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
                if (s.length() > 0) {
                    Character lastCharacter = mUnitsCounterTextView.getText().toString().charAt(mUnitsCounterTextView.getText().toString().length() - 1);
                    if (!lastCharacter.toString().equals(".")) {
                        if (Double.valueOf(s.toString()) > 0 && Double.valueOf(s.toString()) <= mMaxUnits) {
                            mButtonReport.setEnabled(true);
                            //                            mButtonReport.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
//                            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
//                            mUnitsCounter = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
                            Number num = convertNumericStringToNumberObject(mUnitsCounterTextView.getText().toString());
                            mUnitsCounter = convertNumericStringToNumberObject(mUnitsCounterTextView.getText().toString()).doubleValue();
                        }
                        //                        else if(Double.valueOf(s.toString()) < 0)
                        //                        {
                        //                            mUnitsCounterTextView.setText("0");
                        //                            mUnitsCounterTextView.setSelection(mUnitsCounterTextView.length());
                        //                        }
//                        if(Double.valueOf(s.toString()) > mMaxUnits)
//                        {
//                            mUnitsCounterTextView.setText(String.valueOf(mMaxUnits));
//                            mUnitsCounterTextView.setSelection(mUnitsCounterTextView.length());
//                        }
                    } else {
                        mButtonReport.setEnabled(false);
                        //                        mButtonReport.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_disabled));
                    }
                } else {
                    mButtonReport.setEnabled(false);
                    //                    mButtonReport.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_disabled));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);

        initJobsSpinner();
        disableSpinnerProgressBar();
    }

    private Number convertNumericStringToNumberObject(String strValue) {
        //double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
        DecimalFormat df = new DecimalFormat("######.0");
        NumberFormat format = NumberFormat.getInstance(new Locale("EN", "en"));

        try {
            Number number = format.parse(strValue);
            //number = df.parse(strValue);
            return number;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1.0;
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
        mPlusButton.setOnClickListener(null);
        mMinusButton.setOnClickListener(null);
        mButtonReport.setOnClickListener(null);
        mButtonCancel.setOnClickListener(null);


        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.report_cycle_unit_action_bar, null);

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
        if (mUnitsCounter < mMaxUnits) {
//            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
//            value = value + 1;
//            value = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
//            mUnitsCounter = value;
            mUnitsCounter = convertNumericStringToNumberObject(mUnitsCounterTextView.getText().toString()).doubleValue() + 1.0;
            mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));

            mPlusButton.setEnabled(true);
        } else {
            mUnitsCounterTextView.setText(new StringBuilder(String.valueOf(mMaxUnits)).append(".0"));
            mPlusButton.setEnabled(false);
        }
        mMinusButton.setEnabled(true);
        mButtonReport.setEnabled(true);
    }

    private void decrease() {
        mUnitsCounter--;
        if (mUnitsCounter <= 0) {
            mUnitsCounterTextView.setText("0.0");
            mButtonReport.setEnabled(false);
            mMinusButton.setEnabled(false);
            mPlusButton.setEnabled(true);
        } else {
//            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
//            value = value - 1;
//            value = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
//            mUnitsCounter = value;
            mUnitsCounter = convertNumericStringToNumberObject(mUnitsCounterTextView.getText().toString()).doubleValue() - 1.0;
            mButtonReport.setEnabled(true);
            mMinusButton.setEnabled(true);
            mPlusButton.setEnabled(true);
            mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
        }
    }

    private void sendReport() {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.inject(NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        ZLogger.i(LOG_TAG, "sendReport units value is: " + String.valueOf(mUnitsCounter) + " JobId: " + mJobId);

        mReportCore.sendCycleUnitsReport(mUnitsCounter, mJobId);

        if (getFragmentManager() != null) {

            getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

//        SendBroadcast.refreshPolling(getContext());
    }

    private ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess(Object o) {

            ErrorResponseNewVersion response = objectToNewError(o);
            SendBroadcast.refreshPolling(getContext());
            ZLogger.i(LOG_TAG, "sendReportSuccess() units value is: " + mUnitsCounter);
            mReportCore.unregisterListener();

//            if (getFragmentManager() != null) {
//
//                getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//            }
            dismissProgressDialog();
            if (response.isFunctionSucceed()){
                // TODO: 17/07/2018 add crouton for success
                // ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getmError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);

                //mDashboardCroutonListener.onShowCrouton(response.getmError().getErrorDesc());
            }else {
                mDashboardCroutonListener.onShowCrouton(response.getmError().getErrorDesc());
            }

//            if (o != null){
//
//                mDashboardCroutonListener.onShowCrouton(((ErrorResponse) o).getErrorDesc());
//            }

        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason) {
            ZLogger.i(LOG_TAG, "sendReportFailure() reason: " + reason.getDetailedDescription());
            mDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + reason.getDetailedDescription());

            dismissProgressDialog();
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
                mJobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
                mJobsSpinner.setSelection(mSelectedPosition);
                mJobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        activeJobsSpinnerAdapter.setTitle(position);
                        mJobId = mActiveJobsListForMachine.getActiveJobs().get(position).getJoshID();
                        mMaxUnits = mActiveJobsListForMachine.getActiveJobs().get(position).getCavitiesStandard();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }

    @Override
    public int getCroutonRoot() {
        return R.id.top_layout;
    }

}
