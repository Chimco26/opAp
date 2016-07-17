package com.operators.machinestatuscore;

import android.util.Log;

import com.operators.infra.GetMachineStatusCallback;
import com.operators.infra.GetMachineStatusNetworkBridgeInterface;

import com.operators.infra.MachineStatus;
import com.operators.infra.PersistenceManagerInterface;
import com.operators.machinestatuscore.interfaces.MachineStatusUICallback;
import com.operators.machinestatuscore.interfaces.OnTimeToEndChangedListener;
import com.operators.machinestatuscore.timecounter.TimeToEndCounter;
;

public class MachineStatusCore implements OnTimeToEndChangedListener
{
    private static final String LOG_TAG = MachineStatusCore.class.getSimpleName();
    private GetMachineStatusNetworkBridgeInterface mGetMachineStatusNetworkBridgeInterface;
    private PersistenceManagerInterface mPersistenceManagerInterface;
    private TimeToEndCounter mTimeToEndCounter;
    private MachineStatusUICallback mMachineStatusUICallback;

    public MachineStatusCore(GetMachineStatusNetworkBridgeInterface getMachineStatusNetworkBridgeInterface, PersistenceManagerInterface persistenceManagerInterface)
    {
        mGetMachineStatusNetworkBridgeInterface = getMachineStatusNetworkBridgeInterface;
        mPersistenceManagerInterface = persistenceManagerInterface;
        mTimeToEndCounter = new TimeToEndCounter(this);
    }

    public void getMachineStatus(String siteUrl, String sessionId, String machineId, final MachineStatusUICallback machineStatusUICallback)
    {
        mMachineStatusUICallback = machineStatusUICallback;
        mGetMachineStatusNetworkBridgeInterface.getMachineStatus(mPersistenceManagerInterface.getSiteUrl(), mPersistenceManagerInterface.getSessionId(), machineId, new GetMachineStatusCallback()
        {
            @Override
            public void onGetMachineStatusSucceeded(MachineStatus machineStatus)
            {
                if (machineStatusUICallback != null)
                {
                    mMachineStatusUICallback.onStatusReceivedSuccessfully(machineStatus);
                    int timeToEndInSeconds = machineStatus.getShiftEndingIn() * 1000;
                    startTimer(timeToEndInSeconds);
                }
                else
                {
                    Log.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
                }
            }

            @Override
            public void onGetMachineStatusFailed()
            {
                if (mMachineStatusUICallback != null)
                {
                    mMachineStatusUICallback.onStatusReceiveFailed();
                }
                else
                {
                    Log.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");

                }
            }
        }, mPersistenceManagerInterface.getTotalRetries(), mPersistenceManagerInterface.getRequestTimeout());
    }

    private void startTimer(int timeInSeconds)
    {
        if (mTimeToEndCounter != null)
        {
            mTimeToEndCounter.calculateTimeToEnd(timeInSeconds);
        }
        else
        {
            Log.w(LOG_TAG, "startTimer() mMachineStatusUICallback is null");
        }
    }

    @Override
    public void onTimeToEndChanged(String timeToEndInHours)
    {
        if (mMachineStatusUICallback != null)
        {
            mMachineStatusUICallback.onTimerChanged(timeToEndInHours);
        }
        else
        {
            Log.w(LOG_TAG, "onTimeToEndChanged() mMachineStatusUICallback is null");
        }
    }
}