package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingJobsListAdapter extends RecyclerView.Adapter<PendingJobsListAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<PendingJob> mPandingjobs;
    private final String[] mOrderedHederasKey;

    private PendingJobsAdapter.PendingJobsAdapterListener mListener;


    public PendingJobsListAdapter(ArrayList<PendingJob> list, String[] orderedHederasKey, PendingJobsAdapter.PendingJobsAdapterListener listener, Context context) {

        mListener = listener;
        mContext = context;
        mPandingjobs = list;
        mOrderedHederasKey = orderedHederasKey;
    }


    @NonNull
    @Override
    public PendingJobsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new PendingJobsListAdapter.ViewHolder(inflater.inflate(R.layout.item_job_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingJobsListAdapter.ViewHolder viewHolder, final int position) {

        ArrayList<Property> properties = (ArrayList<Property>) mPandingjobs.get(viewHolder.getAdapterPosition()).getProperties();
        HashMap<String, String> propertiesHashMap = listToHashMap(properties);

        if (mOrderedHederasKey[0] != null && mOrderedHederasKey[0].toLowerCase().equals("ProductImagePath".toLowerCase())) {
            ImageLoader.getInstance().displayImage(propertiesHashMap.get(mOrderedHederasKey[0]), viewHolder.m1Img);
        } else {
            viewHolder.m1Tv.setText(propertiesHashMap.get(mOrderedHederasKey[0]));
        }
        if (mOrderedHederasKey[1] != null && mOrderedHederasKey[1].toLowerCase().equals("ProductImagePath".toLowerCase())) {
            ImageLoader.getInstance().displayImage(propertiesHashMap.get(mOrderedHederasKey[1]), viewHolder.m2Img);
        } else {
            viewHolder.m2Tv.setText(propertiesHashMap.get(mOrderedHederasKey[1]));
        }
        if (mOrderedHederasKey[2] != null && mOrderedHederasKey[2].toLowerCase().equals("ProductImagePath".toLowerCase())) {
            ImageLoader.getInstance().displayImage(propertiesHashMap.get(mOrderedHederasKey[2]), viewHolder.m3Img);
        } else {
            viewHolder.m3Tv.setText(propertiesHashMap.get(mOrderedHederasKey[2]));
        }
        if (mOrderedHederasKey[3] != null && mOrderedHederasKey[3].toLowerCase().equals("ProductImagePath".toLowerCase())) {
            ImageLoader.getInstance().displayImage(propertiesHashMap.get(mOrderedHederasKey[3]), viewHolder.m4Img);
        } else {
            viewHolder.m4Tv.setText(propertiesHashMap.get(mOrderedHederasKey[3]));
        }
        if (mOrderedHederasKey[4] != null && mOrderedHederasKey[4].toLowerCase().equals("ProductImagePath".toLowerCase())) {
            ImageLoader.getInstance().displayImage(propertiesHashMap.get(mOrderedHederasKey[4]), viewHolder.m5Img);
        } else {
            viewHolder.m5Tv.setText(propertiesHashMap.get(mOrderedHederasKey[4]));
        }
        if (mOrderedHederasKey[5] != null && mOrderedHederasKey[5].toLowerCase().equals("ProductImagePath".toLowerCase())) {
            ImageLoader.getInstance().displayImage(propertiesHashMap.get(mOrderedHederasKey[5]), viewHolder.m6Img);
        } else {
            viewHolder.m6Tv.setText(propertiesHashMap.get(mOrderedHederasKey[5]));
        }
        if (mOrderedHederasKey[6] != null && mOrderedHederasKey[6].toLowerCase().equals("ProductImagePath".toLowerCase())) {
            ImageLoader.getInstance().displayImage(propertiesHashMap.get(mOrderedHederasKey[6]), viewHolder.m7Img);
        } else {
            viewHolder.m7Tv.setText(propertiesHashMap.get(mOrderedHederasKey[6]));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onPendingJobSelected(mPandingjobs.get(position));

            }
        });

        if (viewHolder.itemView.getContext().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            viewHolder.mArrow.setRotationY(180);
        }
    }

    private HashMap<String, String> listToHashMap(ArrayList<Property> properties) {

        HashMap<String, String> propertiesHashmap = new HashMap<>();

        for (Property property : properties) {

            propertiesHashmap.put(property.getKey(), property.getValue());
        }
        return propertiesHashmap;
    }

    @Override
    public int getItemCount() {
        if (mPandingjobs != null) {
            return mPandingjobs.size();
        } else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView m1Tv;
        private final TextView m2Tv;
        private final TextView m3Tv;
        private final TextView m4Tv;
        private final TextView m5Tv;
        private final TextView m6Tv;
        private final TextView m7Tv;
        private final ImageView m1Img;
        private final ImageView m2Img;
        private final ImageView m3Img;
        private final ImageView m4Img;
        private final ImageView m5Img;
        private final ImageView m6Img;
        private final ImageView m7Img;
        private final View mArrow;

        ViewHolder(View itemView) {
            super(itemView);

            m1Img = itemView.findViewById(R.id.FJL_img_1);
            m2Img = itemView.findViewById(R.id.FJL_img_2);
            m3Img = itemView.findViewById(R.id.FJL_img_3);
            m4Img = itemView.findViewById(R.id.FJL_img_4);
            m5Img = itemView.findViewById(R.id.FJL_img_5);
            m6Img = itemView.findViewById(R.id.FJL_img_6);
            m7Img = itemView.findViewById(R.id.FJL_img_7);
            m1Tv = itemView.findViewById(R.id.FJL_1);
            m2Tv = itemView.findViewById(R.id.FJL_2);
            m3Tv = itemView.findViewById(R.id.FJL_3);
            m4Tv = itemView.findViewById(R.id.FJL_4);
            m5Tv = itemView.findViewById(R.id.FJL_5);
            m6Tv = itemView.findViewById(R.id.FJL_6);
            m7Tv = itemView.findViewById(R.id.FJL_7);
            mArrow = itemView.findViewById(R.id.FJL_arrow);
        }

    }

    public interface PendingJobsAdapterNewListener {

        void onPendingJobSelected(PendingJob pendingJob);
    }
}
