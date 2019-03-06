package com.operatorsapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
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
import com.operatorsapp.utils.StringUtil;

import java.util.ArrayList;

import static com.operatorsapp.utils.TimeUtils.SIMPLE_FORMAT_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_T_FORMAT;
import static com.operatorsapp.utils.TimeUtils.convertDateToMillisecond;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> implements Filterable {

    private static final int PIXEL_FOR_MINUTE = 4;
    private boolean mIsOpenState;
    private boolean mIsSelectionMode;
    private final OnStopClickListener mOnStopClickListener;

    private Context mContext;
    private ArrayList<Event> mEvents = new ArrayList<>();
    private ArrayList<Float> mSelectedEvents;
    private boolean mIsWorkingTimeChecked = true, mIsStopEventChecked = true, mIsServiceCallsChecked = true, mIsmMessagesChecked = true, mIsRejectsChecked = true, mIsProductionReportChecked = true;
    private EventsFilter mFilter;
    private ArrayList<Event> mEventsFiltered = new ArrayList<>();

    public EventsAdapter(Context context, OnStopClickListener onStopClickListener, boolean selectMode, boolean closedState) {
        mContext = context;
        mOnStopClickListener = onStopClickListener;
        mIsSelectionMode = selectMode;
        mIsOpenState = closedState;
        mFilter = new EventsFilter();
        getFilter().filter("");
    }

    public void setSelectedEvents(ArrayList<Float> selectedEvents) {
        mSelectedEvents = selectedEvents;
    }

    public void setEvents(ArrayList<Event> events) {
        mEvents = events;
        getFilter().filter("");
    }

    public void setIsSelectionMode(boolean mIsSelectionMode) {
        this.mIsSelectionMode = mIsSelectionMode;
    }

    public void setCheckedFilters(boolean isWorkingEventChecked, boolean isEventDetailsChecked, boolean isServiceCallsChecked, boolean isMessagesChecked, boolean isRejectsChecked, boolean isProductionReportChecked) {

        mIsWorkingTimeChecked = isWorkingEventChecked;
        mIsStopEventChecked = isEventDetailsChecked;
        mIsServiceCallsChecked = isServiceCallsChecked;
        mIsmMessagesChecked = isMessagesChecked;
        mIsRejectsChecked = isRejectsChecked;
        mIsProductionReportChecked = isProductionReportChecked;
        getFilter().filter("");

    }

    public void setClosedState(boolean isSelectionMode) {
        mIsOpenState = isSelectionMode;
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
        return mEventsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View mView;
        private final View mCircle;
        private TextView mText;
        private TextView mTime;
        private View mLine;
        private LinearLayout mCheckContainer;
        private CheckBox mCheckBox;
        private RelativeLayout mTechContainer;
        private ImageView mCheckV;
        private ImageView mScissors;
        private Event mEvent;

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
            mCheckV = itemView.findViewById(R.id.EI_check_sign);
            mScissors = itemView.findViewById(R.id.EI_Scissors);
            mScissors.setOnClickListener(this);
        }

        private void updateItem(int position, final ViewHolder holder) {
            mEvent = mEvents.get(position);
            if (position > mEventsFiltered.size() - 1) {
                return;
            }
            final Event event = mEventsFiltered.get(position);

            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (mOnStopClickListener != null && event.getType() < 1) {
                        mOnStopClickListener.onStopEventSelected(event.getEventID(), isChecked);
                    }

                }
            });
            event.setChecked(false);
            if (mSelectedEvents != null) {
                for (Float event1 : mSelectedEvents) {
                    if (event.getEventID() == event1) {
                        event.setChecked(true);
                    }
                }
            }

            if (mIsSelectionMode && event.isChecked()) {

                holder.mCheckBox.setChecked(true);
            } else {
                holder.mCheckBox.setChecked(false);
            }

            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            params.height = getViewHeight(event);
//            params.height = filterHeight(params.height, event);
            mView.setLayoutParams(params);
            updateNotification(event);

            mLine.setBackgroundColor(Color.parseColor(event.getColor()));
            GradientDrawable circleBackground = (GradientDrawable) mCircle.getBackground();
            circleBackground.setColor(Color.parseColor(event.getColor()));

            String textTime = "";
            if (event.getType() != 2 && event.getEventTime() != null && event.getEventTime().length() > 0) {
                textTime = event.getEventTime().substring(10, 16);
            } else if (event.getEventEndTime() != null && event.getEventEndTime().length() > 0) {
                textTime = event.getEventEndTime().substring(10, 16);
            }

            mTime.setText(textTime);

//            if (mIsStopEventChecked) {
            mText.setVisibility(View.VISIBLE);
//            } else {
//                mText.setVisibility(View.GONE);
//            }

            long duration = event.getDuration();
            if (duration == 0) {
                duration = 1;
            }

            String text = duration + "m " + (OperatorApplication.isEnglishLang() ? event.getSubtitleEname() : event.getSubtitleLname());

            if (!mIsOpenState && text.length() > 12) {
                mText.setText(String.format("%s...", text.substring(0, 12)));

            } else {
                mText.setText(text);
            }
            GradientDrawable textBackground = (GradientDrawable) mText.getBackground();
            textBackground.setColor(Color.parseColor(event.getColor()));

            if (event.getEventReasonID() != 0 && event.getEventGroupID() != 20) {
                mCheckV.setVisibility(View.VISIBLE);
            } else {
                mCheckV.setVisibility(View.GONE);
            }

            if (!mIsSelectionMode && position == 0 && event.getEventEndTime().isEmpty()) {
                mScissors.setVisibility(View.VISIBLE);
            } else {
                mScissors.setVisibility(View.GONE);
            }


            if (mIsSelectionMode) {
                mCheckContainer.setVisibility(View.VISIBLE);
                mTechContainer.setVisibility(View.GONE);

                if (event.getType() < 1 || event.getEventGroupID() != 20) {

                    holder.mCheckBox.setVisibility(View.VISIBLE);
                    holder.mCheckBox.setChecked(event.isChecked());
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
                        if (mOnStopClickListener != null && event.getType() < 1 && event.getEventGroupID() != 20) {
                            mOnStopClickListener.onSelectMode(event);
                        }
                    } else {
                        holder.mCheckBox.setChecked(true);
                    }
                }
            });

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

            int eventViewHeight = getViewHeight(event);
//            eventViewHeight = filterHeight(eventViewHeight, event);
            int margin = getNotificationRelativePosition(event, time, eventViewHeight);
            if (eventViewHeight - margin < 20) {
                margin = eventViewHeight - 20;
            }

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            params1.setMargins(0, margin, 0, 0);
            params1.setMarginStart(4);

            view.setLayoutParams(params1);
            timeTV.setText(getNotificationTime(time, margin, eventViewHeight));
//                    timeTV.setText(time);
            detailsTV.setText(getTextByState(details));

            switch (type) {
                case 1:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_black));
                    break;

                case 2:
                    setNotificationIcon(iconIV, icon);
                    break;

                case 3:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.production_black));
                    break;

                case 4:
                    iconIV.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rejects_black));
                    break;
            }
            mTechContainer.addView(view);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.EI_Scissors:
                    validateDialog(mEvent.getEventID());
                    break;
            }
        }

    private void validateDialog(final float eventID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.dialog_notes, null);
        builder.setView(dialogView);

        TextView bodyTv = dialogView.findViewById(R.id.DN_note_title);
        TextView titleTv = dialogView.findViewById(R.id.DN_note_main_title);
        Button submitBtn = dialogView.findViewById(R.id.DN_btn);
        ImageButton closeButton = dialogView.findViewById(R.id.DN_close_btn);
        dialogView.findViewById(R.id.DN_note).setVisibility(View.GONE);


        submitBtn.setText(R.string.split);
        bodyTv.setText(R.string.split_event_validation_text);
        bodyTv.setTextSize(16);
        titleTv.setText(R.string.split_event_validation_title);
        titleTv.setTextSize(28);

        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnStopClickListener.onSplitEventPressed(eventID);
                alertDialog.dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    }


    private int filterHeight(int height, Event event) {
        if (((mIsSelectionMode || !mIsWorkingTimeChecked) && (event.getType() != 0 || event.getEventGroupID() == 20))
                || (!mIsStopEventChecked && (event.getType() == 0 && event.getEventGroupID() != 20))) {
            return 0;
        } else {
            return height;
        }
    }

    private String getNotificationTime(String time, int margin, int eventViewHeight) {
        if (margin > 10 && margin < eventViewHeight - 10) {
            return time;
        } else {
            return "";
        }
    }

    private int getViewHeight(Event event) {
        if ((int) event.getDuration() * PIXEL_FOR_MINUTE > 300) {
            return 300;
        } else if (event.getDuration() > 4) {
            return (int) event.getDuration() * PIXEL_FOR_MINUTE;
        } else {
            return 5 * PIXEL_FOR_MINUTE;
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

    private int getNotificationRelativePosition(Event event, String time, int eventViewHeight) {
        long duration = event.getDuration() * 60 * 1000;
        if (duration == 0) {
            duration = 1;
        }
        String eventTime = event.getEventEndTime().replace(event.getEventEndTime().subSequence(11, 16), time);
        long difference = convertDateToMillisecond(event.getEventEndTime()) - convertDateToMillisecond(eventTime);
        long marging = difference * eventViewHeight / duration;
        return (int) marging;
    }

    private String getTextByState(String details) {
        if (mIsOpenState) {
            return details;
        } else {
            return StringUtil.getResizedString(details, 6);
        }
    }

    private class EventsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            ArrayList<Event> filtered = new ArrayList<>();
            ArrayList<Event> toDelete = new ArrayList<>();
            filtered.addAll(mEvents);

            for (Event event : filtered) {

                if (((mIsSelectionMode || !mIsWorkingTimeChecked) && (event.getType() != 0 || event.getEventGroupID() == 20))
                        || (!mIsStopEventChecked && (event.getType() == 0 && event.getEventGroupID() != 20))) {
                    toDelete.add(event);
                }

            }
            filtered.removeAll(toDelete);

            results.values = filtered;
            results.count = filtered.size();

            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mEventsFiltered = (ArrayList<Event>) results.values;

            notifyDataSetChanged();
        }

    }
}


