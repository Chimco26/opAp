package com.operatorsapp.firebase;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.callback.PostNotificationTokenCallback;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.utils.Consts;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 04/10/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private static final String LOG_TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d(LOG_TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

        final PersistenceManager pm = PersistenceManager.getInstance();
        pm.setNotificationToken(token);

        String sessionID = pm.getSessionId();
        int machineId = pm.getMachineId();
        String siteUrl = pm.getSiteUrl();
        final boolean retry[] = {true};

        if (machineId > 0 && siteUrl.length() > 0) {
            PostNotificationTokenRequest request = new PostNotificationTokenRequest(sessionID, machineId, token);
            NetworkManager.getInstance().postNotificationToken(request, new Callback<ErrorResponseNewVersion>() {
                @Override
                public void onResponse(Call<ErrorResponseNewVersion> call, Response<ErrorResponseNewVersion> response) {
                    if (response != null && response.body() != null && response.isSuccessful()) {
                        Log.d(LOG_TAG, "token sent");
                        pm.setNeedUpdateToken(false);
                    }else {
                        pm.setNeedUpdateToken(true);
                        Log.d(LOG_TAG, "token failed");
                        if (retry[0]){
                            retry[0] = false;
                            call.clone();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ErrorResponseNewVersion> call, Throwable t) {
                    pm.setNeedUpdateToken(true);
                    Log.d(LOG_TAG, "token failed");
                    if (retry[0]){
                        retry[0] = false;
                        call.clone();
                    }
                }
            });

        }else {
            pm.setNeedUpdateToken(true);
        }


    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(LOG_TAG, "Message data payload: " + remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                // scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

            createNewNotification(remoteMessage);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(LOG_TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }

    public void createNewNotification(RemoteMessage remoteMessage){
        Map<String, String> data = remoteMessage.getData();

        try {

            int notificationType = Integer.parseInt(data.get("NotificationType"));
            int responseType = Integer.parseInt(data.get("ResponseType"));
            int id = Integer.parseInt(data.get("ID"));

            Notification notification = new Notification(remoteMessage.getNotification().getBody(),
                    data.get("SourceUserName"),
                    data.get("ResponseDate"),
                    responseType,
                    "",
                    data.get("TargetUserName"),
                    id);

            ArrayList<Notification> notificationList = PersistenceManager.getInstance().getNotificationHistory();

            notificationList.add(notification);
            PersistenceManager.getInstance().setNotificationHistory(notificationList);
            notifyFragment(notificationType, responseType, id);
        }catch (Exception e){

        }
    }

    public void notifyFragment(int type, int responseType, int id){
        Intent intent = new Intent(Consts.NOTIFICATION_BROADCAST_NAME);
        intent.putExtra(Consts.NOTIFICATION_TYPE, type);
        intent.putExtra(Consts.NOTIFICATION_ID, id);

        switch (type){
            case Consts.NOTIFICATION_TYPE_REAL_TIME:
                break;
            case Consts.NOTIFICATION_TYPE_FROM_WEB:
                break;
            case Consts.NOTIFICATION_TYPE_TECHNICIAN:

                intent.putExtra(Consts.NOTIFICATION_TECHNICIAN_STATUS, responseType);
                break;

        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
