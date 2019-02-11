package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 10/10/2018.
 */

public class NotificationHistoryAdapter extends RecyclerView.Adapter<NotificationHistoryAdapter.ViewHolder>{


    private static final long MILLIS_IN_DAYS = 1000 * 60 * 60 * 24;
    private final OnNotificationResponseSelected mListener;
    private final Context mContext;
    private Calendar mCalendar;
    private ArrayList<Notification> mNotificationsList = new ArrayList<>();
    private int mFirstTodayPosition;
    private int mFirstYesterdayPosition;

    public NotificationHistoryAdapter(Context context, ArrayList<Notification> notificationHistory, OnNotificationResponseSelected listener) {
        this.mContext = context;

        mNotificationsList = notificationHistory;
        mCalendar = Calendar.getInstance();
        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        mListener = listener;
        mFirstTodayPosition = mNotificationsList.size();
        mFirstYesterdayPosition = mNotificationsList.size();
        getTitlesPosition();
    }

    private void getTitlesPosition() {

        Date date;
        Notification notification;
        Calendar c = Calendar.getInstance();

        for (int i = 0; i < mNotificationsList.size(); i++) {

            notification = mNotificationsList.get(i);
            date = TimeUtils.getDateForNotification(notification.getmResponseDate());
            if (date == null){
                date = TimeUtils.getDateForNotification(notification.getmSentTime());
            }

            if (date != null) {
                c.setTime(date);

                if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == c.get(Calendar.DAY_OF_YEAR) && i < mFirstTodayPosition) {
                    mFirstTodayPosition = i;
                }

                if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == c.get(Calendar.DAY_OF_YEAR) + 1 && i < mFirstYesterdayPosition) {
                    mFirstYesterdayPosition = i;
                }
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

        if (date != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            String minutes;
            String hours;

            if (c.get(Calendar.MINUTE) < 10 ){
                minutes = "0" + c.get(Calendar.MINUTE);
            }else {
                minutes = c.get(Calendar.MINUTE) + "";
            }
            if (c.get(Calendar.HOUR_OF_DAY) < 10 ){
                hours = "0" + c.get(Calendar.HOUR_OF_DAY);
            }else {
                hours = c.get(Calendar.HOUR_OF_DAY) + "";
            }

            time = hours + ":" + minutes;

            if (position == mFirstTodayPosition){
                holder.mDayTitleTv.setText(R.string.today);
                holder.mDayTitleTv.setVisibility(View.VISIBLE);
            }else if (position == mFirstYesterdayPosition) {
                holder.mDayTitleTv.setText(R.string.yesterday);
                holder.mDayTitleTv.setVisibility(View.VISIBLE);
            }else if (mCalendar.get(Calendar.DAY_OF_YEAR) != c.get(Calendar.DAY_OF_YEAR)){
                holder.mDayTitleTv.setText(TimeUtils.getDateFromFormat(c.getTime(), TimeUtils.ONLY_DATE_FORMAT));
                holder.mDayTitleTv.setVisibility(View.VISIBLE);
                mCalendar = c;
            }else {
                holder.mDayTitleTv.setVisibility(View.GONE);
            }
        }

        holder.mBodyTv.setText(notification.getmBody(mContext));
        holder.mTimeTv.setText(time);

        switch (notification.getmResponseType()){
            case Consts.NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS:
                holder.mClarifyBtn.setVisibility(View.GONE);
                holder.mApproveBtn.setVisibility(View.GONE);
                holder.mDeclineBtn.setVisibility(View.GONE);
                holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.question_circle_outline));
                holder.mSubtextTv.setText(mContext.getResources().getString(R.string.more_details));
                break;

            case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                holder.mClarifyBtn.setVisibility(View.GONE);
                holder.mApproveBtn.setVisibility(View.GONE);
                holder.mDeclineBtn.setVisibility(View.GONE);

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
                    holder.mSubtextTv.setText(mContext.getResources().getString(R.string.call_approved));
                    holder.mBodyTv.setText(String.format(mContext.getResources().getString(R.string.call_approved2), notification.getmSender()));
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.call_sent_blue));
                }else {
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.baseline_check_circle));
                }
                break;

            case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                holder.mClarifyBtn.setVisibility(View.GONE);
                holder.mApproveBtn.setVisibility(View.GONE);
                holder.mDeclineBtn.setVisibility(View.GONE);

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
                    holder.mSubtextTv.setText(mContext.getResources().getString(R.string.call_declined));
                    holder.mBodyTv.setText(String.format(mContext.getResources().getString(R.string.call_declined2), notification.getmSender()));
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.call_declined));
                }else {
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.close_circle_outline));
                }
                break;


            case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.at_work_blue));
                holder.mSubtextTv.setText(mContext.getResources().getString(R.string.at_work));
                holder.mBodyTv.setText(String.format(mContext.getResources().getString(R.string.started_service2), notification.getmSender()));
                break;


            case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                holder.mSubtextTv.setText(mContext.getResources().getString(R.string.service_completed));
                holder.mBodyTv.setText(String.format(mContext.getResources().getString(R.string.service_completed2), notification.getmSender()));
                holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.service_done));
                break;

            case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cancel_blue));
                holder.mSubtextTv.setText(mContext.getString(R.string.service_call_was_canceled));
                holder.mBodyTv.setText(String.format(mContext.getResources().getString(R.string.call_cancelled2), notification.getmSender()));
                break;

            default:
                holder.mClarifyBtn.setVisibility(View.VISIBLE);
                holder.mApproveBtn.setVisibility(View.VISIBLE);
                holder.mDeclineBtn.setVisibility(View.VISIBLE);

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.call_recieved));
                    holder.mSubtextTv.setText(mContext.getResources().getString(R.string.waiting_for_replay));
                }else {
                    holder.mSubtextTv.setText("");
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_icon));
                }

                break;

        }


        if (mNotificationsList.get(position).getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
            holder.mSenderTv.setText(mContext.getString(R.string.technician) + " " + notification.getmTargetName());
            holder.mBtnsLil.setVisibility(View.GONE);
            if (notification.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET){
                holder.mBodyTv.setText(String.format(mContext.getResources().getString(R.string.default_unanswered_technician_call), notification.getmSender()));
            }
        }else {

            holder.mSenderTv.setText(notification.getmSender());
            holder.mBtnsLil.setVisibility(View.VISIBLE);

            holder.mApproveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNotificationResponse(mNotificationsList.get(position).getmNotificationID(), Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE);
                }
            });

            holder.mDeclineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNotificationResponse(mNotificationsList.get(position).getmNotificationID(), Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE);
                }
            });

            holder.mClarifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onNotificationResponse(mNotificationsList.get(position).getmNotificationID(), Consts.NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mNotificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mSubtextTv;
        private LinearLayout mBtnsLil;
        private Button mApproveBtn;
        private Button mClarifyBtn;
        private Button mDeclineBtn;
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
