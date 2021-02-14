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
import com.example.common.request.BaseRequest;
import com.example.common.request.BaseTimeRequest;
import com.example.common.request.MachineIdRequest;
import com.operatorsapp.server.requests.CreateTaskNotesRequest;
import com.operatorsapp.server.requests.GetTaskNoteRequest;
import com.operatorsapp.server.requests.GetTopRejectsAndEventsRequest;
import com.operatorsapp.server.requests.JobTestRequest;
import com.operatorsapp.server.requests.NotificationHistoryRequest;
import com.operatorsapp.server.requests.PostDeleteTokenRequest;
import com.operatorsapp.server.requests.PostIncrementCounterRequest;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.server.requests.PostTechnicianCallRequest;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.requests.SendNotificationRequest;
import com.operatorsapp.server.requests.TechCall24HRequest;
import com.operatorsapp.server.requests.TestOrderMaterialRequest;
import com.operatorsapp.server.requests.TopNotificationRequest;
import com.operatorsapp.server.responses.AppVersionResponse;
import com.operatorsapp.server.responses.JobListForMaterialResponse;
import com.operatorsapp.server.responses.JobListForTestResponse;
import com.operatorsapp.server.responses.JobTestResponse;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.server.responses.ResponseKPIS;
import com.operatorsapp.server.responses.StopAndCriticalEventsResponse;
import com.operatorsapp.server.responses.StopReasonsResponse;
import com.operatorsapp.server.responses.TaskNotesResponse;
import com.example.common.task.TaskStepResponse;
import com.operatorsapp.server.responses.TechCall24HResponse;
import com.operatorsapp.server.responses.TopRejectResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

    @POST("/LeaderMESApi/IncreaseMachineTotaCycles")
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
    @GET("https://s3-eu-west-1.amazonaws.com/release.leadermes.com/OpApp/versions.json") /// versions.json is for release; versions1.json is for debug
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

    @POST("/LeaderMESApi/GetEventReasonAndGroupsForMachine")
    Call<StopReasonsResponse> getStopReasons(@Body MachineIdRequest machineIdRequest);

    @POST("/LeaderMESApi/GetOpenCallsAnd24Hours")
    Call<TechCall24HResponse> getOpenCallsAnd24Hours(@Body TechCall24HRequest request);

    @POST("/LeaderMESApi/GetTaskNotes")
    Call<TaskNotesResponse> getTaskNotes(@Body GetTaskNoteRequest request);

    @POST("/LeaderMESApi/CreateTaskNotes")
    Call<StandardResponse> createTaskNotes(@Body CreateTaskNotesRequest request);

    @POST("/LeaderMESApi/GetTaskSteps")
    Call<TaskStepResponse> getTaskSteps(@Body GetTaskNoteRequest request);

    @POST("/LeaderMESApi/GetTranslationForKPIS")
    Call<ResponseKPIS> getKPIs(@Body BaseRequest request);

    @POST("/LeaderMESApi/GetJobsForTestOrder")
    Call<JobListForTestResponse> getJobsForTest(@Body BaseRequest request);

    @POST("/LeaderMESApi/GetMaterialsForTestOrder")
    Call<JobListForMaterialResponse> getMaterialsForTestOrder(@Body BaseRequest request);

    @POST("/LeaderMESApi/GetMaterialTestOrder")
    Call<TestOrderResponse> getMaterialTestOrder(@Body TestOrderMaterialRequest request);
}
