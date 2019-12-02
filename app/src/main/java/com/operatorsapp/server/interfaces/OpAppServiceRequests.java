package com.operatorsapp.server.interfaces;

import com.example.common.QCModels.SaveTestDetailsRequest;
import com.example.common.QCModels.SaveTestDetailsResponse;
import com.example.common.QCModels.TestDetailsRequest;
import com.example.common.QCModels.TestDetailsResponse;
import com.example.common.QCModels.TestOrderRequest;
import com.example.common.QCModels.TestOrderResponse;
import com.example.common.QCModels.TestOrderSendRequest;
import com.example.common.StandardResponse;
import com.example.common.permissions.PermissionResponse;
import com.example.common.reportShift.DepartmentShiftGraphRequest;
import com.example.common.reportShift.DepartmentShiftGraphResponse;
import com.example.common.reportShift.ServiceCallsResponse;
import com.example.common.request.BaseTimeRequest;
import com.example.common.request.MachineIdRequest;
import com.operatorsapp.server.requests.GetTopRejectsAndEventsRequest;
import com.operatorsapp.server.requests.NotificationHistoryRequest;
import com.operatorsapp.server.requests.PostDeleteTokenRequest;
import com.operatorsapp.server.requests.PostIncrementCounterRequest;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.server.requests.PostTechnicianCallRequest;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.requests.SendNotificationRequest;
import com.operatorsapp.server.requests.TopNotificationRequest;
import com.operatorsapp.server.responses.AppVersionResponse;
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
    Call<StandardResponse> postNotificationTokenRequest(@Body PostNotificationTokenRequest postNotificationTokenRequest);

    @POST("/LeaderMESApi/CallForTechnicianNotification")
    Call<StandardResponse> postTechnicianCallRequest(@Body PostTechnicianCallRequest postTechnicianCallRequest);

    @POST("/LeaderMESApi/GetNotificationHistory")
    Call<NotificationHistoryResponse> getNotificationHistoryRequest(@Body NotificationHistoryRequest notificationHistoryRequest);

    @POST("/LeaderMESApi/NotificationResponse")
    Call<StandardResponse> postNotificationResponse(@Body RespondToNotificationRequest respondToNotificationRequest);

    @POST("/LeaderMESApi/ControllerFieldUpdateTotalCycles")
    Call<StandardResponse> postIncrementCounterRequest(@Body PostIncrementCounterRequest postIncrementCounterRequest);

    @POST("/LeaderMESApi/GetStopAndCriticalEvents")
    Call<StopAndCriticalEventsResponse> getStopAndCriticalEventsRequest(@Body GetTopRejectsAndEventsRequest request);

    @POST("/LeaderMESApi/GetRejects")
    Call<TopRejectResponse> getRejects(@Body GetTopRejectsAndEventsRequest request);

    @POST("/LeaderMESApi/DeleteToken")
    Call<StandardResponse> postDeleteToken(@Body PostDeleteTokenRequest request);

    @POST("/LeaderMESApi/GetServiceCalls")
    Call<ServiceCallsResponse> getServiceCalls(@Body BaseTimeRequest request);

    @POST("/LeaderMESApi/GetDepartmentShiftGraph")
    Call<DepartmentShiftGraphResponse> getDepartmentShiftGraph(@Body DepartmentShiftGraphRequest request);

    //    @GET("/LeaderMESApi/GetApplicationVersion")
    @GET("https://s3-eu-west-1.amazonaws.com/release.leadermes.com/OpApp/versions.json")
    Call<AppVersionResponse> GetApplicationVersion();

    @POST("/LeaderMESApi/SendNotificationToOpApp")
    Call<NotificationHistoryResponse> sendNotification(@Body SendNotificationRequest notification);

    @POST("/LeaderMESApi/GetTopNotifications")
    Call<NotificationHistoryResponse> getTopNotifications(@Body TopNotificationRequest notificationRequest);

    @POST("/LeaderMESApi/GetOperatorPermissionsForMachine")
    Call<PermissionResponse> getPermissionForMachine(@Body MachineIdRequest machineIdRequest);

    @POST("/LeaderMESApi/GetTestOrder")
    Call<TestOrderResponse> getQCTestOrder(@Body TestOrderRequest testOrderRequest);

    @POST("/LeaderMESApi/SendTestOrder")
    Call<StandardResponse> postQCSendTestOrder(@Body TestOrderSendRequest testOrderSendRequest);

    @POST("/LeaderMESApi/GetTestDetails")
    Call<TestDetailsResponse> getQCTestDetails(@Body TestDetailsRequest testDetailsRequest);

    @POST("/LeaderMESApi/SaveTestDetails")
    Call<SaveTestDetailsResponse> postQCSaveTestDetails(@Body SaveTestDetailsRequest testDetailsResponse);
}
