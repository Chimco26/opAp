package com.operators.reportrejectnetworkbridge.interfaces;

import com.example.common.StandardResponse;
import com.example.common.StopLogs.StopLogsResponse;
import com.example.common.department.DepartmentsMachinesResponse;
import com.example.common.department.MachineLineRequest;
import com.example.common.department.MachineLineResponse;
import com.example.common.machineData.ShiftOperatorResponse;
import com.example.common.operator.SaveShiftWorkersRequest;
import com.example.common.request.BaseRequest;
import com.example.common.request.MachineIdRequest;
import com.example.common.task.CreateTaskHistoryRequest;
import com.example.common.task.CreateTaskRequest;
import com.example.common.task.GetTaskFilesRequest;
import com.example.common.task.TaskDefaultRequest;
import com.example.common.task.TaskFilesResponse;
import com.example.common.task.TaskListResponse;
import com.example.common.task.TaskObjectsForCreateOrEditResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetSimple {
    @POST("/LeaderMESApi/GetDepartmentsMachinesOpApp")
    Call<DepartmentsMachinesResponse> getAllDepartmentsRequest(@Body BaseRequest baseRequest);

    @POST("/LeaderMESApi/GetCurrentLineMachineStatus")
    Call<MachineLineResponse> getMachineLineRequest(@Body MachineLineRequest request);

    @POST("/LeaderMESApi/GetLineShiftLog")
    Call<StopLogsResponse> GetLineShiftLog(@Body MachineLineRequest request);

    @POST("/LeaderMESApi/GetShiftWorkers")
    Call<ShiftOperatorResponse> GetShiftWorkers(@Body MachineIdRequest request);

    @POST("/LeaderMESApi/saveShiftWorkers")
    Call<StandardResponse> saveShiftWorkers(@Body SaveShiftWorkersRequest request);

    @POST("/LeaderMESApi/GetTaskObjects")
    Call<TaskObjectsForCreateOrEditResponse> getTaskObjectsForCreateOrEdit(@Body TaskDefaultRequest request);

    @POST("/LeaderMESApi/GetTaskForMachine")
    Call<TaskListResponse> getTaskList(@Body TaskDefaultRequest request);

    @POST("/LeaderMESApi/GetTaskForMachinesInLine")
    Call<TaskListResponse> getTaskListForLine(@Body MachineLineRequest request);

    @POST("/LeaderMESApi/CreateTask")
    Call<StandardResponse> createOrdUpdateTask(@Body CreateTaskRequest request);

    @POST("/LeaderMESApi/CreateTaskHistory")
    Call<StandardResponse> updateTaskStatus(@Body CreateTaskHistoryRequest request);

    @POST("/LeaderMESApi/GetTaskFiles")
    Call<TaskFilesResponse> getTaskFiles(@Body GetTaskFilesRequest request);

    @POST("/LeaderMESApi/UpdateMachineStopBit")
    Call<StandardResponse> updateMachineStopBit(@Body MachineIdRequest request);
}

