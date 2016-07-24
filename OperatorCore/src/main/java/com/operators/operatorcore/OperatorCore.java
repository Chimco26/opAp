package com.operators.operatorcore;

import android.util.Log;

import com.app.operatorinfra.ErrorObjectInterface;
import com.app.operatorinfra.GetOperatorByIdCallback;
import com.app.operatorinfra.OperatorNetworkBridgeInterface;
import com.app.operatorinfra.Operator;
import com.app.operatorinfra.OperatorPersistenceManagerInterface;
import com.app.operatorinfra.SetOperatorForMachineCallback;
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
        if (mOperatorForMachineUICallbackListener != null) {
            mOperatorForMachineUICallbackListener = null;
        }
    }

    public void getOperatorById(String operatorId) {
        mOperatorNetworkBridgeInterface.getOperator(mOperatorPersistenceManagerInterface.getSiteUrl(), mOperatorPersistenceManagerInterface.getSessionId(), operatorId, new GetOperatorByIdCallback() {
            @Override
            public void onGetOperatorSucceeded(Operator operator) {
                if (mOperatorForMachineUICallbackListener != null) {
                    mOperatorForMachineUICallbackListener.onOperatorDataReceived(operator);
                }
                else {
                    Log.w(LOG_TAG, "onGetOperatorSucceeded() UI Callback is null");
                }
            }

            @Override
            public void onGetOperatorFailed(ErrorObjectInterface reason) {
                if (mOperatorForMachineUICallbackListener != null) {
                    mOperatorForMachineUICallbackListener.onOperatorDataReceiveFailure(reason);
                }
                else {
                    Log.w(LOG_TAG, "onGetOperatorFailed() UI Callback is null");
                }
            }
        }, mOperatorPersistenceManagerInterface.getTotalRetries(), mOperatorPersistenceManagerInterface.getRequestTimeout());
    }

    public void setOperatorForMachine(String operatorId) {
        mOperatorNetworkBridgeInterface.setOperatorForMachine(mOperatorPersistenceManagerInterface.getSiteUrl(), mOperatorPersistenceManagerInterface.getSessionId(), String.valueOf(mOperatorPersistenceManagerInterface.getMachineId()), operatorId, new SetOperatorForMachineCallback() {
            @Override
            public void onSetOperatorForMachineSuccess() {
                if (mOperatorForMachineUICallbackListener != null) {
                    mOperatorForMachineUICallbackListener.onSetOperatorSuccess();
                }
                else {
                    Log.w(LOG_TAG, "onSetOperatorForMachineSuccess() UI Callback is null");
                }
            }

            @Override
            public void onSetOperatorForMachineFailed(ErrorObjectInterface reason) {
                if (mOperatorForMachineUICallbackListener != null) {
                    mOperatorForMachineUICallbackListener.onSetOperatorFailed(reason);
                }
                else {
                    Log.w(LOG_TAG, "onGetOperatorFailed() UI Callback is null");
                }
            }
        }, mOperatorPersistenceManagerInterface.getTotalRetries(), mOperatorPersistenceManagerInterface.getRequestTimeout());

    }
}
