package com.operators.infra;


import java.util.ArrayList;

public interface GetMachinesCallback {

    void onGetMachinesSucceeded(ArrayList machines);

    void onGetMachinesFailed(ErrorObjectInterface reason);
}
