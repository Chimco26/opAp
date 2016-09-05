package com.operatorsapp.interfaces;

import com.operators.operatorcore.OperatorCore;

public interface OperatorCoreToDashboardActivityCallback {

    OperatorCore onSignInOperatorFragmentAttached();
    void onSetOperatorForMachineSuccess(String operatorId , String operatorName);
}
