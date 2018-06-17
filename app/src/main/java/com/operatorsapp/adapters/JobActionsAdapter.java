package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.operatorsapp.R;
import com.operatorsapp.model.GalleryModel;

import java.util.ArrayList;

public class JobActionsAdapter extends RecyclerView.Adapter<JobActionsAdapter.ViewHolder> {

    private final Context mContext;

    private JobActionsAdapterListener mListener;


    public JobActionsAdapter(ArrayList<String> list, JobActionsAdapterListener listener, Context context) {

        mListener = listener;

        mContext = context;
    }


    @NonNull
    @Override
    public JobActionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new JobActionsAdapter.ViewHolder(inflater.inflate(R.layout.item_job_action_actions, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final JobActionsAdapter.ViewHolder viewHolder, final int position) {


    }

    @Override
    public int getItemCount() {
//        if (mGalleryModels != null) {
//            return mGalleryModels.size();
//        } else
 return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox mCheckBox;

        private final TextView mTv;

        ViewHolder(View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.IJAA_checkbox);

            mTv = itemView.findViewById(R.id.IJAA_tv);

        }

    }

    public interface JobActionsAdapterListener {

    }
}
