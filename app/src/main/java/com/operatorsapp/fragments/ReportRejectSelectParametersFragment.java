package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.common.ErrorResponse;
import com.example.common.StandardResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.oppapplog.OppAppLogger;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.broadcast.SendBroadcast;

public class ReportRejectSelectParametersFragment extends BackStackAwareFragment implements View.OnClickListener, CroutonRootProvider
{

    private static final String LOG_TAG = ReportRejectSelectParametersFragment.class.getSimpleName();
    private static final String REJECT_REASON = "reject_reason";
    private static final String REJECT_CAUSE = "reject_cause";
    private static final String REJECT_REASON_TITLE = "reject_reason_title";
    private static final String SELECTED_JOB_ID = "selected_job_id";
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    public static final String DASHBOARD_FRAGMENT = "dashboard_fragment";

    private int mSelectedReasonId;
    private int mSelectedCauseId;
    private String mSelectedReasonName;
    private Integer mJobId = null;
    private EditText mUnitsEditText;
    private EditText mWeightEditText;
    private Button mReportButton;
    private TextView mCancelButton;
    private OnCroutonRequestListener mOnCroutonRequestListener;
    private ReportCore mReportCore;
    private String mCurrentProductName;
    private int mCurrentProductId;
    private Double mUnitsData = null;
    private Double mWeightData = null;
    private ShowDashboardCroutonListener mDashboardCroutonListener;


    public static ReportRejectSelectParametersFragment newInstance(int selectedReasonId, int selectedCauseId, String selectedReasonName, Integer jobId, String currentProductName, int currentProductId)
    {
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
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mOnCroutonRequestListener = (OnCroutonRequestListener) getActivity();

        if (context instanceof ShowDashboardCroutonListener) {
            mDashboardCroutonListener = (ShowDashboardCroutonListener) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_report_rejects_selected_parameters, container, false);

        setActionBar();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mUnitsEditText, InputMethodManager.SHOW_IMPLICIT);

        TextView productNameTextView = (TextView) view.findViewById(R.id.report_rejects_product_name_text_view);
        TextView productIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);
        TextView jobIdTextView = (TextView) view.findViewById(R.id.report_rejects_selected_job_id_text_view);

        mReportButton = (Button) view.findViewById(R.id.button_report);
        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);

        mUnitsEditText = (EditText) view.findViewById(R.id.units_edit_text);
        mWeightEditText = (EditText) view.findViewById(R.id.weight_edit_text);
        ((TextView)view.findViewById(R.id.FRRSP_units_tv)).setText(PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("units"));

        mUnitsEditText.setFocusableInTouchMode(true);
        mUnitsEditText.requestFocus();

        if(mCurrentProductName != null)
        {
            productNameTextView.setText(new StringBuilder(mCurrentProductName).append(","));
        }
        else
        {
            productNameTextView.setText(getActivity().getString(R.string.dashes));
        }

        productIdTextView.setText(String.valueOf(mCurrentProductId));

        if(mJobId != null)
        {
            jobIdTextView.setText(String.valueOf(mJobId));
        }
        else
        {
            jobIdTextView.setText(getActivity().getString(R.string.dashes));

        }


        mUnitsEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                try
                {
                    mUnitsData= Double.parseDouble(s.toString());
                }
                catch (NumberFormatException e)
                {
                    mUnitsData = null;
                }
                refreshSendButtonState();
            }
        });

        mWeightEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                try
                {
                    mWeightData = Double.parseDouble(s.toString());
                }
                catch (NumberFormatException e)
                {
                    mWeightData = null;
                }
                refreshSendButtonState();
            }
        });
    }

    private void refreshSendButtonState()
{
    if(canSendReport())
    {
        mReportButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
        mReportButton.setEnabled(true);
    }
    else
    {
        mReportButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_bg_disabled));
        mReportButton.setEnabled(false);
    }
}

    @Override
    public void onPause()
    {
        super.onPause();
        mCancelButton.setOnClickListener(null);
        mReportButton.setOnClickListener(null);
        View view = getActivity().getCurrentFocus();
        if(view != null)
        {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mCancelButton.setOnClickListener(this);
        mReportButton.setOnClickListener(this);

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
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.report_reject_parameters_action_bar, null);

            LinearLayout buttonClose = (LinearLayout) view.findViewById(R.id.close_image_report_reject_selected);
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

            TextView reasonTextView = (TextView) view.findViewById(R.id.reason_text_view);
            reasonTextView.setText(mSelectedReasonName);
            actionBar.setCustomView(view);

        }
    }

    private boolean canSendReport()
    {
        return (mUnitsData != null || mWeightData != null);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.button_report:
            {
                OppAppLogger.d(LOG_TAG, "reason: " + mSelectedReasonId + " cause: " + mSelectedCauseId + " units: " + mUnitsEditText.getText().toString() + " weight: " + mWeightEditText.getText().toString() + " jobId " + mJobId);
                if(canSendReport())
                {
                    sendReport();
                }
                break;
            }
            case R.id.button_cancel:
            {
//                getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

                FragmentManager fragmentManager = getFragmentManager();
                if(fragmentManager != null)
                {
                    fragmentManager.popBackStack();
                }
                break;
            }
        }
    }

    private void sendReport()
    {
        ProgressDialogManager.show(getActivity());
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        mReportCore.sendReportReject(mSelectedReasonId, mSelectedCauseId, mUnitsData, mWeightData, mJobId);
        // TODO: 25/06/2018 check all instances and make the call after response
//        SendBroadcast.refreshPolling(getContext());
    }

    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener()
    {
        @Override
        public void sendReportSuccess(StandardResponse o)
        {//TODO crouton error
            SendBroadcast.refreshPolling(getContext());
            dismissProgressDialog();

            if (o != null){

                mDashboardCroutonListener.onShowCrouton((o.getError().getErrorDesc()), false);
            }
            OppAppLogger.i(LOG_TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();

            if (getFragmentManager() != null){

                getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

        @Override
        public void sendReportFailure(StandardResponse reason)
        {
            dismissProgressDialog();
            OppAppLogger.w(LOG_TAG, "sendReportFailure()");
            if(reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Credentials_mismatch)
            {
                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mOnCroutonRequestListener, new SilentLoginCallback()
                {
                    @Override
                    public void onSilentLoginSucceeded()
                    {
                        sendReport();
                    }

                    @Override
                    public void onSilentLoginFailed(StandardResponse reason)
                    {
                        OppAppLogger.w(LOG_TAG, "Failed silent login");
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
                        ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
                        dismissProgressDialog();
                    }
                });
            }
            else
            {

                StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
                ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener, errorObject);
            }
        }
    };

    private void dismissProgressDialog()
    {
        if(getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable()
            {
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
    public int getCroutonRoot()
    {
        return R.id.report_reject_selected_items_crouton_root;
    }
}
