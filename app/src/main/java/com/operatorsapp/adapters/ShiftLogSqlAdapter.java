package com.operatorsapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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


import java.util.ArrayList;

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
    public LayoutInflater inflater;
    private boolean mIsSelectionMode;
    private ArrayList<Integer> mSelectedEvents;
    private ArrayList<Event> mUpdatedAlarms;

    public ShiftLogSqlAdapter(Context context, Cursor cursor, boolean closedState, int closeWidth,
                              OnStopClickListener onStopClickListener, int openWidth, int height,
                              boolean selectMode, ArrayList<Integer> selectedEvents) {
        super(cursor);
        mContext = context;
        mClosedState = closedState;
        mCloseWidth = closeWidth;
        mOnStopClickListener = onStopClickListener;
        mOpenWidth = openWidth;
        mHeight = height;
        mIsSelectionMode = selectMode;
        mSelectedEvents = selectedEvents;
        mUpdatedAlarms = new ArrayList<>();
    }

    public void setSelectedEvents(ArrayList<Integer> selectedEvents) {
        mSelectedEvents = selectedEvents;
    }


    public class ShiftLogViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox mStopEventCheckBox;
        private TextView mParameterSubReasonTv;
        //    private LinearLayout mStoppedParentLayout;
        private LinearLayout mStoppedTitleLayout;
        private AutofitTextView mStoppedTitle;
        private ImageView mStoppedIcon;
        private TextView mStopEventSubReasonTv;
        private TextView mStoppedStart;
        private TextView mStoppedStartDate;
        private TextView mStoppedDuration;
        private TextView mStoppedEnd;
        private TextView mStoppedEndDate;
        private TextView mStoppedTime;
        private View mStoppedDivider;
        private View mStoppedBottomDivider;
        private LinearLayout mStoppedSubtitle;
        //   private LinearLayout mParameterParentLayout;
        private LinearLayout mParameterTitleLayout;
        private AutofitTextView mParameterTitle;
        private ImageView mParameterIcon;
        private TextView mParameterSubtitleText;
        private TextView mParameterSubTitleValue;
        private TextView mParameterStandard;
        private TextView mParameterMin;
        private TextView mParameterMax;
        private TextView mParameterTime;
        private View mParameterDivider;
        private LinearLayout mParameterSubtitle;
        private View mParameterBottomDivider;
        private View mParameterCard;
        private View mStoppedCard;

        ShiftLogViewHolder(View itemView) {
            super(itemView);
            mParameterCard = itemView.findViewById(R.id.parameter_cardview);
            mStoppedCard = itemView.findViewById(R.id.stopped_cardview);
            mStopEventCheckBox = itemView.findViewById(R.id.event_stop_checkbox);
            mStopEventSubReasonTv = itemView.findViewById(R.id.event_stopped_shift_log_item_sub_reason);
            //       mStoppedParentLayout = itemView.findViewById(R.id.event_stopped_parent_layout);
            mStoppedTitleLayout = itemView.findViewById(R.id.event_stopped_title_layout);
            mStoppedTitle = itemView.findViewById(R.id.event_stopped_shift_log_item_title);
            mStoppedIcon = itemView.findViewById(R.id.event_stopped_shift_log_item_icon);
            mStoppedTime = itemView.findViewById(R.id.event_stopped_shift_log_item_time);
            mStoppedStart = itemView.findViewById(R.id.event_stopped_shift_log_item_start);
            mStoppedStartDate = itemView.findViewById(R.id.event_stopped_shift_log_item_start_date);
            mStoppedDuration = itemView.findViewById(R.id.event_stopped_shift_log_item_duration);
            mStoppedEnd = itemView.findViewById(R.id.event_stopped_shift_log_item_end);
            mStoppedEndDate = itemView.findViewById(R.id.event_stopped_shift_log_item_end_date);
            mStoppedDivider = itemView.findViewById(R.id.event_stopped_shift_log_divider);
            mStoppedBottomDivider = itemView.findViewById(R.id.event_stopped_shift_log_bottom_divider);
            mStoppedSubtitle = itemView.findViewById(R.id.event_stopped_shift_log_item_subtitle);
            //mSplitEvent = itemView.findViewById(R.id.event_stopped_shift_log_item_split_event);
            //         mParameterParentLayout = itemView.findViewById(R.id.event_parameter_parent_layout);
            mParameterTitleLayout = itemView.findViewById(R.id.event_parameter_title_layout);
            mParameterTitle = itemView.findViewById(R.id.event_parameter_shift_log_item_title);
            mParameterIcon = itemView.findViewById(R.id.event_parameter_shift_log_item_icon);
            mParameterSubtitleText = itemView.findViewById(R.id.event_parameter_shift_log_item_subtitle_text);
            mParameterSubTitleValue = itemView.findViewById(R.id.event_parameter_shift_log_item_subtitle_value);
            mParameterStandard = itemView.findViewById(R.id.event_parameter_shift_log_item_standard);
            mParameterMax = itemView.findViewById(R.id.event_parameter_shift_log_item_max);
            mParameterMin = itemView.findViewById(R.id.event_parameter_shift_log_item_min);
            mParameterTime = itemView.findViewById(R.id.event_parameter_shift_log_item_time);
            mParameterDivider = itemView.findViewById(R.id.event_parameter_shift_log_divider);
            mParameterSubtitle = itemView.findViewById(R.id.event_parameter_shift_log_item_subtitle);
            mParameterBottomDivider = itemView.findViewById(R.id.event_parameter_shift_log_bottom_divider);
            mParameterSubReasonTv = itemView.findViewById(R.id.event_parameter_shift_log_item_sub_reason);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new ShiftLogViewHolder(inflater.inflate(R.layout.event_cardview, parent, false));
    }

    public void changeState(boolean closedState) {
        mClosedState = closedState;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final Cursor cursor) {

        mOnStopClickListener.onLastItemUpdated();

        final ShiftLogViewHolder holder = (ShiftLogViewHolder) viewHolder;

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
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_TREATED)) > 0,
                cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.KEY_ALARM_VALUE)),
                cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.KEY_ALARM_H_VALUE)),
                cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.KEY_ALARM_L_VALUE)),
                cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.KEY_ALARM_STANDARD_VALUE))
        );




        final int type = getItemViewType(event.getEventGroupID());

        if (type == STOPPED) {

//            if (viewHolder.getAdapterPosition() <= mFirstStopEventPosition && event.getEventEndTime().isEmpty()){
//
//                mFirstStopEventPosition = viewHolder.getAdapterPosition();
//                holder.mSplitEvent.setVisibility(View.VISIBLE);
//                holder.mSplitEvent.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // TODO: 05/07/2018 split event
//                        mOnStopClickListener.onSplitEventPressed(event.getEventID());
//                    }
//                });
//
//            }else {
//                holder.mSplitEvent.setVisibility(View.GONE);
//                holder.mSplitEvent.setOnClickListener(null);
//            }



            if (mIsSelectionMode) {

               // holder.mSplitEvent.setVisibility(View.GONE);
                holder.mStopEventCheckBox.setVisibility(View.VISIBLE);
                holder.mStopEventCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                        event.setChecked(checked);

                        mOnStopClickListener.onStopEventSelected(event.getEventID(), checked);

                    }
                });

            } else {

                holder.mStopEventCheckBox.setVisibility(View.GONE);
            }

            if (mSelectedEvents != null) {
                for (Integer event1 : mSelectedEvents) {
                    if (event.getEventID() == event1) {
                        event.setChecked(true);
                    }
                }
            }
            if (mIsSelectionMode && event.isChecked()) {

                holder.mStopEventCheckBox.setChecked(true);
            } else {
                holder.mStopEventCheckBox.setChecked(false);
            }

            holder.mStoppedCard.setVisibility(View.VISIBLE);

            holder.mParameterCard.setVisibility(View.GONE);

            ViewGroup.LayoutParams mItemViewParams;

            mItemViewParams = holder.mStoppedCard.getLayoutParams();

            mItemViewParams.height = (int) (mHeight * 0.23);

            holder.mStoppedCard.requestLayout();

            ViewGroup.LayoutParams mShWoopListParams;

            mShWoopListParams = holder.mStoppedTitleLayout.getLayoutParams();

            mShWoopListParams.width = (int) (mOpenWidth * 0.38);

            holder.mStoppedTitleLayout.requestLayout();


            if (event.getEventGroupID() != 6) {

                event.setTreated(true);
            }

            if (!event.isTreated()) {

                if (event.getPriority() == 1) {


                    holder.mStoppedIcon.setImageResource(R.drawable.ic_hand_red);
                    holder.mStoppedTitle.setTextColor(Color.RED);
                    holder.mStoppedTime.setTextColor(Color.RED);
                } else {
                    holder.mStoppedIcon.setImageResource(R.drawable.ic_hand_blue);
                    holder.mStoppedTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    holder.mStoppedTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                    holder.mStoppedTime.setTypeface(null, Typeface.BOLD);
                }
            } else {
                holder.mStoppedIcon.setImageResource(ReasonImage.getImageForStopReasonShiftLog(event.getEventGroupID()));
                holder.mStoppedTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                holder.mStoppedTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                holder.mStoppedTime.setTypeface(null, Typeface.NORMAL);
            }
            holder.mStoppedTime.setText(TimeUtils.getTimeFromString(event.getTime()));
            String groupName = OperatorApplication.isEnglishLang() ? event.getEventGroupEname() : event.getEventGroupLname();
            holder.mStoppedTitle.setText(groupName);
            String subtitleNameByLang = event.getSubtitleEname();
            if (subtitleNameByLang != null) {

                if (subtitleNameByLang.length() > 15) {
                    holder.mStopEventSubReasonTv.setText(String.format("%s...", subtitleNameByLang.substring(0, 15)));
                } else {
                    holder.mStopEventSubReasonTv.setText(subtitleNameByLang);
                }
                holder.mStopEventSubReasonTv.setVisibility(View.VISIBLE);
            } else {
                holder.mStopEventSubReasonTv.setVisibility(View.GONE);
            }
            holder.mStoppedStart.setText(TimeUtils.getTimeFromString(event.getTime()));
            holder.mStoppedStartDate.setText(TimeUtils.getDateFromString(event.getTime()));
            holder.mStoppedEnd.setText(TimeUtils.getTimeFromString(event.getEventEndTime()));
            holder.mStoppedEndDate.setText(TimeUtils.getDateFromString(event.getEventEndTime()));
            long durationInMillis = event.getDuration() * 1000 * 60; // duration is sent in minutes.
            holder.mStoppedDuration.setText(TimeUtils.getDurationTime(mContext, durationInMillis));
            if (mClosedState) {
                holder.mStoppedDivider.setVisibility(View.GONE);
                holder.mStoppedSubtitle.setVisibility(View.INVISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = holder.mStoppedBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = (int) (mCloseWidth * 0.5);
                holder.mStoppedBottomDivider.requestLayout();
            } else {
                holder.mStoppedDivider.setVisibility(View.VISIBLE);
                holder.mStoppedSubtitle.setVisibility(View.VISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = holder.mStoppedBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                holder.mStoppedBottomDivider.requestLayout();
            }

            holder.mStoppedCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!mIsSelectionMode) {

                        event.setChecked(true);
                        mOnStopClickListener.onSelectMode(type, event.getEventID());
//                        mOnStopClickListener.onStopEventSelected(event.getEventID(), true);

                        v.setTag(true);
                        holder.mStoppedTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        holder.mStoppedTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                        holder.mStoppedTime.setTypeface(null, Typeface.NORMAL);
//                        mOnStopClickListener.onStopClicked(event.getEventID(), event.getTime(), event.getEventEndTime(), event.getDuration());
                        event.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()));
                    } else {

                        holder.mStopEventCheckBox.setChecked(!event.isChecked());
                    }
                }
            });
        } else if (type == PARAMETER) {
            holder.mStoppedCard.setVisibility(View.GONE);

//            if (mIsSelectionMode){
//                holder.mParameterCard.setVisibility(View.GONE);
//                return;
//            }
            holder.mParameterCard.setVisibility(View.VISIBLE);

            if (mUpdatedAlarms != null) {
                for (Event event1 : mUpdatedAlarms) {
                    if (event.getEventID() == event1.getEventID()) {
                        event.setTreated(event1.isTreated());
                        break;
                    }
                }
            }

            ViewGroup.LayoutParams mItemViewParams;
            mItemViewParams = holder.mParameterCard.getLayoutParams();
            mItemViewParams.height = (int) (mHeight * 0.23);
            holder.mParameterCard.requestLayout();

            ViewGroup.LayoutParams mShWoopListParams;
            mShWoopListParams = holder.mParameterTitleLayout.getLayoutParams();
            mShWoopListParams.width = (int) (mOpenWidth * 0.38);
            holder.mParameterTitleLayout.requestLayout();

            if (!event.isTreated()) {
                if (event.getPriority() == 1) {
                    holder.mParameterIcon.setImageResource(R.drawable.ic_sun_red);
                    holder.mParameterTitle.setTextColor(Color.RED);
                    holder.mParameterTime.setTextColor(Color.RED);
                } else {
                    holder.mParameterIcon.setImageResource(R.drawable.ic_sun_blue);
                    holder.mParameterTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    holder.mParameterTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                    holder.mParameterTime.setTypeface(null, Typeface.BOLD);
                }
            } else {
                holder.mParameterIcon.setImageResource(R.drawable.ic_sun_grey);
                holder.mParameterTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                holder.mParameterTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                holder.mParameterTime.setTypeface(null, Typeface.NORMAL);
            }
            String groupName = OperatorApplication.isEnglishLang() ? event.getEventGroupEname() : event.getEventGroupLname();
            holder.mParameterTitle.setText(groupName);
            holder.mParameterTime.setText(TimeUtils.getTimeFromString(event.getTime()));
            String subtitleNameByLang = OperatorApplication.isEnglishLang() ? event.getEventETitle() : event.getEventLTitle();
            holder.mParameterSubtitleText.setText(subtitleNameByLang);
            holder.mParameterSubTitleValue.setText(String.valueOf(event.getAlarmValue()));
            holder.mParameterMin.setText(String.valueOf(event.getAlarmLValue()));
            holder.mParameterMax.setText(String.valueOf(event.getAlarmHValue()));
            holder.mParameterStandard.setText(String.valueOf(event.getAlarmStandardValue()));
            String subtitleParameterNameByLang = event.getSubtitleEname();
            if (subtitleNameByLang != null) {

                if (subtitleNameByLang.length() > 15) {
                    holder.mParameterSubReasonTv.setText(String.format("%s...", subtitleParameterNameByLang.substring(0, 15)));
                } else {
                    holder.mParameterSubReasonTv.setText(subtitleParameterNameByLang);
                }
                holder.mParameterSubReasonTv.setVisibility(View.VISIBLE);
            } else {
                holder.mParameterSubReasonTv.setVisibility(View.GONE);
            }
            if (mClosedState) {
                holder.mParameterDivider.setVisibility(View.GONE);
                holder.mParameterSubtitle.setVisibility(View.INVISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = holder.mParameterBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = (int) (mCloseWidth * 0.5);
                holder.mParameterBottomDivider.requestLayout();
            } else {
                holder.mParameterDivider.setVisibility(View.VISIBLE);
                holder.mParameterSubtitle.setVisibility(View.VISIBLE);

                final ViewGroup.LayoutParams mBottomDividerLayoutParams = holder.mParameterBottomDivider.getLayoutParams();
                mBottomDividerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                holder.mParameterBottomDivider.requestLayout();

            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!event.isTreated()) {
                        holder.mParameterIcon.setImageResource(R.drawable.ic_sun_grey);
                        holder.mParameterTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        holder.mParameterTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                        holder.mParameterTime.setTypeface(null, Typeface.NORMAL);
                        event.setTreated(true);
                    } else {
                        if (event.getPriority() == 1) {
                            holder.mParameterIcon.setImageResource(R.drawable.ic_sun_red);
                            holder.mParameterTitle.setTextColor(Color.RED);
                            holder.mParameterTime.setTextColor(Color.RED);
                        } else {
                            holder.mParameterIcon.setImageResource(R.drawable.ic_sun_blue);
                            holder.mParameterTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                            holder.mParameterTime.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
                        }
                        holder.mParameterTime.setTypeface(null, Typeface.BOLD);
                        event.setTreated(false);
                    }
                    event.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()));

                    if (mUpdatedAlarms == null) {

                        mUpdatedAlarms = new ArrayList<>();
                    }

                    Event eventToUpdate = null;

                    for (Event updatedAlarm : mUpdatedAlarms) {
                        if (event.getEventID() == updatedAlarm.getEventID()) {
                            eventToUpdate = updatedAlarm;
                            updatedAlarm.setTreated(event.isTreated());
                            break;
                        }
                    }
                    if (eventToUpdate == null){

                        mUpdatedAlarms.add(event);

                    }

                }
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
