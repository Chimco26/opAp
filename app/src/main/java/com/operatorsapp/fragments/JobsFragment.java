package com.operatorsapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.operators.jobsinfra.ErrorObjectInterface;
import com.operators.jobsinfra.Job;
import com.operators.jobsinfra.JobListForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.OnGoToScreenListener;
import com.operatorsapp.adapters.JobsRecyclerViewAdapter;
import com.operatorsapp.fragments.interfaces.OnJobSelectedCallbackListener;
import com.operatorsapp.interfaces.DashboardActivityToJobsFragmentCallback;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;

import java.util.List;


public class JobsFragment extends Fragment implements OnJobSelectedCallbackListener, DashboardActivityToJobsFragmentCallback {
    private static final String LOG_TAG = JobsFragment.class.getSimpleName();
    private static final String JOB_ID = "jobId";
    private static final String PRODUCT_NAME = "productName";
    private static final String PRODUCT_ERP = "productERP";
    private static final String PLANNED_START = "plannedStart";
    private static final String NUMBER_OF_UNITS = "numberOfUnits";
    public static final String SELECTED_JOB = "selected_job";
    private RecyclerView mJobsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private JobsRecyclerViewAdapter mJobsRecyclerViewAdapter;
    private List<Job> mJobList;

    private OnGoToScreenListener mOnGoToScreenListener;

    private JobsFragmentToDashboardActivityCallback mJobsFragmentToDashboardActivityCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnGoToScreenListener = (OnGoToScreenListener) getActivity();
            mJobsFragmentToDashboardActivityCallback = (JobsFragmentToDashboardActivityCallback) getActivity();
            mJobsFragmentToDashboardActivityCallback.onJobFragmentAttached(this);
            mJobsFragmentToDashboardActivityCallback.initJobsCore();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnCroutonRequestListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);
        setActionBar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mJobsRecyclerView = (RecyclerView) view.findViewById(R.id.job_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mJobsRecyclerView.setLayoutManager(mLayoutManager);
        mJobsFragmentToDashboardActivityCallback.getJobsForMachineList();

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
            View view = inflater.inflate(R.layout.jobs_fragment_action_bar, null);

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
    public void onJobSelected(int position) {

        Log.i(LOG_TAG, "onJobSelected(), Selected Job ID: " + mJobList.get(position).getJobId());
        SelectedJobFragmentFragment selectedJobFragment = new SelectedJobFragmentFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        Job job = mJobList.get(position);
        String jobString = gson.toJson(job, Job.class);
        bundle.putString(SELECTED_JOB, jobString);

        selectedJobFragment.setArguments(bundle);
        mOnGoToScreenListener.goToFragment(selectedJobFragment, true);
    }


    @Override
    public void onJobReceiveFailed(ErrorObjectInterface reason) {
        Log.i(LOG_TAG, "onJobReceiveFailed()");
    }

    @Override
    public void onJobReceived(JobListForMachine jobListForMachine) {
        mJobList = jobListForMachine.getJobs();
        mJobsRecyclerViewAdapter = new JobsRecyclerViewAdapter(this, mJobList);
        mJobsRecyclerView.setAdapter(mJobsRecyclerViewAdapter);
    }

}
