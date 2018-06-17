package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operatorsapp.R;

import java.util.ArrayList;

public class JobMoldSplitsAdapter extends RecyclerView.Adapter<JobMoldSplitsAdapter.ViewHolder> {

    private final Context mContext;

    private JobMoldSplitsAdapterListener mListener;


    public JobMoldSplitsAdapter(ArrayList<String> list, JobMoldSplitsAdapterListener listener, Context context) {

        mListener = listener;

        mContext = context;
    }


    @NonNull
    @Override
    public JobMoldSplitsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new JobMoldSplitsAdapter.ViewHolder(inflater.inflate(R.layout.item_splits_job_actions_mold, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final JobMoldSplitsAdapter.ViewHolder viewHolder, final int position) {


    }

    @Override
    public int getItemCount() {
//        if (mGalleryModels != null) {
//            return mGalleryModels.size();
//        } else
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitleTv;

        private final TextView mValueTv;

        ViewHolder(View itemView) {
            super(itemView);

            mTitleTv = itemView.findViewById(R.id.ISJAMOLD_tv1);

            mValueTv = itemView.findViewById(R.id.ISJAMOLD_tv2);

        }
    }

    public interface JobMoldSplitsAdapterListener {

    }
}
