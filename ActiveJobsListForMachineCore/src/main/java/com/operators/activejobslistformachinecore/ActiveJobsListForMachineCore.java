package com.operators.activejobslistformachinecore;

import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.operators.activejobslistformachinecore.interfaces.ActiveJobsListForMachineUICallbackListener;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachineCallback;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachineNetworkBridgeInterface;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachinePersistenceManagerInterface;

import java.lang.ref.WeakReference;

/**
 * Created by Sergey on 14/08/2016.
 */
public class ActiveJobsListForMachineCore {
    private static final String LOG_TAG = ActiveJobsListForMachineCore.class.getSimpleName();
    private WeakReference<ActiveJobsListForMachineNetworkBridgeInterface> mActiveJobsListForMachineNetworkBridgeInterface;
    private WeakReference<ActiveJobsListForMachineUICallbackListener> mActiveJobsListForMachineUICallbackListener;
    private WeakReference<ActiveJobsListForMachinePersistenceManagerInterface> mActiveJobsListForMachinePersistenceManagerInterface;

    public ActiveJobsListForMachineCore(ActiveJobsListForMachinePersistenceManagerInterface activeJobsListForMachinePersistenceManagerInterface, ActiveJobsListForMachineNetworkBridgeInterface activeJobsListForMachineNetworkBridgeInterface) {
        mActiveJobsListForMachinePersistenceManagerInterface = new WeakReference<>(activeJobsListForMachinePersistenceManagerInterface);
        mActiveJobsListForMachineNetworkBridgeInterface = new WeakReference<>(activeJobsListForMachineNetworkBridgeInterface);
    }

    public void registerListener(ActiveJobsListForMachineUICallbackListener activeJobsListForMachineUICallbackListener) {
        mActiveJobsListForMachineUICallbackListener = new WeakReference<>(activeJobsListForMachineUICallbackListener);
    }

    public void unregisterListener() {
        mActiveJobsListForMachineUICallbackListener = null;
    }

    public void getActiveJobsListForMachine() {
        if (mActiveJobsListForMachinePersistenceManagerInterface != null && mActiveJobsListForMachineNetworkBridgeInterface.get() != null) {
            mActiveJobsListForMachineNetworkBridgeInterface.get().getActiveJobsForMachine(mActiveJobsListForMachinePersistenceManagerInterface.get().getSiteUrl(), mActiveJobsListForMachinePersistenceManagerInterface.get().getSessionId(),
                    mActiveJobsListForMachinePersistenceManagerInterface.get().getMachineId(), new ActiveJobsListForMachineCallback() {
                        @Override
                        public void onGetActiveJobsListForMachineSuccess(ActiveJobsListForMachine activeJobsListForMachine) {
                            if (activeJobsListForMachine != null) {
                                if (activeJobsListForMachine.getActiveJobs() != null) {
                                    if (activeJobsListForMachine.getActiveJobs().size() > 0 && mActiveJobsListForMachineUICallbackListener != null && mActiveJobsListForMachineUICallbackListener.get() != null) {
                                        mActiveJobsListForMachineUICallbackListener.get().onActiveJobsListForMachineReceived(activeJobsListForMachine);
                                    }
                                    else {
                                        OppAppLogger.w(LOG_TAG, "getActiveJobsListForMachine() activeJobsListForMachine list is empty");
                                    }
                                }
                                else {
                                    OppAppLogger.w(LOG_TAG, "getActiveJobsListForMachine() activeJobsListForMachine list null");
                                }
                            }
                            else {
                                OppAppLogger.w(LOG_TAG, "getActiveJobsListForMachine() activeJobsListForMachine is null");

                            }
                        }

                        @Override
                        public void onGetActiveJobsListForMachineFailed(StandardResponse reason) {
                            if (mActiveJobsListForMachineUICallbackListener != null && mActiveJobsListForMachineUICallbackListener.get() != null) {
                                mActiveJobsListForMachineUICallbackListener.get().onActiveJobsListForMachineReceiveFailed(reason);
                            }
                            OppAppLogger.e(LOG_TAG, "getActiveJobsListForMachine() - onGetActiveJobsListForMachineFailed() ");


                        }
                    }, mActiveJobsListForMachinePersistenceManagerInterface.get().getTotalRetries(),
                    mActiveJobsListForMachinePersistenceManagerInterface.get().getRequestTimeout());
        }
    }
}
