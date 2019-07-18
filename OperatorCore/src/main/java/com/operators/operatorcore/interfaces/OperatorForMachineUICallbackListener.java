package com.operators.operatorcore.interfaces;


import com.app.operatorinfra.Operator;
import com.example.common.StandardResponse;

public interface OperatorForMachineUICallbackListener {
    void onOperatorDataReceived(Operator operator);

    void onOperatorDataReceiveFailure(StandardResponse reason);

    void onSetOperatorSuccess(String operatorId);

    void onSetOperatorFailed(StandardResponse reason);
}
