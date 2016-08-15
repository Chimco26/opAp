package com.operators.activejobslistformachinecore.interfaces;

import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.errorobject.ErrorObjectInterface;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface ActiveJobsListForMachineUICallbackListener {

    void onActiveJobsListForMachineReceived(ActiveJobsListForMachine activeJobsListForMachine);
    void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason);
}
