package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

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

import static com.operatorsapp.fragments.ReportProductionFragment.CURRENT_JOB_LIST_FOR_MACHINE;
import static com.operatorsapp.fragments.ReportProductionFragment.CURRENT_PRODUCT_ID;
import static com.operatorsapp.fragments.ReportProductionFragment.CURRENT_SELECTED_POSITION;

public class FixUnitsProducedFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider {

    private static final String TAG = FixUnitsProducedFragment.class.getSimpleName();
    private static final int REFRESH_DELAY_MILLIS = 3000;
    private ShowDashboardCroutonListener mDashboardCroutonListener;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private View mActiveJobsProgressBar;
    private int mCurrentProductId;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private int mSelectedPosition;
    private Integer mJoshID;
    private Spinner mJoshSpinner;
    private ReportCore mReportCore;
    private EditText mAmountEt;

    public static FixUnitsProducedFragment newInstance(int currentProductId, ActiveJobsListForMachine activeJobsListForMachine, int selectedPosition) {
        FixUnitsProducedFragment fragment = new FixUnitsProducedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CURRENT_PRODUCT_ID, currentProductId);
        bundle.putParcelable(CURRENT_JOB_LIST_FOR_MACHINE, activeJobsListForMachine);
        bundle.putInt(CURRENT_SELECTED_POSITION, selectedPosition);
        fragment.setArguments(bundle);
        return fragment;
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
        mDashboardCroutonListener = null;
        super.onDetach();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        setActionBar();

        return inflater.inflate(R.layout.fragment_fix_produced_unit, container, false);
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
                mJoshID = mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJobID();
            }

        }

        mAmountEt = view.findViewById(R.id.units_text_view);

        TextView mProductIdTextView = view.findViewById(R.id.report_cycle_id_text_view);

        mProductIdTextView.setText(String.valueOf(mCurrentProductId));

        ((TextView)view.findViewById(R.id.FRCU_units_tv)).setText(PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("Units"));
        String txt = getResources().getString(R.string.units_to_add_remove);
        txt = txt.replace(getResources().getString(R.string.placeholder1), PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("Units"));
        ((TextView)view.findViewById(R.id.FRCU_units_subtitle_tv)).setText(txt);

        mJoshSpinner = view.findViewById(R.id.report_job_spinner);

        initJobsSpinner();
        disableSpinnerProgressBar();

        initListener(view);
    }

    @Override
    public void onPause() {
        super.onPause();
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

    private void initListener(View view) {
        view.findViewById(R.id.button_report).setOnClickListener(this);
        view.findViewById(R.id.button_cancel).setOnClickListener(this);
    }

    private void disableSpinnerProgressBar() {
        mActiveJobsProgressBar.setVisibility(View.GONE);
    }

    private void initJobsSpinner() {
        if (getActivity() != null) {
            if (mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null) {
                mJoshSpinner.setVisibility(View.VISIBLE);
                final ActiveJobsSpinnerAdapter activeJobsSpinnerAdapter = new ActiveJobsSpinnerAdapter(getActivity(), R.layout.active_jobs_spinner_item, mActiveJobsListForMachine.getActiveJobs());
                activeJobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mJoshSpinner.setAdapter(activeJobsSpinnerAdapter);
                mJoshSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
                mJoshSpinner.setSelection(mSelectedPosition);
                mJoshSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        activeJobsSpinnerAdapter.setTitle(position);
                        mJoshID = mActiveJobsListForMachine.getActiveJobs().get(position).getJoshID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

    @Override
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

    private void sendReport() {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.inject(NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        OppAppLogger.i(TAG, "sendReport units value is: " + mAmountEt.getText().toString() + " JoShId: " + mJoshID);

        mReportCore.sendReportFixUnits(Double.parseDouble(mAmountEt.getText().toString()), mJoshID);

//        SendBroadcast.refreshPolling(getContext());
    }

    private ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess(StandardResponse o) {

            StandardResponse response = objectToNewError(o);
            OppAppLogger.i(TAG, "sendReportSuccess() units value is: " + mAmountEt.getText().toString());

            dismissProgressDialog();
            if (response.getFunctionSucceed()) {
//                ShowCrouton.showSimpleCrouton(mOnCroutonRequestListener, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
                mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc(), false);
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.FIX_UNITS, true, "Report Fix Units");
                //mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc());
            } else {
                mDashboardCroutonListener.onShowCrouton(response.getError().getErrorDesc(), true);
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.FIX_UNITS, false, "Report Fix Units- " + response.getError().getErrorDesc());
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null && !getActivity().isDestroyed()) {
                        SendBroadcast.refreshPolling(getContext());
                    }
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
            OppAppLogger.i(TAG, "sendReportFailure() reason: " + reason.getError().getErrorDesc());
            mDashboardCroutonListener.onShowCrouton("sendReportFailure() reason: " + reason.getError().getErrorDesc(), true);
            new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.FIX_UNITS, false, "Report Fix Units- " + reason.getError().getErrorDesc());
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
                    if (getActivity() != null && !getActivity().isDestroyed()) {
                        ProgressDialogManager.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public int getCroutonRoot() {
        return R.id.top_layout;
    }
}
