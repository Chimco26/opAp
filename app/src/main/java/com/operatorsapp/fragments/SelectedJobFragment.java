package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.example.oppapplog.OppAppLogger;
import com.google.gson.Gson;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.DashBoardActivityToSelectedJobFragmentCallback;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.CurrentJob;
import com.zemingo.logrecorder.ZLogger;

public class SelectedJobFragment extends BackStackAwareFragment implements View.OnClickListener, DashBoardActivityToSelectedJobFragmentCallback, CroutonRootProvider {

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
        if (mJobsFragmentToDashboardActivityCallback != null) {
            mJobsFragmentToDashboardActivityCallback.onSelectedJobFragmentAttached(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_selected_job, container, false);

        // Analytics
        OperatorApplication application = (OperatorApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        PersistenceManager pm = PersistenceManager.getInstance();
        mTracker.setScreenName(LOG_TAG);
        mTracker.setClientId("machine id: " + pm.getMachineId());
        mTracker.setAppVersion(pm.getVersion() + "");
        mTracker.setHostname(pm.getSiteName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Bundle bundle = this.getArguments();
        Gson gson = new Gson();
        if (bundle != null) {
            mCurrentJob = gson.fromJson(bundle.getString(SELECTED_JOB), CurrentJob.class);
        }
        String[] headersArray = mCurrentJob.getHeaders();
        setActionBar();

        mCancelButton = view.findViewById(R.id.button_cancel);

        mActivateNewJobButton = view.findViewById(R.id.button_activate_new_job);
        setTitles(view);
        setValues(view, headersArray);

        return view;
    }

    private void setTitles(View view) {
        TextView firstTitle = view.findViewById(R.id.first_title_text_view);
        TextView secondTitle = view.findViewById(R.id.second_title_text_view);
        TextView thirdTitle = view.findViewById(R.id.third_title_text_view);
        TextView fourthTitle = view.findViewById(R.id.fourth_title_text_view);
        TextView fifthTitle = view.findViewById(R.id.fifth_title_text_view);

        firstTitle.setText(mCurrentJob.getFirstField());
        secondTitle.setText(mCurrentJob.getSecondField());
        thirdTitle.setText(mCurrentJob.getThirdField());
        fourthTitle.setText(mCurrentJob.getFourthField());
        fifthTitle.setText(mCurrentJob.getFifthField());
    }

    private void setValues(View view, String[] headersArray) {
        TextView jobIdTitle = view.findViewById(R.id.first_field_text_view);
        TextView secondField = view.findViewById(R.id.second_field_text_view);
        TextView thirdField = view.findViewById(R.id.third_field_text_view);
        TextView fourthField = view.findViewById(R.id.fourth_field_text_view);
        TextView fifthField = view.findViewById(R.id.fifth_field_text_view);

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

    protected void setActionBar() {

        ActionBar actionBar = null;
        if (getActivity() != null) {
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
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
            ImageView arrowBack = view.findViewById(R.id.arrow_back);
            arrowBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getActivity().onBackPressed();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    if (fragmentManager != null) {
//                        fragmentManager.popBackStack();
//                    }
                    //getFragmentManager().popBackStack(DASHBOARD_FRAGMENT, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel: {

                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                break;
            }
            case R.id.button_activate_new_job: {
                ProgressDialogManager.show(getActivity());
                mJobsFragmentToDashboardActivityCallback.startJobForMachine(mCurrentJob.getJobId());
                break;
            }
        }
    }

    @Override
    public void onStartJobSuccess() {
        OppAppLogger.getInstance().i(LOG_TAG, "onStartJobSuccess()");
        dismissProgressDialog();
    }

    @Override
    public void onStartJobFailure() {
        OppAppLogger.getInstance().i(LOG_TAG, "onStartJobFailure()");
        dismissProgressDialog();
    }

    @Override
    public int getCroutonRoot() {
        return R.id.selected_job_crouton_root;
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
}
