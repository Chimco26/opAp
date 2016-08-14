package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportRejectCore;
import com.operators.reportrejectnetworkbridge.ReportRejectNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;

/**
 * Created by Sergey on 11/08/2016.
 */
public class ReportCycleUnitsFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = ReportCycleUnitsFragment.class.getSimpleName();
    private static final String CURRENT_PRODUCT_NAME = "current_product_name";
    private static final String CURRENT_PRODUCT_ID = "current_product_id";
    private String mCurrentProductName;
    private int mCurrentProductId;

    private TextView mProductTitleTextView;
    private TextView mProductIdTextView;

    private ImageView mPlusButton;
    private ImageView mMinusButton;
    private EditText mUnitsCounterTextView;
    private Button mButtonReport;
    private TextView mButtonCancel;

    private double mUnitsCounter = 1;
    private ReportRejectCore mReportRejectCore;
    private Integer mJobId = null;


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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report_cycle_unit, container, false);

        if (getArguments() != null) {
            mCurrentProductName = getArguments().getString(CURRENT_PRODUCT_NAME);
            mCurrentProductId = getArguments().getInt(CURRENT_PRODUCT_ID);
        }

        setActionBar();

        mProductTitleTextView = (TextView) view.findViewById(R.id.report_cycle_u_product_name_text_view);
        mProductIdTextView = (TextView) view.findViewById(R.id.report_cycle_id_text_view);

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
                                mUnitsCounter = Double.valueOf(String.format("%.3f", value));
                            }
                        }
                        else {
                            mButtonReport.setEnabled(false);
                        }
                    }
                }
                else {
                    mButtonReport.setEnabled(false);
                }
            }
        });

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
        value = Double.valueOf(String.format("%.3f", value));
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
        }
        else {
            double value = Double.valueOf(mUnitsCounterTextView.getText().toString());
            value = value - 1;
            value = Double.valueOf(String.format("%.3f", value));
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
        Log.i(LOG_TAG, "sendReport units value is: " + String.valueOf(mUnitsCounter));

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
}
