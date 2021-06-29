package com.operatorsapp.server.pulling;

import android.util.Log;

import com.example.common.ErrorResponse;
import com.example.common.Event;
import com.example.common.StandardResponse;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.callback.GetMachineJoshDataCallback;
import com.example.common.callback.GetStopLogCallback;
import com.example.common.callback.MachineJoshDataCallback;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.permissions.PermissionResponse;
import com.example.common.request.MachineIdRequest;
import com.example.common.request.MachineJoshDataRequest;
import com.example.oppapplog.OppAppLogger;
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
import com.operators.shiftloginfra.model.ShiftForMachineResponse;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.polling.EmeraldJobBase;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.pulling.interfaces.ActualBarExtraDetailsUICallback;
import com.operatorsapp.server.pulling.interfaces.MachineDataUICallback;
import com.operatorsapp.server.pulling.interfaces.MachinePermissionCallback;
import com.operatorsapp.server.pulling.interfaces.MachineStatusUICallback;
import com.operatorsapp.server.pulling.interfaces.OnTimeToEndChangedListener;
import com.operatorsapp.server.pulling.interfaces.ShiftForMachineUICallback;
import com.operatorsapp.server.pulling.interfaces.ShiftLogUICallback;
import com.operatorsapp.server.pulling.timecounter.TimeToEndCounter;
import com.operatorsapp.utils.SimpleRequests;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ravtech.co.il.publicutils.JobBase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.format.DateUtils.DAY_IN_MILLIS;

public class AllDashboardDataCore {
    private static final String LOG_TAG = AllDashboardDataCore.class.getSimpleName();

    private static final int START_DELAY = 0;
    private static boolean isOnlineChecking = false;
    private final WeakReference<AllDashboardDataCoreListener> mListener;
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
    private MachineJoshDataCallback mMachineJoshDataCallback;
    private MachinePermissionCallback mMachinePermissionsCallback;
    private GetStopLogCallback mStopEventsLineCallback;


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

        mListener = new WeakReference<>(listener);

    }

    public void registerListener(MachineStatusUICallback machineStatusUICallback, MachineDataUICallback machineDataUICallback,
                                 ShiftLogUICallback shiftLogUICallback, ActualBarExtraDetailsUICallback actualBarExtraDetailsUICallback, MachineJoshDataCallback machineJoshDataCallback,
                                 MachinePermissionCallback machinePermissionCallback, GetStopLogCallback stopEventsLineCallback) {
        if (machineStatusUICallback != null) {
            mMachineStatusUICallback = machineStatusUICallback;
        }
        if (machineDataUICallback != null) {
            mMachineDataUICallback = machineDataUICallback;
        }
        if (shiftLogUICallback != null) {
            mShiftLogUICallback = shiftLogUICallback;
        }
        if (actualBarExtraDetailsUICallback != null) {
            mActualBarExtraUICallback = actualBarExtraDetailsUICallback;
        }
        if (machineJoshDataCallback != null) {
            mMachineJoshDataCallback = machineJoshDataCallback;
        }
        if (machinePermissionCallback != null) {
            mMachinePermissionsCallback = machinePermissionCallback;
        }
        if (stopEventsLineCallback != null) {
            mStopEventsLineCallback = stopEventsLineCallback;
        }
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
        if (mMachineJoshDataCallback != null) {
            mMachineJoshDataCallback = null;
        }
        if (mStopEventsLineCallback != null) {
            mStopEventsLineCallback = null;
        }
        if (mMachinePermissionsCallback != null) {
            mMachinePermissionsCallback = null;
        }
        stopTimer();
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

                if (mListener != null && mListener.get() != null) {
                    mListener.get().onExecuteJob(onJobFinishedListener);
                }
                //sendRequestForPolling(onJobFinishedListener, jobId);
            }
        };
        // getPollingFrequency for all


        OppAppLogger.d(LOG_TAG, "startPolling(), Frequency: " + mMachineStatusPersistenceManagerInterface.getPollingFrequency());
        mJob.startJob(START_DELAY, mMachineStatusPersistenceManagerInterface.getPollingFrequency(), TimeUnit.SECONDS);

    }

    public void sendRequestForPolling(JobBase.OnJobFinishedListener onJobFinishedListener, Integer jobId, Integer selectProductJobId, String shiftLogStartingFrom) {

        if (selectProductJobId != null) {

            jobId = selectProductJobId;

        } else if (jobId == null) {

            jobId = 0;
        }

        isOnline();
        getPermissionForMachine();
        getMachineStatus(onJobFinishedListener, jobId);
        getMachineData(onJobFinishedListener, jobId);
        getMachineJoshData();
        getActualBarExtraDetails(shiftLogStartingFrom);
        getShiftLogs(onJobFinishedListener);
        getStopEventsLine();
    }

    public void stopPolling() {

        if (mJob != null) {
            mJob.stopJob();
        }
    }

    private void isOnline() {
        boolean isOnline = false;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            isOnline = (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!isOnline && mListener != null && mListener.get() != null) {
            mListener.get().onNoInternetConnection();
        }

//        HandlerThread handlerThread = new HandlerThread("HandlerThread");
//        handlerThread.start();
//
//        if (!isOnlineChecking) {
//            isOnlineChecking = true;
//            Handler handler = new Handler(handlerThread.getLooper());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        int timeoutMs = 1500;
//                        Socket sock = new Socket();
//                        SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
//
//                        sock.connect(sockaddr, timeoutMs);
//                        sock.close();
//                        isOnlineChecking = false;
//                    } catch (IOException e) {
//                        isOnlineChecking = false;
//                        OperatorApplication.showNoInternetMsg();
//                        mListener.onNoInternetConnection();
//                    }
//                }
//            });
//        }
    }

    private void getStopEventsLine() {
        PersistenceManager pm = PersistenceManager.getInstance();
        if (mStopEventsLineCallback != null && pm.getMachineLineId() > 0) {
            SimpleRequests.getLineShiftLog(pm.getSiteUrl(), mStopEventsLineCallback, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
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
                        OppAppLogger.w(LOG_TAG, "" + mMachineDataUICallback);
                        if (mMachineStatusUICallback != null) {
                            if (machineStatus.getAllMachinesData().size() > 0) {
                                mMachineStatusUICallback.onStatusReceivedSuccessfully(machineStatus);
                            } else {
                                OppAppLogger.w(LOG_TAG, "All Machine Data Is 0!!");
                            }
                        } else {
                            OppAppLogger.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
                        }
                    } else {
                        OppAppLogger.e(LOG_TAG, "machineStatus is null");
                    }
                    mGetMachineStatusFinish = true;
                    setJobFinishToAll(onJobFinishedListener);
                }

                @Override
                public void onGetMachineStatusFailed(StandardResponse reason) {
                    OppAppLogger.w(LOG_TAG, "getMachineStatus() onGetMachineStatusFailed " + reason.getError());
                    if (mMachineStatusUICallback != null) {
                        mMachineStatusUICallback.onStatusReceiveFailed(reason);
                    } else {
                        OppAppLogger.w(LOG_TAG, "getMachineStatus() mMachineStatusUICallback is null");
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
                        OppAppLogger.w(LOG_TAG, "getMachineData() mMachineDataUICallback is null");
                    }
                    mGetMachineDataFinish = true;
                    setJobFinishToAll(onJobFinishedListener);

                }

                @Override
                public void onGetMachineDataFailed(StandardResponse reason) {
                    OppAppLogger.w(LOG_TAG, "getMachineData() onGetMachineDataFailed " + reason.getError());
                    mGetMachineDataFinish = true;
                    setJobFinishToAll(onJobFinishedListener);

                    if (mMachineDataUICallback != null) {
                        mMachineDataUICallback.onDataReceiveFailed(reason);
                    }
                }
            }, mMachineDataPersistenceManagerInterface.getTotalRetries(), mMachineDataPersistenceManagerInterface.getRequestTimeout(), joshID);
        }
    }

    private void getPermissionForMachine() {
        NetworkManager.getInstance().getPermissionForMachine(new MachineIdRequest(String.valueOf(PersistenceManager.getInstance().getMachineId())), new Callback<PermissionResponse>() {
            @Override
            public void onResponse(Call<PermissionResponse> call, Response<PermissionResponse> response) {

                if (mMachinePermissionsCallback != null) {
                    if (response.body() != null && response.body().getError().getErrorDesc() == null) {
                        mMachinePermissionsCallback.onMachinePermissionCallbackSucceeded(response.body());
                    } else {
                        onFailure(call, new Throwable(""));
                    }
                } else {
                    OppAppLogger.w(LOG_TAG, "getPermissionForMachine() mMachinePermissionsCallback is null");
                }
            }

            @Override
            public void onFailure(Call<PermissionResponse> call, Throwable t) {
                OppAppLogger.w(LOG_TAG, "getPermissionForMachine() failed");
                StandardResponse errorResponse = new StandardResponse(ErrorResponse.ErrorCode.No_data, "getPermissionForMachine Response is null Error");
                if (mMachinePermissionsCallback != null) {
                    mMachinePermissionsCallback.onMachinePermissionCallbackFailed(errorResponse);
                }
            }
        });
    }

    private void getShiftLogs(final JobBase.OnJobFinishedListener onJobFinishedListener) {
        // remove seconds and milliseconds from shift log starting from.
        String startingFrom = mShiftLogPersistenceManagerInterface.getShiftLogStartingFrom();
        //setShiftLogStartingFrom to last polling time (know!)
        //in ationbarAndEventsFragment in oncomplete of MyTask if is New shiftlog (mIsTimeLine = true) => change this value to end of last event if last event his closed event
        mShiftLogPersistenceManagerInterface.setShiftLogStartingFrom(getDate(System.currentTimeMillis() - DAY_IN_MILLIS, "yyyy-MM-dd HH:mm:ss.SSS"));
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
                            OppAppLogger.w(LOG_TAG, "getShiftLogs() mShiftLogUICallback is null");
                        }
                        mGetShiftLogFinish = true;
                        setJobFinishToAll(onJobFinishedListener);
                    }

                    @Override
                    public void onShiftLogFailed(StandardResponse reason) {
                        OppAppLogger.w(LOG_TAG, "getShiftLogs() onGetShiftLogFailed " + reason.getError());
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
                if (shiftForMachineUICallback != null) {
                    shiftForMachineUICallback.onGetShiftForMachineSucceeded(shiftForMachineResponse);
                }
                OppAppLogger.w(LOG_TAG, "getShiftForMachine() onShiftForMachineSucceeded");
            }

            @Override
            public void onShiftForMachineFailed(StandardResponse reason) {
                if (shiftForMachineUICallback != null) {
                    shiftForMachineUICallback.onGetShiftForMachineFailed(reason);
                }
                OppAppLogger.w(LOG_TAG, "getShiftForMachine() onShiftForMachineFailed" + reason.getError().getErrorDesc());
            }

        }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout());

    }

    public void getActualBarExtraDetails(String shiftLogStartingFrom) {
//        String startingFrom = mShiftLogPersistenceManagerInterface.getShiftLogStartingFrom();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
//        try {
//            Date date = dateFormat.parse(startingFrom);
//            startingFrom = getTimeForRequest(date, dateFormat);
////            startingFrom = getDate(System.currentTimeMillis() - DAY_IN_MILLIS, "yyyy-MM-dd HH:mm:ss.SSS");
//        } catch (java.text.ParseException e) {
//            if (e.getMessage() != null) {
//                Log.e(TAG, e.getMessage());
//            }
//        }
        String endTime = getTimeForRequest(new Date(), dateFormat);
        shiftLogStartingFrom = getTimeForRequest(new Date(new Date().getTime() - DAY_IN_MILLIS), dateFormat);
        mShiftLogNetworkBridgeInterface.GetActualBarExtraDetails(mShiftLogPersistenceManagerInterface.getSiteUrl(),
                mShiftLogPersistenceManagerInterface.getSessionId(), shiftLogStartingFrom, endTime, String.valueOf(mShiftLogPersistenceManagerInterface.getMachineId()), new ActualBarExtraDetailsCallback<ActualBarExtraResponse>() {
                    @Override
                    public void onActualBarExtraDetailsSucceeded(ActualBarExtraResponse actualBarExtraResponse) {
                        if (mActualBarExtraUICallback != null) {
                            mActualBarExtraUICallback.onActualBarExtraDetailsSucceeded(actualBarExtraResponse);
                            OppAppLogger.w(LOG_TAG, "getActualBarExtraDetails() onActualBarExtraDetailsSucceeded");
                        }
                    }

                    @Override
                    public void onActualBarExtraDetailsFailed(StandardResponse reason) {
                        if (mActualBarExtraUICallback != null) {
                            mActualBarExtraUICallback.onActualBarExtraDetailsFailed(reason);
                            OppAppLogger.w(LOG_TAG, "getActualBarExtraDetails() onActualBarExtraDetailsFailed" + reason.getError().getErrorDesc());
                        }
                    }

                }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout());

    }

    public void getMachineJoshData() {
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
        startingFrom = getTimeForRequest(new Date(new Date().getTime() - DAY_IN_MILLIS), dateFormat);
        mShiftLogNetworkBridgeInterface.GetMachineJoshData(mShiftLogPersistenceManagerInterface.getSiteUrl(),
                mShiftLogPersistenceManagerInterface.getSessionId(), startingFrom, endTime, String.valueOf(mShiftLogPersistenceManagerInterface.getMachineId()), new GetMachineJoshDataCallback<MachineJoshDataResponse>() {
                    @Override
                    public void onGetMachineJoshDataSuccess(MachineJoshDataResponse machineJoshDataResponse) {
                        if (mMachineJoshDataCallback != null) {
                            mMachineJoshDataCallback.onMachineJoshDataCallbackSucceeded(machineJoshDataResponse);
                            OppAppLogger.w(LOG_TAG, "getMachineJoshData() onGetMachineJoshDataSuccess");
                        }
                    }

                    @Override
                    public void onGetMachineJoshDataFailed(StandardResponse reason) {
                        if (mMachineJoshDataCallback != null) {
                            mMachineJoshDataCallback.onMachineJoshDataCallbackFailed(reason);
                            OppAppLogger.w(LOG_TAG, "getMachineJoshData() onGetMachineJoshDataFailed" + reason.getError().getErrorDesc());
                        }
                    }

                }, mShiftLogPersistenceManagerInterface.getTotalRetries(), mShiftLogPersistenceManagerInterface.getRequestTimeout()
                , new MachineJoshDataRequest(mShiftLogPersistenceManagerInterface.getMachineId(), startingFrom, mShiftLogPersistenceManagerInterface.getSessionId()));

    }


    private void startTimer(int timeInSeconds) {
        if (mTimeToEndCounter == null) {//|| mTimeToEndCounter.getOnTimeToEndChangedListener() == null
            stopTimer();
            mTimeToEndCounter = null;
            mTimeToEndCounter = new TimeToEndCounter();
        }
        mTimeToEndCounter.calculateTimeToEnd(timeInSeconds, new OnTimeToEndChangedListener() {
            @Override
            public void onTimeToEndChanged(long millisUntilFinished) {
                if (mMachineStatusUICallback != null) {
                    mMachineStatusUICallback.onTimerChanged(millisUntilFinished);
                } else {
                    OppAppLogger.w(LOG_TAG, "onTimeToEndChanged() mMachineStatusUICallback is null");
                    stopTimer();
                }
            }
        });
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public MachineStatusUICallback getmMachineStatusUICallback() {
        return mMachineStatusUICallback;
    }

    public MachineDataUICallback getmMachineDataUICallback() {
        return mMachineDataUICallback;
    }

    public ActualBarExtraDetailsUICallback getmActualBarExtraUICallback() {
        return mActualBarExtraUICallback;
    }

    public MachineJoshDataCallback getmMachineJoshDataCallback() {
        return mMachineJoshDataCallback;
    }

    public ShiftLogUICallback getmShiftLogUICallback() {
        return mShiftLogUICallback;
    }

    public GetStopLogCallback getmStopEventsLineCallback() {
        return mStopEventsLineCallback;
    }

    public MachinePermissionCallback getmMachinePermissionsCallback() {
        return mMachinePermissionsCallback;
    }

    public interface AllDashboardDataCoreListener {

        void onExecuteJob(JobBase.OnJobFinishedListener onJobFinishedListener);

        void onNoInternetConnection();
    }
}
