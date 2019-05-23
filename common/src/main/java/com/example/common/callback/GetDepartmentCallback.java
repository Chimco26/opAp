package com.example.common.callback;

import com.example.common.department.DepartmentsMachinesResponse;

public interface GetDepartmentCallback {

    void onGetDepartmentSuccess(DepartmentsMachinesResponse response);

    void onGetDepartmentFailed(ErrorObjectInterface reason);
}
