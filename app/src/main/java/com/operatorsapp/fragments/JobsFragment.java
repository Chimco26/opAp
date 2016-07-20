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

import com.operators.jobscore.JobsCore;
import com.operators.jobscore.interfaces.JobsForMachineUICallbackListener;
import com.operators.jobsinfra.ErrorObjectInterface;
import com.operators.jobsinfra.Job;
import com.operators.jobsinfra.JobListForMachine;
import com.operators.jobsnetworkbridge.JobsNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.OnGoToScreenListener;
import com.operatorsapp.adapters.JobsRecyclerViewAdapter;
import com.operatorsapp.fragments.interfaces.OnJobSelectedCallbackListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;

import java.util.List;


public class JobsFragment extends Fragment implements OnJobSelectedCallbackListener
{
    private static final String LOG_TAG = JobsFragment.class.getSimpleName();
    private RecyclerView mJobsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private JobsRecyclerViewAdapter mJobsRecyclerViewAdapter;
    private List<Job> mJobList;

    private OnGoToScreenListener mOnGoToScreenListener;
    private JobsCore mJobsCore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        JobsNetworkBridge jobsNetworkBridge = new JobsNetworkBridge();
        jobsNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());

        mJobsCore = new JobsCore(jobsNetworkBridge, PersistenceManager.getInstance());

        mJobsCore.registerListener(new JobsForMachineUICallbackListener()
        {
            @Override
            public void onJobListReceived(JobListForMachine jobListForMachine)
            {
                mJobList = jobListForMachine.getJobs();
                Log.i(LOG_TAG, "onJobListReceived()");
            }

            @Override
            public void onJobListReceiveFailed(ErrorObjectInterface reason)
            {
                Log.i(LOG_TAG, "onJobListReceiveFailed()");

            }

            @Override
            public void onStartJobSuccess()
            {
                Log.i(LOG_TAG, "onStartJobSuccess()");

            }

            @Override
            public void onStartJobFailed(ErrorObjectInterface reason)
            {
                Log.i(LOG_TAG, "onStartJobFailed()");

            }
        });

        mJobsCore.getJobsListForMachine();
        mJobsCore.startJobForMachine(0);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            mOnGoToScreenListener = (OnGoToScreenListener) getActivity();

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException("Calling fragment must implement OnCroutonRequestListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);
        setActionBar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mJobsCore.registerListener(new JobsForMachineUICallbackListener()
        {
            @Override
            public void onJobListReceived(JobListForMachine jobListForMachine)
            {
                mJobList = jobListForMachine.getJobs();
                Log.i(LOG_TAG, "onJobListReceived()");
            }

            @Override
            public void onJobListReceiveFailed(ErrorObjectInterface reason)
            {
                Log.i(LOG_TAG, "onJobListReceiveFailed()");

            }

            @Override
            public void onStartJobSuccess()
            {
                Log.i(LOG_TAG, "onStartJobSuccess()");

            }

            @Override
            public void onStartJobFailed(ErrorObjectInterface reason)
            {
                Log.i(LOG_TAG, "onStartJobFailed()");

            }
        });

        mJobsCore.getJobsListForMachine();

        mJobsRecyclerView = (RecyclerView) view.findViewById(R.id.job_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mJobsRecyclerView.setLayoutManager(mLayoutManager);
        mJobsRecyclerViewAdapter = new JobsRecyclerViewAdapter(this, mJobList);
        mJobsRecyclerView.setAdapter(mJobsRecyclerViewAdapter);
    }

    private void setActionBar()
    {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
        {
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
            buttonClose.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getFragmentManager().popBackStack();
                }
            });
            actionBar.setCustomView(view);
//            actionBar.setIcon(R.drawable.logo);
        }
    }

    @Override
    public void onJobSelected(int jobId)
    {
        Log.i(LOG_TAG, "onJobSelected(), Selected Job ID: " + jobId);
        mOnGoToScreenListener.goToFragment(new SelectedJobFragment(), true);
    }

    @Override
    public void onJobListLoadingFailed()
    {
        Log.i(LOG_TAG, "onJobListLoadingFailed()");
    }
}
