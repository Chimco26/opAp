package com.operatorsapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.adapters.NotificationHistoryAdapter;
import com.operatorsapp.adapters.NotificationsTemplateAdapter;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.TopNotificationRequest;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsDialog extends Dialog implements View.OnClickListener, NotificationHistoryAdapter.OnNotificationResponseSelected {

    private static final int DEFAULT_NUM_TEMPLATE = 10;
    private final Context mContext;
    private NotificationsDialogListener mListener;
    private EditText newNotBodyEt;
    private SwipeRefreshLayout swipeRefresh;
    private ImageView newNotSendIv;
    private ImageView newNotTemplateIv;
    private RecyclerView recyclerView;
    private ArrayList<Notification> mTemplateList;

    public NotificationsDialog(Context context, NotificationsDialogListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.x = Gravity.END;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(wlp);

        setContentView(R.layout.notification_view_pager);

        recyclerView = findViewById(R.id.NVP_view_pager);
        swipeRefresh = findViewById(R.id.NVP_swipe);
        newNotBodyEt = findViewById(R.id.NVP_create_notification_tv);
        newNotSendIv = findViewById(R.id.NVP_create_notification_iv);
        newNotTemplateIv = findViewById(R.id.NVP_template_notification_iv);

        newNotSendIv.setOnClickListener(this);
        newNotTemplateIv.setOnClickListener(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListener.onGetNotificationsFromServer(true);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new NotificationHistoryAdapter(getContext(), getNotificationList(), this));

        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.NVP_create_notification_iv:
                if (!newNotBodyEt.getText().toString().isEmpty()){

                    mListener.onSendNewNotification(newNotBodyEt.getText().toString());
                }
                break;
            case R.id.NVP_template_notification_iv:
                if (mTemplateList == null || mTemplateList.isEmpty()) {
                    getTopNotifications();
                }else {
                    openTemplateDialog();
                }
                break;

        }
    }

    private void getTopNotifications() {
        ProgressDialogManager.show((AppCompatActivity) mContext);
        NetworkManager.getInstance().getTopNotification(new TopNotificationRequest(PersistenceManager.getInstance().getSessionId(), DEFAULT_NUM_TEMPLATE), new Callback<NotificationHistoryResponse>() {
            @Override
            public void onResponse(Call<NotificationHistoryResponse> call, Response<NotificationHistoryResponse> response) {
                ProgressDialogManager.dismiss();
                if (response.body() != null && response.body().getError().getErrorDesc() == null) {
                    mTemplateList = response.body().getmNotificationsList();
                    openTemplateDialog();
                }else {
                    onFailure(call, new Throwable("failed to get templates"));
                }
            }

            @Override
            public void onFailure(Call<NotificationHistoryResponse> call, Throwable t) {
                ProgressDialogManager.dismiss();
            }
        });
    }

    private void openTemplateDialog() {
        final Dialog templateDialog = new Dialog(getContext());
        templateDialog.setCanceledOnTouchOutside(true);
        templateDialog.setContentView(R.layout.notification_template_dialog);
        templateDialog.getWindow().setGravity(Gravity.END|Gravity.TOP);

        RecyclerView templateRv = templateDialog.findViewById(R.id.template_dialog_rv);
        templateRv.setLayoutManager(new LinearLayoutManager(getContext()));
        templateRv.setAdapter(new NotificationsTemplateAdapter(mTemplateList, new NotificationsTemplateAdapter.TemplateAdapterListener() {
            @Override
            public void onTemplateClicked(int position) {
                newNotBodyEt.setText(mTemplateList.get(position).getmBody(getContext()));
                templateDialog.dismiss();
            }
        }));

        templateDialog.show();
    }

    private ArrayList<Notification> getNotificationList() {
//
//        ArrayList<Notification> notificationList = PersistenceManager.getInstance().getNotificationHistory();
//        ArrayList<Notification> tempList = new ArrayList<>();
//
//        for (Notification notification : notificationList) {
//            if (notification.getmNotificationType() != Consts.NOTIFICATION_TYPE_TECHNICIAN) {
//                tempList.add(notification);
//            }
//
//        }
//        return tempList;
        TextView noNotificationsTitleTv = findViewById(R.id.NVP_no_notifications_tv);
        ArrayList<Notification> list = PersistenceManager.getInstance().getNotificationHistoryNoTech();
        if (list == null || list.size() == 0){
            noNotificationsTitleTv.setVisibility(View.VISIBLE);
        }else {
            noNotificationsTitleTv.setVisibility(View.GONE);
        }
        return list;
    }

    @Override
    public void onNotificationResponse(int notificationId, int responseType) {
        mListener.onNotificationResponse(notificationId, responseType);
    }

    public interface NotificationsDialogListener{
        void onSendNewNotification(String text);
        void onNotificationResponse(int notificationId, int responseType);
        void onGetNotificationsFromServer(boolean openNotificationDialog);
    }
}
