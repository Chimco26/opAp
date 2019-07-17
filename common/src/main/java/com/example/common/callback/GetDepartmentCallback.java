package com.example.common.callback;

import com.example.common.StandardResponse;
import com.example.common.department.DepartmentsMachinesResponse;

public interface GetDepartmentCallback {

    void onGetDepartmentSuccess(DepartmentsMachinesResponse response);

    void onGetDepartmentFailed(StandardResponse reason);
}
