package com.operators.machinedatacore.interfaces;


import com.operators.machinedatainfra.interfaces.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;

import java.util.ArrayList;
import java.util.List;

public interface MachineDataUICallback {
    void onDataReceivedSuccessfully(ArrayList<Widget> widgetList);

    void onDataReceiveFailed(ErrorObjectInterface reason);
}
