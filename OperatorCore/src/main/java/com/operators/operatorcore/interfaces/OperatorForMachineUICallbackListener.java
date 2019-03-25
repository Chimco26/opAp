package com.operators.operatorcore.interfaces;


import com.app.operatorinfra.Operator;
import com.example.common.callback.ErrorObjectInterface;

public interface OperatorForMachineUICallbackListener {
    void onOperatorDataReceived(Operator operator);

    void onOperatorDataReceiveFailure(ErrorObjectInterface reason);

    void onSetOperatorSuccess(String operatorId);

    void onSetOperatorFailed(ErrorObjectInterface reason);
}
