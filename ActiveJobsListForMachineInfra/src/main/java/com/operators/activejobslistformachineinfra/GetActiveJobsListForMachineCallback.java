package com.operators.activejobslistformachineinfra;

import com.operators.errorobject.ErrorObjectInterface;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface GetActiveJobsListForMachineCallback {
    void onGetJobsListForMachineSuccess(ActiveJobsListForMachine activeJobsListForMachine);

    void onGetJobsListForMachineFailed(ErrorObjectInterface reason);
}
