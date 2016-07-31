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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;


/**
 * Created by Sergey on 31/07/2016.
 */
public class ReportRejectsFragment extends Fragment implements View.OnClickListener {

    public static final String LOG_TAG = ReportRejectsFragment.class.getSimpleName();
    private Spinner mRejectReasonSpinner;
    private Spinner mCauseSpinner;
    private TextView mCancelButton;
    private Button mNextButton;
    boolean mIsFirstReasonSpinnerSelection = true;
    boolean mIsReasonSelected;
    private GoToScreenListener mGoToScreenListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGoToScreenListener = (GoToScreenListener) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_report_rejects, container, false);
        setActionBar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRejectReasonSpinner = (Spinner) view.findViewById(R.id.reject_reason_spinner);

        ArrayAdapter<String> reasonSpinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.reject_reasons_array));
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
                    mNextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.buttons_selector));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mCauseSpinner = (Spinner) view.findViewById(R.id.cause_spinner);
        ArrayAdapter<String> causeSpinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.reject_reasons_cause_array));
        causeSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCauseSpinner.setAdapter(causeSpinnerArrayAdapter);
        mCauseSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);


        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);
        mNextButton = (Button) view.findViewById(R.id.button_next);

        super.onViewCreated(view, savedInstanceState);
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
            case R.id.button_next: {
                if (!mIsReasonSelected) {
                    Log.i(LOG_TAG, "reason not Selected");

                }
                else {
                    Log.i(LOG_TAG, "reason Selected");
                    ReportRejectSelectParametersFragment reportRejectSelectParametersFragment = new ReportRejectSelectParametersFragment();

                    mGoToScreenListener.goToFragment(reportRejectSelectParametersFragment, true);
                }
                break;
            }
        }
    }
}
