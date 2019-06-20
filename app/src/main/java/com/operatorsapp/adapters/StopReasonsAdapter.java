package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.StopReasons;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.utils.ReasonImage;
import com.operatorsapp.utils.ReasonImageLenox;

import java.util.List;

public class StopReasonsAdapter extends RecyclerView.Adapter<StopReasonsAdapter.ViewHolder> {

    private List<StopReasons> mStopItemsList;
    private Context mContext;
    private OnStopReasonSelectedCallbackListener mOnStopReasonSelectedCallbackListener;

    public StopReasonsAdapter(Context context, List<StopReasons> stopItemsList, OnStopReasonSelectedCallbackListener onStopReasonSelectedCallbackListener) {
        mStopItemsList = stopItemsList;
        mContext = context;
        mOnStopReasonSelectedCallbackListener = onStopReasonSelectedCallbackListener;
    }

    @NonNull
    @Override
    public StopReasonsAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_report_grid_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnStopReasonSelectedCallbackListener.onStopReasonSelected(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String nameByLang = OperatorApplication.isEnglishLang() ? mStopItemsList.get(holder.getAdapterPosition()).getEName() : mStopItemsList.get(holder.getAdapterPosition()).getLName();

        holder.mStopTitle.setText(nameByLang);

        if (BuildConfig.FLAVOR.equals(mContext.getString(R.string.lenox_flavor_name))) {
            holder.mReasonImage.setBackground(mContext.getResources().getDrawable(ReasonImageLenox.getImageForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getId())));
        } else {

            holder.mReasonImage.setBackground(mContext.getResources().getDrawable(mStopItemsList.get(position).getGroupIcon(mContext)));
//            holder.mReasonImage.setBackground(mContext.getResources().getDrawable(ReasonImage.getImageForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getId())));
//            holder.mReasonImage.setBackground(mContext.getResources().getDrawable(ReasonImage.getImageForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getEventGroupIconID())));
        }
    }

    @Override
    public int getItemCount() {
        return mStopItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mStopTitle;
        private ImageView mReasonImage;

        public ViewHolder(View view) {
            super(view);
            mStopTitle = view.findViewById(R.id.stop_reason_title);
            mReasonImage = view.findViewById(R.id.grid_reason_image_view);
        }
    }

}
