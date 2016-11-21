package com.operators.machinestatuscore;

import android.util.Log;


import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinestatuscore.interfaces.MachineStatusUICallback;
import com.operators.machinestatuscore.interfaces.OnTimeToEndChangedListener;
import com.operators.machinestatuscore.polling.EmeraldJobBase;
import com.operators.machinestatuscore.timecounter.TimeToEndCounter;
import com.operators.machinestatusinfra.interfaces.GetMachineStatusCallback;
import com.operators.machinestatusinfra.interfaces.GetMachineStatusNetworkBridgeInterface;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.machinestatusinfra.interfaces.MachineStatusPersistenceManagerInterface;
import com.zemingo.logrecorder.ZLogger;
import com.zemingo.pollingmachanaim.JobBase;

import java.util.concurrent.TimeUnit;

public class MachineStatusCore implements OnTimeToEndChangedListener {
    private static final String LOG_TAG = MachineStatusCore.class.getSimpleName();

    private static final int START_DELAY = 0;
    private GetMachineStatusNetworkBridgeInterface mGetMachineStatusNetworkBridgeInterface;
    private MachineStatusPersistenceManagerInterface mMachineStatusPersistenceManagerInterface;
    private TimeToEndCounter mTimeToEndCounter;
    private MachineStatusUICallback mMachineStatusUICallback;
    private EmeraldJobBase mJob;

    public MachineStatusCore(GetMachineStatusNetworkBridgeInterface getMachineStatusNetworkBridge, MachineStatusPersistenceManagerInterface machineStatusPersistenceManager) {
        mGetMachineStatusNetworkBridgeInterface = getMachineStatusNetworkBridge;
        mMachineStatusPersistenceManagerInterface = machineStatusPersistenceManager;
    }

    public void registerListener(MachineStatusUICallback machineStatusUICallback) {
        mMachineStatusUICallback = machineStatusUICallback;
    }

    public void unregisterListener() {
        if (mMachineStatusUICallback != null) {
            mMachineStatusUICallback = null;
        }
    }

    public void stopTimer() {
        if (mTimeToEndCounter != null) {
            mTimeToEndCounter.stopTimer();
        }
    }

    public void startPolling() {
        mJob = null;
        mJob = new EmeraldJobBase() {
            @Override
            protected void executeJob(final JobBase.OnJobFinishedListener onJobFinishedListener) {
                getMachineStatus(onJobFinishedListener);
            }
        };

        mJob.startJob(START_DELAY, mMachineStatusPersistenceManagerInterface.getPollingFrequency(), TimeUnit.SECONDS);
    }

    public void stopPolling() {
        if (mJob != null) {
            mJob.stopJob();
        }
    }

    public void getMachineStatus(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        if (mMachineStatusPersistenceManagerInterface != null) {
            mGetMachineStatusNetworkBridgeInterface.getMachineStatus(mMachineStatusPersistenceManagerInterface.getSiteUrl(), mMachineStatusPersistenceManagerInterface.getSessionId(), mMachineStatusPersistenceManagerInterface.getMachineId(), new GetMachineStatusCallback() {
                @Override
                public void onGetMachineStatusSucceeded(MachineStatus machineStatus) {
                    int timeToEndInSeconds = 0;
                    if (machineStatus != null) {
                        if (machineStatus.getAllMachinesData() != null) {
                            if (machineStatus.getAllMachinesData().size() > 0) {
                                timeToEndInSeconds = machineStatus.getAllMachinesData().get(0).getShiftEndingIn();
                                startTimer(timeToEndInSeconds);
                            }
                        }
                        if (mMachineStatusUICallback != null) {
                            if (machineStatus.getAllMachinesData().size() > 0) {
                                mMachineStatusUICallback.onStatusReceivedSuccessfully(machineStatus);
                            } else {
                                ZLogger.w(LOG_TAG, "All Machine Data Is 0!!");
                            }
                        } else {
                            ZLogger.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
                        }
                    } else {
                        ZLogger.e(LOG_TAG, "machineStatus is null");
                    }
                    onJobFinishedListener.onJobFinished();
                }

                @Override
                public void onGetMachineStatusFailed(ErrorObjectInterface reason) {
                    ZLogger.w(LOG_TAG, "getMachineStatus() onGetMachineStatusFailed");
                    if (mMachineStatusUICallback != null) {
                        if (reason != null) {
                            mMachineStatusUICallback.onStatusReceiveFailed(reason);
                        }
                    } else {
                        ZLogger.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
                    }
                    onJobFinishedListener.onJobFinished();
                }
            }, mMachineStatusPersistenceManagerInterface.getTotalRetries(), mMachineStatusPersistenceManagerInterface.getRequestTimeout());
        }
    }

    private void startTimer(int timeInSeconds) {
        if (mTimeToEndCounter == null) {
            mTimeToEndCounter = new TimeToEndCounter(this);
        }
        mTimeToEndCounter.calculateTimeToEnd(timeInSeconds);
    }

    @Override
    public void onTimeToEndChanged(long millisUntilFinished) {
        if (mMachineStatusUICallback != null) {
            mMachineStatusUICallback.onTimerChanged(millisUntilFinished);
        } else {
            ZLogger.w(LOG_TAG, "onTimeToEndChanged() mMachineStatusUICallback is null");
        }
    }
}