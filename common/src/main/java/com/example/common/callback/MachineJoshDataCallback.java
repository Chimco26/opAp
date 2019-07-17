package com.example.common.callback;

import com.example.common.StandardResponse;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;

public interface MachineJoshDataCallback {

    void onMachineJoshDataCallbackSucceeded(MachineJoshDataResponse machineJoshDataResponse);

    void onMachineJoshDataCallbackFailed(StandardResponse reason);
}

