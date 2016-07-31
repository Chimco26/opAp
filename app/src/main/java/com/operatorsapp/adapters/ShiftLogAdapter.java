package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.shiftloginfra.Event;
import com.operatorsapp.R;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

public class ShiftLogAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Event> mEvents;
    private boolean mClosedState;

    private final int PARAMETER = 1;
    private final int STOPPED = 2;

    public ShiftLogAdapter(Context context, ArrayList<Event> events, boolean closedState) {
        mEvents = events;
        mContext = context;
        mClosedState = closedState;
    }

    public void changeState(boolean closedState) {
        mClosedState = closedState;
    }

    private class ShiftLogStoppedViewHolder extends RecyclerView.ViewHolder {

        private AutofitTextView mTitle;
        private ImageView mIcon;
        private TextView mSubtitle;
        private TextView mTime;
        private View mDivider;
        private TextView mPlease;
        private View mBottomDivider;

        public ShiftLogStoppedViewHolder(View itemView) {
            super(itemView);
            mTitle = (AutofitTextView) itemView.findViewById(R.id.shift_log_item_title);
            mIcon = (ImageView) itemView.findViewById(R.id.shift_log_item_icon);
            mSubtitle = (TextView) itemView.findViewById(R.id.shift_log_item_subtitle);
            mTime = (TextView) itemView.findViewById(R.id.shift_log_item_time);
            mDivider = itemView.findViewById(R.id.shift_log_divider);
            mPlease = (TextView) itemView.findViewById(R.id.shift_log_please);
            mBottomDivider = itemView.findViewById(R.id.shift_log_bottom_divider);
        }
    }

    private class ShiftLogParameterViewHolder extends RecyclerView.ViewHolder {

        private AutofitTextView mTitle;
        private ImageView mIcon;
        private TextView mSubtitleStart;
        private TextView mSubtitleEnd;
        private TextView mTime;
        private View mDivider;
        private TextView mPlease;
        private LinearLayout mSubtitle;
        private View mBottomDivider;

        public ShiftLogParameterViewHolder(View itemView) {
            super(itemView);
            mTitle = (AutofitTextView) itemView.findViewById(R.id.shift_log_item_title);
            mIcon = (ImageView) itemView.findViewById(R.id.shift_log_item_icon);
            mSubtitleStart = (TextView) itemView.findViewById(R.id.shift_log_item_subtitle_start);
            mSubtitleEnd = (TextView) itemView.findViewById(R.id.shift_log_item_subtitle_end);
            mTime = (TextView) itemView.findViewById(R.id.shift_log_item_time);
            mDivider = itemView.findViewById(R.id.shift_log_divider);
            mPlease = (TextView) itemView.findViewById(R.id.shift_log_please);
            mSubtitle = (LinearLayout) itemView.findViewById(R.id.shift_log_item_subtitle);
            mBottomDivider = itemView.findViewById(R.id.shift_log_bottom_divider);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case STOPPED: {
                return new ShiftLogStoppedViewHolder(inflater.inflate(R.layout.event_stopped_cardview, parent, false));
            }
            case PARAMETER: {
                return new ShiftLogParameterViewHolder(inflater.inflate(R.layout.event_parameter_cardview, parent, false));
            }
        }
        return new ShiftLogStoppedViewHolder(inflater.inflate(R.layout.event_stopped_cardview, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Event event = mEvents.get(position);

        int type = getItemViewType(position);

        if (type == STOPPED) {
            final ShiftLogStoppedViewHolder shiftLogStoppedViewHolder = (ShiftLogStoppedViewHolder) holder;

            if (!event.isTreated()) {
                if (event.getPriority() == 1) {
                    shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_red);
                    shiftLogStoppedViewHolder.mTitle.setTextColor(Color.RED);
                    shiftLogStoppedViewHolder.mTime.setTextColor(Color.RED);
                } else {
                    shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_blue);
                    shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                    shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                }
            } else {
                shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_grey);
                shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
            }
            shiftLogStoppedViewHolder.mTitle.setText(event.getTitle());
            shiftLogStoppedViewHolder.mSubtitle.setText(event.getSubtitleL());
            if (mClosedState) {
                shiftLogStoppedViewHolder.mDivider.setVisibility(View.GONE);
                shiftLogStoppedViewHolder.mSubtitle.setVisibility(View.INVISIBLE);
                shiftLogStoppedViewHolder.mPlease.setVisibility(View.GONE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogStoppedViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = 105;
                shiftLogStoppedViewHolder.mBottomDivider.requestLayout();
            } else {
                shiftLogStoppedViewHolder.mDivider.setVisibility(View.VISIBLE);
                shiftLogStoppedViewHolder.mSubtitle.setVisibility(View.VISIBLE);
                shiftLogStoppedViewHolder.mPlease.setVisibility(View.VISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogStoppedViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                shiftLogStoppedViewHolder.mBottomDivider.requestLayout();
            }
//        int minutes = (int) ((event.getTimestamp() / (1000 * 60)) % 60);
//        int hours = (int) ((event.getTimestamp() / (1000 * 60 * 60)) % 24);
            shiftLogStoppedViewHolder.mTime.setText(event.getTime()/*hours + ":" + minutes*/);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setTag(true);
                    if (!event.isTreated()) {
                        shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_grey);
                        shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                        shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                        event.setTreated(true);
                    } else {
                        if (event.getPriority() == 1) {
                            shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_red);
                            shiftLogStoppedViewHolder.mTitle.setTextColor(Color.RED);
                            shiftLogStoppedViewHolder.mTime.setTextColor(Color.RED);
                        } else {
                            shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_blue);
                            shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                            shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                        }
                        event.setTreated(false);
                    }
                }
            });
        } else if (type == PARAMETER) {
            final ShiftLogParameterViewHolder shiftLogParameterViewHolder = (ShiftLogParameterViewHolder) holder;

            if (!event.isTreated()) {
                if (event.getPriority() == 1) {
                    shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_red);
                    shiftLogParameterViewHolder.mTitle.setTextColor(Color.RED);
                    shiftLogParameterViewHolder.mTime.setTextColor(Color.RED);
                } else {
                    shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_blue);
                    shiftLogParameterViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                    shiftLogParameterViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                }
            } else {
                shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_grey);
                shiftLogParameterViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                shiftLogParameterViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
            }
            shiftLogParameterViewHolder.mTitle.setText(event.getTitle());

            shiftLogParameterViewHolder.mSubtitleStart.setText(new StringBuilder("Start " + event.getStartTime()));
            shiftLogParameterViewHolder.mSubtitleEnd.setText(new StringBuilder("End " + event.getEndTime()));
            if (mClosedState) {
                shiftLogParameterViewHolder.mDivider.setVisibility(View.GONE);
                shiftLogParameterViewHolder.mSubtitleStart.setVisibility(View.INVISIBLE);
                shiftLogParameterViewHolder.mSubtitleEnd.setVisibility(View.INVISIBLE);
                shiftLogParameterViewHolder.mPlease.setVisibility(View.GONE);
                shiftLogParameterViewHolder.mSubtitle.setVisibility(View.GONE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogParameterViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = 105;
                shiftLogParameterViewHolder.mBottomDivider.requestLayout();
            } else {
                shiftLogParameterViewHolder.mDivider.setVisibility(View.VISIBLE);
                shiftLogParameterViewHolder.mSubtitleStart.setVisibility(View.VISIBLE);
                shiftLogParameterViewHolder.mSubtitleEnd.setVisibility(View.VISIBLE);
                shiftLogParameterViewHolder.mPlease.setVisibility(View.VISIBLE);
                shiftLogParameterViewHolder.mSubtitle.setVisibility(View.VISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogParameterViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                shiftLogParameterViewHolder.mBottomDivider.requestLayout();

            }
//        int minutes = (int) ((event.getTimestamp() / (1000 * 60)) % 60);
//        int hours = (int) ((event.getTimestamp() / (1000 * 60 * 60)) % 24);
            shiftLogParameterViewHolder.mTime.setText(event.getTime()/*hours + ":" + minutes*/);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!event.isTreated()) {
                        shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_grey);
                        shiftLogParameterViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                        shiftLogParameterViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                        event.setTreated(true);
                    } else {
                        if (event.getPriority() == 1) {
                            shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_red);
                            shiftLogParameterViewHolder.mTitle.setTextColor(Color.RED);
                            shiftLogParameterViewHolder.mTime.setTextColor(Color.RED);
                        } else {
                            shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_blue);
                            shiftLogParameterViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                            shiftLogParameterViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                        }
                        event.setTreated(false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    @Override
    public int getItemViewType(int position) {
        Event event = mEvents.get(position);
        if (event.getType() == 1) {
            return PARAMETER;
        }
        return STOPPED;
    }

}
