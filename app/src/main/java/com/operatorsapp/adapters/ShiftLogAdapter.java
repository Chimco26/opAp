package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.shiftloginfra.Event;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.ReasonImage;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import me.grantland.widget.AutofitTextView;

public class ShiftLogAdapter extends RecyclerView.Adapter {

//    public static final int DAY_MILLIS = 86400000;

    private Context mContext;
    private ArrayList<Event> mEvents;
    private boolean mClosedState;
    private int mCloseWidth;
    private int mOpenWidth;
    private int mHeight;

    private HashMap<Integer, Integer> mEventId = new HashMap();

    private final int PARAMETER = 1;
    private final int STOPPED = 2;
    private OnStopClickListener mOnStopClickListener;

    public ShiftLogAdapter(Context context, ArrayList<Event> events, boolean closedState, int closeWidth, OnStopClickListener onStopClickListener, int openWidth, int height) {
        mEvents = events;
        mContext = context;
        mClosedState = closedState;
        mCloseWidth = closeWidth;
        mOnStopClickListener = onStopClickListener;
        mOpenWidth = openWidth;
        mHeight = height;
        sortEventsByStartTime();
    }

    public void setNewData(ArrayList<Event> events) {
        mEvents = events;
        sortEventsByStartTime();
        notifyDataSetChanged();
    }

    private void sortEventsByStartTime() {
        Collections.sort(mEvents, new Comparator<Event>() {
            @Override
            public int compare(Event lhs, Event rhs) {
                if (lhs.getEventTime() == null) {
                    if (rhs.getEventTime() == null) {
                        return -1;
                    }
                    return 0;
                } else if (rhs.getEventTime() == null) {
                    return 1;
                }

                //int retval = (int) (TimeUtils.getLongFromDateString(rhs.getEventTime(), "dd/MM/yyyy HH:mm:ss") - TimeUtils.getLongFromDateString(lhs.getEventTime(), "dd/MM/yyyy HH:mm:ss"));
                //return retval;

                try {
                    long retval = (TimeUtils.getLongFromDateString(rhs.getEventTime(), "dd/MM/yyyy HH:mm:ss") - TimeUtils.getLongFromDateString(lhs.getEventTime(), "dd/MM/yyyy HH:mm:ss"));

                    if (retval > 0) {
                        return 1;
                    }

                    if (retval == 0) {
                        return 0;
                    } else {
                        return -1;
                    }
                } catch (RuntimeException e) {
                    return 0;
                }

            }
        });

    }

    public void updateData(ArrayList<Event> events) {
        for (Event event : events) {
            for (Event e : mEvents) {
                if (e.getEventID() == event.getEventID()) {
                    updateStopEvent(event, e);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void updateStopEvent(Event event, Event e) {
        e.setDuration(event.getDuration());
        e.setAlarmValue(event.getAlarmValue());
        e.setEventEndTime(event.getEventEndTime());
        e.setEventGroupID(event.getEventGroupID());
        e.setEventGroupLname(event.getEventGroupLname());
        e.setEventSubTitleEname(event.getEventSubTitleEname());
        e.setEventSubTitleLname(event.getEventSubTitleLname());
        e.setEventTime(event.getEventTime());
        e.setEventTitle(event.getEventTitle());
        e.setPriority(event.getPriority());
        e.setEventReasonID(event.getEventReasonID());

    }

    public void changeState(boolean closedState) {
        mClosedState = closedState;
        notifyDataSetChanged();
    }

    private class ShiftLogStoppedViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mParentLayout;
        private LinearLayout mTitleLayout;
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
            mParentLayout = (LinearLayout) itemView.findViewById(R.id.event_stopped_parent_layout);
            mTitleLayout = (LinearLayout) itemView.findViewById(R.id.event_stopped_title_layout);
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

        private LinearLayout mParentLayout;
        private LinearLayout mTitleLayout;
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
            mParentLayout = (LinearLayout) itemView.findViewById(R.id.event_parameter_parent_layout);
            mTitleLayout = (LinearLayout) itemView.findViewById(R.id.event_parameter_title_layout);
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

            ViewGroup.LayoutParams mItemViewParams;

            mItemViewParams = shiftLogStoppedViewHolder.mParentLayout.getLayoutParams();

            mItemViewParams.height = (int) (mHeight * 0.23);

            shiftLogStoppedViewHolder.mParentLayout.requestLayout();

            ViewGroup.LayoutParams mShWoopListParams;

            mShWoopListParams = shiftLogStoppedViewHolder.mTitleLayout.getLayoutParams();

            mShWoopListParams.width = (int) (mOpenWidth * 0.38);

            shiftLogStoppedViewHolder.mTitleLayout.requestLayout();

            Log.e(DavidVardi.SHIFT_LOG, "Position: " + position + " EventGroupID: " + event.getEventGroupID());

            if (event.getEventGroupID() != 6) {

                event.setTreated(true);
            }

            if (!event.isTreated()) {

                if (event.getPriority() == 1) {

                    // TODO: 12 יולי 2017 VR change the image as per the event Reason ID

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
                shiftLogStoppedViewHolder.mIcon.setImageResource(ReasonImage.getImageForStopReasonShiftLog(event.getEventGroupID()));
                shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                shiftLogStoppedViewHolder.mTime.setTypeface(null, Typeface.NORMAL);
            }
            shiftLogStoppedViewHolder.mTime.setText(TimeUtils.getTimeFromString(event.getTime()));
            String groupName = OperatorApplication.isEnglishLang() ? event.getEventGroupEname() : event.getEventGroupLname();
            shiftLogStoppedViewHolder.mTitle.setText(groupName);
            shiftLogStoppedViewHolder.mStart.setText(TimeUtils.getTimeFromString(event.getTime()));
            shiftLogStoppedViewHolder.mStartDate.setText(TimeUtils.getDateFromString(event.getTime()));
            shiftLogStoppedViewHolder.mEnd.setText(TimeUtils.getTimeFromString(event.getEventEndTime()));
            shiftLogStoppedViewHolder.mEndDate.setText(TimeUtils.getDateFromString(event.getEventEndTime()));
            long durationInMillis = event.getDuration() * 1000 * 60; // duration is sent in minutes.
            shiftLogStoppedViewHolder.mDuration.setText(TimeUtils.getDurationTime(mContext, durationInMillis));
            if (mClosedState) {
                shiftLogStoppedViewHolder.mDivider.setVisibility(View.GONE);
                shiftLogStoppedViewHolder.mSubtitle.setVisibility(View.INVISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogStoppedViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = (int) (mCloseWidth * 0.5);
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
                    //  if (!event.isTreated()) {

                    shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                    shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                    shiftLogStoppedViewHolder.mTime.setTypeface(null, Typeface.NORMAL);
                    mOnStopClickListener.onStopClicked(event.getEventID(), event.getTime(), event.getEventEndTime(), event.getDuration());

                /*    } else {
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
                    }*/
                    PersistenceManager.getInstance().saveShiftLogs(mEvents);
                }
            });
        } else if (type == PARAMETER) {
            final ShiftLogParameterViewHolder shiftLogParameterViewHolder = (ShiftLogParameterViewHolder) holder;

            ViewGroup.LayoutParams mItemViewParams;
            mItemViewParams = shiftLogParameterViewHolder.mParentLayout.getLayoutParams();
            mItemViewParams.height = (int) (mHeight * 0.23);
            shiftLogParameterViewHolder.mParentLayout.requestLayout();

            ViewGroup.LayoutParams mShWoopListParams;
            mShWoopListParams = shiftLogParameterViewHolder.mTitleLayout.getLayoutParams();
            mShWoopListParams.width = (int) (mOpenWidth * 0.38);
            shiftLogParameterViewHolder.mTitleLayout.requestLayout();

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
            String groupName = OperatorApplication.isEnglishLang() ? event.getEventGroupEname() : event.getEventGroupLname();
            shiftLogParameterViewHolder.mTitle.setText(groupName);
            shiftLogParameterViewHolder.mTime.setText(TimeUtils.getTimeFromString(event.getTime()));
            String subtitleNameByLang = OperatorApplication.isEnglishLang() ? event.getEventETitle() : event.getEventLTitle();
            shiftLogParameterViewHolder.mSubtitleText.setText(subtitleNameByLang);
            shiftLogParameterViewHolder.mSubTitleValue.setText(String.valueOf(event.getAlarmValue()));
            shiftLogParameterViewHolder.mMin.setText(String.valueOf(event.getAlarmLValue()));
            shiftLogParameterViewHolder.mMax.setText(String.valueOf(event.getAlarmHValue()));
            shiftLogParameterViewHolder.mStandard.setText(String.valueOf(event.getAlarmStandardValue()));
            if (mClosedState) {
                shiftLogParameterViewHolder.mDivider.setVisibility(View.GONE);
                shiftLogParameterViewHolder.mSubtitle.setVisibility(View.INVISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = shiftLogParameterViewHolder.mBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = (int) (mCloseWidth * 0.5);
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

            default: // all event group id's that are not 20 should default to a stop event.
                type = STOPPED;
                break;
        }

        return type;

    }

}
