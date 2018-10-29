package com.operatorsapp.server.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operatorsapp.server.requests.NotificationHistoryRequest;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.server.requests.PostTechnicianCallRequest;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.responses.NotificationHistoryResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by alex on 07/10/2018.
 */

public interface OpAppServiceRequests {

    @POST("/LeaderMESApi/SaveApplicationTokenForUser")
    Call<ErrorResponseNewVersion> postNotificationTokenRequest(@Body PostNotificationTokenRequest postNotificationTokenRequest);

    @POST("/LeaderMESApi/CallForTechnicianNotification")
    Call<ErrorResponseNewVersion> postTechnicianCallRequest(@Body PostTechnicianCallRequest postTechnicianCallRequest);

    @POST("/LeaderMESApi/GetNotificationHistory")
    Call<NotificationHistoryResponse> getNotificationHistoryRequest(@Body NotificationHistoryRequest notificationHistoryRequest);

    @POST("/LeaderMESApi/NotificationResponse")
    Call<ErrorResponseNewVersion> postNotificationResponse(@Body RespondToNotificationRequest respondToNotificationRequest);

}