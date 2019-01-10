package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.StopReasons;
import com.operators.reportfieldsformachineinfra.SubReasons;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.interfaces.OnStopReasonSelectedCallbackListener;
import com.operatorsapp.utils.ReasonImage;
import com.operatorsapp.utils.ReasonImageLenox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by alex on 26/12/2018.
 */

public class NewStopReasonsAdapter extends RecyclerView.Adapter<NewStopReasonsAdapter.ViewHolder> {

    private ArrayList<StopReasons> mStopItemsList;
    private Context mContext;
    private OnStopReasonSelectedCallbackListener mOnStopReasonSelectedCallbackListener;

    public NewStopReasonsAdapter(Context context, List<StopReasons> stopItemsList, OnStopReasonSelectedCallbackListener onStopReasonSelectedCallbackListener) {
        mStopItemsList = (ArrayList<StopReasons>) stopItemsList;
        mContext = context;
        mOnStopReasonSelectedCallbackListener = onStopReasonSelectedCallbackListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_report_horizontal_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String nameByLang = OperatorApplication.isEnglishLang() ? mStopItemsList.get(position).getEName() : mStopItemsList.get(position).getLName();
        holder.mStopTitle.setText(nameByLang);
        int imgId;
        if (BuildConfig.FLAVOR.equals(mContext.getString(R.string.lenox_flavor_name))) {
            imgId = ReasonImageLenox.getImageForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getId());
        } else {
            imgId = ReasonImage.getImageForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getId());
        }
        holder.mTitleLil.setBackgroundColor(ReasonImage.getColorForStopReason(mStopItemsList.get(holder.getAdapterPosition()).getId()));
        holder.mReasonImage.setBackground(mContext.getResources().getDrawable(imgId));
        holder.mHorizontalRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false));
        holder.mHorizontalRv.setAdapter(new VerticalAdapter(mStopItemsList.get(position).getSubReasons(), imgId));
        int color = new Random().nextInt(4);
        Color.parseColor(mContext.getResources().getStringArray(R.array.color_array)[color]);
       // holder.mTitleLil.setBackgroundColor(Color.parseColor(mContext.getResources().getStringArray(R.array.color_array)[color]));
    }

    @Override
    public int getItemCount() {
        return mStopItemsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout mTitleLil;
        private RecyclerView mHorizontalRv;
        private TextView mStopTitle;
        private ImageView mReasonImage;

        public ViewHolder(View view) {
            super(view);
            mStopTitle = view.findViewById(R.id.report_horizontal_tv);
            mReasonImage = view.findViewById(R.id.report_horizontal_iv);
            mHorizontalRv = view.findViewById(R.id.report_vertical_rv);
            mTitleLil = view.findViewById(R.id.report_horizontal_lil);
        }
    }



    /****  Vertical Adapter  ****/

    private class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder> {
        private final List<SubReasons> mVerticalList;
        private final int mImgId;

        public VerticalAdapter(List<SubReasons> subReasons, int imgId) {
            mVerticalList = subReasons;
            mImgId = imgId;
        }

        @NonNull
        @Override
        public VerticalAdapter.VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.stop_report_vertical_item, parent, false);

            return new VerticalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VerticalAdapter.VerticalViewHolder holder, final int position) {
            String title = OperatorApplication.isEnglishLang() ? mVerticalList.get(position).getEName() : mVerticalList.get(position).getLName();
            holder.mVerticalTitle.setText(title);
            holder.mVerticalImage.setBackground(mContext.getResources().getDrawable(mImgId));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnStopReasonSelectedCallbackListener.onSubReasonSelected(mVerticalList.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mVerticalList.size();
        }

        public class VerticalViewHolder extends RecyclerView.ViewHolder {
            private final TextView mVerticalTitle;
            private final ImageView mVerticalImage;

            public VerticalViewHolder(View view) {
                super(view);
                mVerticalTitle = view.findViewById(R.id.report_vertical_tv);
                mVerticalImage = view.findViewById(R.id.report_vertical_iv);

            }
        }
    }
}