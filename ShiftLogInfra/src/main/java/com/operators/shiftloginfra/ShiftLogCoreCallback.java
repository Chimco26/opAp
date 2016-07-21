package com.operators.shiftloginfra;


public interface ShiftLogCoreCallback {

    void onLoginSucceeded(ShiftLog shiftLog);

    void onLoginFailed(ErrorObjectInterface reason);
}
