package com.app.operatorinfra;

import com.example.common.callback.ErrorObjectInterface;

public interface GetOperatorByIdCallback {
    void onGetOperatorSucceeded(Operator operator);

    void onGetOperatorFailed(ErrorObjectInterface reason);
}
