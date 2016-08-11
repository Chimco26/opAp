package com.operators.operatorcore.interfaces;


import com.app.operatorinfra.Operator;
import com.operators.errorobject.ErrorObjectInterface;

public interface OperatorForMachineUICallbackListener {
    void onOperatorDataReceived(Operator operator);

    void onOperatorDataReceiveFailure(ErrorObjectInterface reason);

    void onSetOperatorSuccess();

    void onSetOperatorFailed(ErrorObjectInterface reason);
}
