package com.operatorsapp.interfaces;

import com.operators.operatorcore.OperatorCore;

public interface SignInOperatorToDashboardActivityCallback {

    OperatorCore onSignInOperatorFragmentAttached();
    void onSetOperatorForMachineSuccess(String operatorId , String operatorName);
}
