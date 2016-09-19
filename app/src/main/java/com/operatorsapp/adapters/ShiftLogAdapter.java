package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operators.shiftloginfra.Event;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;

public class ShiftLogAdapter extends RecyclerView.Adapter {

//    public static final int DAY_MILLIS = 86400000;

    private Context mContext;
    private ArrayList<Event> mEvents;
    private boolean mClosedState;
    private int mPanelWidth;

    private final int PARAMETER = 1;
    private final int STOPPED = 2;
    private OnStopClickListener mOnStopClickListener;

    public ShiftLogAdapter(Context context, ArrayList<Event> events, boolean closedState, int panelWidth, OnStopClickListener onStopClickListener) {
        mEvents = events;
        mContext = context;
        mClosedState = closedState;
        mPanelWidth = panelWidth;
        mOnStopClickListener = onStopClickListener;
    }

    public void setNewData(ArrayList<Event> events) {
        mEvents = events;
        notifyDataSetChanged();
    }

    public void changeState(boolean closedState) {
        mClosedState = closedState;
    }

    private class ShiftLogStoppedViewHolder extends RecyclerView.ViewHolder {

        private AutofitTextView mTitle;
        private ImageView mIcon;
        private TextView mStart;
        private TextView mStartDate;
        private TextView mDuration;
        private TextView mEnd;
        private TextView mEndDate;
        private TextView mTime;
        private View mDivider;
        private View mBottomDivider;
        private LinearLayout mSubtitle;

        public ShiftLogStoppedViewHolder(View itemView) {
            super(itemView);
            mTitle = (AutofitTextView) itemView.findViewById(R.id.shift_log_item_title);
            mIcon = (ImageView) itemView.findViewById(R.id.shift_log_item_icon);
            mTime = (TextView) itemView.findViewById(R.id.shift_log_item_time);
            mStart = (TextView) itemView.findViewById(R.id.shift_log_item_start);
            mStartDate = (TextView) itemView.findViewById(R.id.shift_log_item_start_date);
            mDuration = (TextView) itemView.findViewById(R.id.shift_log_item_duration);
            mEnd = (TextView) itemView.findViewById(R.id.shift_log_item_end);
            mEndDate = (TextView) itemView.findViewById(R.id.shift_log_item_end_date);
            mDivider = itemView.findViewById(R.id.shift_log_divider);
            mBottomDivider = itemView.findViewById(R.id.shift_log_bottom_divider);
            mSubtitle = (LinearLayout) itemView.findViewById(R.id.shift_log_item_subtitle);
        }
    }

    private class ShiftLogParameterViewHolder extends RecyclerView.ViewHolder {

        private AutofitTextView mTitle;
        private ImageView mIcon;
        private TextView mSubtitleText;
        private TextView mSubTitleValue;
        private TextView mStandard;
        private TextView mMin;
        private TextView mMax;
        private TextView mTime;
        private View mDivider;
        private LinearLayout mSubtitle;
        private View mBottomDivider;

        public ShiftLogParameterViewHolder(View itemView) {
            super(itemView);
            mTitle = (AutofitTextView) itemView.findViewById(R.id.shift_log_item_title);
            mIcon = (ImageView) itemView.findViewById(R.id.shift_log_item_icon);
            mSubtitleText = (TextView) itemView.findViewById(R.id.shift_log_item_subtitle_text);
            mSubTitleValue = (TextView) itemView.findViewById(R.id.shift_log_item_subtitle_value);
            mStandard = (TextView) itemView.findViewById(R.id.shift_log_item_standard);
            mMax = (TextView) itemView.findViewById(R.id.shift_log_item_max);
            mMin = (TextView) itemView.findViewById(R.id.shift_log_item_min);
            mTime = (TextView) itemView.findViewById(R.id.shift_log_item_time);
            mDivider = itemView.findViewById(R.id.shift_log_divider);
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
                    shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                    shiftLogStoppedViewHolder.mTime.setTypeface(null, Typeface.BOLD);
                }
            } else {
                shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_grey);
                shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                shiftLogStoppedViewHolder.mTime.setTypeface(null, Typeface.NORMAL);
            }
            shiftLogStoppedViewHolder.mTime.setText(TimeUtils.getTimeFromString(event.getTime()));
            shiftLogStoppedViewHolder.mTitle.setText(event.getEventGroupLname());
            shiftLogStoppedViewHolder.mStart.setText(TimeUtils.getTimeFromString(event.getTime()));
            shiftLogStoppedViewHolder.mStartDate.setText(TimeUtils.getDateFromString(event.getTime()));
            shiftLogStoppedViewHolder.mEnd.setText(TimeUtils.getTimeFromString(event.getEndTime()));
            shiftLogStoppedViewHolder.mEndDate.setText(TimeUtils.getDateFromString(event.getEndTime()));
            shiftLogStoppedViewHolder.mDuration.setText(TimeUtils.getDurationTime(mContext, event.getDuration()));
            if (mClosedState) {
                shiftLogStoppedViewHolder.mDivider.setVisibility(View.GONE);
                shiftLogStoppedViewHolder.mSubtitle.setVisibility(View.INVISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogStoppedViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = (int) (mPanelWidth * 0.4);
                shiftLogStoppedViewHolder.mBottomDivider.requestLayout();
            } else {
                shiftLogStoppedViewHolder.mDivider.setVisibility(View.VISIBLE);
                shiftLogStoppedViewHolder.mSubtitle.setVisibility(View.VISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogStoppedViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                shiftLogStoppedViewHolder.mBottomDivider.requestLayout();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setTag(true);
                    if (!event.isTreated()) {
                        shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_grey);
                        shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                        shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                        shiftLogStoppedViewHolder.mTime.setTypeface(null, Typeface.NORMAL);
                        mOnStopClickListener.onStopClicked(event.getEventID(), event.getTime(), event.getEndTime(), event.getDuration());

                        event.setTreated(true);
                    } else {
                        if (event.getPriority() == 1) {
                            shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_red);
                            shiftLogStoppedViewHolder.mTitle.setTextColor(Color.RED);
                            shiftLogStoppedViewHolder.mTime.setTextColor(Color.RED);
                        } else {
                            shiftLogStoppedViewHolder.mIcon.setImageResource(R.drawable.ic_hand_blue);
                            shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                            shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                        }
                        shiftLogStoppedViewHolder.mTime.setTypeface(null, Typeface.BOLD);
                        event.setTreated(false);
                    }
                    PersistenceManager.getInstance().saveShiftLogs(mEvents);
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
                    shiftLogParameterViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                    shiftLogParameterViewHolder.mTime.setTypeface(null, Typeface.BOLD);
                }
            } else {
                shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_grey);
                shiftLogParameterViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                shiftLogParameterViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                shiftLogParameterViewHolder.mTime.setTypeface(null, Typeface.NORMAL);
            }
            shiftLogParameterViewHolder.mTitle.setText(event.getEventGroupLname());
            shiftLogParameterViewHolder.mTime.setText(TimeUtils.getTimeFromString(event.getTime()));
            shiftLogParameterViewHolder.mSubtitleText.setText(event.getSubtitleEname());
            shiftLogParameterViewHolder.mSubTitleValue.setText(String.valueOf(event.getAlarmValue()));
            shiftLogParameterViewHolder.mMin.setText(new StringBuilder(mContext.getString(R.string.shift_log_min)).append(mContext.getString(R.string.space)).append(String.valueOf(event.getAlarmLValue())));
            shiftLogParameterViewHolder.mMax.setText(new StringBuilder(mContext.getString(R.string.shift_log_max)).append(mContext.getString(R.string.space)).append(String.valueOf(event.getAlarmHValue())));
            shiftLogParameterViewHolder.mStandard.setText(new StringBuilder(mContext.getString(R.string.shift_log_standard)).append(mContext.getString(R.string.space)).append(String.valueOf(event.getAlarmStandardValue())));
            if (mClosedState) {
                shiftLogParameterViewHolder.mDivider.setVisibility(View.GONE);
                shiftLogParameterViewHolder.mSubtitle.setVisibility(View.INVISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogParameterViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = (int) (mPanelWidth * 0.4);
                shiftLogParameterViewHolder.mBottomDivider.requestLayout();
            } else {
                shiftLogParameterViewHolder.mDivider.setVisibility(View.VISIBLE);
                shiftLogParameterViewHolder.mSubtitle.setVisibility(View.VISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogParameterViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                shiftLogParameterViewHolder.mBottomDivider.requestLayout();

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!event.isTreated()) {
                        shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_grey);
                        shiftLogParameterViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                        shiftLogParameterViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                        shiftLogParameterViewHolder.mTime.setTypeface(null, Typeface.NORMAL);
                        event.setTreated(true);
                    } else {
                        if (event.getPriority() == 1) {
                            shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_red);
                            shiftLogParameterViewHolder.mTitle.setTextColor(Color.RED);
                            shiftLogParameterViewHolder.mTime.setTextColor(Color.RED);
                        } else {
                            shiftLogParameterViewHolder.mIcon.setImageResource(R.drawable.ic_sun_blue);
                            shiftLogParameterViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                            shiftLogParameterViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                        }
                        shiftLogParameterViewHolder.mTime.setTypeface(null, Typeface.BOLD);
                        event.setTreated(false);
                    }
                    PersistenceManager.getInstance().saveShiftLogs(mEvents);
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
        int type;
        switch (event.getEventGroupID()) {
            case 20:
                type = PARAMETER;
                break;

            case 6:
                type = STOPPED;
                break;

            default:
                type = PARAMETER;
                break;
        }

        return type;

    }
}
