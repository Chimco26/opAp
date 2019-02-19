package com.operatorsapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.utils.Consts;

import java.util.ArrayList;

/**
 * Created by alex on 23/10/2018.
 */

public class NotificationsPagerAdapter extends PagerAdapter {

    private static final String TITLE_RIGHT = "Service Calls";
    private static final String TITLE_LEFT = "General";
    private static final int ITEM_RIGHT = 1;
    private static final int ITEM_LEFT = 0;

    private static int NUM_ITEMS = 1;
    private final Context mContext;
    private final ArrayList<Notification> mNotificationList;
    private final NotificationHistoryAdapter.OnNotificationResponseSelected mListener;

    public NotificationsPagerAdapter(Context context, ArrayList<Notification> notificationHistory, NotificationHistoryAdapter.OnNotificationResponseSelected listener) {
        mContext = context;
        mNotificationList = notificationHistory;
        mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final NotificationHistoryAdapter notificationHistoryAdapter = new NotificationHistoryAdapter(mContext, getNotificationList(position), mListener);

        RecyclerView rvDialog = new RecyclerView(mContext);
        rvDialog.setBackgroundColor(mContext.getResources().getColor(R.color.white_five));
        rvDialog.setLayoutManager(new LinearLayoutManager(mContext));
        rvDialog.setAdapter(notificationHistoryAdapter);
        container.addView(rvDialog);

        return rvDialog;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        
        switch (position){
            case 0:
                return TITLE_LEFT;

            case 1:{
                return TITLE_RIGHT;
            }
        }
        return "";

    }

    private ArrayList<Notification> getNotificationList(int position) {

        ArrayList<Notification> tempList = new ArrayList<>();


        switch (position){

            case ITEM_LEFT:

                for (Notification notification : mNotificationList) {
                    if (notification.getmNotificationType() != Consts.NOTIFICATION_TYPE_TECHNICIAN) {
                        tempList.add(notification);
                    }

                }

                break;

            case ITEM_RIGHT:

                for (Notification notification : mNotificationList) {
                    if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN && notification.getmResponseType() != Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED) {
                        tempList.add(notification);
                    }

                }

                break;

        }

        return tempList;
    }

//    public interface OnNotificationResponseSelected{
//        void onNotificationResponse(int notificationId, int responseType);
//    }

}
