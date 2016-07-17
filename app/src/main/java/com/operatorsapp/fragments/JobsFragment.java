package com.operatorsapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;
import com.operatorsapp.adapters.JobsRecyclerViewAdapter;


public class JobsFragment extends Fragment
{
    private RecyclerView mJobsRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private JobsRecyclerViewAdapter mJobsRecyclerViewAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mJobsRecyclerView = (RecyclerView) view.findViewById(R.id.job_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mJobsRecyclerView.setLayoutManager(mLayoutManager);

        mJobsRecyclerViewAdapter = new JobsRecyclerViewAdapter();
        mJobsRecyclerView.setAdapter(mJobsRecyclerViewAdapter);
    }
}
