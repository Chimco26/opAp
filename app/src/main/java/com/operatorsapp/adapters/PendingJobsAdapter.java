package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PandingJob;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingJobsAdapter extends RecyclerView.Adapter<PendingJobsAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<PandingJob> mPandingjobs;
    private final HashMap<String, Header> mHashMapHeaders;

    private PendingJobsAdapterListener mListener;


    public PendingJobsAdapter(ArrayList<PandingJob> list, HashMap<String, Header> hashMapHeaders, PendingJobsAdapterListener listener, Context context) {

        mListener = listener;

        mContext = context;

        mPandingjobs = list;

        mHashMapHeaders = hashMapHeaders;
    }


    @NonNull
    @Override
    public PendingJobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new PendingJobsAdapter.ViewHolder(inflater.inflate(R.layout.item_pending_jobs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingJobsAdapter.ViewHolder viewHolder, final int position) {

        if (mPandingjobs.get(position).isSelected()){

            viewHolder.mSelectedBarView.setVisibility(View.VISIBLE);
            viewHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }else {

            viewHolder.mSelectedBarView.setVisibility(View.INVISIBLE);
            viewHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.transparentColor));
        }

        if (mPandingjobs.get(position).getProperties().size() > 0) {
            viewHolder.mTv1.setText(String.format("%s %s", mPandingjobs.get(position).getProperties().get(0).getKey(), mPandingjobs.get(position).getProperties().get(0).getValue()));
            viewHolder.mTv1.setTextColor(Color.parseColor(mHashMapHeaders.get(mPandingjobs.get(position).getProperties().get(0).getKey()).getColor().replace("\t","")));
        }

        if (mPandingjobs.get(position).getProperties().size() > 1) {

            viewHolder.mTv2.setText(String.format("%s %s", mPandingjobs.get(position).getProperties().get(1).getKey(), mPandingjobs.get(position).getProperties().get(1).getValue()));
            viewHolder.mTv2.setTextColor(Color.parseColor(mHashMapHeaders.get(mPandingjobs.get(position).getProperties().get(1).getKey()).getColor().replace("\t","")));

        }
        if (mPandingjobs.get(position).getProperties().size() > 2) {

            viewHolder.mTv3.setText(String.format("%s %s", mPandingjobs.get(position).getProperties().get(2).getKey(), mPandingjobs.get(position).getProperties().get(2).getValue()));
            viewHolder.mTv3.setTextColor(Color.parseColor(mHashMapHeaders.get(mPandingjobs.get(position).getProperties().get(2).getKey()).getColor().replace("\t","")));

        }

        if (mPandingjobs.get(position).getProperties().size() > 3) {

            viewHolder.mTv4.setText(String.format("%s %s", mPandingjobs.get(position).getProperties().get(3).getKey(), mPandingjobs.get(position).getProperties().get(3).getValue()));
            viewHolder.mTv4.setTextColor(Color.parseColor(mHashMapHeaders.get(mPandingjobs.get(position).getProperties().get(3).getKey()).getColor().replace("\t","")));

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPandingjobs.get(position).isSelected()){

                    mPandingjobs.get(position).setSelected(false);

                }else {

                    resetSelectedItem();
                    mPandingjobs.get(position).setSelected(true);

                }

                updateView(viewHolder, mPandingjobs.get(position).isSelected());

                notifyDataSetChanged();
                mListener.onPandingJobSelected(mPandingjobs.get(position));
            }
        });

    }

    private void resetSelectedItem() {

        for (PandingJob pandingJob: mPandingjobs){

            pandingJob.setSelected(false);
        }
    }

    private void updateView(ViewHolder viewHolder, boolean selected) {

        if (selected){

            viewHolder.mSelectedBarView.setVisibility(View.VISIBLE);
            viewHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }else {

            viewHolder.mSelectedBarView.setVisibility(View.INVISIBLE);
            viewHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.transparentColor));
        }
    }

    @Override
    public int getItemCount() {
        if (mPandingjobs != null) {
            return mPandingjobs.size();
        } else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView mTv1;
        private final TextView mTv2;
        private final TextView mTv3;
        private final TextView mTv4;
        private final View mSelectedBarView;
        private final View mLayout;


        ViewHolder(View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.PI_main_ly);

            mTv1 = itemView.findViewById(R.id.PI_tv1);
            mTv2 = itemView.findViewById(R.id.PI_tv2);
            mTv3 = itemView.findViewById(R.id.PI_tv3);
            mTv4 = itemView.findViewById(R.id.PI_tv4);

            mSelectedBarView = itemView.findViewById(R.id.PI_selected_bar);

        }

    }

    public interface PendingJobsAdapterListener {

        void onPandingJobSelected(PandingJob pandingJob);
    }
}
