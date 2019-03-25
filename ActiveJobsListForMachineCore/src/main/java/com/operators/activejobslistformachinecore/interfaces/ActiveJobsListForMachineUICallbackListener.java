package com.operators.activejobslistformachinecore.interfaces;

import com.example.common.callback.ErrorObjectInterface;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface ActiveJobsListForMachineUICallbackListener {

    void onActiveJobsListForMachineReceived(ActiveJobsListForMachine activeJobsListForMachine);
    void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason);
}
