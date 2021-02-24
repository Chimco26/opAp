package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.broadcast.SendBroadcast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ReportCycleUnitsFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider {

    public static final String LOG_TAG = ReportCycleUnitsFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private static final String CURRENT_JOB_LIST_FOR_MACHINE = "CURRENT_JOB_LIST_FOR_MACHINE";
    private static final String CURRENT_SELECTED_POSITION = "CURRENT_SELECTED_POSITION";
    private static final int REFRESH_DELAY_MILLIS = 3000;
    private int mCurrentProductId;

    private ImageView mPlusButton;
    private ImageView mMinusButton;
    private TextView mUnitsCounterTextView;
    private Button mButtonReport;
    private TextView mButtonCancel;

    private double mUnitsCounter = 1;
    private ReportCore mReportCore;
    private Integer mJobId = null;
    private float mMaxUnits = 0f;

    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private Spinner mJobsSpinner;
    private ProgressBar mActiveJobsProgressBar;
    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private int mSelectedPosition;
    private OnCroutonRequestListener mOnCroutonRequestListener;

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

        if (context instanceof ShowDashboardCroutonListener) {
            mDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        }

        if (context instanceof OnCroutonRequestListener) {
            mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
        }

    }

    @Override
    public void onDetach() {
        mOnCroutonRequestListener = null;
        super.onDetach();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        setActionBar();

        return inflater.inflate(R.layout.fragment_report_cycle_unit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //        inputMethodManager.showSoftInput(mUnitsCounterTextView, InputMethodManager.SHOW_IMPLICIT);

        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        mActiveJobsProgressBar = view.findViewById(R.id.active_jobs_progressBar);
//        getActiveJobs();

        if (getArguments() != null) {
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
            mActiveJobsListForMachine = getArguments().getParcelable(CURRENT_JOB_LIST_FOR_MACHINE);
            mSelectedPosition = getArguments().getInt(CURRENT_SELECTED_POSITION);

            if (mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null
                    && mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition) != null) {
                mJobId = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshID();
                mUnitsCounter = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getCavitiesActual();
            }

        }


        TextView mProductIdTextView = view.findViewById(R.id.report_cycle_id_text_view);

        mProductIdTextView.setText(String.valueOf(mCurrentProductId));

        mUnitsCounterTextView = view.findViewById(R.id.units_text_view);
        mUnitsCounterTextView.setFocusableInTouchMode(true);
        mUnitsCounterTextView.requestFocus();
        mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
        mPlusButton = view.findViewById(R.id.button_plus);
        mMinusButton = view.findViewById(R.id.button_minus);

        ((TextView)view.findViewById(R.id.FRCU_units_tv)).setText(PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("Units"));
        String txt = getResources().getString(R.string.report_cycle_nunits);
        txt = txt.replace(getResources().getString(R.string.placeholder1), PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("Units"));
        ((TextView)view.findViewById(R.id.FRCU_units_subtitle_tv)).setText(txt);

        mButtonReport = view.findViewById(R.id.button_report);
        mButtonCancel = view.findViewById(R.id.button_cancel);

        mUnitsCounterTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    Character lastCharacter = mUnitsCounterTextView.getText().toString().charAt(mUnitsCounterTextView.getText().toString().length() - 1);
                    if (!lastCharacter.toString().equals(".") && !s.toString().replaceFirst("\\.", "").contains(".")) {
                        if (Double.valueOf(s.toString()) > 0 && Double.valueOf(s.toString()) <= mMaxUnits) {
                            mButtonReport.setEnabled(true);
                            //                            mButtonReport.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
//                            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
//                            mUnitsCounter = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
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

        mJobsSpinner = view.findViewById(R.id.report_job_spinner);

        initJobsSpinner();
        disableSpinnerProgressBar();
    }

    private Number convertNumericStringToNumberObject(String strValue) {
        //double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
        NumberFormat format = NumberFormat.getInstance(new Locale("EN", "en"));

        try {
            //number = df.parse(strValue);
            return format.parse(strValue);

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


        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
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
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.report_cycle_unit_action_bar, null);
                String txt = getResources().getString(R.string.report_cycle_units);
                txt = txt.replace(getResources().getString(R.string.placeholder1), PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("GoodUnits"));
                ((TextView)view.findViewById(R.id.report_cycle_unit_actionbar_units_tv)).setText(txt);
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
        if (mUnitsCounter < mMaxUnits || PersistenceManager.getInstance().getUnitsInCycleType() > 1) {
//            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
//            value = value + 1;
//            value = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
//            mUnitsCounter = value;
            mUnitsCounter = convertNumericStringToNumberObject(mUnitsCounterTextView.getText().toString()).doubleValue() + 1.0;
            mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));

            mPlusButton.setEnabled(true);
        } else {
            mUnitsCounterTextView.setText(new StringBuilder(String.valueOf(mMaxUnits)));
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
        OppAppLogger.i(LOG_TAG, "sendReport units value is: " + String.valueOf(mUnitsCounter) + " JobId: " + mJobId);

        mReportCore.sendCycleUnitsReport(mUnitsCounter, mJobId);

//        SendBroadcast.refreshPolling(getContext());
    }

    private ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess(StandardResponse o) {

            StandardResponse response = objectToNewError(o);
            OppAppLogger.i(LOG_TAG, "sendReportSuccess() units value is: " + mUnitsCounter);

            dismissProgressDialog();
            if (response.getFunctionSucceed()) {
//                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
                mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc(), false);
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.CHANGE_UNIT_IN_CYCLE, true, "Change unit in cycle");
                //mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc());
            } else {
                mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc(), true);
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.CHANGE_UNIT_IN_CYCLE, false, "Change unit in cycle- " + response.getError().getErrorDesc());
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SendBroadcast.refreshPolling(getContext());
                }
            }, REFRESH_DELAY_MILLIS);

            SendBroadcast.refreshPolling(getContext());
            mReportCore.unregisterListener();

            if (getFragmentManager() != null) {

                getFragmentManager().popBackStack(null, getFragmentManager().POP_BACK_STACK_INCLUSIVE);

            }


        }

        @Override
        public void sendReportFailure(StandardResponse reason) {
            OppAppLogger.i(LOG_TAG, "sendReportFailure() reason: " + reason.getError().getErrorDesc());
            mDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + reason.getError().getErrorDesc(), true);
            new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.CHANGE_UNIT_IN_CYCLE, false, "Change unit in cycle- " + reason.getError().getErrorDesc());
            dismissProgressDialog();
            SendBroadcast.refreshPolling(getContext());
            if (getFragmentManager() != null) {

                getFragmentManager().popBackStack(null, getFragmentManager().POP_BACK_STACK_INCLUSIVE);

            }

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
                mJobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
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
