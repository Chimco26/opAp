package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.common.Event;
import com.example.common.actualBarExtraResponse.Notification;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.TechCallInfo;

import java.util.ArrayList;

import static com.operatorsapp.utils.TimeUtils.convertDateToMillisecond;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private static final int PIXEL_FOR_MINUTE = 4;
    private boolean mClosedState;
    private boolean mIsSelectionMode;
    private final OnStopClickListener mOnStopClickListener;

    private Context mContext;
    private ArrayList<Event> mEvents;

    public EventsAdapter(Context context, OnStopClickListener onStopClickListener, boolean selectMode, boolean closedState, ArrayList<Event> events) {
        mContext = context;
        mOnStopClickListener = onStopClickListener;
        mIsSelectionMode = selectMode;
        mClosedState = closedState;


        mEvents = events;
    }

//    public void setEvents(ArrayList<Event> events) {
//        updateList(events);
//        notifyDataSetChanged();
//    }

    public void setIsSelectionMode(boolean mIsSelectionMode) {
        this.mIsSelectionMode = mIsSelectionMode;
    }

    public void setClosedState(boolean isSelectionMode) {
        mClosedState = isSelectionMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.event_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.updateItem(position);

    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private final View mView;
        private final RelativeLayout mTimeContainer;
        private final View mCircle;
        private TextView mText;
        private TextView mTime;
        private View mLine;
        private LinearLayout mCheckContainer;
        private RelativeLayout mRelative;
        private CheckBox mCheckBox;
        private Event mEvent;
        //        private ImageView mTechCall;
        private RelativeLayout mTechContainer;

//        private Event mEvent;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mView = itemView;
            mText = itemView.findViewById(R.id.EI_text);
            mTimeContainer = itemView.findViewById(R.id.time_container);
            mTime = itemView.findViewById(R.id.EI_time);
            mLine = itemView.findViewById(R.id.EI_line);
            mCircle = itemView.findViewById(R.id.EI_circle);
            mCheckContainer = itemView.findViewById(R.id.EI_check_container);
            mRelative = itemView.findViewById(R.id.EI_relative);
            mCheckBox = itemView.findViewById(R.id.EI_check_box);
            mCheckBox.setOnCheckedChangeListener(this);
//            mTechCall = itemView.findViewById(R.id.EI_tech_call);
            mTechContainer = itemView.findViewById(R.id.EI_service_call_container);

        }

        private void updateItem(int position) {
            mEvent = mEvents.get(position);

            setViewHeight();
            updateNotification();

            mLine.setBackgroundColor(Color.parseColor(mEvent.getColor()));
            GradientDrawable circleBackground = (GradientDrawable) mCircle.getBackground();
            circleBackground.setColor(Color.parseColor(mEvent.getColor()));

            String startTime = "";
            if (mEvent.getEventTime() != null && mEvent.getEventTime().length() > 0) {
                startTime = mEvent.getEventTime().substring(10, 16);
            }
            if (mEvent.getEventEndTime() != null && mEvent.getEventEndTime().length() > 0) {
                startTime += "; " + mEvent.getEventEndTime().substring(10, 16) + " id; " + mEvent.getEventID();
            }
            mTime.setText(startTime);

//            if (!mEvent.getColor().equals("#1aa917")) {

            mText.setVisibility(View.VISIBLE);

            String text = mEvent.getDuration() + "m " + mEvent.getSubtitleLname();
            if (!mClosedState && text.length() > 12) {
                mText.setText(String.format("%s...", text.substring(0, 12)));

            } else {
                mText.setText(text + "; " + startTime);
            }
            GradientDrawable textBackground = (GradientDrawable) mText.getBackground();
            textBackground.setColor(Color.parseColor(mEvent.getColor()));

//            } else {
//                mText.setVisibility(View.GONE);
//            }


            if (mIsSelectionMode) {
                mCheckContainer.setVisibility(View.VISIBLE);

                if (mEvent.getType() != 1) {

                    mCheckBox.setVisibility(View.VISIBLE);
                    mCheckBox.setChecked(mEvent.isChecked());
                } else {
                    mCheckBox.setVisibility(View.GONE);

                }
            } else {
                mCheckContainer.setVisibility(View.GONE);
                mCheckBox.setVisibility(View.GONE);

            }

//            updateTech();
        }

        private void setViewHeight() {

            ViewGroup.LayoutParams params = mView.getLayoutParams();

//            if (mIsSelectionMode && mEvent.getEventID() == NEW_EVENT_ID || mEvent.getDuration() == 0) {
            if (mIsSelectionMode && mEvent.getType() == 1) {
                params.height = 0;
            } else if ((int) mEvent.getDuration() * PIXEL_FOR_MINUTE > 300) {
                params.height = 300;
            } else if (mEvent.getDuration() > 4) {
                params.height = (int) mEvent.getDuration() * PIXEL_FOR_MINUTE;
//            } else if (mEvent.getDuration() > 0){
            } else {
                params.height = 5 * PIXEL_FOR_MINUTE;
            }
            params.height = 20;
            mView.setLayoutParams(params);
        }

        private void updateNotification() {
            if (mTechContainer.getChildCount() > 0) {
                mTechContainer.removeAllViews();
            }

            if (mEvent.getNotifications() != null) {

                for (Notification notification : mEvent.getNotifications()) {
                    setNotification(1, notification.getSentTime().substring(11, 16), notification.getText(), notification.getNotificationType());
                }
            }
//            for ( REJECTS : mEvent.getREJECTSs()) {
//            setNotification(1, notification.startTime(), notification.details(), notification.getType());
//            }
//            for (Notification notification : mEvent.getNotifications()) {
//            setNotification(1, notification.startTime(), notification.details(), notification.getType());
//            }
//            for (Notification notification : mEvent.getNotifications()) {
//            setNotification(1, notification.startTime(), notification.details(), notification.getType());
//            }


//                    long height = (techCallInfo.getmCallTime() - eventStartMilli) * PIXEL_FOR_MINUTE;
//                    if (height > mView.getHeight()) {
//                        view.setPadding(0, mView.getHeight() - view.getHeight(), 0, 0);
//                    } else {
//                        view.setPadding(0, (int) height, 0, 0);
//                    }


//                }
        }

        private void setNotification(int type, String time, String details, int icon) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.service_call_item, mTechContainer, false);
            TextView timeTV = view.findViewById(R.id.SCI_time);
            TextView detailsTV = view.findViewById(R.id.SCI_details);
            ImageView iconIV = view.findViewById(R.id.SCI_service_call_icon);


            switch (type) {
                case 1:
                    timeTV.setVisibility(View.VISIBLE);
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    setNotificationIcon(iconIV, icon);
                    break;

                case 2:
                    timeTV.setVisibility(View.VISIBLE);
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    setNotificationIcon(iconIV, 11);
                    break;
                case 3:
                    timeTV.setVisibility(View.GONE);
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    setNotificationIcon(iconIV, 12);
                    break;

                case 4:
                    timeTV.setVisibility(View.GONE);
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    setNotificationIcon(iconIV, 13);
                    break;

                default:
                    timeTV.setVisibility(View.GONE);
                    detailsTV.setVisibility(View.GONE);
                    iconIV.setVisibility(View.GONE);
                    break;
            }
            mTechContainer.addView(view);

        }


        /**
         * @param iconIV
         * @param icon   the number of icon
         *               1 - 9 service calls
         *               case 10 message
         *               case 11 production
         *               case 12 rejects
         */
        private void setNotificationIcon(ImageView iconIV, int icon) {

            switch (icon) {
                case 1:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.technicaian_black));
                    break;

                case 10:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_dark));
                    break;

                case 11:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.production_black));
                    break;

                case 12:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rejects_black));
                    break;
            }
        }


        @Override
        public void onClick(View view) {
            mEvent.setChecked(!mEvent.isChecked());

            if (!mIsSelectionMode) {

                if (mOnStopClickListener != null && mEvent.getType() != 1) {
                    mIsSelectionMode = true;
                    mOnStopClickListener.onSelectMode(mEvent);
                    notifyDataSetChanged();
                }
            } else {
                mCheckBox.setChecked(mEvent.isChecked());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            mEvent.setChecked(checked);

            if (mOnStopClickListener != null && mEvent.getType() != 1) {
                mOnStopClickListener.onStopEventSelected(mEvent.getEventID(), checked);
            }
        }
    }


}
