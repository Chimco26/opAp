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


    private final OnNotificationResponseSelected mListener;
    private final Context mContext;
    private ArrayList<Notification> mNotificationsList = new ArrayList<>();
    private int mFirstTodayPosition;
    private int mFirstYesterdayPosition;

    public NotificationHistoryAdapter(Context context, ArrayList<Notification> notificationHistory, OnNotificationResponseSelected listener) {
        this.mContext = context;

//        for (Notification item : notificationHistory) {
//            if (item.getmNotificationType() != Consts.NOTIFICATION_TYPE_TECHNICIAN){
//                mNotificationsList.add(item);
//            }
//        }

        mNotificationsList = notificationHistory;

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
            date = TimeUtils.getDateForNotification(notification.getmSentTime());
            c.setTime(date);

            if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == c.get(Calendar.DAY_OF_YEAR) && i < mFirstTodayPosition){
                mFirstTodayPosition = i;
            }

            if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == c.get(Calendar.DAY_OF_YEAR)+1 && i < mFirstYesterdayPosition){
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
        Date date = TimeUtils.getDateForNotification(notification.getmSentTime());
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
            }else if (position == mFirstYesterdayPosition){
                holder.mDayTitleTv.setText(R.string.yesterday);
                holder.mDayTitleTv.setVisibility(View.VISIBLE);
            }else {
                holder.mDayTitleTv.setVisibility(View.GONE);
            }
        }

        switch (notification.getmResponseType()){
            case Consts.NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS:
                holder.mClarifyBtn.setVisibility(View.GONE);
                holder.mApproveBtn.setVisibility(View.GONE);
                holder.mDeclineBtn.setVisibility(View.GONE);
                break;

            case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                holder.mClarifyBtn.setVisibility(View.GONE);
                holder.mApproveBtn.setVisibility(View.GONE);
                holder.mDeclineBtn.setVisibility(View.GONE);

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.call_recieved));
                }else {
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.v));
                }
                break;

            case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                holder.mClarifyBtn.setVisibility(View.GONE);
                holder.mApproveBtn.setVisibility(View.GONE);
                holder.mDeclineBtn.setVisibility(View.GONE);

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.call_declined));
                }else {
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.x));
                }
                break;


            case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.at_work_blue));
                break;


            case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.service_done));
                break;

            default:
                holder.mClarifyBtn.setVisibility(View.VISIBLE);
                holder.mApproveBtn.setVisibility(View.VISIBLE);
                holder.mDeclineBtn.setVisibility(View.VISIBLE);

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.technician_dark));
                }else {
                    holder.mIconIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.message_dark));
                }

                break;

        }

        holder.mBodyTv.setText(notification.getmBody());
        holder.mSenderTv.setText(notification.getmSender());
        holder.mTimeTv.setText(time);

        if (mNotificationsList.get(position).getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN){
            holder.mBtnsLil.setVisibility(View.GONE);
            if (notification.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET){
                holder.mBodyTv.setText(mContext.getResources().getString(R.string.default_unanswered_technocian_call));
            }
        }else {

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
        }
    }

    public interface OnNotificationResponseSelected{
        void onNotificationResponse(int notificationId, int responseType);
    }
}
