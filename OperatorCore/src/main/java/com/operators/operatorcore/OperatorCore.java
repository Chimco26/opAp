package com.operators.operatorcore;

import com.app.operatorinfra.GetOperatorByIdCallback;
import com.app.operatorinfra.Operator;
import com.app.operatorinfra.OperatorNetworkBridgeInterface;
import com.app.operatorinfra.OperatorPersistenceManagerInterface;
import com.app.operatorinfra.SetOperatorForMachineCallback;
import com.example.common.StandardResponse;
import com.example.oppapplog.OppAppLogger;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;

public class OperatorCore {
    private static final String LOG_TAG = OperatorCore.class.getSimpleName();
    private OperatorNetworkBridgeInterface mOperatorNetworkBridgeInterface;
    private OperatorPersistenceManagerInterface mOperatorPersistenceManagerInterface;
    private OperatorForMachineUICallbackListener mOperatorForMachineUICallbackListener;

    public OperatorCore(OperatorNetworkBridgeInterface operatorNetworkBridgeInterface, OperatorPersistenceManagerInterface operatorPersistenceManagerInterface) {
        mOperatorNetworkBridgeInterface = operatorNetworkBridgeInterface;
        mOperatorPersistenceManagerInterface = operatorPersistenceManagerInterface;
    }

    public void registerListener(OperatorForMachineUICallbackListener operatorForMachineUICallbackListener) {
        mOperatorForMachineUICallbackListener = operatorForMachineUICallbackListener;
    }

    public void unregisterListener() {
        mOperatorForMachineUICallbackListener = null;
    }

    public void getOperatorById(String operatorId) {
        if (mOperatorPersistenceManagerInterface != null) {
            mOperatorNetworkBridgeInterface.getOperator(mOperatorPersistenceManagerInterface.getSiteUrl(), mOperatorPersistenceManagerInterface.getSessionId(), operatorId, new GetOperatorByIdCallback() {
                @Override
                public void onGetOperatorSucceeded(Operator operator) {
                    if (mOperatorForMachineUICallbackListener != null) {
                        if (operator != null) {
                            mOperatorForMachineUICallbackListener.onOperatorDataReceived(operator);
                        }
                        else {
                            OppAppLogger.w(LOG_TAG, "operator is nul");
                        }
                    }
                    else {
                        OppAppLogger.w(LOG_TAG, "onGetOperatorSucceeded() UI Callback is null");
                    }
                }

                @Override
                public void onGetOperatorFailed(StandardResponse reason) {
                    if (mOperatorForMachineUICallbackListener != null) {
                        if (reason != null) {
                            mOperatorForMachineUICallbackListener.onOperatorDataReceiveFailure(reason);
                        }
                        else {
                            OppAppLogger.w(LOG_TAG, "reason is nul");
                        }
                    }
                    else {
                        OppAppLogger.w(LOG_TAG, "onGetOperatorFailed() UI Callback is null");
                    }
                }
            }, mOperatorPersistenceManagerInterface.getTotalRetries(), mOperatorPersistenceManagerInterface.getRequestTimeout());
        }
    }

    public void setOperatorForMachine(final String operatorId) {
        if (mOperatorPersistenceManagerInterface != null) {
            mOperatorNetworkBridgeInterface.setOperatorForMachine(mOperatorPersistenceManagerInterface.getSiteUrl(), mOperatorPersistenceManagerInterface.getSessionId(), String.valueOf(mOperatorPersistenceManagerInterface.getMachineId()), operatorId, new SetOperatorForMachineCallback() {
                @Override
                public void onSetOperatorForMachineSuccess() {
                    if (mOperatorForMachineUICallbackListener != null) {
                        mOperatorForMachineUICallbackListener.onSetOperatorSuccess(operatorId);
                    }
                    else {
                        OppAppLogger.w(LOG_TAG, "onSetOperatorForMachineSuccess() UI Callback is null");
                    }
                }

                @Override
                public void onSetOperatorForMachineFailed(StandardResponse reason) {
                    if (mOperatorForMachineUICallbackListener != null) {
                        if (reason != null) {
                            mOperatorForMachineUICallbackListener.onSetOperatorFailed(reason);
                        }
                        else {
                            OppAppLogger.w(LOG_TAG, "reason is nul");
                        }
                    }
                    else {
                        OppAppLogger.w(LOG_TAG, "onGetOperatorFailed() UI Callback is null");
                    }
                }
            }, mOperatorPersistenceManagerInterface.getTotalRetries(), mOperatorPersistenceManagerInterface.getRequestTimeout());
        }
    }
}
