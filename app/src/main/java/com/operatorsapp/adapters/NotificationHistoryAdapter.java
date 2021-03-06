package com.operatorsapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by alex on 10/10/2018.
 */

public class NotificationHistoryAdapter extends RecyclerView.Adapter<NotificationHistoryAdapter.ViewHolder>{


    private static final long MILLIS_IN_DAYS = 1000 * 60 * 60 * 24;
    private final OnNotificationResponseSelected mListener;
    private Calendar mCalendar;
    private ArrayList<Notification> mNotificationsList = new ArrayList<>();
    private int mFirstTodayPosition;
    private int mFirstYesterdayPosition;

    public NotificationHistoryAdapter(ArrayList<Notification> notificationHistory, OnNotificationResponseSelected listener) {

        mNotificationsList = notificationHistory;
        mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        mListener = listener;
        mFirstTodayPosition = -1; // mNotificationsList.size();
        mFirstYesterdayPosition = -1;//mNotificationsList.size();
        getTitlesPosition();
    }

    private void getTitlesPosition() {

        Date date;
        Notification notification;
        mFirstYesterdayPosition = mNotificationsList.size();

        //order by notification response date
        Collections.sort(mNotificationsList, new Comparator<Notification>() {
            @Override
            public int compare(Notification o1, Notification o2) {

                o1.setmSentTime(TimeUtils.getStringNoTFormatForNotification(o1.getmSentTime()));
                o1.setmResponseDate(TimeUtils.getStringNoTFormatForNotification(o1.getmResponseDate()));
                o2.setmSentTime(TimeUtils.getStringNoTFormatForNotification(o2.getmSentTime()));
                o2.setmResponseDate(TimeUtils.getStringNoTFormatForNotification(o2.getmResponseDate()));

                Date d1 = TimeUtils.getDateForNotification(o1.getmResponseDate());
                Date d2 = TimeUtils.getDateForNotification(o2.getmResponseDate());

                if (d1 == null) {
                    return 1;
                } else if (d2 == null) {
                    return -1;
                } else if (d1.after(d2)) {
                    return -1;
                } else if(d1.before(d2)) {
                    return 1;
                }else {
                    return 0;
                }
            }
        });


        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);

        Calendar yesterdayCalendar = Calendar.getInstance();
        yesterdayCalendar.add(Calendar.DAY_OF_YEAR, -1);
        yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        yesterdayCalendar.set(Calendar.MINUTE, 0);

        for (int i = 0; i < mNotificationsList.size(); i++) {

            notification = mNotificationsList.get(i);
            if (notification.getmResponseDate() != null && notification.getmResponseDate().length() > 0) {
                date = TimeUtils.getDateForNotification(notification.getmResponseDate());
            } else {
                date = TimeUtils.getDateForNotification(notification.getmSentTime());
            }

            if (date != null && date.after(todayCalendar.getTime()) && i == 0) {
                mFirstTodayPosition = 0;
            }else  if (date != null && date.after(yesterdayCalendar.getTime()) && date.before(todayCalendar.getTime()) && i < mFirstYesterdayPosition) {
                mFirstYesterdayPosition = i;
            }
        }
    }

    @NonNull
    @Override
    public NotificationHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_recycler_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHistoryAdapter.ViewHolder holder, final int position) {

        Notification notification = mNotificationsList.get(position);
        Date date = TimeUtils.getDateForNotification(notification.getmResponseDate());
        if (position > 0){
            mCalendar.setTime(TimeUtils.getDateForNotification(mNotificationsList.get(position -1).getmResponseDate()));
        }

        String time = "";
        String name = notification.getmOriginalSenderName();
        String body;

        if (date != null) {
            Calendar previousCalendar = Calendar.getInstance();
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(date);
            time = getTime(currentCalendar);

            if (position == 0)
            {
                holder.mDayTitleTv.setVisibility(View.VISIBLE);
                if (position == mFirstTodayPosition){
                    holder.mDayTitleTv.setText(R.string.today);
                }else {
                    holder.mDayTitleTv.setText(TimeUtils.getDateFromFormat(currentCalendar.getTime(), TimeUtils.ONLY_DATE_FORMAT));
                }
            } else{
                previousCalendar.setTime(getDate(mNotificationsList.get(position -1)));
                if (position == mFirstYesterdayPosition)
                {
                    holder.mDayTitleTv.setText(R.string.yesterday);
                    holder.mDayTitleTv.setVisibility(View.VISIBLE);
                } else if (previousCalendar.get(Calendar.DAY_OF_YEAR) != currentCalendar.get(Calendar.DAY_OF_YEAR))
                {
                    holder.mDayTitleTv.setText(TimeUtils.getDateFromFormat(currentCalendar.getTime(), TimeUtils.ONLY_DATE_FORMAT));
                    holder.mDayTitleTv.setVisibility(View.VISIBLE);
                } else {
                    holder.mDayTitleTv.setVisibility(View.GONE);
                }
            }
        }
        Context context = holder.itemView.getContext();
        holder.mBodyTv.setText(notification.getmBody(context));
        holder.mTimeTv.setText(time);

        switch (notification.getmResponseType()){
            case Consts.NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS:
                holder.mClarifyBtn.setVisibility(View.GONE);
                holder.mApproveBtn.setVisibility(View.GONE);
                holder.mDeclineBtn.setVisibility(View.GONE);
                holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.question_circle_outline));
                holder.mSubtextTv.setText(context.getResources().getString(R.string.more_details));
                break;

            case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                holder.mClarifyBtn.setVisibility(View.GONE);
                holder.mApproveBtn.setVisibility(View.GONE);
                holder.mDeclineBtn.setVisibility(View.GONE);

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
                    holder.mSubtextTv.setText(context.getResources().getString(R.string.call_approved));
                    holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.call_sent_blue));

//                    if (name.replaceAll("[^a-zA-Z0-9_-]", "").equals(notification.getmOriginalSenderName())) {
//                        body = String.format(context.getResources().getString(R.string.call_approved2), name);
//                    } else{
//                        String msg = context.getResources().getString(R.string.call_approved2);
//                        body = msg.replace("%1$s", notification.getmOriginalSenderName());
//                    }

                    body = context.getResources().getString(R.string.call_approved2).replace(context.getResources().getString(R.string.placeholder1), name);
                    holder.mBodyTv.setText(body);
//                    holder.mBodyTv.setText(String.format(context.getResources().getString(R.string.call_approved2), notification.getmOriginalSenderName()));
                }else {
                    holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_check_circle));
                }
                break;

            case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                holder.mClarifyBtn.setVisibility(View.GONE);
                holder.mApproveBtn.setVisibility(View.GONE);
                holder.mDeclineBtn.setVisibility(View.GONE);

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
                    holder.mSubtextTv.setText(context.getResources().getString(R.string.call_declined));
                    holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.call_declined));

//                    if (name.replaceAll("[^a-zA-Z0-9_-]", "").equals(notification.getmOriginalSenderName())) {
//                        body = String.format(context.getResources().getString(R.string.call_declined2), name);
//                    } else{
//                        String msg = context.getResources().getString(R.string.call_declined2);
//                        body = msg.replace("%1$s", notification.getmOriginalSenderName());
//                    }
                    body = context.getResources().getString(R.string.call_declined2).replace(context.getResources().getString(R.string.placeholder1), name);
                    holder.mBodyTv.setText(body);
//                    holder.mBodyTv.setText(String.format(context.getResources().getString(R.string.call_declined2), notification.getmOriginalSenderName()));
                }else {
                    holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.close_circle_outline));
                }
                break;


            case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.at_work_blue));
                holder.mSubtextTv.setText(context.getResources().getString(R.string.at_work));

//                if (name.replaceAll("[^a-zA-Z0-9_-]", "").equals(notification.getmOriginalSenderName())) {
//                    body = String.format(context.getResources().getString(R.string.started_service2), name);
//                } else{
//                    String msg = context.getResources().getString(R.string.started_service2);
//                    body = msg.replace("%1$s", notification.getmOriginalSenderName());
//                }

                body = context.getResources().getString(R.string.started_service2).replace(context.getResources().getString(R.string.placeholder1), name);
                holder.mBodyTv.setText(body);
//                holder.mBodyTv.setText(String.format(context.getResources().getString(R.string.started_service2), notification.getmOriginalSenderName()));
                break;


            case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                holder.mSubtextTv.setText(context.getResources().getString(R.string.service_completed));

//                if (name.replaceAll("[^a-zA-Z0-9_-]", "").equals(notification.getmOriginalSenderName())) {
//                    body = String.format(context.getResources().getString(R.string.service_completed2), name);
//                } else{
//                    String msg = context.getResources().getString(R.string.service_completed2);
//                    body = msg.replace("%1$s", notification.getmOriginalSenderName());
//                }

                body = context.getResources().getString(R.string.service_completed2).replace(context.getResources().getString(R.string.placeholder1), name);
                holder.mBodyTv.setText(body);
//                holder.mBodyTv.setText(String.format(context.getResources().getString(R.string.service_completed2), notification.getmOriginalSenderName()));
                holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.service_done));
                break;

            case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.cancel_blue));
                holder.mSubtextTv.setText(context.getString(R.string.service_call_was_canceled));

//                if (name.replaceAll("[^a-zA-Z0-9_-]", "").equals(notification.getmOriginalSenderName())) {
//                    body = String.format(context.getResources().getString(R.string.call_cancelled2), name);
//                } else{
//                    String msg = context.getResources().getString(R.string.call_cancelled2);
//                    body = msg.replace("%1$s", notification.getmOriginalSenderName());
//                }

                body = context.getResources().getString(R.string.call_cancelled2).replace(context.getResources().getString(R.string.placeholder1), name);
                holder.mBodyTv.setText(body);
//                holder.mBodyTv.setText(String.format(context.getResources().getString(R.string.call_cancelled2), notification.getmOriginalSenderName()));
                break;

            default:
                holder.mClarifyBtn.setVisibility(View.VISIBLE);
                holder.mApproveBtn.setVisibility(View.VISIBLE);
                holder.mDeclineBtn.setVisibility(View.VISIBLE);

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
                    holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.call_recieved));
                    holder.mSubtextTv.setText(context.getResources().getString(R.string.waiting_for_replay));
                }else {
                    holder.mSubtextTv.setText("");
                    holder.mIconIv.setImageDrawable(context.getResources().getDrawable(R.drawable.message_icon));
                }

                break;

        }


        if (mNotificationsList.get(position).getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
            holder.mSenderTv.setText(context.getString(R.string.technician) + " " + notification.getmTargetName());
            holder.mBtnsLil.setVisibility(View.GONE);
            if (notification.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET){
                name = notification.getmSender();
//                if (name.replaceAll("[^a-zA-Z0-9_-]", "").equals(notification.getmSender())) {
//                    String txt = context.getResources().getString(R.string.default_unanswered_technician_call);
//                    holder.mBodyTv.setText(String.format(txt, name));
//                } else{
//                    String msg = context.getResources().getString(R.string.default_unanswered_technician_call);
//                    holder.mBodyTv.setText(msg.replace("%1$s", notification.getmSender()));
//                }

                body = context.getResources().getString(R.string.default_unanswered_technician_call).replace(context.getResources().getString(R.string.placeholder1), name);
                holder.mBodyTv.setText(body);
            }
        }else {

//            holder.mSenderTv.setText(notification.getmSender());
            holder.mSenderTv.setText(PersistenceManager.getInstance().getCurrentLang().equals("en") ? notification.getmOriginalSenderName() : notification.getmOriginalSenderHName());
            holder.mBtnsLil.setVisibility(View.VISIBLE);

            holder.mApproveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onNotificationResponse(mNotificationsList.get(position).getmNotificationID(), Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE);
                    }
                }
            });

            holder.mDeclineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onNotificationResponse(mNotificationsList.get(position).getmNotificationID(), Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE);
                    }
                }
            });

            holder.mClarifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onNotificationResponse(mNotificationsList.get(position).getmNotificationID(), Consts.NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS);
                    }
                }
            });
        }
    }

    private String getTime(Calendar c) {
        String time;
        String minutes;
        String hours;

        if (c.get(Calendar.MINUTE) < 10) {
            minutes = "0" + c.get(Calendar.MINUTE);
        } else {
            minutes = c.get(Calendar.MINUTE) + "";
        }
        if (c.get(Calendar.HOUR_OF_DAY) < 10) {
            hours = "0" + c.get(Calendar.HOUR_OF_DAY);
        } else {
            hours = c.get(Calendar.HOUR_OF_DAY) + "";
        }

        time = hours + ":" + minutes;
        return time;
    }

    private Date getDate(Notification notification) {
        Date date;
        if (notification.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET) {
            date = TimeUtils.getDateForNotification(notification.getmSentTime());
        } else {
            date = TimeUtils.getDateForNotification(notification.getmResponseDate());
        }
        return date;
    }

    @Override
    public int getItemCount() {
        return mNotificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mSubtextTv;
        private LinearLayout mBtnsLil;
        private ImageView mApproveBtn;
        private ImageView mClarifyBtn;
        private ImageView mDeclineBtn;
        private TextView mDayTitleTv;
        private TextView mBodyTv;
        private TextView mSenderTv;
        private TextView mTimeTv;
        private ImageView mIconIv;

        public ViewHolder(View view) {
            super(view);

            mDayTitleTv = view.findViewById(R.id.notification_item_tv_day_title);
            mApproveBtn = view.findViewById(R.id.notification_item_approve_btn);
            mClarifyBtn = view.findViewById(R.id.notification_item_clarify_btn);
            mDeclineBtn = view.findViewById(R.id.notification_item_decline_btn);
            mBodyTv = view.findViewById(R.id.notification_item_tv_body);
            mSenderTv = view.findViewById(R.id.notification_item_tv_sender);
            mTimeTv = view.findViewById(R.id.notification_item_tv_time);
            mIconIv = view.findViewById(R.id.notification_item_iv);
            mBtnsLil = view.findViewById(R.id.notification_item_btns_lil);
            mSubtextTv = view.findViewById(R.id.notification_item_subtext_tv);
        }
    }

    public interface OnNotificationResponseSelected{
        void onNotificationResponse(int notificationId, int responseType);
    }
}
