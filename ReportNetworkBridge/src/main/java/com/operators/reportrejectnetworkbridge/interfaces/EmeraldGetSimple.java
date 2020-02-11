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
import com.example.common.task.CreateTaskRequest;
import com.example.common.task.TaskListRequest;

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
    Call<StandardResponse> getTaskObjects(@Body TaskListRequest request);

    @POST("/LeaderMESApi/CreateTask")
    Call<StandardResponse> createTaskObjects(@Body CreateTaskRequest request);
}

