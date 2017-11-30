package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.StopReasons;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.utils.ReasonImage;

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

    @Override
    public StopReasonsAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {

        String nameByLang = OperatorApplication.isEnglishLang() ? mStopItemsList.get(position).getEName() : mStopItemsList.get(position).getLName();

        holder.mStopTitle.setText(nameByLang);

        holder.mReasonImage.setBackground(mContext.getDrawable(ReasonImage.getImageForStopReason(mStopItemsList.get(position).getId())));
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
            mStopTitle = (TextView) view.findViewById(R.id.stop_reason_title);
            mReasonImage = (ImageView) view.findViewById(R.id.grid_reason_image_view);
        }
    }

}
