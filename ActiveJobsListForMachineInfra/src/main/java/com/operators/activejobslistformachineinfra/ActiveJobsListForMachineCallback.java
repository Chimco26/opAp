package com.operators.activejobslistformachineinfra;

import com.example.common.callback.ErrorObjectInterface;

/**
 * Created by Sergey on 14/08/2016.
 */
public interface ActiveJobsListForMachineCallback {
    void onGetActiveJobsListForMachineSuccess(ActiveJobsListForMachine activeJobsListForMachine);

    void onGetActiveJobsListForMachineFailed(ErrorObjectInterface reason);
}
