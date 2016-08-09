package com.operators.machinedatainfra.interfaces;

import com.operators.machinedatainfra.models.Widget;

import java.util.List;

public interface GetMachineDataCallback<T> {
    void onGetMachineDataSucceeded(List<Widget> widgets);

    void onGetMachineDataFailed(ErrorObjectInterface reason);
}
