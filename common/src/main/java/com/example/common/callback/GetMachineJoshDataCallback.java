package com.example.common.callback;

import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;

public interface GetMachineJoshDataCallback<T> {

    void onGetMachineJoshDataSuccess(MachineJoshDataResponse response);

    void onGetMachineJoshDataFailed(ErrorObjectInterface reason);
}
