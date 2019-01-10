package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by alex on 07/01/2019.
 */

public class TechCallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<TechCallInfo> mTechList;
    private final Context mContext;
    private final TechCallItemListener mListener;

    public TechCallAdapter(Context context, ArrayList<TechCallInfo> mTechList, TechCallItemListener listener) {
        mContext = context;
        this.mTechList = mTechList;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tech_call_recycler_item, parent, false);

        return new TechViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final TechViewHolder techViewHolder = (TechViewHolder) holder;
        techViewHolder.mTextTv.setText(mTechList.get(position).getmName());
        techViewHolder.mTimeTv.setText(TimeUtils.getDate(mTechList.get(position).getmCallTime(), TimeUtils.COMMON_DATE_FORMAT));
        techViewHolder.mRemoveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRemoveCallPressed(mTechList.get(position));
            }
        });

        int icon = R.drawable.technician_blue;
        switch (mTechList.get(position).getmResponseType()){

            case Consts.NOTIFICATION_RESPONSE_TYPE_UNSET:
                icon = R.drawable.call_sent_blue;
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                icon = R.drawable.call_recieved;
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                icon = R.drawable.call_declined;
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                icon = R.drawable.cancel_blue;
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                icon = R.drawable.at_work_blue;
                break;
            case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                icon = R.drawable.service_done;
                break;
        }
        techViewHolder.mStatusIv.setImageResource(icon);
    }

    @Override
    public int getItemCount() {
        return mTechList.size();
    }

    public class TechViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTv;
        private TextView mTimeTv;
        private ImageView mRemoveIv;
        private ImageView mStatusIv;

        public TechViewHolder(View v) {
            super(v);
            mRemoveIv = itemView.findViewById(R.id.tech_call_item_remove_iv);
            mTextTv = itemView.findViewById(R.id.tech_call_item_text_tv);
            mTimeTv = itemView.findViewById(R.id.tech_call_item_time_tv);
            mStatusIv = itemView.findViewById(R.id.tech_call_item_status_iv);
        }
    }

    public interface TechCallItemListener{
        void onRemoveCallPressed(TechCallInfo techCallInfo);
    }
}
