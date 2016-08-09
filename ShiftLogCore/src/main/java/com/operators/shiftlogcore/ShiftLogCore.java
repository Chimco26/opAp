package com.operators.shiftlogcore;

import android.util.Log;

import com.operators.shiftloginfra.ShiftLogCoreCallback;
import com.operators.shiftloginfra.ShiftLogPersistenceManagerInterface;
import com.operators.shiftlogcore.interfaces.ShiftLogUICallback;
import com.operators.shiftlogcore.polling.EmeraldJobBase;
import com.operators.shiftloginfra.ErrorObjectInterface;
import com.operators.shiftloginfra.Event;
import com.operators.shiftloginfra.ShiftLogNetworkBridgeInterface;
import com.zemingo.pollingmachanaim.JobBase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ShiftLogCore {
    public static final String LOG_TAG = ShiftLogCore.class.getSimpleName();
    private static final int INTERVAL_DELAY = 60;
    private static final int START_DELAY = 0;

    private static ShiftLogCore msInstance;

    private ShiftLogPersistenceManagerInterface mShiftLogPersistenceManagerInterface;
    private ShiftLogNetworkBridgeInterface mShiftLogNetworkBridgeInterface;
    private ShiftLogUICallback mShiftLogUICallback;

    private EmeraldJobBase mJob;
//    private ArrayList<Event> mShiftLogs;

//    public static ShiftLogCore getInstance() {
//        if (msInstance == null) {
//            msInstance = new ShiftLogCore();
//        }
//        return msInstance;
//    }

    public ShiftLogCore(ShiftLogPersistenceManagerInterface shiftLogPersistenceManagerInterface, ShiftLogNetworkBridgeInterface shiftLogNetworkBridgeInterface) {
        mShiftLogPersistenceManagerInterface = shiftLogPersistenceManagerInterface;
        mShiftLogNetworkBridgeInterface = shiftLogNetworkBridgeInterface;
    }

    public void registerListener(ShiftLogUICallback shiftLogUICallback) {
        mShiftLogUICallback = shiftLogUICallback;
    }

    public void unregisterListener() {
        if (mShiftLogUICallback != null) {
            mShiftLogUICallback = null;
        }
    }

   /* public void startPolling(final String siteUrl, final String sessionId, final int machineId, final String startingFrom, final ShiftLogUICallback shiftLogUICallback) {
        mJob = null;
        mJob = new EmeraldJobBase() {
            @Override
            protected void executeJob(final JobBase.OnJobFinishedListener onJobFinishedListener) {
                getShiftLogs(siteUrl, sessionId, machineId, startingFrom, shiftLogUICallback, onJobFinishedListener);
            }
        };

        mJob.startJob(START_DELAY, INTERVAL_DELAY, TimeUnit.SECONDS);
    }

    public void stopPolling() {
        if (mJob != null) {
            mJob.stopJob();
        }
    }*/

    public void startPolling() {
        mJob = null;
        mJob = new EmeraldJobBase() {
            @Override
            protected void executeJob(final OnJobFinishedListener onJobFinishedListener) {
                getShiftLogs(onJobFinishedListener);
            }
        };

        mJob.startJob(START_DELAY, INTERVAL_DELAY, TimeUnit.SECONDS);
    }

    public void stopPolling() {
        if (mJob != null) {
            mJob.stopJob();
        }
    }

    /*public void getShiftLogs(String siteUrl, final String sessionId, int machineId, String startingFrom, final ShiftLogUICallback shiftLogUICallback, final JobBase.OnJobFinishedListener onJobFinishedListener) {

        mShiftLogNetworkBridgeInterface.getShiftLog(siteUrl, sessionId, machineId, startingFrom, new ShiftLogCoreCallback<Event>() {
            @Override
            public void onShiftLogSucceeded(ArrayList<Event> events) {

                for (Event event : events) {
//                    event.setDialogShown(false);
                    event.setTimeOfAdded(System.currentTimeMillis());
                }
//                if (mShiftLogs == null) {
//                    mShiftLogs = new ArrayList<>();
//                }
//                mShiftLogs.addAll(events);
                shiftLogUICallback.onGetShiftLogSucceeded(events);
                Log.d(LOG_TAG, "getShiftLog success");
                onJobFinishedListener.onJobFinished();
            }

            @Override
            public void onShiftLogFailed(ErrorObjectInterface reason) {
                shiftLogUICallback.onGetShiftLogFailed(reason);
                onJobFinishedListener.onJobFinished();
            }
        }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout());
    }*/

    public void getShiftLogs(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        mShiftLogNetworkBridgeInterface.getShiftLog(mShiftLogPersistenceManagerInterface.getSiteUrl(), mShiftLogPersistenceManagerInterface.getSessionId(), mShiftLogPersistenceManagerInterface.getMachineId(), "1.5.98"/*todo*/, new ShiftLogCoreCallback<Event>() {
            @Override
            public void onShiftLogSucceeded(ArrayList<Event> events) {
                if (mShiftLogUICallback != null) {
                    mShiftLogUICallback.onGetShiftLogSucceeded(events);
                } else {
                    Log.w(LOG_TAG, "getMachineData() mMachineDataUICallback is null");
                }
                onJobFinishedListener.onJobFinished();
            }

            @Override
            public void onShiftLogFailed(ErrorObjectInterface reason) {
                Log.w(LOG_TAG, "getShiftLogs() onGetShiftLogFailed");
                onJobFinishedListener.onJobFinished();
                if (mShiftLogUICallback != null) {
                    mShiftLogUICallback.onGetShiftLogFailed(reason);
                }
            }

        }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout());
    }


//    public void setShiftLogDialogStatus(ArrayDeque<Event> shiftLogs) {
//        mShiftLogs.clear();
//        mShiftLogs.addAll(shiftLogs);
//    }

    public void inject(ShiftLogPersistenceManagerInterface shiftLogPersistenceManagerInterface, ShiftLogNetworkBridgeInterface shiftLogNetworkBridgeInterface) {
        mShiftLogPersistenceManagerInterface = shiftLogPersistenceManagerInterface;
        mShiftLogNetworkBridgeInterface = shiftLogNetworkBridgeInterface;
    }


}
