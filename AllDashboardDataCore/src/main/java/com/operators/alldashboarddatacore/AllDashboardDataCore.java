package com.operators.alldashboarddatacore;

import android.net.ParseException;
import android.util.Log;

import com.operators.alldashboarddatacore.interfaces.MachineDataUICallback;
import com.operators.alldashboarddatacore.interfaces.MachineStatusUICallback;
import com.operators.alldashboarddatacore.interfaces.OnTimeToEndChangedListener;
import com.operators.alldashboarddatacore.interfaces.ShiftForMachineUICallback;
import com.operators.alldashboarddatacore.interfaces.ShiftLogUICallback;
import com.operators.alldashboarddatacore.polling.EmeraldJobBase;
import com.operators.alldashboarddatacore.timecounter.TimeToEndCounter;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatainfra.interfaces.GetMachineDataCallback;
import com.operators.machinedatainfra.interfaces.GetMachineDataNetworkBridgeInterface;
import com.operators.machinedatainfra.interfaces.MachineDataPersistenceManagerInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.interfaces.GetMachineStatusCallback;
import com.operators.machinestatusinfra.interfaces.GetMachineStatusNetworkBridgeInterface;
import com.operators.machinestatusinfra.interfaces.MachineStatusPersistenceManagerInterface;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.shiftloginfra.ShiftForMachineCoreCallback;
import com.operators.shiftloginfra.ShiftForMachineResponse;
import com.operators.shiftloginfra.ShiftLogCoreCallback;
import com.operators.shiftloginfra.ShiftLogNetworkBridgeInterface;
import com.operators.shiftloginfra.ShiftLogPersistenceManagerInterface;
import com.ravtech.david.sqlcore.Event;
import com.zemingo.logrecorder.ZLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ravtech.co.il.publicutils.JobBase;

public class AllDashboardDataCore implements OnTimeToEndChangedListener {
    private static final String LOG_TAG = AllDashboardDataCore.class.getSimpleName();

    private static final int START_DELAY = 0;
    private EmeraldJobBase mJob;

    private GetMachineStatusNetworkBridgeInterface mGetMachineStatusNetworkBridgeInterface;
    private MachineStatusPersistenceManagerInterface mMachineStatusPersistenceManagerInterface;
    private MachineStatusUICallback mMachineStatusUICallback;

    private GetMachineDataNetworkBridgeInterface mGetMachineDataNetworkBridgeInterface;
    private MachineDataPersistenceManagerInterface mMachineDataPersistenceManagerInterface;
    private MachineDataUICallback mMachineDataUICallback;

    private ShiftLogPersistenceManagerInterface mShiftLogPersistenceManagerInterface;
    private ShiftLogNetworkBridgeInterface mShiftLogNetworkBridgeInterface;
    private ShiftLogUICallback mShiftLogUICallback;

    private boolean mGetMachineStatusFinish;
    private boolean mGetMachineDataFinish;
    private boolean mGetShiftLogFinish;

    private TimeToEndCounter mTimeToEndCounter;


    public AllDashboardDataCore(GetMachineStatusNetworkBridgeInterface getMachineStatusNetworkBridge, MachineStatusPersistenceManagerInterface machineStatusPersistenceManager, GetMachineDataNetworkBridgeInterface getMachineDataNetworkBridgeInterface, MachineDataPersistenceManagerInterface machineDataPersistenceManagerInterface, ShiftLogPersistenceManagerInterface shiftLogPersistenceManagerInterface, ShiftLogNetworkBridgeInterface shiftLogNetworkBridgeInterface) {
        mGetMachineStatusNetworkBridgeInterface = getMachineStatusNetworkBridge;
        mMachineStatusPersistenceManagerInterface = machineStatusPersistenceManager;

        mGetMachineDataNetworkBridgeInterface = getMachineDataNetworkBridgeInterface;
        mMachineDataPersistenceManagerInterface = machineDataPersistenceManagerInterface;

        mShiftLogPersistenceManagerInterface = shiftLogPersistenceManagerInterface;
        mShiftLogNetworkBridgeInterface = shiftLogNetworkBridgeInterface;

    }

    public void registerListener(MachineStatusUICallback machineStatusUICallback, MachineDataUICallback machineDataUICallback, ShiftLogUICallback shiftLogUICallback) {
        mMachineStatusUICallback = machineStatusUICallback;
        mMachineDataUICallback = machineDataUICallback;
        mShiftLogUICallback = shiftLogUICallback;
    }

    public void unregisterListener() {
        if (mMachineStatusUICallback != null) {
            mMachineStatusUICallback = null;
        }
        if (mMachineDataUICallback != null) {
            mMachineDataUICallback = null;
        }
        if (mShiftLogUICallback != null) {
            mShiftLogUICallback = null;
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

                getMachineData(onJobFinishedListener);
                getMachineStatus(onJobFinishedListener);
                getShiftLogs(onJobFinishedListener);
            }
        };
        // getPollingFrequency for all
        ZLogger.d(LOG_TAG, "startPolling(), Frequency: " + mMachineStatusPersistenceManagerInterface.getPollingFrequency());
        mJob.startJob(START_DELAY, mMachineStatusPersistenceManagerInterface.getPollingFrequency(), TimeUnit.SECONDS);

    }

    public void stopPolling() {

        if (mJob != null) {
            mJob.stopJob();
        }
    }

    private void getMachineStatus(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        if (mMachineStatusPersistenceManagerInterface != null) {
            mGetMachineStatusNetworkBridgeInterface.getMachineStatus(mMachineStatusPersistenceManagerInterface.getSiteUrl(), mMachineStatusPersistenceManagerInterface.getSessionId(), mMachineStatusPersistenceManagerInterface.getMachineId(), new GetMachineStatusCallback() {
                @Override
                public void onGetMachineStatusSucceeded(MachineStatus machineStatus) {


                    int timeToEndInSeconds;
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
                    mGetMachineStatusFinish = true;
                    setJobFinishToAll(onJobFinishedListener);
                }

                @Override
                public void onGetMachineStatusFailed(ErrorObjectInterface reason) {
                    ZLogger.w(LOG_TAG, "getMachineStatus() onGetMachineStatusFailed " + reason.getError());
                    if (mMachineStatusUICallback != null) {
                        mMachineStatusUICallback.onStatusReceiveFailed(reason);
                    } else {
                        ZLogger.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
                    }
                    mGetMachineStatusFinish = true;
                    setJobFinishToAll(onJobFinishedListener);
                }
            }, mMachineStatusPersistenceManagerInterface.getTotalRetries(), mMachineStatusPersistenceManagerInterface.getRequestTimeout());
        }
    }

    private void getMachineData(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        if (mMachineDataPersistenceManagerInterface != null) {
            mGetMachineDataNetworkBridgeInterface.getMachineData(mMachineDataPersistenceManagerInterface.getSiteUrl(), mMachineDataPersistenceManagerInterface.getSessionId(), mMachineDataPersistenceManagerInterface.getMachineId(), mMachineDataPersistenceManagerInterface.getMachineDataStartingFrom(), new GetMachineDataCallback<Widget>() {
                @Override
                public void onGetMachineDataSucceeded(ArrayList<Widget> widgetList) {
                    if (mMachineDataUICallback != null) {


                        mMachineDataUICallback.onDataReceivedSuccessfully(widgetList);
                    } else {
                        ZLogger.w(LOG_TAG, "getMachineData() mMachineDataUICallback is null");
                    }
                    mGetMachineDataFinish = true;
                    setJobFinishToAll(onJobFinishedListener);

                }

                @Override
                public void onGetMachineDataFailed(ErrorObjectInterface reason) {
                    ZLogger.w(LOG_TAG, "getMachineData() onGetMachineDataFailed " + reason.getError());
                    mGetMachineDataFinish = true;
                    setJobFinishToAll(onJobFinishedListener);

                    if (mMachineDataUICallback != null) {
                        mMachineDataUICallback.onDataReceiveFailed(reason);
                    }
                }
            }, mMachineDataPersistenceManagerInterface.getTotalRetries(), mMachineDataPersistenceManagerInterface.getRequestTimeout());
        }
    }

    private void getShiftLogs(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        // remove seconds and milliseconds from shift log starting from.
        String startingFrom = mShiftLogPersistenceManagerInterface.getShiftLogStartingFrom();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        try {
            Date date = dateFormat.parse(startingFrom);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startingFrom = dateFormat.format(cal.getTime());
        } catch (ParseException ignored) {
        } catch (java.text.ParseException e) {
            if (e.getMessage() != null)
                Log.e(LOG_TAG, e.getMessage());
        }

        Log.d(LOG_TAG, "shift log startingfrom: " + startingFrom);

        mShiftLogNetworkBridgeInterface.getShiftLog(
                mShiftLogPersistenceManagerInterface.getSiteUrl(),
                mShiftLogPersistenceManagerInterface.getSessionId(),
                mShiftLogPersistenceManagerInterface.getMachineId(),
                startingFrom,
                new ShiftLogCoreCallback<Event>() {
                    @Override
                    public void onShiftLogSucceeded(ArrayList<Event> events) {
                        if (mShiftLogUICallback != null) {
                            mShiftLogUICallback.onGetShiftLogSucceeded(events);
                        } else {
                            ZLogger.w(LOG_TAG, "getShiftLogs() mShiftLogUICallback is null");
                        }
                        mGetShiftLogFinish = true;
                        setJobFinishToAll(onJobFinishedListener);
                    }

                    @Override
                    public void onShiftLogFailed(ErrorObjectInterface reason) {
                        ZLogger.w(LOG_TAG, "getShiftLogs() onGetShiftLogFailed " + reason.getError());
                        mGetShiftLogFinish = true;
                        setJobFinishToAll(onJobFinishedListener);

                        if (mShiftLogUICallback != null) {
                            mShiftLogUICallback.onGetShiftLogFailed(reason);
                        }
                    }

                }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout());
    }



    private void setJobFinishToAll(JobBase.OnJobFinishedListener onJobFinishedListener) {
        if (mGetMachineStatusFinish && mGetMachineDataFinish && mGetShiftLogFinish) {
            onJobFinishedListener.onJobFinished();

            mGetMachineStatusFinish = false;
            mGetMachineDataFinish = false;
            mGetShiftLogFinish = false;
        }
    }

    public void getShiftForMachine(final ShiftForMachineUICallback shiftForMachineUICallback) {
        mShiftLogNetworkBridgeInterface.GetShiftForMachine(mShiftLogPersistenceManagerInterface.getSiteUrl(), mShiftLogPersistenceManagerInterface.getSessionId(), mShiftLogPersistenceManagerInterface.getMachineId(), new ShiftForMachineCoreCallback<ShiftForMachineResponse>() {
            @Override
            public void onShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse) {
                shiftForMachineUICallback.onGetShiftForMachineSucceeded(shiftForMachineResponse);
                ZLogger.w(LOG_TAG, "getShiftForMachine() onShiftForMachineSucceeded");
            }

            @Override
            public void onShiftForMachineFailed(ErrorObjectInterface reason) {
                shiftForMachineUICallback.onGetShiftForMachineFailed(reason);
                ZLogger.w(LOG_TAG, "getShiftForMachine() onShiftForMachineFailed" + reason.getDetailedDescription());
            }

        }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout());

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
