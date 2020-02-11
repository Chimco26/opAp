package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingJobsAdapter extends RecyclerView.Adapter<PendingJobsAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<PendingJob> mPandingjobs;
    private final HashMap<String, Header> mHashMapHeaders;

    private PendingJobsAdapterListener mListener;


    public PendingJobsAdapter(ArrayList<PendingJob> list, HashMap<String, Header> hashMapHeaders, PendingJobsAdapterListener listener, Context context) {

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

        ArrayList<Property> properties = (ArrayList<Property>) mPandingjobs.get(viewHolder.getAdapterPosition()).getProperties();

        if (mPandingjobs.get(viewHolder.getAdapterPosition()).isSelected()) {

            viewHolder.mSelectedBarView.setVisibility(View.VISIBLE);
            viewHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        } else {

            viewHolder.mSelectedBarView.setVisibility(View.INVISIBLE);
            viewHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.transparentColor));
        }

        if (properties.size() > 0) {
            initPropertyView(properties, viewHolder.mTv1, 0, viewHolder.mTv1Value);
        }
        if (properties.size() > 1) {
            initPropertyView(properties, viewHolder.mTv2, 1, viewHolder.mTv2Value);
        }
        if (properties.size() > 2) {
            initPropertyView(properties, viewHolder.mTv3, 2, viewHolder.mTv3Value);
        }
        if (properties.size() > 3) {
            initPropertyView(properties, viewHolder.mTv4, 3, viewHolder.mTv4Value);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mPandingjobs.get(position).isSelected()) {

                    resetSelectedItem();
                    mPandingjobs.get(position).setSelected(true);

                    updateView(viewHolder, mPandingjobs.get(position).isSelected());

                    notifyDataSetChanged();
                    mListener.onPendingJobSelected(mPandingjobs.get(position));
                }

            }
        });

    }

    public void initPropertyView(ArrayList<Property> properties, TextView mTv4, int i, TextView mTv4Value) {
        mTv4.setText(String.format("%s: ", mHashMapHeaders.get(properties.get(i).getKey()).getDisplayName()));
        mTv4.setTextColor(Color.parseColor(mHashMapHeaders.get(properties.get(i).getKey()).getColor().replace("\t", "")));
        mTv4Value.setText(properties.get(i).getValue());
        if (properties.get(i).getValue() != null) {
            if (properties.get(i).getValue().length() > 15) {
                mTv4Value.setSelected(true);
            } else {
                mTv4Value.setSelected(false);
            }
        }
    }


    private void resetSelectedItem() {

        for (PendingJob pendingJob : mPandingjobs) {

            pendingJob.setSelected(false);
        }
    }

    private void updateView(ViewHolder viewHolder, boolean selected) {

        if (selected) {

            viewHolder.mSelectedBarView.setVisibility(View.VISIBLE);
            viewHolder.mLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        } else {

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
        private final TextView mTv1Value;
        private final TextView mTv2Value;
        private final TextView mTv3Value;
        private final TextView mTv4Value;


        ViewHolder(View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.PI_main_ly);

            mTv1 = itemView.findViewById(R.id.PI_tv1);
            mTv2 = itemView.findViewById(R.id.PI_tv2);
            mTv3 = itemView.findViewById(R.id.PI_tv3);
            mTv4 = itemView.findViewById(R.id.PI_tv4);

            mTv1Value = itemView.findViewById(R.id.PI_tv1_value);
            mTv2Value = itemView.findViewById(R.id.PI_tv2_value);
            mTv3Value = itemView.findViewById(R.id.PI_tv3_value);
            mTv4Value = itemView.findViewById(R.id.PI_tv4_value);

            mSelectedBarView = itemView.findViewById(R.id.PI_selected_bar);

        }

    }

    public interface PendingJobsAdapterListener {

        void onPendingJobSelected(PendingJob pendingJob);
    }
}
