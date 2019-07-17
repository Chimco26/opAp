package com.operatorsapp.server.pulling.interfaces;


import com.example.common.StandardResponse;
import com.operators.machinedatainfra.models.Widget;

import java.util.ArrayList;

public interface MachineDataUICallback
{
    void onDataReceivedSuccessfully(ArrayList<Widget> widgetList);

    void onDataReceiveFailed(StandardResponse reason);
}
