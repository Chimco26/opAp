package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operatorsapp.R;

import java.util.ArrayList;

public class JobHeadersAdaper extends RecyclerView.Adapter<JobHeadersAdaper.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Header> mHeaders;

    private JobHeadersAdaperListener mListener;


    public JobHeadersAdaper(ArrayList<Header> list, JobHeadersAdaperListener listener, Context context) {

        mListener = listener;

        mContext = context;

        mHeaders = list;
    }


    @NonNull
    @Override
    public JobHeadersAdaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new JobHeadersAdaper.ViewHolder(inflater.inflate(R.layout.item_product_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final JobHeadersAdaper.ViewHolder viewHolder, final int position) {

        viewHolder.mTv.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        viewHolder.mTv.setTextColor(mContext.getResources().getColor(R.color.blue1));

//   TODO     viewHolder.mTv.setBackgroundColor(mContext.getResources().getColor(R.color.blue1));
//        viewHolder.mTv.setTextColor(mContext.getResources().getColor(R.color.white));

        viewHolder.mTv.setText(mHeaders.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        if (mHeaders != null) {
            return mHeaders.size();
        } else
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTv;

        ViewHolder(View itemView) {
            super(itemView);

            mTv = itemView.findViewById(R.id.IPS_search_tv);

        }

    }

    public interface JobHeadersAdaperListener {

    }
}
