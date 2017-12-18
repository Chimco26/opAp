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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.adapters.ActiveJobsSpinnerAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.zemingo.logrecorder.ZLogger;

import java.util.Locale;

public class ReportCycleUnitsFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider
{

    public static final String LOG_TAG = ReportCycleUnitsFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private String mCurrentProductName;
    private int mCurrentProductId;

    private ImageView mPlusButton;
    private ImageView mMinusButton;
    private TextView mUnitsCounterTextView;
    private Button mButtonReport;
    private TextView mButtonCancel;
    private OnCroutonRequestListener mOnCroutonRequestListener;

    private double mUnitsCounter = 1;
    private ReportCore mReportCore;
    private Integer mJoshId = null;
    private int mMaxUnits = 0;

    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private Spinner mJobsSpinner;
    private ProgressBar mActiveJobsProgressBar;

    public static ReportCycleUnitsFragment newInstance(String currentProductName, int currentProductId)
    {
        ReportCycleUnitsFragment reportCycleUnitsFragment = new ReportCycleUnitsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_PRODUCT_NAME, currentProductName);
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        reportCycleUnitsFragment.setArguments(bundle);
        return reportCycleUnitsFragment;
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActiveJobs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_report_cycle_unit, container, false);

        setActionBar();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //        final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //        inputMethodManager.showSoftInput(mUnitsCounterTextView, InputMethodManager.SHOW_IMPLICIT);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mActiveJobsProgressBar = (ProgressBar) view.findViewById(R.id.active_jobs_progressBar);
        getActiveJobs();

        if(getArguments() != null)
        {
            mCurrentProductName = getArguments().getString(CURRENT_PRODUCT_NAME);
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
        }


        TextView mProductTitleTextView = (TextView) view.findViewById(R.id.report_cycle_u_product_name_text_view);
        TextView mProductIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);

        mProductTitleTextView.setText(mCurrentProductName);
        mProductIdTextView.setText(String.valueOf(mCurrentProductId));

        mUnitsCounterTextView = (TextView) view.findViewById(R.id.units_text_view);
        mUnitsCounterTextView.setFocusableInTouchMode(true);
        mUnitsCounterTextView.requestFocus();
        mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
        mPlusButton = (ImageView) view.findViewById(R.id.button_plus);
        mMinusButton = (ImageView) view.findViewById(R.id.button_minus);

        mButtonReport = (Button) view.findViewById(R.id.button_report);
        mButtonCancel = (TextView) view.findViewById(R.id.button_cancel);

        mUnitsCounterTextView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length() > 0)
                {
                    Character lastCharacter = mUnitsCounterTextView.getText().toString().charAt(mUnitsCounterTextView.getText().toString().length() - 1);
                    if(!lastCharacter.toString().equals("."))
                    {
                        if(Double.valueOf(s.toString()) > 0 && Double.valueOf(s.toString()) <= mMaxUnits)
                        {
                            mButtonReport.setEnabled(true);
                            //                            mButtonReport.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
                            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
                            mUnitsCounter = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
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
                    }
                    else
                    {
                        mButtonReport.setEnabled(false);
                        //                        mButtonReport.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_disabled));
                    }
                }
                else
                {
                    mButtonReport.setEnabled(false);
                    //                    mButtonReport.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_disabled));
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mJobsSpinner = (Spinner) view.findViewById(R.id.report_job_spinner);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPlusButton.setOnClickListener(this);
        mMinusButton.setOnClickListener(this);
        mButtonReport.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mPlusButton.setOnClickListener(null);
        mMinusButton.setOnClickListener(null);
        mButtonReport.setOnClickListener(null);
        mButtonCancel.setOnClickListener(null);


        View view = getActivity().getCurrentFocus();
        if(view != null)
        {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void setActionBar()
    {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // rootView null
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.report_cycle_unit_action_bar, null);

            LinearLayout buttonClose = (LinearLayout) view.findViewById(R.id.close_image);
            buttonClose.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentManager fragmentManager = getFragmentManager();
                    if(fragmentManager != null)
                    {
                        fragmentManager.popBackStack();
                    }
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.button_plus:
            {
                increase();
                break;
            }
            case R.id.button_minus:
            {
                decrease();
                break;
            }
            case R.id.button_cancel:
            {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.button_report:
            {
                sendReport();
                break;
            }
        }
    }

    private void increase()
    {
        if(mUnitsCounter < mMaxUnits)
        {
            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
            value = value + 1;
            value = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
            mUnitsCounter = value;
            mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
            mPlusButton.setEnabled(true);
        }
        else
        {
            mUnitsCounterTextView.setText(new StringBuilder(String.valueOf(mMaxUnits)).append(".0"));
            mPlusButton.setEnabled(false);
        }
        mMinusButton.setEnabled(true);
        mButtonReport.setEnabled(true);
    }

    private void decrease()
    {
        mUnitsCounter--;
        if(mUnitsCounter <= 0)
        {
            mUnitsCounterTextView.setText("0.0");
            mButtonReport.setEnabled(false);
            mMinusButton.setEnabled(false);
            mPlusButton.setEnabled(true);
        }
        else
        {
            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
            value = value - 1;
            value = Double.valueOf(String.format(Locale.getDefault(), "%.3f", value));
            mUnitsCounter = value;
            mButtonReport.setEnabled(true);
            mMinusButton.setEnabled(true);
            mPlusButton.setEnabled(true);
            mUnitsCounterTextView.setText(String.valueOf(mUnitsCounter));
        }
    }

    private void sendReport()
    {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.inject(NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        ZLogger.i(LOG_TAG, "sendReport units value is: " + String.valueOf(mUnitsCounter) + " JobId: " + mJoshId);

        mReportCore.sendCycleUnitsReport(mUnitsCounter, mJoshId);

        SendBroadcast.refreshPolling(getContext());
    }

    private ReportCallbackListener mReportCallbackListener = new ReportCallbackListener()
    {
        @Override
        public void sendReportSuccess()
        {
            ZLogger.i(LOG_TAG, "sendReportSuccess() units value is: " + mUnitsCounter);
            mReportCore.unregisterListener();
            getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            dismissProgressDialog();
        }

        @Override
        public void sendReportFailure(ErrorObjectInterface reason)
        {
            ZLogger.i(LOG_TAG, "sendReportFailure() reason: " + reason.getDetailedDescription());
            dismissProgressDialog();
        }
    };

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

    private void getActiveJobs()
    {
        ActiveJobsListForMachineNetworkBridge activeJobsListForMachineNetworkBridge = new ActiveJobsListForMachineNetworkBridge();
        activeJobsListForMachineNetworkBridge.inject(NetworkManager.getInstance());
        ActiveJobsListForMachineCore mActiveJobsListForMachineCore = new ActiveJobsListForMachineCore(PersistenceManager.getInstance(), activeJobsListForMachineNetworkBridge);
        mActiveJobsListForMachineCore.registerListener(mActiveJobsListForMachineUICallbackListener);
        mActiveJobsListForMachineCore.getActiveJobsListForMachine();
    }

    private ActiveJobsListForMachineUICallbackListener mActiveJobsListForMachineUICallbackListener = new ActiveJobsListForMachineUICallbackListener()
    {
        @Override
        public void onActiveJobsListForMachineReceived(ActiveJobsListForMachine activeJobsListForMachine)
        {
            if(activeJobsListForMachine != null)
            {
                mActiveJobsListForMachine = activeJobsListForMachine;
                mJoshId = mActiveJobsListForMachine.getActiveJobs().get(0).getJoshID();
                mMaxUnits = mActiveJobsListForMachine.getActiveJobs().get(0).getCavitiesStandard();
                initJobsSpinner();
                ZLogger.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            }
            else
            {
                mJoshId = null;
                mMaxUnits = 0;
                ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
            }
            disableProgressBar();

        }

        @Override
        public void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason)
        {
            mJoshId = null;
            mMaxUnits = 0;
            ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getDetailedDescription());
            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener);
            disableProgressBar();

        }
    };

    private void disableProgressBar()
    {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }


    private void initJobsSpinner()
    {
        if(getActivity() != null)
        {
            if(mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null)
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
                        mJoshId = mActiveJobsListForMachine.getActiveJobs().get(position).getJoshID();
                        mMaxUnits = mActiveJobsListForMachine.getActiveJobs().get(position).getCavitiesStandard();
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
        return R.id.top_layout;
    }
}
