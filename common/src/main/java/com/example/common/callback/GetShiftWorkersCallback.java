package com.example.common.callback;

import com.example.common.StandardResponse;
import com.example.common.machineData.ShiftOperatorResponse;

public interface GetShiftWorkersCallback {

    void onGetShiftWorkersSuccess(ShiftOperatorResponse response);

    void onGetShiftWorkersFailed(StandardResponse reason);
}