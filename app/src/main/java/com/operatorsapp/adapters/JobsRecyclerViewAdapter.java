package com.operatorsapp.adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.List;

public class JobsRecyclerViewAdapter extends RecyclerView.Adapter<JobsRecyclerViewAdapter.ViewHolder>
{
    private List<String> mJobsList;

    public JobsRecyclerViewAdapter()
    {
        mJobsList = new ArrayList<>();

    }

    @Override
    public JobsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JobsRecyclerViewAdapter.ViewHolder holder, final int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return mJobsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;

        public ViewHolder(View v)
        {
            super(v);
           // cardView = (CardView)itemView.findViewById(R.id.job_card_view);

        }
    }
}
