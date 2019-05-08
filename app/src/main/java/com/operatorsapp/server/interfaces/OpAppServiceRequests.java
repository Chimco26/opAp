package com.operatorsapp.server.interfaces;

import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.operators.reportrejectnetworkbridge.server.response.ResponseStatus;
import com.example.common.request.MachineJoshDataRequest;
import com.operatorsapp.server.responses.AppVersionResponse;
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
    Call<ResponseStatus> postNotificationTokenRequest(@Body PostNotificationTokenRequest postNotificationTokenRequest);

    @POST("/LeaderMESApi/CallForTechnicianNotification")
    Call<ResponseStatus> postTechnicianCallRequest(@Body PostTechnicianCallRequest postTechnicianCallRequest);

    @POST("/LeaderMESApi/GetNotificationHistory")
    Call<NotificationHistoryResponse> getNotificationHistoryRequest(@Body NotificationHistoryRequest notificationHistoryRequest);

    @POST("/LeaderMESApi/NotificationResponse")
    Call<ResponseStatus> postNotificationResponse(@Body RespondToNotificationRequest respondToNotificationRequest);

    @POST("/LeaderMESApi/ControllerFieldUpdateTotalCycles")
    Call<ResponseStatus> postIncrementCounterRequest(@Body PostIncrementCounterRequest postIncrementCounterRequest);

    @POST("/LeaderMESApi/GetStopAndCriticalEvents")
    Call<StopAndCriticalEventsResponse> getStopAndCriticalEventsRequest(@Body GetTopRejectsAndEventsRequest request);

    @POST("/LeaderMESApi/GetRejects")
    Call<TopRejectResponse> getRejects(@Body GetTopRejectsAndEventsRequest request);

    @POST("/LeaderMESApi/DeleteToken")
    Call<ResponseStatus> postDeleteToken(@Body PostDeleteTokenRequest request);

    @Streaming
    @GET("/files/1Mb.dat")
    Call<ResponseBody> getNewVersionFile();

//    @GET("/LeaderMESApi/GetApplicationVersion")
    @GET("https://s3-eu-west-1.amazonaws.com/release.leadermes.com/OpApp/versions.json")
    Call<AppVersionResponse> GetApplicationVersion();
}
