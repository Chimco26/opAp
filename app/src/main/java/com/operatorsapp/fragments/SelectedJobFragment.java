package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.operators.jobsinfra.Job;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.DashboardActivityToSelectedJobFragmentCallback;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;

public class SelectedJobFragment extends Fragment implements View.OnClickListener, DashboardActivityToSelectedJobFragmentCallback {

    private static final String LOG_TAG = SelectedJobFragment.class.getSimpleName();
    private static final String SELECTED_JOB = "selected_job";

    private Job mJob;
    private Button mCancelButton;
    private Button mActivateNewJobButton;
    private JobsFragmentToDashboardActivityCallback mJobsFragmentToDashboardActivityCallback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mJobsFragmentToDashboardActivityCallback = (JobsFragmentToDashboardActivityCallback) getActivity();
        mJobsFragmentToDashboardActivityCallback.onSelectedJobFragmentAttached(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_selected_job, container, false);

        Bundle bundle = this.getArguments();
        Gson gson = new Gson();
        mJob = gson.fromJson(bundle.getString(SELECTED_JOB), Job.class);

        setActionBar();

        mCancelButton = (Button) view.findViewById(R.id.button_cancel);
        mActivateNewJobButton = (Button)view.findViewById(R.id.button_activate_new_job);
        TextView jobIdTextView = (TextView) view.findViewById(R.id.job_id_text_view);
        TextView plannedStartTextView = (TextView) view.findViewById(R.id.planned_start_text_view);
        TextView productNameTextView = (TextView) view.findViewById(R.id.product_name_text_view);
        TextView productERP = (TextView) view.findViewById(R.id.erp_id_text_view);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity_text_view);


        jobIdTextView.setText(String.valueOf(mJob.getJobId()));
        productNameTextView.setText(mJob.getProductName());
        productERP.setText(String.valueOf(mJob.getErp()));
        quantityTextView.setText(String.valueOf(mJob.getNumberOfUnits()));
        plannedStartTextView.setText(mJob.getPlannedStart());

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mCancelButton.setOnClickListener(this);
        mActivateNewJobButton.setOnClickListener(this);


    }

    @Override
    public void onPause() {
        super.onPause();
        mCancelButton.setOnClickListener(null);
        mActivateNewJobButton.setOnClickListener(null);
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
            View view = inflater.inflate(R.layout.selected_job_action_bar, null);
            ImageView arrowBack = (ImageView) view.findViewById(R.id.arrow_back);
            arrowBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            });

            actionBar.setCustomView(view);
//            actionBar.setIcon(R.drawable.logo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel: {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.button_activate_new_job: {
                mJobsFragmentToDashboardActivityCallback.startJobForMachine(mJob.getJobId());
                break;
            }
        }

    }

    @Override
    public void onStartJobSuccess() {
        Log.i(LOG_TAG, "onStartJobSuccess()");
    }

    @Override
    public void onStartJobFailure() {
        Log.i(LOG_TAG, "onStartJobFailure()");
    }
}
