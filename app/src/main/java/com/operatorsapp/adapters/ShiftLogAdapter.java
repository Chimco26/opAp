package com.operatorsapp.adapters;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.model.ShiftLog;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

public class ShiftLogAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<ShiftLog> mShiftLogs;
    private boolean mClosedState;
    private float mAlpha;

    public ShiftLogAdapter(Context context, ArrayList<ShiftLog> shiftLogs, boolean closedState) {
        mShiftLogs = shiftLogs;
        mContext = context;
        mClosedState = closedState;
    }

    public void changeState(boolean closedState) {
        mClosedState = closedState;
    }

    public void changeAlpha(float alpha) {
        mAlpha = alpha;
    }

    private class ShiftLogViewHolder extends RecyclerView.ViewHolder {

        private View mPriority;
        private AutofitTextView mTitle;
        private ImageView mIcon;
        private TextView mSubtitle;
        private TextView mTime;

        public ShiftLogViewHolder(View itemView) {
            super(itemView);
            mPriority = itemView.findViewById(R.id.shift_log_item_priority);
            mTitle = (AutofitTextView) itemView.findViewById(R.id.shift_log_item_title);
            mIcon = (ImageView) itemView.findViewById(R.id.shift_log_item_icon);
            mSubtitle = (TextView) itemView.findViewById(R.id.shift_log_item_subtitle);
            mTime = (TextView) itemView.findViewById(R.id.shift_log_item_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ShiftLogViewHolder(inflater.inflate(R.layout.shift_log_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ShiftLog shiftLog = mShiftLogs.get(position);
        final ShiftLogViewHolder shiftLogViewHolder = (ShiftLogViewHolder) holder;

        if (shiftLog.isPriority()) {
            shiftLogViewHolder.mPriority.setBackgroundResource(R.color.C3);
        } else {
            shiftLogViewHolder.mPriority.setBackgroundResource(R.color.white);
        }
        shiftLogViewHolder.mTitle.setText(shiftLog.getTitle());
        shiftLogViewHolder.mIcon.setImageResource(shiftLog.getIcon());
        shiftLogViewHolder.mSubtitle.setText(shiftLog.getSubtitle());
        shiftLogViewHolder.mSubtitle.setAlpha(mAlpha);
        if (mClosedState) {
            shiftLogViewHolder.mSubtitle.setVisibility(View.INVISIBLE);
        } else {
            shiftLogViewHolder.mSubtitle.setVisibility(View.VISIBLE);
        }
        shiftLogViewHolder.mTime.setText(shiftLog.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mShiftLogs.size();
    }
}
