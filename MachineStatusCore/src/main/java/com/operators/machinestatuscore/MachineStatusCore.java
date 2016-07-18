package com.operators.machinestatuscore;

import android.util.Log;

import com.operators.infra.ErrorObjectInterface;
import com.operators.infra.GetMachineStatusCallback;
import com.operators.infra.GetMachineStatusNetworkBridgeInterface;

import com.operators.infra.MachineStatus;
import com.operators.infra.PersistenceManagerInterface;
import com.operators.machinestatuscore.interfaces.MachineStatusUICallback;
import com.operators.machinestatuscore.interfaces.OnTimeToEndChangedListener;
import com.operators.machinestatuscore.timecounter.TimeToEndCounter;
import com.operators.polling.EmeraldJobBase;
import com.zemingo.pollingmachanaim.JobBase;

import java.util.concurrent.TimeUnit;

public class MachineStatusCore implements OnTimeToEndChangedListener {
    private static final String LOG_TAG = MachineStatusCore.class.getSimpleName();
    public static final int MILLISECONDS_TO_SECONDS = 1000;
    public static final int INTERVAL_DELAY = 60;
    public static final int START_DELAY = 0;
    private GetMachineStatusNetworkBridgeInterface mGetMachineStatusNetworkBridgeInterface;
    private PersistenceManagerInterface mPersistenceManagerInterface;
    private TimeToEndCounter mTimeToEndCounter;
    private MachineStatusUICallback mMachineStatusUICallback;
    private EmeraldJobBase mJob;

    public MachineStatusCore(GetMachineStatusNetworkBridgeInterface getMachineStatusNetworkBridgeInterface, PersistenceManagerInterface persistenceManagerInterface) {
        mGetMachineStatusNetworkBridgeInterface = getMachineStatusNetworkBridgeInterface;
        mPersistenceManagerInterface = persistenceManagerInterface;
    }

    public void registerListener(MachineStatusUICallback machineStatusUICallback) {
        mMachineStatusUICallback = machineStatusUICallback;
    }

    public void unregisterListener() {
        if (mMachineStatusUICallback != null) {
            mMachineStatusUICallback = null;
        }
    }

    public void startPolling() {
        mJob = new EmeraldJobBase() {
            @Override
            protected void executeJob(final JobBase.OnJobFinishedListener onJobFinishedListener) {
                getMachineStatus(onJobFinishedListener);
            }
        };

        mJob.startJob(START_DELAY, INTERVAL_DELAY, TimeUnit.SECONDS);
    }

    public void stopPolling() {
        if (mJob != null) {
            mJob.stopJob();
        }
    }

    public void getMachineStatus(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        mGetMachineStatusNetworkBridgeInterface.getMachineStatus(mPersistenceManagerInterface.getSiteUrl(), mPersistenceManagerInterface.getSessionId(), mPersistenceManagerInterface.getMachineId(), new GetMachineStatusCallback() {
            @Override
            public void onGetMachineStatusSucceeded(MachineStatus machineStatus) {
                int timeToEndInSeconds = machineStatus.getShiftEndingIn() * MILLISECONDS_TO_SECONDS;
                startTimer(timeToEndInSeconds);

                if (mMachineStatusUICallback != null) {
                    mMachineStatusUICallback.onStatusReceivedSuccessfully(machineStatus);
                }
                else {
                    Log.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
                }
                onJobFinishedListener.onJobFinished();
            }

            @Override
            public void onGetMachineStatusFailed(ErrorObjectInterface reason) {
                if (mMachineStatusUICallback != null) {
                    mMachineStatusUICallback.onStatusReceiveFailed(reason);
                }
                else {
                    Log.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");

                }
                onJobFinishedListener.onJobFinished();
            }
        }, mPersistenceManagerInterface.getTotalRetries(), mPersistenceManagerInterface.getRequestTimeout());
    }

    private void startTimer(int timeInSeconds) {
        if (mTimeToEndCounter == null) {
            mTimeToEndCounter = new TimeToEndCounter(this);
        }
        mTimeToEndCounter.calculateTimeToEnd(timeInSeconds);
    }

    @Override
    public void onTimeToEndChanged(String formattedTimeToEnd) {
        if (mMachineStatusUICallback != null) {
            mMachineStatusUICallback.onTimerChanged(formattedTimeToEnd);
        }
        else {
            Log.w(LOG_TAG, "onTimeToEndChanged() mMachineStatusUICallback is null");
        }
    }
}