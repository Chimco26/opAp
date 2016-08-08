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
import com.operatorsapp.R;
import com.operatorsapp.interfaces.DashboardActivityToSelectedJobFragmentCallback;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;
import com.operatorsapp.model.CurrentJob;

public class SelectedJobFragment extends Fragment implements View.OnClickListener, DashboardActivityToSelectedJobFragmentCallback {

    private static final String LOG_TAG = SelectedJobFragment.class.getSimpleName();
    private static final String SELECTED_JOB = "selected_job";
    private TextView mCancelButton;
    private Button mActivateNewJobButton;
    private JobsFragmentToDashboardActivityCallback mJobsFragmentToDashboardActivityCallback;
    private CurrentJob mCurrentJob;

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
        mCurrentJob = gson.fromJson(bundle.getString(SELECTED_JOB), CurrentJob.class);
        String[] headersArray = mCurrentJob.getHeaders();
        setActionBar();

        mCancelButton = (TextView) view.findViewById(R.id.button_cancel);

        mActivateNewJobButton = (Button) view.findViewById(R.id.button_activate_new_job);
        setTitles(view);
        setValues(view, headersArray);

        return view;
    }

    private void setTitles(View view) {
        TextView firstTitle = (TextView) view.findViewById(R.id.first_title_text_view);
        TextView secondTitle = (TextView) view.findViewById(R.id.second_title_text_view);
        TextView thirdTitle = (TextView) view.findViewById(R.id.third_title_text_view);
        TextView fourthTitle = (TextView) view.findViewById(R.id.fourth_title_text_view);
        TextView fifthTitle = (TextView) view.findViewById(R.id.fifth_title_text_view);

        firstTitle.setText(mCurrentJob.getFirstField());
        secondTitle.setText(mCurrentJob.getSecondField());
        thirdTitle.setText(mCurrentJob.getThirdField());
        fourthTitle.setText(mCurrentJob.getFourthField());
        fifthTitle.setText(mCurrentJob.getFifthField());
    }

    private void setValues(View view, String[] headersArray) {
        TextView jobIdTitle = (TextView) view.findViewById(R.id.first_field_text_view);
        TextView secondField = (TextView) view.findViewById(R.id.second_field_text_view);
        TextView thirdField = (TextView) view.findViewById(R.id.third_field_text_view);
        TextView fourthField = (TextView) view.findViewById(R.id.fourth_field_text_view);
        TextView fifthField = (TextView) view.findViewById(R.id.fifth_field_text_view);

        jobIdTitle.setText(headersArray[0]);
        secondField.setText(headersArray[1]);
        thirdField.setText(headersArray[2]);
        fourthField.setText(headersArray[3]);
        fifthField.setText(headersArray[4]);
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
                    //TODO refactor
                    getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel: {
                //TODO check
                getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            }
            case R.id.button_activate_new_job: {
                mJobsFragmentToDashboardActivityCallback.startJobForMachine(mCurrentJob.getJobId());
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
