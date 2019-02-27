package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.common.actualBarExtraResponse.Inventory;
import com.example.common.actualBarExtraResponse.Notification;
import com.example.common.actualBarExtraResponse.Reject;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.OnStopClickListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.operatorsapp.utils.TimeUtils.SIMPLE_FORMAT_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_T_FORMAT;
import static com.operatorsapp.utils.TimeUtils.convertDateToMillisecond;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private static final int PIXEL_FOR_MINUTE = 4;
    private boolean mClosedState;
    private boolean mIsSelectionMode;
    private final OnStopClickListener mOnStopClickListener;

    private Context mContext;
    private ArrayList<Event> mEvents = new ArrayList<>();
    private ArrayList<Float> mSelectedEvents;
    private boolean mIsWorkingEventChecked = true, mIsEventDetailsChecked = true, mIsServiceCallsChecked = true, mIsmMessagesChecked = true, mIsRejectsChecked = true, mIsProductionReportChecked = true;


    public EventsAdapter(Context context, OnStopClickListener onStopClickListener, boolean selectMode, boolean closedState, ArrayList<Event> events, ArrayList<Float> selectedEvents) {
        mContext = context;
        mOnStopClickListener = onStopClickListener;
        mIsSelectionMode = selectMode;
        mClosedState = closedState;
        mEvents = events;
        mSelectedEvents = selectedEvents;
    }

    public EventsAdapter(Context context, OnStopClickListener onStopClickListener, boolean selectMode, boolean closedState) {
        mContext = context;
        mOnStopClickListener = onStopClickListener;
        mIsSelectionMode = selectMode;
        mClosedState = closedState;
    }

    public void setSelectedEvents(ArrayList<Float> selectedEvents) {
        mSelectedEvents = selectedEvents;
    }

    public void setEvents(ArrayList<Event> events) {
        mEvents = events;
    }

    public void setIsSelectionMode(boolean mIsSelectionMode) {
        this.mIsSelectionMode = mIsSelectionMode;
    }

    public void setCheckedFilters(boolean isWorkingEventChecked, boolean isEventDetailsChecked, boolean isServiceCallsChecked, boolean isMessagesChecked, boolean isRejectsChecked, boolean isProductionReportChecked) {

        mIsWorkingEventChecked = isWorkingEventChecked;
        mIsEventDetailsChecked = isEventDetailsChecked;
        mIsServiceCallsChecked = isServiceCallsChecked;
        mIsmMessagesChecked = isMessagesChecked;
        mIsRejectsChecked = isRejectsChecked;
        mIsProductionReportChecked = isProductionReportChecked;
        notifyDataSetChanged();

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
        holder.updateItem(position, holder);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final View mCircle;
        private TextView mText;
        private TextView mTime;
        private View mLine;
        private LinearLayout mCheckContainer;
        private CheckBox mCheckBox;
        private RelativeLayout mTechContainer;
        Event mEvent;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mText = itemView.findViewById(R.id.EI_text);
            mTime = itemView.findViewById(R.id.EI_time);
            mLine = itemView.findViewById(R.id.EI_line);
            mCircle = itemView.findViewById(R.id.EI_circle);
            mCheckContainer = itemView.findViewById(R.id.EI_check_container);
            mCheckBox = itemView.findViewById(R.id.EI_check_box);
            mTechContainer = itemView.findViewById(R.id.EI_service_call_container);

        }

        private void updateItem(int position, final ViewHolder holder) {
            if (position > mEvents.size() - 1) {
                return;
            }
            mEvent = mEvents.get(position);

            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (mOnStopClickListener != null && mEvent.getType() != 1) {
                        mOnStopClickListener.onStopEventSelected(mEvent.getEventID(), isChecked);
                    }

                }
            });
            mEvent.setChecked(false);
            if (mSelectedEvents != null) {
                for (Float event1 : mSelectedEvents) {
                    if (mEvent.getEventID() == event1) {
                        mEvent.setChecked(true);
                    }
                }
            }

            if (mIsSelectionMode && mEvent.isChecked()) {

                holder.mCheckBox.setChecked(true);
            } else {
                holder.mCheckBox.setChecked(false);
            }
            setViewHeight(mEvent);
            updateNotification(mEvent);

            mLine.setBackgroundColor(Color.parseColor(mEvent.getColor()));
            GradientDrawable circleBackground = (GradientDrawable) mCircle.getBackground();
            circleBackground.setColor(Color.parseColor(mEvent.getColor()));

            String startTime = "";
            if (mEvent.getEventTime() != null && mEvent.getEventTime().length() > 0) {
                startTime = mEvent.getEventTime().substring(10, 16);
            }

            mTime.setText(startTime);

            if (mIsEventDetailsChecked) {
                mText.setVisibility(View.VISIBLE);
            } else {
                mText.setVisibility(View.GONE);
            }

            long duration = mEvent.getDuration();
            if (duration == 0) {
                duration = 1;
            }

            String text = duration + "m " + (OperatorApplication.isEnglishLang() ? mEvent.getSubtitleEname() : mEvent.getSubtitleLname());

            if (!mClosedState && text.length() > 12) {
                mText.setText(String.format("%s...", text.substring(0, 12)));

            } else {
                mText.setText(text);
            }
            GradientDrawable textBackground = (GradientDrawable) mText.getBackground();
            textBackground.setColor(Color.parseColor(mEvent.getColor()));


            if (mIsSelectionMode) {
                mCheckContainer.setVisibility(View.VISIBLE);
                mTechContainer.setVisibility(View.GONE);

                if (mEvent.getType() != 1 || mEvent.getEventGroupID() != 20) {

                    holder.mCheckBox.setVisibility(View.VISIBLE);
                    holder.mCheckBox.setChecked(mEvent.isChecked());
                } else {
                    holder.mCheckBox.setVisibility(View.GONE);

                }
            } else {
                mCheckContainer.setVisibility(View.GONE);
                holder.mCheckBox.setVisibility(View.GONE);
                mTechContainer.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!mIsSelectionMode) {
                        if (mOnStopClickListener != null && mEvent.getType() != 1 && mEvent.getEventGroupID() != 20) {
                            mOnStopClickListener.onSelectMode(mEvent);
                        }
                    } else {
                        holder.mCheckBox.setChecked(true);
                    }
                }
            });

        }

        private void setViewHeight(Event event) {
            ViewGroup.LayoutParams params = mView.getLayoutParams();
            if (mIsSelectionMode && (event.getType() == 1 || event.getEventGroupID() == 20)) {
                params.height = 0;
            } else if (!mIsWorkingEventChecked && event.getType() == 1) {
                params.height = 0;
            } else if ((int) event.getDuration() * PIXEL_FOR_MINUTE > 300) {
                params.height = 300;
            } else if (event.getDuration() > 4) {
                params.height = (int) event.getDuration() * PIXEL_FOR_MINUTE;
            } else {
                params.height = 5 * PIXEL_FOR_MINUTE;
            }
            mView.setLayoutParams(params);
        }

        private void updateNotification(Event event) {
            if (mTechContainer.getChildCount() > 0) {
                mTechContainer.removeAllViews();
            }


            if (event.getNotifications() != null && event.getNotifications().size() > 0) {
                for (Notification notification : event.getNotifications()) {

                    String text = notification.getSourceUserName() + " " + notification.getText();
                    if (notification.getNotificationType() == 1 && mIsmMessagesChecked) {
                        long startTimeMilli = convertDateToMillisecond(notification.getSentTime(), SQL_T_FORMAT);
                        setNotification(event, 1, notification.getSentTime().substring(11, 16), startTimeMilli, text, 0);
                    } else if (notification.getNotificationType() == 2 && mIsServiceCallsChecked) {
                        long startTimeMilli = convertDateToMillisecond(notification.getSentTime(), SQL_T_FORMAT);
                        setNotification(event, 2, notification.getSentTime().substring(11, 16), startTimeMilli, text, notification.getResponseTypeID());
                    }
                }
            }

            if (mIsProductionReportChecked && event.getInventories() != null && event.getInventories().size() > 0) {
                for (Inventory inventory : event.getInventories()) {
                    long startTimeMilli = convertDateToMillisecond(inventory.getTime(), SIMPLE_FORMAT_FORMAT);
                    setNotification(event, 3, inventory.getTime(), startTimeMilli, inventory.getAmount() + " " + inventory.getLName(), 0);
                }
            }

            if (mIsRejectsChecked && event.getRejects() != null && event.getRejects().size() > 0) {
                for (Reject reject : event.getRejects()) {

                    String name = OperatorApplication.isEnglishLang() ? reject.getEName() : reject.getLName();
                    long startTimeMilli = convertDateToMillisecond(reject.getTime(), SIMPLE_FORMAT_FORMAT);
                    setNotification(event, 4, reject.getTime().substring(11, 16), startTimeMilli, reject.getAmount() + " " + name, 0);
                }
            }


        }

        private void setNotification(Event event, int type, String time, long startTimeMilli, String details, int icon) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.service_call_item, mTechContainer, false);
            TextView timeTV = view.findViewById(R.id.SCI_time);
            TextView detailsTV = view.findViewById(R.id.SCI_details);
//            TextView textTV = itemView.findViewById(R.id.SCI_text);

            ImageView iconIV = view.findViewById(R.id.SCI_service_call_icon);

            if (event.getDuration() <= 5) {
                timeTV.setVisibility(View.GONE);
            } else {
                timeTV.setVisibility(View.VISIBLE);
            }


            switch (type) {
                case 1:
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_black));
                    break;

                case 2:
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    setNotificationIcon(iconIV, icon);
                    break;

                case 3:
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.production_black));
                    break;

                case 4:
                    timeTV.setText(time);
                    detailsTV.setText(details);
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rejects_black));
                    break;
            }
//            setViewLocation(view, startTimeMilli);
            mTechContainer.addView(view);
        }

        private void setViewLocation(View view, long notificationTimeMillis) {
            long eventTimeMillis = convertDateToMillisecond(mEvent.getEventTime(), SIMPLE_FORMAT_FORMAT);

            long minutes = TimeUnit.MILLISECONDS.toMinutes(notificationTimeMillis - eventTimeMillis);

            minutes = minutes * PIXEL_FOR_MINUTE;

            if (minutes > (mView.getHeight())) {
//                    Log.e( "setViewLocation: ", "minutes " + minutes + "; OK "  + mView.getHeight());
                view.setPadding(0, (int) mView.getHeight() - view.getHeight(), 0, 0);
            } else {
//                    Log.e( "setViewLocation: ", "minutes " + minutes + "; False" );
                view.setPadding(0, (int) minutes, 0, 0);
            }

        }
    }

    private void setNotificationIcon(ImageView iconIV, int icon) {
        switch (icon) {
            case 0:
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.called_black));
                break;
            case 1:
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_recieved_black));
                break;
            case 2:
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_declined_black));
                break;
            case 3:
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cancel_black));
                break;
            case 4:
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.at_work_black));
                break;
            case 5:
                iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.work_completed_black));
                break;

            default:
                iconIV.setImageDrawable(null);
                break;
        }
    }
}


