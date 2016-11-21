package com.operators.machinedatacore;

import android.util.Log;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatacore.interfaces.MachineDataUICallback;
import com.operators.machinedatacore.polling.EmeraldJobBase;
import com.operators.machinedatainfra.interfaces.GetMachineDataCallback;
import com.operators.machinedatainfra.interfaces.GetMachineDataNetworkBridgeInterface;
import com.operators.machinedatainfra.interfaces.MachineDataPersistenceManagerInterface;
import com.operators.machinedatainfra.models.Widget;
import com.zemingo.logrecorder.ZLogger;
import com.zemingo.pollingmachanaim.JobBase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MachineDataCore {
    private static final String LOG_TAG = MachineDataCore.class.getSimpleName();
    private static final int MILLISECONDS_TO_SECONDS = 1000;

    private static final int START_DELAY = 0;
    private EmeraldJobBase mJob;

    private GetMachineDataNetworkBridgeInterface mGetMachineDataNetworkBridgeInterface;
    private MachineDataPersistenceManagerInterface mMachineDataPersistenceManagerInterface;
    private MachineDataUICallback mMachineDataUICallback;

    public MachineDataCore(GetMachineDataNetworkBridgeInterface getMachineDataNetworkBridgeInterface, MachineDataPersistenceManagerInterface machineDataPersistenceManagerInterface) {
        mGetMachineDataNetworkBridgeInterface = getMachineDataNetworkBridgeInterface;
        mMachineDataPersistenceManagerInterface = machineDataPersistenceManagerInterface;
    }

    public void registerListener(MachineDataUICallback machineDataUICallback) {
        mMachineDataUICallback = machineDataUICallback;
    }

    public void unregisterListener() {
        if (mMachineDataUICallback != null) {
            mMachineDataUICallback = null;
        }
    }


    public void startPolling() {
        mJob = null;
        mJob = new EmeraldJobBase() {
            @Override
            protected void executeJob(final OnJobFinishedListener onJobFinishedListener) {
                getMachineData(onJobFinishedListener);
            }
        };

        mJob.startJob(START_DELAY, mMachineDataPersistenceManagerInterface.getPollingFrequency(), TimeUnit.SECONDS);
    }

    public void stopPolling() {
        if (mJob != null) {
            mJob.stopJob();
        }
    }

    public void getMachineData(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        if (mMachineDataPersistenceManagerInterface != null) {
            mGetMachineDataNetworkBridgeInterface.getMachineData(mMachineDataPersistenceManagerInterface.getSiteUrl(), mMachineDataPersistenceManagerInterface.getSessionId(), mMachineDataPersistenceManagerInterface.getMachineId(), mMachineDataPersistenceManagerInterface.getMachineDataStartingFrom(), new GetMachineDataCallback<Widget>() {
                        @Override
                        public void onGetMachineDataSucceeded(ArrayList<Widget> widgetList) {
                            if (mMachineDataUICallback != null) {
                                mMachineDataUICallback.onDataReceivedSuccessfully(widgetList);
                            } else {
                                ZLogger.w(LOG_TAG, "getMachineData() mMachineDataUICallback is null");
                            }
                            onJobFinishedListener.onJobFinished();
                        }

                        @Override
                        public void onGetMachineDataFailed(ErrorObjectInterface reason) {
                            ZLogger.w(LOG_TAG, "getMachineData() onGetMachineDataFailed " + reason.getError());
                            onJobFinishedListener.onJobFinished();
                            if (mMachineDataUICallback != null) {
                                mMachineDataUICallback.onDataReceiveFailed(reason);
                            }
                        }
                    },
                    mMachineDataPersistenceManagerInterface.getTotalRetries(), mMachineDataPersistenceManagerInterface.getRequestTimeout());
        }

    }
}