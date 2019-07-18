package com.operators.infra;

import com.example.common.StandardResponse;

import java.util.ArrayList;

public interface GetMachinesCallback<T> {

    void onGetMachinesSucceeded(ArrayList<T> machines);

    void onGetMachinesFailed(StandardResponse reason);
}
