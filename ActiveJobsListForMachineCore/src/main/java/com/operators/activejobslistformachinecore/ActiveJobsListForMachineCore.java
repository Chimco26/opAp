package com.operators.activejobslistformachinecore;

import android.util.Log;

import com.operators.activejobslistformachinecore.interfaces.ActiveJobsListForMachineUICallbackListener;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachineCallback;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachineNetworkBridgeInterface;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachinePersistenceManagerInterface;
import com.operators.errorobject.ErrorObjectInterface;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJobsListForMachineCore {
    private static final String LOG_TAG = ActiveJobsListForMachineCore.class.getSimpleName();
    private ActiveJobsListForMachineNetworkBridgeInterface mActiveJobsListForMachineNetworkBridgeInterface;
    private ActiveJobsListForMachineUICallbackListener mActiveJobsListForMachineUICallbackListener;
    private ActiveJobsListForMachinePersistenceManagerInterface mActiveJobsListForMachinePersistenceManagerInterface;

    public ActiveJobsListForMachineCore(ActiveJobsListForMachinePersistenceManagerInterface activeJobsListForMachinePersistenceManagerInterface, ActiveJobsListForMachineNetworkBridgeInterface activeJobsListForMachineNetworkBridgeInterface) {
        mActiveJobsListForMachinePersistenceManagerInterface = activeJobsListForMachinePersistenceManagerInterface;
        mActiveJobsListForMachineNetworkBridgeInterface = activeJobsListForMachineNetworkBridgeInterface;
    }

    public void registerListener(ActiveJobsListForMachineUICallbackListener activeJobsListForMachineUICallbackListener) {
        mActiveJobsListForMachineUICallbackListener = activeJobsListForMachineUICallbackListener;
    }

    public void unregisterListener() {
        mActiveJobsListForMachineUICallbackListener = null;
    }

    public void getActiveJobsListForMachine() {
        if (mActiveJobsListForMachinePersistenceManagerInterface != null) {
            mActiveJobsListForMachineNetworkBridgeInterface.getActiveJobsForMachine(mActiveJobsListForMachinePersistenceManagerInterface.getSiteUrl(), mActiveJobsListForMachinePersistenceManagerInterface.getSessionId(),
                    mActiveJobsListForMachinePersistenceManagerInterface.getMachineId(), new ActiveJobsListForMachineCallback() {
                        @Override
                        public void onGetActiveJobsListForMachineSuccess(ActiveJobsListForMachine activeJobsListForMachine) {
                            if (activeJobsListForMachine != null) {
                                if (activeJobsListForMachine.getActiveJobs() != null) {
                                    if (activeJobsListForMachine.getActiveJobs().size() > 0) {
                                        mActiveJobsListForMachineUICallbackListener.onActiveJobsListForMachineReceived(activeJobsListForMachine);
                                    }
                                    else {
                                        Log.w(LOG_TAG, "getActiveJobsListForMachine() activeJobsListForMachine list is empty");
                                    }
                                }
                                else {
                                    Log.w(LOG_TAG, "getActiveJobsListForMachine() activeJobsListForMachine list null");
                                }
                            }
                            else {
                                Log.w(LOG_TAG, "getActiveJobsListForMachine() activeJobsListForMachine is null");

                            }
                        }

                        @Override
                        public void onGetActiveJobsListForMachineFailed(ErrorObjectInterface reason) {
                            mActiveJobsListForMachineUICallbackListener.onActiveJobsListForMachineReceiveFailed(reason);
                            Log.e(LOG_TAG, "getActiveJobsListForMachine() - onGetActiveJobsListForMachineFailed() ");


                        }
                    }, mActiveJobsListForMachinePersistenceManagerInterface.getTotalRetries(),
                    mActiveJobsListForMachinePersistenceManagerInterface.getRequestTimeout());
        }
    }
}
