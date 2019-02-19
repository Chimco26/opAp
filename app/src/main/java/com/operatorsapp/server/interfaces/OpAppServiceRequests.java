package com.operatorsapp.server.interfaces;

import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operatorsapp.server.requests.GetTopRejectsAndEventsRequest;
import com.operatorsapp.server.requests.NotificationHistoryRequest;
import com.operatorsapp.server.requests.PostDeleteTokenRequest;
import com.operatorsapp.server.requests.PostIncrementCounterRequest;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.server.requests.PostTechnicianCallRequest;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.server.responses.StopAndCriticalEventsResponse;
import com.operatorsapp.server.responses.TopRejectResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

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

    @POST("/LeaderMESApi/ControllerFieldUpdateTotalCycles")
    Call<ErrorResponseNewVersion> postIncrementCounterRequest(@Body PostIncrementCounterRequest postIncrementCounterRequest);

    @POST("/LeaderMESApi/GetStopAndCriticalEvents")
    Call<StopAndCriticalEventsResponse> getStopAndCriticalEventsRequest(@Body GetTopRejectsAndEventsRequest request);

    @POST("/LeaderMESApi/GetRejects")
    Call<TopRejectResponse> getRejects(@Body GetTopRejectsAndEventsRequest request);

    @POST("/LeaderMESApi/DeleteToken")
    Call<ErrorResponseNewVersion> postDeleteToken(@Body PostDeleteTokenRequest request);

    @Streaming
    @GET("/files/1Mb.dat")
    Call<ResponseBody> getNewVersionFile();
}
