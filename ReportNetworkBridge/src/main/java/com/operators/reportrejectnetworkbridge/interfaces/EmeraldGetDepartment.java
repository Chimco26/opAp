package com.operators.reportrejectnetworkbridge.interfaces;

import com.example.common.department.DepartmentsMachinesResponse;
import com.example.common.request.BaseRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmeraldGetDepartment {
    @POST("/LeaderMESApi/GetDepartmentsMachinesOpApp")
    Call<DepartmentsMachinesResponse> getAllDepartmentsRequest(@Body BaseRequest baseRequest);
}
