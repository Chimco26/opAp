package com.operators.operatorcore.interfaces;


import com.app.operatorinfra.ErrorObjectInterface;
import com.app.operatorinfra.Operator;

public interface OperatorForMachineUICallbackListener {
    void onOperatorDataReceived(Operator operator);

    void onOperatorDataReceiveFailure(ErrorObjectInterface reason);

    void onSetOperatorSuccess();

    void onSetOperatorFailed(ErrorObjectInterface reason);
}
