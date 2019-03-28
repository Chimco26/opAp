package com.operators.infra;

import com.example.common.callback.ErrorObjectInterface;

import java.util.ArrayList;

public interface GetMachinesCallback<T> {

    void onGetMachinesSucceeded(ArrayList<T> machines);

    void onGetMachinesFailed(ErrorObjectInterface reason);
}
