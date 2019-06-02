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
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.PendingJob;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Property;
import com.operatorsapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.operatorsapp.fragments.JobListFragment.END_TIME;
import static com.operatorsapp.fragments.JobListFragment.ID;
import static com.operatorsapp.fragments.JobListFragment.PRODUCT_CATALOG_ID;
import static com.operatorsapp.fragments.JobListFragment.TIME_LEFT_HR_HOUR;
import static com.operatorsapp.fragments.JobListFragment.UNITS_PRODUCED;
import static com.operatorsapp.fragments.JobListFragment.UNITS_TARGET;

public class PendingJobsAdapterNew extends RecyclerView.Adapter<PendingJobsAdapterNew.ViewHolder> {

    private final Context mContext;
    private final ArrayList<PendingJob> mPandingjobs;
    private final HashMap<String, Header> mHashMapHeaders;

    private PendingJobsAdapter.PendingJobsAdapterListener mListener;


    public PendingJobsAdapterNew(ArrayList<PendingJob> list, HashMap<String, Header> hashMapHeaders, PendingJobsAdapter.PendingJobsAdapterListener listener, Context context) {

        mListener = listener;

        mContext = context;

        mPandingjobs = list;

        mHashMapHeaders = hashMapHeaders;
    }


    @NonNull
    @Override
    public PendingJobsAdapterNew.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new PendingJobsAdapterNew.ViewHolder(inflater.inflate(R.layout.item_job_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PendingJobsAdapterNew.ViewHolder viewHolder, final int position) {

        ArrayList<Property> properties = (ArrayList<Property>) mPandingjobs.get(viewHolder.getAdapterPosition()).getProperties();
        HashMap<String, String> propertiesHashMap = listToHashMap(properties);

        viewHolder.mIndexTv.setText(propertiesHashMap.get(ID));
        viewHolder.mCatalogTv.setText(propertiesHashMap.get(PRODUCT_CATALOG_ID));
        viewHolder.mTargetTv.setText(propertiesHashMap.get(UNITS_TARGET));
        viewHolder.mProducedTv.setText(propertiesHashMap.get(UNITS_PRODUCED));
        viewHolder.mEndTimeTv.setText(propertiesHashMap.get(END_TIME));
        viewHolder.mJobLeftTv.setText(propertiesHashMap.get(TIME_LEFT_HR_HOUR));

        ImageLoader.getInstance().displayImage("", viewHolder.mImage);

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

    private int getHeaderValue(HashMap<String, String> propertiesHashMap) {
        return 0;
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


        private final TextView mIndexTv;
        private final TextView mCatalogTv;
        private final TextView mTargetTv;
        private final TextView mProducedTv;
        private final TextView mEndTimeTv;
        private final TextView mJobLeftTv;
        private final ImageView mImage;
        private final View mArrow;

        ViewHolder(View itemView) {
            super(itemView);

            mIndexTv = itemView.findViewById(R.id.FJL_index);
            mCatalogTv = itemView.findViewById(R.id.FJL_catalog);
            mTargetTv = itemView.findViewById(R.id.FJL_target);
            mProducedTv = itemView.findViewById(R.id.FJL_produced);
            mEndTimeTv = itemView.findViewById(R.id.FJL_end_time);
            mJobLeftTv = itemView.findViewById(R.id.FJL_job_left);
            mImage = itemView.findViewById(R.id.FJL_image);
            mArrow = itemView.findViewById(R.id.FJL_arrow);
        }

    }

    public interface PendingJobsAdapterNewListener {

        void onPendingJobSelected(PendingJob pendingJob);
    }
}
