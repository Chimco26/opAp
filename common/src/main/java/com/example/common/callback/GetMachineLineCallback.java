package com.example.common.callback;

import com.example.common.StandardResponse;
import com.example.common.department.MachineLineResponse;

public interface GetMachineLineCallback {
    void onGetDepartmentSuccess(MachineLineResponse response);

    void onGetDepartmentFailed(StandardResponse reason);
}
