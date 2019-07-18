package com.operators.machinedatainfra.interfaces;

import com.example.common.StandardResponse;
import com.operators.machinedatainfra.models.Widget;

import java.util.ArrayList;

public interface GetMachineDataCallback<T> {
    void onGetMachineDataSucceeded(ArrayList<Widget> widgets);

    void onGetMachineDataFailed(StandardResponse reason);
}
