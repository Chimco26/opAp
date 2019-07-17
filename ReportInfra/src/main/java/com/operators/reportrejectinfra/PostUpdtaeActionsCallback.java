package com.operators.reportrejectinfra;

import com.example.common.StandardResponse;

public interface PostUpdtaeActionsCallback {

    void onPostUpdtaeActionsSuccess(StandardResponse response);

    void onPostUpdtaeActionsFailed(StandardResponse reason);
}
