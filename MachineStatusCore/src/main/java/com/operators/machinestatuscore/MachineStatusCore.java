package com.operators.machinestatuscore;

import android.content.Context;
import android.util.Log;


import com.operators.machinestatuscore.interfaces.MachineStatusUICallback;
import com.operators.machinestatuscore.interfaces.OnTimeToEndChangedListener;
import com.operators.machinestatuscore.timecounter.TimeToEndCounter;
import com.operators.machinestatusinfra.ErrorObjectInterface;
import com.operators.machinestatusinfra.GetMachineStatusCallback;
import com.operators.machinestatusinfra.GetMachineStatusNetworkBridgeInterface;
import com.operators.machinestatusinfra.MachineStatus;
import com.operators.machinestatusinfra.MachineStatusPersistenceManagerInterface;
import com.operators.polling.EmeraldJobBase;
import com.zemingo.pollingmachanaim.JobBase;

import java.util.concurrent.TimeUnit;

public class MachineStatusCore implements OnTimeToEndChangedListener {
    private static final String LOG_TAG = MachineStatusCore.class.getSimpleName();
    private static final int MILLISECONDS_TO_SECONDS = 1000;
    private static final int INTERVAL_DELAY = 60;
    private static final int START_DELAY = 0;
    private GetMachineStatusNetworkBridgeInterface mGetMachineStatusNetworkBridgeInterface;
    private MachineStatusPersistenceManagerInterface mMachineStatusPersistenceManagerInterface;
    private TimeToEndCounter mTimeToEndCounter;
    private MachineStatusUICallback mMachineStatusUICallback;
    private EmeraldJobBase mJob;
    private Context mContext;

    public MachineStatusCore(GetMachineStatusNetworkBridgeInterface getMachineStatusNetworkBridgeInterface, MachineStatusPersistenceManagerInterface machineStatusPersistenceManagerInterface) {
        mGetMachineStatusNetworkBridgeInterface = getMachineStatusNetworkBridgeInterface;
        mMachineStatusPersistenceManagerInterface = machineStatusPersistenceManagerInterface;
    }

    public void registerListener(Context context, MachineStatusUICallback machineStatusUICallback) {
        mContext = context;
        mMachineStatusUICallback = machineStatusUICallback;
    }

    public void unregisterListener() {
        if (mMachineStatusUICallback != null) {
            mMachineStatusUICallback = null;
        }
        if (mTimeToEndCounter != null) {
            mTimeToEndCounter.stopTimer();
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
        mGetMachineStatusNetworkBridgeInterface.getMachineStatus(mMachineStatusPersistenceManagerInterface.getSiteUrl(), mMachineStatusPersistenceManagerInterface.getSessionId(), mMachineStatusPersistenceManagerInterface.getMachineId(), new GetMachineStatusCallback() {
            @Override
            public void onGetMachineStatusSucceeded(MachineStatus machineStatus) {
                if (machineStatus != null) {

                  int timeToEndInSeconds = machineStatus.getAllMachinesData().get(0).getShiftEndingIn();
                    startTimer(timeToEndInSeconds);

                    if (mMachineStatusUICallback != null) {
                        mMachineStatusUICallback.onStatusReceivedSuccessfully(machineStatus);
                    }
                    else {
                        Log.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
                    }
                    onJobFinishedListener.onJobFinished();
                }
                else {
                    Log.e(LOG_TAG,"machineStatus is null");
                }
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
        }, mMachineStatusPersistenceManagerInterface.getTotalRetries(), mMachineStatusPersistenceManagerInterface.getRequestTimeout());
    }

    private void startTimer(int timeInSeconds) {
        if (mTimeToEndCounter == null) {
            mTimeToEndCounter = new TimeToEndCounter(this);
        }
        mTimeToEndCounter.calculateTimeToEnd(timeInSeconds, mContext);
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