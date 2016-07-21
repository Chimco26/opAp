package com.app.operatorinfra;


public interface GetOperatorByIdCallback {
    void onGetOperatorSucceeded(Operator operator);

    void onGetOperatorFailed(ErrorObjectInterface reason);
}
