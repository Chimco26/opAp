package com.operators.machinedatacore.interfaces;


import com.operators.machinedatainfra.interfaces.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;

import java.util.List;

public interface MachineDataUICallback {
    void onDataReceivedSuccessfully(List<Widget> widgetList);

    void onDataReceiveFailed(ErrorObjectInterface reason);
}
