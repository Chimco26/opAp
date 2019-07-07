package com.operatorsapp.server.pulling.interfaces;


import com.example.common.callback.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;

import java.util.ArrayList;

public interface MachineDataUICallback
{
    void onDataReceivedSuccessfully(ArrayList<Widget> widgetList);

    void onDataReceiveFailed(ErrorObjectInterface reason);
}
