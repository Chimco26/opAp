package com.operators.infra;


import java.util.ArrayList;

public interface GetMachinesCallback {

    void onLoginSucceeded(ArrayList machines);

    void onLoginFailed(ErrorObjectInterface reason);
}
