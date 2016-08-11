package com.app.operatorinfra;


import com.operators.errorobject.ErrorObjectInterface;

public interface GetOperatorByIdCallback {
    void onGetOperatorSucceeded(Operator operator);

    void onGetOperatorFailed(ErrorObjectInterface reason);
}
