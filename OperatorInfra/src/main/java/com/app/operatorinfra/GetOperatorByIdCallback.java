package com.app.operatorinfra;

import com.example.common.StandardResponse;

public interface GetOperatorByIdCallback {
    void onGetOperatorSucceeded(Operator operator);

    void onGetOperatorFailed(StandardResponse reason);
}
