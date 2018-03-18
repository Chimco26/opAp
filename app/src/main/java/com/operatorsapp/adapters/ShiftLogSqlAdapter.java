package com.operatorsapp.adapters;

import android.content.Context;
import android.database.Cursor;
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

import com.ravtech.david.sqlcore.Event;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.utils.ReasonImage;
import com.operatorsapp.utils.TimeUtils;
import com.ravtech.david.sqlcore.DatabaseHelper;



import me.grantland.widget.AutofitTextView;

public class ShiftLogSqlAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private boolean mClosedState;
    private int mCloseWidth;
    private int mOpenWidth;
    private int mHeight;
    private final int PARAMETER = 1;
    private final int STOPPED = 2;
    private OnStopClickListener mOnStopClickListener;

    public ShiftLogSqlAdapter(Context context,Cursor cursor,boolean closedState, int closeWidth, OnStopClickListener onStopClickListener, int openWidth, int height){
        super(context,cursor);
        mContext = context;
        mClosedState = closedState;
        mCloseWidth = closeWidth;
        mOnStopClickListener = onStopClickListener;
        mOpenWidth = openWidth;
        mHeight = height;
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

        ShiftLogStoppedViewHolder(View itemView) {
            super(itemView);
            mParentLayout =  itemView.findViewById(R.id.event_stopped_parent_layout);
            mTitleLayout =  itemView.findViewById(R.id.event_stopped_title_layout);
            mTitle = itemView.findViewById(R.id.shift_log_item_title);
            mIcon = itemView.findViewById(R.id.shift_log_item_icon);
            mTime = itemView.findViewById(R.id.shift_log_item_time);
            mStart = itemView.findViewById(R.id.shift_log_item_start);
            mStartDate = itemView.findViewById(R.id.shift_log_item_start_date);
            mDuration = itemView.findViewById(R.id.shift_log_item_duration);
            mEnd = itemView.findViewById(R.id.shift_log_item_end);
            mEndDate = itemView.findViewById(R.id.shift_log_item_end_date);
            mDivider = itemView.findViewById(R.id.shift_log_divider);
            mBottomDivider = itemView.findViewById(R.id.shift_log_bottom_divider);
            mSubtitle = itemView.findViewById(R.id.shift_log_item_subtitle);
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

        ShiftLogParameterViewHolder(View itemView) {
            super(itemView);
            mParentLayout = itemView.findViewById(R.id.event_parameter_parent_layout);
            mTitleLayout = itemView.findViewById(R.id.event_parameter_title_layout);
            mTitle = itemView.findViewById(R.id.shift_log_item_title);
            mIcon = itemView.findViewById(R.id.shift_log_item_icon);
            mSubtitleText = itemView.findViewById(R.id.shift_log_item_subtitle_text);
            mSubTitleValue = itemView.findViewById(R.id.shift_log_item_subtitle_value);
            mStandard = itemView.findViewById(R.id.shift_log_item_standard);
            mMax = itemView.findViewById(R.id.shift_log_item_max);
            mMin = itemView.findViewById(R.id.shift_log_item_min);
            mTime = itemView.findViewById(R.id.shift_log_item_time);
            mDivider = itemView.findViewById(R.id.shift_log_divider);
            mSubtitle = itemView.findViewById(R.id.shift_log_item_subtitle);
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

    public void changeState(boolean closedState) {
        mClosedState = closedState;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {

        final Event event = new Event(
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TITLE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_E_TITLE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_L_TITLE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_SUB_TITLE_E_NAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TIME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_END_TIME)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_GROUP_ID)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_EVENT_ID)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_PRIORITY)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_GROUP_E_NAME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_GROUP_L_NAME)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_DURATION)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TREATED))> 0,
                cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.KEY_ALARM_VALUE)),
                cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.KEY_ALARM_H_VALUE)),
                cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.KEY_ALARM_L_VALUE)),
                cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.KEY_ALARM_STANDARD_VALUE))

        );

        int type = getItemViewType(event.getEventGroupID());

        if (type == STOPPED) {
            final ShiftLogStoppedViewHolder shiftLogStoppedViewHolder = (ShiftLogStoppedViewHolder) viewHolder;

            ViewGroup.LayoutParams mItemViewParams;

            mItemViewParams = shiftLogStoppedViewHolder.mParentLayout.getLayoutParams();

            mItemViewParams.height = (int) (mHeight * 0.23);

            shiftLogStoppedViewHolder.mParentLayout.requestLayout();

            ViewGroup.LayoutParams mShWoopListParams;

            mShWoopListParams = shiftLogStoppedViewHolder.mTitleLayout.getLayoutParams();

            mShWoopListParams.width = (int) (mOpenWidth * 0.38);

            shiftLogStoppedViewHolder.mTitleLayout.requestLayout();


            if (event.getEventGroupID() != 6) {

                event.setTreated(true);
            }

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

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setTag(true);

                    shiftLogStoppedViewHolder.mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray));
                    shiftLogStoppedViewHolder.mTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                    shiftLogStoppedViewHolder.mTime.setTypeface(null, Typeface.NORMAL);
                    mOnStopClickListener.onStopClicked(event.getEventID(), event.getTime(), event.getEventEndTime(), event.getDuration());
                    event.updateAll("meventid = ?", String.valueOf(event.getEventID()));
                }
            });
        } else if (type == PARAMETER) {
            final ShiftLogParameterViewHolder shiftLogParameterViewHolder = (ShiftLogParameterViewHolder) viewHolder;

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

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
                    event.updateAll("EventID = ?", String.valueOf(event.getEventID()));                }
            });
        }
    }





    @Override
    public int getItemViewType(int groupID) {
        int type;
        switch (groupID) {
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
