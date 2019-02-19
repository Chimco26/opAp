package com.operators.alldashboarddatacore;

import android.util.Log;

import com.example.common.Event;
import com.example.oppapplog.OppAppLogger;
import com.operators.alldashboarddatacore.interfaces.ActualBarExtraDetailsUICallback;
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
import com.operators.shiftloginfra.ActualBarExtraDetailsCallback;
import com.operators.shiftloginfra.ShiftForMachineCoreCallback;
import com.operators.shiftloginfra.ShiftLogCoreCallback;
import com.operators.shiftloginfra.ShiftLogNetworkBridgeInterface;
import com.operators.shiftloginfra.ShiftLogPersistenceManagerInterface;
import com.operators.shiftloginfra.model.ActualBarExtraResponse;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;

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
    private final AllDashboardDataCoreListener mListener;
    private EmeraldJobBase mJob;

    private GetMachineStatusNetworkBridgeInterface mGetMachineStatusNetworkBridgeInterface;
    private MachineStatusPersistenceManagerInterface mMachineStatusPersistenceManagerInterface;
    private MachineStatusUICallback mMachineStatusUICallback;

    private GetMachineDataNetworkBridgeInterface mGetMachineDataNetworkBridgeInterface;
    private MachineDataPersistenceManagerInterface mMachineDataPersistenceManagerInterface;
    private MachineDataUICallback mMachineDataUICallback;
    private ActualBarExtraDetailsUICallback mActualBarExtraUICallback;

    private ShiftLogPersistenceManagerInterface mShiftLogPersistenceManagerInterface;
    private ShiftLogNetworkBridgeInterface mShiftLogNetworkBridgeInterface;
    private ShiftLogUICallback mShiftLogUICallback;

    private boolean mGetMachineStatusFinish;
    private boolean mGetMachineDataFinish;
    private boolean mGetShiftLogFinish;

    private TimeToEndCounter mTimeToEndCounter;


    public AllDashboardDataCore(AllDashboardDataCoreListener listener, GetMachineStatusNetworkBridgeInterface getMachineStatusNetworkBridge,
                                MachineStatusPersistenceManagerInterface machineStatusPersistenceManager,
                                GetMachineDataNetworkBridgeInterface getMachineDataNetworkBridgeInterface,
                                MachineDataPersistenceManagerInterface machineDataPersistenceManagerInterface,
                                ShiftLogPersistenceManagerInterface shiftLogPersistenceManagerInterface,
                                ShiftLogNetworkBridgeInterface shiftLogNetworkBridgeInterface) {

        mGetMachineStatusNetworkBridgeInterface = getMachineStatusNetworkBridge;
        mMachineStatusPersistenceManagerInterface = machineStatusPersistenceManager;

        mGetMachineDataNetworkBridgeInterface = getMachineDataNetworkBridgeInterface;
        mMachineDataPersistenceManagerInterface = machineDataPersistenceManagerInterface;

        mShiftLogPersistenceManagerInterface = shiftLogPersistenceManagerInterface;
        mShiftLogNetworkBridgeInterface = shiftLogNetworkBridgeInterface;

        mListener = listener;

    }

    public void registerListener(MachineStatusUICallback machineStatusUICallback, MachineDataUICallback machineDataUICallback,
                                 ShiftLogUICallback shiftLogUICallback, ActualBarExtraDetailsUICallback actualBarExtraDetailsUICallback) {
        mMachineStatusUICallback = machineStatusUICallback;
        mMachineDataUICallback = machineDataUICallback;
        mShiftLogUICallback = shiftLogUICallback;
        mActualBarExtraUICallback = actualBarExtraDetailsUICallback;
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
        if (mActualBarExtraUICallback != null) {
            mActualBarExtraUICallback = null;
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

                mListener.onExecuteJob(onJobFinishedListener);
                //sendRequestForPolling(onJobFinishedListener, jobId);
            }
        };
        // getPollingFrequency for all
        OppAppLogger.getInstance().d(LOG_TAG, "startPolling(), Frequency: " + mMachineStatusPersistenceManagerInterface.getPollingFrequency());
        mJob.startJob(START_DELAY, mMachineStatusPersistenceManagerInterface.getPollingFrequency(), TimeUnit.SECONDS);

    }

    public void sendRequestForPolling(JobBase.OnJobFinishedListener onJobFinishedListener, Integer jobId, Integer selectProductJobId) {

        if (selectProductJobId != null) {

            jobId = selectProductJobId;

        } else if (jobId == null) {

            jobId = 0;
        }

        getMachineStatus(onJobFinishedListener, jobId);
        getMachineData(onJobFinishedListener, jobId);
        getActualBarExtraDetails();
        getShiftLogs(onJobFinishedListener);
    }

    public void stopPolling() {

        if (mJob != null) {
            mJob.stopJob();
        }
    }

    private void getMachineStatus(final JobBase.OnJobFinishedListener onJobFinishedListener, Integer joshID) {
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
                                OppAppLogger.getInstance().w(LOG_TAG, "All Machine Data Is 0!!");
                            }
                        } else {
                            OppAppLogger.getInstance().w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
                        }
                    } else {
                        OppAppLogger.getInstance().e(LOG_TAG, "machineStatus is null");
                    }
                    mGetMachineStatusFinish = true;
                    setJobFinishToAll(onJobFinishedListener);
                }

                @Override
                public void onGetMachineStatusFailed(ErrorObjectInterface reason) {
                    OppAppLogger.getInstance().w(LOG_TAG, "getMachineStatus() onGetMachineStatusFailed " + reason.getError());
                    if (mMachineStatusUICallback != null) {
                        mMachineStatusUICallback.onStatusReceiveFailed(reason);
                    } else {
                        OppAppLogger.getInstance().w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
                    }
                    mGetMachineStatusFinish = true;
                    setJobFinishToAll(onJobFinishedListener);
                }
            }, mMachineStatusPersistenceManagerInterface.getTotalRetries(), mMachineStatusPersistenceManagerInterface.getRequestTimeout(), joshID);
        }
    }

    private void getMachineData(final JobBase.OnJobFinishedListener onJobFinishedListener, Integer joshID) {
        if (mMachineDataPersistenceManagerInterface != null) {
            mGetMachineDataNetworkBridgeInterface.getMachineData(mMachineDataPersistenceManagerInterface.getSiteUrl(), mMachineDataPersistenceManagerInterface.getSessionId(), mMachineDataPersistenceManagerInterface.getMachineId(), mMachineDataPersistenceManagerInterface.getMachineDataStartingFrom(), new GetMachineDataCallback<Widget>() {
                @Override
                public void onGetMachineDataSucceeded(ArrayList<Widget> widgetList) {
                    if (mMachineDataUICallback != null) {

                        mMachineDataUICallback.onDataReceivedSuccessfully(widgetList);
                    } else {
                        OppAppLogger.getInstance().w(LOG_TAG, "getMachineData() mMachineDataUICallback is null");
                    }
                    mGetMachineDataFinish = true;
                    setJobFinishToAll(onJobFinishedListener);

                }

                @Override
                public void onGetMachineDataFailed(ErrorObjectInterface reason) {
                    OppAppLogger.getInstance().w(LOG_TAG, "getMachineData() onGetMachineDataFailed " + reason.getError());
                    mGetMachineDataFinish = true;
                    setJobFinishToAll(onJobFinishedListener);

                    if (mMachineDataUICallback != null) {
                        mMachineDataUICallback.onDataReceiveFailed(reason);
                    }
                }
            }, mMachineDataPersistenceManagerInterface.getTotalRetries(), mMachineDataPersistenceManagerInterface.getRequestTimeout(), joshID);
        }
    }

    private void getShiftLogs(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        // remove seconds and milliseconds from shift log starting from.
        String startingFrom = mShiftLogPersistenceManagerInterface.getShiftLogStartingFrom();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

        try {
            Date date = dateFormat.parse(startingFrom);
            startingFrom = getTimeForRequest(date, dateFormat);
        } catch (java.text.ParseException e) {
            if (e.getMessage() != null) {
                Log.e(LOG_TAG, e.getMessage());
            }
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
                            OppAppLogger.getInstance().w(LOG_TAG, "getShiftLogs() mShiftLogUICallback is null");
                        }
                        mGetShiftLogFinish = true;
                        setJobFinishToAll(onJobFinishedListener);
                    }

                    @Override
                    public void onShiftLogFailed(ErrorObjectInterface reason) {
                        OppAppLogger.getInstance().w(LOG_TAG, "getShiftLogs() onGetShiftLogFailed " + reason.getError());
                        mGetShiftLogFinish = true;
                        setJobFinishToAll(onJobFinishedListener);

                        if (mShiftLogUICallback != null) {
                            mShiftLogUICallback.onGetShiftLogFailed(reason);
                        }
                    }

                }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout());
    }

    private String getTimeForRequest(Date date, SimpleDateFormat dateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return dateFormat.format(cal.getTime());
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
                OppAppLogger.getInstance().w(LOG_TAG, "getShiftForMachine() onShiftForMachineSucceeded");
            }

            @Override
            public void onShiftForMachineFailed(ErrorObjectInterface reason) {
                shiftForMachineUICallback.onGetShiftForMachineFailed(reason);
                OppAppLogger.getInstance().w(LOG_TAG, "getShiftForMachine() onShiftForMachineFailed" + reason.getDetailedDescription());
            }

        }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout());

    }

    public void getActualBarExtraDetails() {
        String startingFrom = mShiftLogPersistenceManagerInterface.getShiftLogStartingFrom();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        try {
            Date date = dateFormat.parse(startingFrom);
            startingFrom = getTimeForRequest(date, dateFormat);
//            startingFrom = getDate(System.currentTimeMillis() - DAY_IN_MILLIS, "yyyy-MM-dd HH:mm:ss.SSS");
        } catch (java.text.ParseException e) {
            if (e.getMessage() != null) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }
        String endTime = getTimeForRequest(new Date(), dateFormat);
        mShiftLogNetworkBridgeInterface.GetActualBarExtraDetails(mShiftLogPersistenceManagerInterface.getSiteUrl(),
                mShiftLogPersistenceManagerInterface.getSessionId(), startingFrom, endTime, new ActualBarExtraDetailsCallback<ActualBarExtraResponse>() {
            @Override
            public void onActualBarExtraDetailsSucceeded(ActualBarExtraResponse actualBarExtraResponse) {
                if (mActualBarExtraUICallback != null) {
                    mActualBarExtraUICallback.onActualBarExtraDetailsSucceeded(actualBarExtraResponse);
                    OppAppLogger.getInstance().w(LOG_TAG, "getActualBarExtraDetails() onActualBarExtraDetailsSucceeded");
                }
            }

            @Override
            public void onActualBarExtraDetailsFailed(ErrorObjectInterface reason) {
                if (mActualBarExtraUICallback != null) {
                    mActualBarExtraUICallback.onActualBarExtraDetailsFailed(reason);
                    OppAppLogger.getInstance().w(LOG_TAG, "getActualBarExtraDetails() onActualBarExtraDetailsFailed" + reason.getDetailedDescription());
                }
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
            OppAppLogger.getInstance().w(LOG_TAG, "onTimeToEndChanged() mMachineStatusUICallback is null");
        }
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public interface AllDashboardDataCoreListener {

        void onExecuteJob(JobBase.OnJobFinishedListener onJobFinishedListener);
    }
}
