package com.operatorsapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.jobsinfra.Job;
import com.operatorsapp.R;
import com.operatorsapp.fragments.interfaces.OnJobSelectedCallbackListener;

import java.util.ArrayList;
import java.util.List;

public class JobsRecyclerViewAdapter extends RecyclerView.Adapter<JobsRecyclerViewAdapter.ViewHolder>
{
    private List<Job> mJobsList;
    private OnJobSelectedCallbackListener mOnJobSelectedCallbackListener;

    public JobsRecyclerViewAdapter(OnJobSelectedCallbackListener onJobSelectedCallbackListener, List<Job> jobsList)
    {
        mOnJobSelectedCallbackListener = onJobSelectedCallbackListener;
        mJobsList = jobsList;
    }

    @Override
    public JobsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final JobsRecyclerViewAdapter.ViewHolder holder, final int position)
    {
        final int jobId = mJobsList.get(position).getJobId();

        holder.mJobRowLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mOnJobSelectedCallbackListener.onJobSelected(jobId);
            }
        });

        holder.mJobIdTextView.setText(String.valueOf(jobId));
        holder.mProductNameTextView.setText(mJobsList.get(position).getProductName());
    }

    @Override
    public int getItemCount()
    {
        return mJobsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout mJobRowLayout;
        private TextView mJobIdTextView;
        private TextView mProductNameTextView;


        public ViewHolder(View view)
        {
            super(view);
            mJobRowLayout = (LinearLayout) view.findViewById(R.id.job_row_layout);
            mJobIdTextView = (TextView) view.findViewById(R.id.text_view_job_id);
            mProductNameTextView = (TextView) view.findViewById(R.id.adapter_text_view_product_name);
        }
    }
}
