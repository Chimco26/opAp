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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.common.Event;
import com.example.common.actualBarExtraResponse.Inventory;
import com.example.common.actualBarExtraResponse.Notification;
import com.example.common.actualBarExtraResponse.Reject;
import com.example.common.machineJoshDataResponse.JobDataItem;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.utils.StringUtil;
import com.operatorsapp.utils.TimeUtils;
import com.ravtech.david.sqlcore.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static com.operatorsapp.utils.TimeUtils.SIMPLE_FORMAT_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_T_FORMAT;
import static com.operatorsapp.utils.TimeUtils.convertDateToMillisecond;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
//        implements Filterable {

    private static final int PIXEL_FOR_MINUTE = 4;
    private boolean mIsOpenState;
    private boolean mIsSelectionMode;
    private final OnStopClickListener mOnStopClickListener;

    private Context mContext;
    private ArrayList<Event> mEvents = new ArrayList<>();
    private ArrayList<Float> mSelectedEvents;
    private boolean mIsServiceCallsChecked = true, mIsmMessagesChecked = true, mIsRejectsChecked = true, mIsProductionReportChecked = true;
    //    private EventsFilter mFilter;
    private ArrayList<Event> mEventsFiltered = new ArrayList<>();
    private float mFactor = 1;

    public EventsAdapter(Context context, OnStopClickListener onStopClickListener, boolean selectMode, boolean closedState) {
        mContext = context;
        mOnStopClickListener = onStopClickListener;
        mIsSelectionMode = selectMode;
        mIsOpenState = closedState;
//        mFilter = new EventsFilter();
//        getFilter().filter("");
    }

    public void setSelectedEvents(ArrayList<Float> selectedEvents) {
        mSelectedEvents = selectedEvents;
    }

    public void setEvents(ArrayList<Event> events) {
        mEvents = events;
        mEventsFiltered = events;
//        getFilter().filter("");
    }

    public void setIsSelectionMode(boolean mIsSelectionMode) {
        this.mIsSelectionMode = mIsSelectionMode;
    }

    public void setCheckedFilters(boolean isServiceCallsChecked, boolean isMessagesChecked, boolean isRejectsChecked, boolean isProductionReportChecked) {

        mIsServiceCallsChecked = isServiceCallsChecked;
        mIsmMessagesChecked = isMessagesChecked;
        mIsRejectsChecked = isRejectsChecked;
        mIsProductionReportChecked = isProductionReportChecked;
//        getFilter().filter("");

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

    public void setFactor(float factor) {
        if (mFactor - 0.05 > factor || mFactor + 0.05 < factor) {
            mFactor = factor;
            notifyDataSetChanged();
        }
    }

//    @Override
//    public Filter getFilter() {
//        return mFilter;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final View mCircle;
        private final ImageView mCheckIc;
        private View mCircleSelector;
        private TextView mText;
        private TextView mTime;
        private View mLine;
        private RelativeLayout mCheckContainer;
        private CheckBox mCheckBox;
        private RelativeLayout mTechContainer;
        private ImageView mScissors;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mText = itemView.findViewById(R.id.EI_text);
            mTime = itemView.findViewById(R.id.EI_time);
            mLine = itemView.findViewById(R.id.EI_line);
            mCircle = itemView.findViewById(R.id.EI_circle);
            mCircleSelector = itemView.findViewById(R.id.EI_circle_selector);
            mCheckIc = itemView.findViewById(R.id.EI_check_ic);
            mCheckContainer = itemView.findViewById(R.id.EI_check_container);
            mCheckBox = itemView.findViewById(R.id.EI_text_check);
            mTechContainer = itemView.findViewById(R.id.EI_service_call_container);
            mScissors = itemView.findViewById(R.id.EI_Scissors);
        }

        private void updateItem(int position, final ViewHolder holder) {

            if (position > mEventsFiltered.size() - 1) {
                return;
            }
            final Event event = mEventsFiltered.get(position);

            if (event.getEventEndTime() == null || event.getEventEndTime().length() == 0) {
                event.setEventEndTime(TimeUtils.getDateFromFormat(new Date(), SIMPLE_FORMAT_FORMAT));
            }

            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (mOnStopClickListener != null && event.getType() < 1) {
                        mOnStopClickListener.onStopEventSelected(event.getEventID(), isChecked);
                    }
                    GradientDrawable selectorBackground = (GradientDrawable) mCircleSelector.getBackground();
                    if (isChecked) {
                        selectorBackground.setColor(Color.parseColor(event.getColor()));
                    } else {
                        selectorBackground.setColor(mCircleSelector.getContext().getResources().getColor(R.color.white));
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
            updateNotification(event, params.height);

            mLine.setBackgroundColor(Color.parseColor(event.getColor()));
            GradientDrawable circleBackground = (GradientDrawable) mCircle.getBackground();
            circleBackground.setColor(Color.parseColor(event.getColor()));

            String textTime = "";

            textTime = event.getEventEndTime().substring(10, 16);

            mTime.setText(textTime);

//            if (mIsStopEventChecked) {
            mText.setVisibility(View.VISIBLE);
//            } else {
//                mText.setVisibility(View.GONE);
//            }

            long duration = TimeUnit.MILLISECONDS.toMinutes(convertDateToMillisecond(event.getEventEndTime()) - convertDateToMillisecond(event.getEventTime()));
            if (duration <= 0) {
                duration = 1;
            }

            String text;

            if (!mIsOpenState) {
                text = String.format(Locale.US, "%dm", duration);
                mText.setText(text);

            } else {
                text = String.format(Locale.US, "%dm | %s", duration, OperatorApplication.isEnglishLang() ? event.getSubtitleEname() : event.getSubtitleLname());
                mText.setText(text);
            }
            GradientDrawable textBackground = (GradientDrawable) mText.getBackground();
            textBackground.setColor(Color.parseColor(event.getColor()));

            GradientDrawable selectorBackground = (GradientDrawable) mCircleSelector.getBackground();
            if (event.isChecked() && mIsSelectionMode) {
                selectorBackground.setColor(Color.parseColor(event.getColor()));
            } else {
                selectorBackground.setColor(mCircleSelector.getContext().getResources().getColor(R.color.white));
            }

            if (event.getEventReasonID() != 0 && event.getEventReasonID() != 18 && event.getEventGroupID() != 20) {
                mCheckIc.setVisibility(View.VISIBLE);
                mCheckIc.setColorFilter(Color.parseColor(event.getColor()));
            } else {
                mCheckIc.setVisibility(View.GONE);
            }

            if (!mIsSelectionMode && position == 0 && event.getEventEndTime().isEmpty()
                    && event.getEventReasonID() != 100) {
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

            holder.mScissors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateDialog(event.getEventID());
                }
            });
        }

        private void updateNotification(Event event, int height) {
            if (mTechContainer.getChildCount() > 0) {
                mTechContainer.removeAllViews();
            }
            if (event.getNotifications() != null && event.getNotifications().size() > 0) {
                for (Notification notification : event.getNotifications()) {

                    String text = mTechContainer.getContext().getString(R.string.message);
                    if (notification.getNotificationType() == 1 && mIsmMessagesChecked) {
                        long startTimeMilli = convertDateToMillisecond(notification.getResponseDate(), SQL_T_FORMAT);
                        setNotification(event, 1, notification.getResponseDate().substring(11, 16), getTextByState(text, 6), 0, notification.getResponseDate());
                    } else if (notification.getNotificationType() == 2 && mIsServiceCallsChecked) {
                        text = String.format("%s - %s", getCallTextById(notification.getResponseTypeID()), notification.getTargetUserName());
                        long startTimeMilli = convertDateToMillisecond(notification.getResponseDate(), SQL_T_FORMAT);
                        setNotification(event, 2, notification.getResponseDate().substring(11, 16), getTextByState(text, 6), notification.getResponseTypeID(), notification.getResponseDate());
                    }
                }
            }

            if (mIsProductionReportChecked && event.getInventories() != null && event.getInventories().size() > 0) {
                for (Inventory inventory : event.getInventories()) {
                    long startTimeMilli = convertDateToMillisecond(inventory.getTime(), SIMPLE_FORMAT_FORMAT);
                    setNotification(event, 3, inventory.getTime(), getTextByState(inventory.getAmount() + " " + inventory.getLName(), 6), 0, inventory.getTime());
                }
            }


            if (mIsRejectsChecked && event.getRejects() != null && event.getRejects().size() > 0) {
                for (Reject reject : event.getRejects()) {

                    String name = OperatorApplication.isEnglishLang() ? reject.getEName() : reject.getLName();
                    long startTimeMilli = convertDateToMillisecond(reject.getTime(), SIMPLE_FORMAT_FORMAT);
                    setNotification(event, 4, reject.getTime().substring(11, 16), getTextByState(reject.getAmount() + " " + name, 6), 0, reject.getTime());
                }
            }
            if (event.getJobDataItems() != null && event.getJobDataItems().size() > 0) {
                for (JobDataItem jobDataItem : event.getJobDataItems()) {
                    setNotification(event, 6, jobDataItem.getStartTime().substring(11, 16),
                            getTextByState((OperatorApplication.isEnglishLang() ? jobDataItem.getEName() : jobDataItem.getLName()), 10), 0, jobDataItem.getStartTime());
                }
            }
            //for alarms
//            if (height > 10 * PIXEL_FOR_MINUTE &&
//                    event.getType() == 0 && event.getAlarmsEvents() != null && event.getAlarmsEvents().size() > 0) {
//                for (Event alarmEvent : event.getAlarmsEvents()) {
//                    long startTimeMilli = convertDateToMillisecond(alarmEvent.getTime(), SIMPLE_FORMAT_FORMAT);
//                    setNotification(event, 5, alarmEvent.getTime().substring(11, 16),
//                            getTextByState((OperatorApplication.isEnglishLang() ? alarmEvent.getSubtitleEname() : alarmEvent.getSubtitleLname()), 10), 0);
//                }
//            }

        }

        private void setNotification(Event event, int type, String time, String details, int icon, String completeTime) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.service_call_item, mTechContainer, false);
            TextView timeTV = view.findViewById(R.id.SCI_time);
            TextView detailsTV = view.findViewById(R.id.SCI_details);
            ImageView iconIV = view.findViewById(R.id.SCI_service_call_icon);
            LinearLayout serviceImgAndTextLy = view.findViewById(R.id.SCI_text_ly);
            serviceImgAndTextLy.setBackgroundColor(detailsTV.getContext().getResources().getColor(R.color.white));

            if (event.getDuration() <= 5) {
                timeTV.setVisibility(View.INVISIBLE);
            } else {
                timeTV.setVisibility(View.VISIBLE);
            }

            int eventViewHeight = getViewHeight(event);
//            eventViewHeight = filterHeight(eventViewHeight, event);
            int margin = getNotificationRelativePosition(event, completeTime, eventViewHeight);
            if (margin < 20) {
                margin = 20;
            }
            if (eventViewHeight - margin < 20) {
                margin = eventViewHeight - 20;
            }

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            params1.setMargins(0, margin, 0, 0);
            params1.setMarginStart(4);

            view.setLayoutParams(params1);
            timeTV.setText(getNotificationTime(time, margin, eventViewHeight));

            detailsTV.setText(details);
            if (type == 6) {
                view.findViewById(R.id.SCI_circle).setVisibility(View.VISIBLE);
            }
            view.findViewById(R.id.product_line).setVisibility(View.GONE);
            iconIV.setVisibility(View.VISIBLE);
//            detailsTV.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            detailsTV.setVisibility(View.VISIBLE);
            serviceImgAndTextLy.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) serviceImgAndTextLy.getLayoutParams();
            if (type == 6) {
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
            }
            serviceImgAndTextLy.setLayoutParams(params);


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
                case 5:
                    detailsTV.setBackgroundColor(mContext.getResources().getColor(R.color.alert));
                    break;
                case 6:
                    view.findViewById(R.id.SCI_circle).setVisibility(View.GONE);
                    view.findViewById(R.id.product_line).setVisibility(View.VISIBLE);
                    iconIV.setVisibility(View.GONE);
                    detailsTV.setBackgroundColor(detailsTV.getContext().getResources().getColor(R.color.transparentColor));
                    serviceImgAndTextLy.setBackgroundColor(detailsTV.getContext().getResources().getColor(R.color.transparentColor));


                    if (!mIsOpenState) {
                        detailsTV.setVisibility(View.GONE);
                        serviceImgAndTextLy.setVisibility(View.GONE);
                    }

//                    detailsTV.setBackgroundColor(mContext.getResources().getColor(R.color.transparentColor));
//                    view.findViewById(R.id.SCI_text_ly).setBackgroundColor(mContext.getResources().getColor(R.color.transparentColor));
                    break;
            }
            mTechContainer.addView(view);
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

    private String getCallTextById(Integer textByKeysValues) {
        switch (textByKeysValues) {
            case 1:
                return mContext.getString(R.string.call_approved);
            case 2:
                return mContext.getString(R.string.call_decline);
            case 4:
                return mContext.getString(R.string.started_service);
            case 5:
                return mContext.getString(R.string.service_completed);
            case 6:
                return mContext.getString(R.string.call_cancelled);
            case 0:
                return mContext.getString(R.string.waiting_for_replay);
            default:
                return mContext.getString(R.string.waiting_for_replay);
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

    private String getNotificationTime(String time, int margin, int eventViewHeight) {
        if (margin > 10 && margin < eventViewHeight - 10) {
            return time;
        } else {
            return "";
        }
    }

    private int getViewHeight(Event event) {
        if ((int) event.getDuration() * PIXEL_FOR_MINUTE > 300) {
            return (int) (300 * mFactor);
        } else if (event.getDuration() > 4) {
            return (int) (event.getDuration() * PIXEL_FOR_MINUTE * mFactor);
        } else {
            return (int) (5 * PIXEL_FOR_MINUTE * mFactor);
        }
    }

    private int getNotificationRelativePosition(Event event, String eventTime, int eventViewHeight) {
        long duration = event.getDuration() * 60 * 1000;
        if (duration == 0) {
            duration = 1;
        }
        long difference = convertDateToMillisecond(event.getEventEndTime()) - convertDateToMillisecond(eventTime);
        long marging = difference * eventViewHeight / duration;
        return (int) marging;
    }

//    private String getNotifTime(Event event, String time) {
//        String eventTime = "";
//        if (event.getEventEndTime() != null && event.getEventEndTime().length() > 0) {
//            eventTime = event.getEventEndTime().replace(event.getEventEndTime().subSequence(11, 16), time);
//        } else {
//            eventTime = event.getEventEndTime().replace(TimeUtils.getDateFromFormat(new Date(), SIMPLE_FORMAT_FORMAT).subSequence(11, 16), time);
//        }
//        return eventTime;
//    }

    private String getTextByState(String details, int size) {
        if (mIsOpenState) {
            return details;
        } else {
            return StringUtil.getResizedString(details, size);
        }
    }

//    private class EventsFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            FilterResults results = new FilterResults();
//
//            ArrayList<Event> filtered = new ArrayList<>();
//            ArrayList<Event> toDelete = new ArrayList<>();
//            filtered.addAll(mEvents);
//
//            for (Event event : filtered) {
//
//                if (((mIsSelectionMode) && (event.getType() != 0 || event.getEventGroupID() == 20))) {
//                    toDelete.add(event);
//                }
//
//            }
//            filtered.removeAll(toDelete);
//
//            results.values = filtered;
//            results.count = filtered.size();
//
//            return results;
//        }
//
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//            mEventsFiltered = (ArrayList<Event>) results.values;
//
//            notifyDataSetChanged();
//        }
//
//    }
}


