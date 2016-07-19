package com.operatorsapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.operatorsapp.R;
import com.operatorsapp.fragments.interfaces.OnJobSelectedCallbackListener;

import java.util.ArrayList;
import java.util.List;

public class JobsRecyclerViewAdapter extends RecyclerView.Adapter<JobsRecyclerViewAdapter.ViewHolder> {
    private List<String> mJobsList;
    private OnJobSelectedCallbackListener mOnJobSelectedCallbackListener;

    public JobsRecyclerViewAdapter(OnJobSelectedCallbackListener onJobSelectedCallbackListener) {
        mOnJobSelectedCallbackListener = onJobSelectedCallbackListener;
        mJobsList = new ArrayList<>();
        mJobsList.add("12");
        mJobsList.add("123");

    }

    @Override
    public JobsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final JobsRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mJobRowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnJobSelectedCallbackListener.onJobSelected(holder.getAdapterPosition());  //TODO must be mJobsList.get(position).getId()
            }
        });
    }

    @Override
    public int getItemCount() {
        return mJobsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mJobRowLayout;

        public ViewHolder(View view) {
            super(view);
            mJobRowLayout = (LinearLayout) view.findViewById(R.id.job_row_layout);
        }
    }
}
