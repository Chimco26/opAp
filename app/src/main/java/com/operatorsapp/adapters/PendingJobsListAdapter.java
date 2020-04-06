package com.operatorsapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

    private static final String PRODUCT_IMAGE_PATH = "productimagepath";
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

        setImageOrText(propertiesHashMap, 0, viewHolder.m1Img, viewHolder.m1Tv);
        setImageOrText(propertiesHashMap, 1, viewHolder.m2Img, viewHolder.m2Tv);
        setImageOrText(propertiesHashMap, 2, viewHolder.m3Img, viewHolder.m3Tv);
        setImageOrText(propertiesHashMap, 3, viewHolder.m4Img, viewHolder.m4Tv);
        setImageOrText(propertiesHashMap, 4, viewHolder.m5Img, viewHolder.m5Tv);
        setImageOrText(propertiesHashMap, 5, viewHolder.m6Img, viewHolder.m6Tv);
        setImageOrText(propertiesHashMap, 6, viewHolder.m7Img, viewHolder.m7Tv);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onPendingJobSelected(mPandingjobs.get(position));

            }
        });
    }

    private void setImageOrText(HashMap<String, String> propertiesHashMap, int i, ImageView m1Img, TextView m1Tv) {
        if (mOrderedHederasKey[i] != null && mOrderedHederasKey[i].toLowerCase().equals(PRODUCT_IMAGE_PATH)) {
            ImageLoader.getInstance().displayImage(propertiesHashMap.get(mOrderedHederasKey[i]), m1Img);
            m1Tv.setText("");
        } else {
            ImageLoader.getInstance().displayImage("", m1Img);
            m1Tv.setText(getText(propertiesHashMap.get(mOrderedHederasKey[i])));
        }
    }

    private String getText(String s){
        try {
            float number = 0;
            number = Float.parseFloat(s);
            if (number >= 1000){
                int thousands = (int) (number / 1000);
                float rest = number - (thousands * 1000);
                return thousands + "," + rest;
            }
            return s;
        }catch (NullPointerException | NumberFormatException e){
            return s;
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
