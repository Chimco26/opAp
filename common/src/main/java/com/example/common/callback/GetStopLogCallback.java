package com.example.common.callback;

import com.example.common.StandardResponse;
import com.example.common.StopLogs.StopLogsResponse;

public interface GetStopLogCallback {
    void onGetStopLogSuccess(StopLogsResponse response);

    void onGetStopLogFailed(StandardResponse reason);
}
