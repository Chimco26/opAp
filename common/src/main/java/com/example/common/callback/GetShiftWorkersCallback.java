package com.example.common.callback;

import com.example.common.StandardResponse;

public interface GetShiftWorkersCallback {

    void onGetShiftWorkersSuccess(StandardResponse response);

    void onGetShiftWorkersFailed(StandardResponse reason);
}