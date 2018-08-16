package com.operatorsapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.operators.activejobslistformachinecore.ActiveJobsListForMachineCore;
import com.operators.activejobslistformachinecore.interfaces.ActiveJobsListForMachineUICallbackListener;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachinenetworkbridge.ActiveJobsListForMachineNetworkBridge;
import com.operators.alldashboarddatacore.AllDashboardDataCore;
import com.operators.alldashboarddatacore.interfaces.MachineDataUICallback;
import com.operators.alldashboarddatacore.interfaces.MachineStatusUICallback;
import com.operators.alldashboarddatacore.interfaces.OnTimeToEndChangedListener;
import com.operators.alldashboarddatacore.interfaces.ShiftForMachineUICallback;
import com.operators.alldashboarddatacore.interfaces.ShiftLogUICallback;
import com.operators.alldashboarddatacore.timecounter.TimeToEndCounter;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.getmachinesstatusnetworkbridge.GetMachineStatusNetworkBridge;
import com.operators.getmachinesstatusnetworkbridge.server.requests.SetProductionModeForMachineRequest;
import com.operators.infra.Machine;
import com.operators.jobscore.JobsCore;
import com.operators.jobscore.interfaces.JobsForMachineUICallbackListener;
import com.operators.jobsinfra.JobListForMachine;
import com.operators.jobsnetworkbridge.JobsNetworkBridge;
import com.operators.logincore.LoginCore;
import com.operators.logincore.interfaces.LoginUICallback;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinedatanetworkbridge.GetMachineDataNetworkBridge;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatornetworkbridge.OperatorNetworkBridge;
import com.operators.reportfieldsformachinecore.ReportFieldsForMachineCore;
import com.operators.reportfieldsformachinecore.interfaces.ReportFieldsForMachineUICallback;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachinenetworkbridge.ReportFieldsForMachineNetworkBridge;
import com.operators.reportrejectinfra.GetAllRecipeCallback;
import com.operators.reportrejectinfra.PostSplitEventCallback;
import com.operators.reportrejectnetworkbridge.server.ErrorObject;
import com.operators.reportrejectnetworkbridge.server.request.SplitEventRequest;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.Response;
import com.operators.shiftloginfra.ShiftForMachineResponse;
import com.operators.shiftlognetworkbridge.ShiftLogNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.fragments.ActionBarAndEventsFragment;
import com.operatorsapp.fragments.AdvancedSettingsFragment;
import com.operatorsapp.fragments.RecipeFragment;
import com.operatorsapp.fragments.ReportRejectsFragment;
import com.operatorsapp.fragments.ReportStopReasonFragment;
import com.operatorsapp.fragments.SelectStopReasonFragment;
import com.operatorsapp.fragments.SettingsFragment;
import com.operatorsapp.fragments.SignInOperatorFragment;
import com.operatorsapp.fragments.ViewPagerFragment;
import com.operatorsapp.fragments.WidgetFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnReportFieldsUpdatedCallbackListener;
import com.operatorsapp.interfaces.ApproveFirstItemFragmentCallbackListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.DashBoardActivityToSelectedJobFragmentCallback;
import com.operatorsapp.interfaces.DashboardActivityToJobsFragmentCallback;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.interfaces.SettingsInterface;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.PdfObject;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.callback.PostProductionModeCallback;
import com.operatorsapp.utils.ChangeLang;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.broadcast.RefreshPollingBroadcast;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.ravtech.david.sqlcore.Event;
import com.zemingo.logrecorder.ZLogger;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ravtech.co.il.publicutils.JobBase;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardActivity extends AppCompatActivity implements OnCroutonRequestListener,
        OnActivityCallbackRegistered, GoToScreenListener, JobsFragmentToDashboardActivityCallback,
        OperatorCoreToDashboardActivityCallback,
        ReportFieldsFragmentCallbackListener, SettingsInterface,
        OnTimeToEndChangedListener, CroutonRootProvider, ApproveFirstItemFragmentCallbackListener,
        RefreshPollingBroadcast.RefreshPollingListener, CroutonCreator.CroutonListener,
        ActionBarAndEventsFragment.ActionBarAndEventsFragmentListener,
        ReportStopReasonFragment.ReportStopReasonFragmentListener,
        ViewPagerFragment.OnViewPagerListener,
        RecipeFragment.OnRecipeFragmentListener,
        AdvancedSettingsFragment.AdvancedSettingsListener,
        ShowDashboardCroutonListener, AllDashboardDataCore.AllDashboardDataCoreListener,
        DashboardCentralContainerListener {

    private static final String LOG_TAG = DashboardActivity.class.getSimpleName();

    private boolean ignoreFromOnPause = false;
    public static final String DASHBOARD_FRAGMENT = "dashboard_fragment";
    private CroutonCreator mCroutonCreator;
    private TimeToEndCounter mTimeToEndCounter;
    private DashboardActivityToJobsFragmentCallback mDashboardActivityToJobsFragmentCallback;
    private DashBoardActivityToSelectedJobFragmentCallback mDashboardActivityToSelectedJobFragmentCallback;
    private JobsCore mJobsCore;
    private ReportFieldsForMachineCore mReportFieldsForMachineCore;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private OnReportFieldsUpdatedCallbackListener mOnReportFieldsUpdatedCallbackListener;
    private AllDashboardDataCore mAllDashboardDataCore;
    private RefreshPollingBroadcast mRefreshBroadcast = null;
    private ArrayList<DashboardUICallbackListener> mDashboardUICallbackListenerList = new ArrayList<>();
    private WidgetFragment mWidgetFragment;
    private ActionBarAndEventsFragment mActionBarAndEventsFragment;
    private View mContainer2;
    private ArrayList<Integer> mSelectedEvents;
    private ReportStopReasonFragment mReportStopReasonFragment;
    private SelectStopReasonFragment mSelectStopReasonFragment;
    private View mContainer3;
    private ViewPagerFragment mViewPagerFragment;
    private RecipeFragment mRecipeFragment;
    private Intent mGalleryIntent;
    private Integer mSelectJobId;
    private ArrayList<PdfObject> mPdfList = new ArrayList<>();
    private Integer mSelectProductJobId;
    private JobBase.OnJobFinishedListener mOnJobFinishedListener;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private MachineStatus mCurrentMachineStatus;
    private int mSpinnerProductPosition;
    private Fragment mReportFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZLogger.d(LOG_TAG, "onCreate(), start ");
        setContentView(R.layout.activity_dashboard);
        updateAndroidSecurityProvider(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mCroutonCreator = new CroutonCreator();

        GetMachineStatusNetworkBridge getMachineStatusNetworkBridge = new GetMachineStatusNetworkBridge();

        getMachineStatusNetworkBridge.inject(NetworkManager.getInstance());

        GetMachineDataNetworkBridge getMachineDataNetworkBridge = new GetMachineDataNetworkBridge();

        getMachineDataNetworkBridge.inject(NetworkManager.getInstance());


        ShiftLogNetworkBridge shiftLogNetworkBridge = new ShiftLogNetworkBridge();

        shiftLogNetworkBridge.inject(NetworkManager.getInstance());

        mAllDashboardDataCore = new AllDashboardDataCore(this, getMachineStatusNetworkBridge, PersistenceManager.getInstance(), getMachineDataNetworkBridge, PersistenceManager.getInstance(), PersistenceManager.getInstance(), shiftLogNetworkBridge);

        mActionBarAndEventsFragment = ActionBarAndEventsFragment.newInstance();

        ReportFieldsForMachineNetworkBridge reportFieldsForMachineNetworkBridge = new ReportFieldsForMachineNetworkBridge();
        reportFieldsForMachineNetworkBridge.inject(NetworkManager.getInstance());

        mReportFieldsForMachineCore = new ReportFieldsForMachineCore(reportFieldsForMachineNetworkBridge, PersistenceManager.getInstance());

        mContainer2 = findViewById(R.id.fragments_container_widget);

        mContainer3 = findViewById(R.id.fragments_container_reason);

        openWidgetFragment();

        initViewPagerFragment();

        try {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mActionBarAndEventsFragment).commit();

            getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        } catch (IllegalStateException ignored) {
        }
        ZLogger.d(LOG_TAG, "onCreate(), end ");
    }


    private void openWidgetFragment() {

        mWidgetFragment = WidgetFragment.newInstance();

    }

    private void initViewPagerFragment() {

        mViewPagerFragment = ViewPagerFragment.newInstance();

        try {

            getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), mViewPagerFragment).commit();
        } catch (IllegalStateException ignored) {
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mWidgetFragment != null) {//TODO check because viewpagerFragment

            try {

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().remove(mWidgetFragment).commit();
            } catch (IllegalStateException ignored) {
            }
        }
        if (mActionBarAndEventsFragment != null) {

            try {

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().remove(mActionBarAndEventsFragment).commit();
            } catch (IllegalStateException ignored) {
            }
        }
//        if (mDashboardFragment != null) {
//            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//            fm.beginTransaction().remove(mDashboardFragment).commit();
//        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    boolean first = false;

    private android.support.v4.app.FragmentManager.OnBackStackChangedListener getListener() {

        return new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {

                Fragment fragment = getVisibleFragment();

                if (mActionBarAndEventsFragment != null) {

                    mActionBarAndEventsFragment.setVisiblefragment(fragment);
                }

                if (fragment != null) {
                    if (fragment instanceof ReportRejectsFragment) {
                        ((ReportRejectsFragment) fragment).setActionBar();
                    } else if (fragment instanceof SettingsFragment) {
                        ((SettingsFragment) fragment).setActionBar();
                    } else if (fragment instanceof ActionBarAndEventsFragment ||
                            fragment instanceof RecipeFragment ||
                            fragment instanceof WidgetFragment ||
                            fragment instanceof ReportStopReasonFragment ||
                            fragment instanceof SelectStopReasonFragment) {
                        if (mActionBarAndEventsFragment != null) {
                            mActionBarAndEventsFragment.setActionBar();
                        }
                        first = !first;
                    }
                }
            }
        };
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {

        try {

            ProviderInstaller.installIfNeeded(this);

        } catch (GooglePlayServicesRepairableException e) {


            GoogleApiAvailability.getInstance().getErrorDialog(callingActivity, e.getConnectionStatusCode(), 0);

        } catch (GooglePlayServicesNotAvailableException e) {
            ZLogger.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!ignoreFromOnPause && mGalleryIntent == null) {

            mAllDashboardDataCore.stopPolling();

            mAllDashboardDataCore.unregisterListener();

            mAllDashboardDataCore.stopTimer();

            mReportFieldsForMachineCore.stopPolling();

            mReportFieldsForMachineCore.unregisterListener();

            finish();

        }
    }

    @Override
    protected void onResume() {

        if (!ignoreFromOnPause) {

            if (mActionBarAndEventsFragment != null) {

                mActionBarAndEventsFragment.setVisiblefragment(getVisibleFragment());
            }

            registerReceiver();

            super.onResume();

            dashboardDataStartPolling();

            shiftForMachineTimer();

            mReportFieldsForMachineCore.registerListener(mReportFieldsForMachineUICallback);

            mReportFieldsForMachineCore.startPolling();

        } else {

            ignoreFromOnPause = false;

            super.onResume();

        }

    }

    private void registerReceiver() {

        if (mRefreshBroadcast == null) {

            mRefreshBroadcast = new RefreshPollingBroadcast(this);

            LocalBroadcastManager.getInstance(this).registerReceiver(mRefreshBroadcast, new IntentFilter(RefreshPollingBroadcast.ACTION_REFRESH_POLLING));

        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        removeBroadcasts();
    }


    private void removeBroadcasts() {

        try {

            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRefreshBroadcast);

        } catch (Exception e) {

            if (e.getMessage() != null)
                Log.e(LOG_TAG, e.getMessage());
        }

    }

    public void dashboardDataStartPolling() {

        mAllDashboardDataCore.registerListener(getMachineStatusUICallback(), getMachineDataUICallback(), getShiftLogUICallback());

        mAllDashboardDataCore.stopPolling();

        NetworkManager.getInstance().clearPollingRequest();

        mAllDashboardDataCore.startPolling();

    }

    private void getActiveJobs() {
        ActiveJobsListForMachineNetworkBridge activeJobsListForMachineNetworkBridge = new ActiveJobsListForMachineNetworkBridge();
        activeJobsListForMachineNetworkBridge.inject(NetworkManager.getInstance());
        ActiveJobsListForMachineCore mActiveJobsListForMachineCore = new ActiveJobsListForMachineCore(PersistenceManager.getInstance(), activeJobsListForMachineNetworkBridge);
        mActiveJobsListForMachineCore.registerListener(mActiveJobsListForMachineUICallbackListener);
        mActiveJobsListForMachineCore.getActiveJobsListForMachine();
    }

    private ActiveJobsListForMachineUICallbackListener mActiveJobsListForMachineUICallbackListener = new ActiveJobsListForMachineUICallbackListener() {
        @Override
        public void onActiveJobsListForMachineReceived(ActiveJobsListForMachine activeJobsListForMachine) {

            mActiveJobsListForMachine = activeJobsListForMachine;
            if (activeJobsListForMachine != null) {

                mAllDashboardDataCore.sendRequestForPolling(mOnJobFinishedListener, activeJobsListForMachine.getActiveJobs().get(0).getJobID(), mSelectProductJobId);

                if (activeJobsListForMachine.getActiveJobs().size() <= 1) {

                    mSelectProductJobId = null;
                }

                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {

                        dashboardUICallbackListener.onActiveJobsListForMachineUICallbackListener(activeJobsListForMachine);
                    }

                }

                ZLogger.i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            } else {
                ProgressDialogManager.dismiss();
                ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
            }
        }

        @Override
        public void onActiveJobsListForMachineReceiveFailed(ErrorObjectInterface reason) {
            ZLogger.w(LOG_TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getDetailedDescription());
//            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener);
            mAllDashboardDataCore.sendRequestForPolling(mOnJobFinishedListener, null, mSelectProductJobId);
        }
    };

    @NonNull
    private MachineStatusUICallback getMachineStatusUICallback() {
        return new MachineStatusUICallback() {
            @Override
            public void onStatusReceivedSuccessfully(MachineStatus machineStatus) {

                mCurrentMachineStatus = machineStatus;
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    if (machineStatus != null && machineStatus.getAllMachinesData() != null &&
                            machineStatus.getAllMachinesData().size() > 0) {

                        PersistenceManager.getInstance().setJobId(machineStatus.getAllMachinesData().get(0).getCurrentJobID());
                        PersistenceManager.getInstance().setDisplayRejectFactor(machineStatus.getAllMachinesData().get(0).isDisplayRejectFactor());
                        PersistenceManager.getInstance().setAddRejectsOnSetupEnd(machineStatus.getAllMachinesData().get(0).isAddRejectsOnSetupEnd());
                        PersistenceManager.getInstance().setMinEventDuration(machineStatus.getAllMachinesData().get(0).getMinEventDuration());

                        String opName = machineStatus.getAllMachinesData().get(0).getOperatorName();
                        String opId = machineStatus.getAllMachinesData().get(0).getOperatorId();

                        if (opId != null && !opId.equals("") && opName != null && !opName.equals("")) {

                            PersistenceManager.getInstance().setOperatorName(opName);
                            PersistenceManager.getInstance().setOperatorId(opId);
                        } else {
                            PersistenceManager.getInstance().setOperatorName("");
                            PersistenceManager.getInstance().setOperatorId("");
                        }

                        getAllRecipes(machineStatus.getAllMachinesData().get(0).getCurrentJobID(), true);

                    }

                    for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {

                        dashboardUICallbackListener.onDeviceStatusChanged(machineStatus); // disable the button at least until next polling cycle

                    }
                    ProgressDialogManager.dismiss();
                } else {
                    ProgressDialogManager.dismiss();
                    ZLogger.w(LOG_TAG, " onStatusReceivedSuccessfully() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onTimerChanged(long millisUntilFinished) {
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {

                        Locale locale = getApplicationContext().getResources().getConfiguration().locale;

                        String countDownTimer = String.format(locale, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                        dashboardUICallbackListener.onTimerChanged(countDownTimer);
                    }
                } else {
                    ZLogger.w(LOG_TAG, "onTimerChanged() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onStatusReceiveFailed(ErrorObjectInterface reason) {

                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {

                        ZLogger.i(LOG_TAG, "onStatusReceiveFailed() reason: " + reason.getDetailedDescription());
                        dashboardUICallbackListener.onDataFailure(reason, DashboardUICallbackListener.CallType.Status);
                    }
                }
            }
        };
    }


    @NonNull
    private MachineDataUICallback getMachineDataUICallback() {
        return new MachineDataUICallback() {

            @Override
            public void onDataReceivedSuccessfully(ArrayList<Widget> widgetList) {
                ProgressDialogManager.dismiss();

                if (mSelectJobId != null) {
                    PersistenceManager.getInstance().setJobId(mSelectJobId);
                }
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                        dashboardUICallbackListener.onMachineDataReceived(widgetList);
                    }
                } else {

                    ZLogger.w(LOG_TAG, " onDataReceivedSuccessfully() - DashboardUICallbackListener is null");
                }
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }

            @Override
            public void onDataReceiveFailed(ErrorObjectInterface reason) {
                ZLogger.i(LOG_TAG, "onDataReceivedSuccessfully() reason: " + reason.getDetailedDescription());
                ProgressDialogManager.dismiss();

                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                        dashboardUICallbackListener.onDataFailure(reason, DashboardUICallbackListener.CallType.MachineData);
                    }
                }
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }
        };

    }

    private void shiftForMachineTimer() {
        if (!isFinishing() && !isDestroyed()) {
            mAllDashboardDataCore.getShiftForMachine(getShiftForMachineUICallback());
        }
    }

    @NonNull
    private ShiftForMachineUICallback getShiftForMachineUICallback() {
        return new ShiftForMachineUICallback() {
            @Override
            public void onGetShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse) {
                final long durationOfShift = shiftForMachineResponse.getDuration();

                if (durationOfShift > 0) {
                    startShiftTimer(durationOfShift);
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShiftTimer(durationOfShift);
                        }
                    }, PersistenceManager.getInstance().getPollingFrequency() * 1000);
                }
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }

            @Override
            public void onGetShiftForMachineFailed(ErrorObjectInterface reason) {
                ZLogger.w(LOG_TAG, "get shift for machine failed with reason: " + reason.getError() + " " + reason.getDetailedDescription());
                ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this, reason);

                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }
        };
    }

    private void startShiftTimer(long timeInSeconds) {
        if (mTimeToEndCounter == null) {
            mTimeToEndCounter = new TimeToEndCounter(this);
        }
        mTimeToEndCounter.calculateShiftToEnd(timeInSeconds);

    }

    @Override
    public void onTimeToEndChanged(long millisUntilFinished) {

        if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

            for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                dashboardUICallbackListener.onShiftForMachineEnded();
            }
        }
        shiftForMachineTimer();
    }


    @NonNull
    private ShiftLogUICallback getShiftLogUICallback() {
        return new ShiftLogUICallback() {

            @Override
            public void onGetShiftLogSucceeded(ArrayList<Event> events) {
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    int minDuration = PersistenceManager.getInstance().getMinEventDuration();
                    if (mSelectedEvents != null && mSelectedEvents.size() > 0) {
                        for (Event event : events) {
                            for (int i = 0; i < mSelectedEvents.size(); i++) {

                                if (event.getEventID() == mSelectedEvents.get(i)) {
                                    if (event.getDuration() < minDuration) {
                                        mSelectedEvents.remove(i);
                                    }
                                }
                            }
                        }
                    }

                    for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                        dashboardUICallbackListener.onShiftLogDataReceived(events);
                    }
                    if (ProgressDialogManager.isShowing()) {
                        ProgressDialogManager.dismiss();
                    }
                } else {
                    if (ProgressDialogManager.isShowing()) {
                        ProgressDialogManager.dismiss();
                    }
                    ZLogger.w(LOG_TAG, " shiftLogStartPolling() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onGetShiftLogFailed(ErrorObjectInterface reason) {

                if (reason != null) {

                    ZLogger.i(LOG_TAG, "shiftLogStartPolling() reason: " + reason.getDetailedDescription());

                    if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                        for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                            dashboardUICallbackListener.onDataFailure(reason, DashboardUICallbackListener.CallType.ShiftLog);
                        }
                    }
                }
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }
        };
    }

    ReportFieldsForMachineUICallback mReportFieldsForMachineUICallback = new ReportFieldsForMachineUICallback() {
        @Override
        public void onReportFieldsReceivedSuccessfully(ReportFieldsForMachine reportFieldsForMachine) {
            if (reportFieldsForMachine != null) {
                ZLogger.d(LOG_TAG, "onReportFieldsReceivedSuccessfully()");
                mReportFieldsForMachine = reportFieldsForMachine;
                if (mOnReportFieldsUpdatedCallbackListener != null) {
                    mOnReportFieldsUpdatedCallbackListener.onReportUpdatedSuccess();
                }
            } else {
                ZLogger.w(LOG_TAG, "reportFieldsForMachine is null");
                if (mOnReportFieldsUpdatedCallbackListener != null) {
                    mOnReportFieldsUpdatedCallbackListener.onReportUpdateFailure();
                }
            }
            if (ProgressDialogManager.isShowing()) {
                ProgressDialogManager.dismiss();
            }
        }

        int mRetries = 0;
        final int mMaxRetries = 3;

        @Override
        public void onReportFieldsReceivedSFailure(ErrorObjectInterface reason) {
            ZLogger.i(LOG_TAG, "onReportFieldsReceivedSFailure() reason: " + reason.getDetailedDescription());
            if (mOnReportFieldsUpdatedCallbackListener != null) {
                mOnReportFieldsUpdatedCallbackListener.onReportUpdateFailure();
            }
            mReportFieldsForMachineCore.stopPolling();
            if (mRetries < mMaxRetries) {
                mRetries++;
                mReportFieldsForMachineCore.registerListener(this);
                mReportFieldsForMachineCore.stopPolling();
                mReportFieldsForMachineCore.startPolling();
            } else {
                mReportFieldsForMachineCore.stopPolling();
                mRetries = 0;
            }
            if (ProgressDialogManager.isShowing()) {
                ProgressDialogManager.dismiss();
            }

        }
    };

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (croutonType == CroutonCreator.CroutonType.ALERT_DIALOG) {
            {
                if (!((getVisibleFragment() instanceof ActionBarAndEventsFragment) || (getVisibleFragment() instanceof WidgetFragment)
                        || (getVisibleFragment() instanceof RecipeFragment) || (getVisibleFragment() instanceof ViewPagerFragment))) {
                    return;
                }
            }
        }
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, String.valueOf(croutonMessage), croutonDurationInMilliseconds, getCroutonRoot(), croutonType, this);

        }
    }

    @Override
    public void onHideConnectivityCroutonRequest() {

        Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "onHideConnectivityCroutonRequest");

        if (mCroutonCreator != null) {
            mCroutonCreator.hideConnectivityCrouton();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onFragmentAttached(DashboardUICallbackListener dashboardUICallbackListener) {
        mDashboardUICallbackListenerList.add(dashboardUICallbackListener);
    }

    @Override
    public void onFragmentDetached(DashboardUICallbackListener dashboardUICallbackListener) {
        mDashboardUICallbackListenerList.remove(dashboardUICallbackListener);

    }

    @Override
    public void goToFragment(Fragment fragment, boolean addToBackStack) {

        try {
            getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, fragment).addToBackStack(DASHBOARD_FRAGMENT).commit();
        } catch (IllegalStateException ignored) {
        }

    }

    @Override
    public void goToDashboardActivity(int machine) {

    }

    @Override
    public void isTryToLogin(boolean isTryToLogin) {

    }

    @Override
    public void unregisterListeners() {
        mJobsCore.unregisterListener();
    }

    @Override
    public void initJobsCore() {
        JobsNetworkBridge jobsNetworkBridge = new JobsNetworkBridge();
        jobsNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());

        mJobsCore = new JobsCore(jobsNetworkBridge, PersistenceManager.getInstance());

        mJobsCore.registerListener(new JobsForMachineUICallbackListener() {
            @Override
            public void onJobListReceived(JobListForMachine jobListForMachine) {
                ZLogger.i(LOG_TAG, "onJobListReceived()");
                if (jobListForMachine != null) {
                    if (jobListForMachine.getData() != null || jobListForMachine.getHeaders() != null) {
                        mDashboardActivityToJobsFragmentCallback.onJobsListReceived(jobListForMachine);
                    } else {
                        mDashboardActivityToJobsFragmentCallback.onJobsListReceiveFailed();
                    }
                } else {
                    mDashboardActivityToJobsFragmentCallback.onJobsListReceiveFailed();
                }
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }

            @Override
            public void onJobListReceiveFailed(ErrorObjectInterface reason) {
                ZLogger.w(LOG_TAG, "onJobListReceiveFailed() " + reason.getError());
                mDashboardActivityToJobsFragmentCallback.onJobsListReceiveFailed();
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }

            @Override
            public void onStartJobSuccess() {
                ZLogger.i(LOG_TAG, "onStartJobSuccess()");

                mDashboardActivityToSelectedJobFragmentCallback.onStartJobSuccess();

                if (getSupportFragmentManager() != null) {

                    try {

                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    } catch (IllegalStateException ignored) {
                    }

                }

                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
                startJob();
            }


            @Override
            public void onStartJobFailed(ErrorObjectInterface reason) {
                if (reason.getDetailedDescription().contains("Job started successfully")) {
                    ShowCrouton.jobsLoadingAlertCrouton(DashboardActivity.this, reason.getDetailedDescription());
                } else {
                    ZLogger.i(LOG_TAG, "onStartJobFailed()");
                    mDashboardActivityToSelectedJobFragmentCallback.onStartJobFailure();
                    ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this, reason);
                }
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }
        });
    }

    public void startJob() {
        mAllDashboardDataCore.stopPolling();

        NetworkManager.getInstance().clearPollingRequest();

//        mAllDashboardDataCore.startPolling(null);

        dashboardDataStartPolling();

        PersistenceManager.getInstance().setJobId(mSelectJobId);

        if (mRecipeFragment != null) {

            getAllRecipes(mSelectJobId, true);

            mPdfList = new ArrayList<>();

        }
    }

    @Override
    public void onJobFragmentAttached(DashboardActivityToJobsFragmentCallback dashboardActivityToJobsFragmentCallback) {
        mDashboardActivityToJobsFragmentCallback = dashboardActivityToJobsFragmentCallback;
    }

    @Override
    public void getJobsForMachineList() {
        mJobsCore.getJobsListForMachine();

    }

    @Override
    public void startJobForMachine(int jobId) {
        Log.i(LOG_TAG, "startJobForMachine(), Job Id: " + jobId);
        PersistenceManager.getInstance().setJobId(jobId);

        mSelectJobId = jobId;

        mJobsCore.startJobForMachine(jobId);

    }

    @Override
    public void onSelectedJobFragmentAttached(DashBoardActivityToSelectedJobFragmentCallback dashboardActivityToSelectedJobFragmentCallback) {
        mDashboardActivityToSelectedJobFragmentCallback = dashboardActivityToSelectedJobFragmentCallback;
    }

    @Override
    public OperatorCore onSignInOperatorFragmentAttached() {
        OperatorNetworkBridge operatorNetworkBridge = new OperatorNetworkBridge();
        operatorNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());
        return new OperatorCore(operatorNetworkBridge, PersistenceManager.getInstance());
    }

    @Override
    public void onSetOperatorForMachineSuccess(String operatorId, String operatorName) {
        PersistenceManager.getInstance().setOperatorId(operatorId);
        PersistenceManager.getInstance().setOperatorName(operatorName);

        ZLogger.i(LOG_TAG, "onSetOperatorForMachineSuccess(), operator Id: " + operatorId + " operator name: " + operatorName);

        mAllDashboardDataCore.stopPolling();

        NetworkManager.getInstance().clearPollingRequest();

//        mAllDashboardDataCore.startPolling(null);

        dashboardDataStartPolling();

        if (getSupportFragmentManager() != null) {
            try {

                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    public void silentLoginFromDashBoard(final OnCroutonRequestListener onCroutonRequestListener, final SilentLoginCallback silentLoginCallback) {

        LoginCore.getInstance().silentLoginFromDashBoard(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getUserName(), PersistenceManager.getInstance().getPassword(), new LoginUICallback<Machine>() {
            @Override
            public void onLoginSucceeded(ArrayList<Machine> machines) {

                ZLogger.d(LOG_TAG, "login, onGetMachinesSucceeded(),  go Next");
                silentLoginCallback.onSilentLoginSucceeded();
            }

            @Override
            public void onLoginFailed(ErrorObjectInterface reason) {
                silentLoginCallback.onSilentLoginFailed(reason);

                Fragment fragment = getCurrentFragment();
                if (fragment instanceof SignInOperatorFragment) {

                    ShowCrouton.jobsLoadingErrorCrouton(onCroutonRequestListener, reason);
                }
            }
        });
    }

    private Fragment getCurrentFragment() {
        try {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            int fragmentBackStackSize = fragments.size();
            for (int i = fragmentBackStackSize; i > 0; i--) {
                if (fragments.get(i - 1) != null) {
                    return fragments.get(i - 1);
                }
            }
            return null;
        } catch (NullPointerException ex) {
            ZLogger.e(LOG_TAG, "getCurrentFragment(), error: " + ex.getMessage());
            return null;
        }
    }


    public Fragment getVisibleFragment() {
        Fragment f = null;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    f = fragment;
                }
            }
        }
        return f;
    }

    @Override
    public ReportFieldsForMachine getReportForMachine() {
        if (mReportFieldsForMachine != null) {

            return mReportFieldsForMachine;
        }
        ZLogger.w(LOG_TAG, "mReportFieldsForMachine is null");
        return null;
    }

    @Override
    public void updateReportRejectFields() {
        mReportFieldsForMachineCore.stopPolling();
        mReportFieldsForMachineCore.startPolling();
    }

    @Override
    public void onClearAppDataRequest() {
        Log.i(LOG_TAG, "onClearAppDataRequest() command received from settings screen");
        clearData();
        refreshApp();
        deleteCache(this);
    }


    private void clearData() {

        DataSupport.deleteAll(Event.class);

        String tmpLanguage = PersistenceManager.getInstance().getCurrentLang();

        PersistenceManager.getInstance().clear();

        PersistenceManager.getInstance().items.clear();

        PersistenceManager.getInstance().setCurrentLang(tmpLanguage);

        ZLogger.i(LOG_TAG, "PersistenceManager cleared");
        //Cores clear
        if (mReportFieldsForMachineCore != null) {
            mReportFieldsForMachineCore.stopPolling();
            mReportFieldsForMachineCore.unregisterListener();
            ZLogger.i(LOG_TAG, "mReportFieldsForMachineCore cleared");
        }
        if (mAllDashboardDataCore != null) {
            mAllDashboardDataCore.stopPolling();
            mAllDashboardDataCore.unregisterListener();
            ZLogger.i(LOG_TAG, "mAllDashboardDataCore cleared");
        }

        if (mJobsCore != null) {
            mJobsCore.unregisterListener();
            ZLogger.i(LOG_TAG, "mJobsCore cleared");
        }

        if (mReportFieldsForMachineCore != null) {
            mReportFieldsForMachine = null;
            ZLogger.i(LOG_TAG, "mReportFieldsForMachine cleared");
        }
        //Interfaces clear
        if (mOnReportFieldsUpdatedCallbackListener != null) {
            mOnReportFieldsUpdatedCallbackListener = null;
            ZLogger.i(LOG_TAG, "mOnReportFieldsUpdatedCallbackListener cleared");
        }
        if (mDashboardActivityToJobsFragmentCallback != null) {
            mDashboardActivityToJobsFragmentCallback = null;
            ZLogger.i(LOG_TAG, "mDashboardActivityToJobsFragmentCallback cleared");
        }
        if (mDashboardActivityToSelectedJobFragmentCallback != null) {
            mDashboardActivityToSelectedJobFragmentCallback = null;
            ZLogger.i(LOG_TAG, "mDashboardActivityToSelectedJobFragmentCallback cleared");
        }
        if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {
            mDashboardUICallbackListenerList.clear();
            ZLogger.i(LOG_TAG, "mDashboardUICallbackListenerList cleared");
        }
        if (mReportFieldsForMachineUICallback != null) {
            mReportFieldsForMachineUICallback = null;
            ZLogger.i(LOG_TAG, "mReportFieldsForMachineUICallback cleared");
        }
        if (mCroutonCreator != null) {
            mCroutonCreator = null;
            ZLogger.i(LOG_TAG, "mCroutonCreator cleared");

        }
    }

    @Override
    public void onRefreshReportFieldsRequest(OnReportFieldsUpdatedCallbackListener onReportFieldsUpdatedCallbackListener) {
        mOnReportFieldsUpdatedCallbackListener = onReportFieldsUpdatedCallbackListener;
        mReportFieldsForMachineCore.stopPolling();
        mReportFieldsForMachineCore.startPolling();
        ZLogger.i(LOG_TAG, "onRefreshReportFieldsRequest() command received from settings screen");
    }

    @Override
    public void onRefreshApplicationRequest() {
        ZLogger.i(LOG_TAG, "onRefreshApplicationRequest() command received from settings screen");
        refreshApp();
    }

    private void refreshApp() {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(myIntent);
        finish();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            if (e.getMessage() != null)

                Log.e(LOG_TAG, e.getMessage());
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else
            return dir != null && dir.isFile() && dir.delete();
    }

    public int getCroutonRoot() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null && currentFragment instanceof CroutonRootProvider) {
            return ((CroutonRootProvider) currentFragment).getCroutonRoot();
        }
//        Fragment currentFragment = getCurrentFragment();
//        if (currentFragment != null && currentFragment instanceof CroutonRootProvider) {
//            return ((CroutonRootProvider) currentFragment).getCroutonRoot();
//        }
        Fragment visible = getVisibleFragment();
        if (mActionBarAndEventsFragment != null &&
                visible != null && (visible instanceof ActionBarAndEventsFragment
                || visible instanceof WidgetFragment
                || visible instanceof RecipeFragment
                || visible instanceof SelectStopReasonFragment
                || visible instanceof ReportStopReasonFragment)) {

            return mActionBarAndEventsFragment.getCroutonRoot();

        }
        return R.id.parent_layouts;
    }

    @Override
    public void onRefreshPollingRequest() {
        ZLogger.i(LOG_TAG, "onRefreshPollingRequest() command received from settings screen");

        mAllDashboardDataCore.stopPolling();

        NetworkManager.getInstance().clearPollingRequest();
//        mAllDashboardDataCore.startPolling(null);

        dashboardDataStartPolling();

    }

    @Override
    public void onIgnoreFromOnPause(boolean ignore) {
        ignoreFromOnPause = ignore;
    }

    @Override
    public void onApproveFirstItemComplete() {

        if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

            for (DashboardUICallbackListener dashboardUICallbackListener : mDashboardUICallbackListenerList) {

                dashboardUICallbackListener.onApproveFirstItemEnabledChanged(false); // disable the button at least until next polling cycle

            }
        }
    }

    @Override
    public void onRefreshPolling() {

        Log.e(DavidVardi.DAVID_TAG_SPRINT_1_5, "onRefreshPolling");

        dashboardDataStartPolling();

        if (getSupportFragmentManager() != null) {

            try {

                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            } catch (IllegalStateException ignored) {

            }

        }

        ProgressDialogManager.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ignoreFromOnPause = false;

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            SendBroadcast.SendEmail(this);

        }
    }

    @Override
    public void onCroutonDismiss() {

//        mActionBarAndEventsFragment.openNextDialog();
        mActionBarAndEventsFragment.setAlertChecked();
    }

    @Override
    public void onWidgetChangeState(boolean state) {
        if (mWidgetFragment != null) {
            mWidgetFragment.setWidgetState(state);
        }
    }

    @Override
    public void onWidgetUpdateSpane(int span) {
        if (mWidgetFragment != null) {
            mWidgetFragment.setSpanCount(span);
        }
        if (mSelectStopReasonFragment != null) {
            mSelectStopReasonFragment.setSpanCount(span != 3);
        }
        if (mReportStopReasonFragment != null) {
            mReportStopReasonFragment.setSpanCount(span != 3);
        }

    }

    @Override
    public void onResize(int width, int statusBarsHeight) {

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mContainer2.getLayoutParams();

        layoutParams.setMarginStart(width);

        layoutParams.topMargin = statusBarsHeight;

        mContainer2.setLayoutParams(layoutParams);

        ViewGroup.MarginLayoutParams layoutParams3 = (ViewGroup.MarginLayoutParams) mContainer3.getLayoutParams();

        layoutParams3.setMarginStart(width);

        layoutParams3.topMargin = statusBarsHeight;

        mContainer3.setLayoutParams(layoutParams3);

    }

    @Override
    public void onOpenReportStopReasonFragment(ReportStopReasonFragment reportStopReasonFragment) {

        mReportStopReasonFragment = reportStopReasonFragment;

        try {

            getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), reportStopReasonFragment).commit();
        } catch (IllegalStateException ignored) {
        }

    }

    @Override
    public void onEventSelected(Integer event, boolean checked) {

        if (mSelectedEvents == null) {

            mSelectedEvents = new ArrayList<>();
        }

        if (checked) {
            if (!mSelectedEvents.contains(event)) {
                mSelectedEvents.add(event);
            }
        } else {

            ArrayList<Integer> toDelete = new ArrayList<>();
            for (Integer event1 : mSelectedEvents) {
                if (event.compareTo(event1) == 0) {
                    toDelete.add(event1);
                }
            }
            for (Integer event1 : toDelete) {
                mSelectedEvents.remove(event1);
            }
        }

        if (mSelectStopReasonFragment != null) {

            mSelectStopReasonFragment.setSelectedEvents(mSelectedEvents);
        }
        if (mActionBarAndEventsFragment != null) {

            mActionBarAndEventsFragment.setSelectedEvents(mSelectedEvents);
        }
        if (mSelectedEvents.size() == 0) {
            if (mSelectStopReasonFragment != null) {

                onBackPressed();
            }
            if (mReportStopReasonFragment != null) {

                onBackPressed();
            }
        }
    }

    @Override
    public void onClearAllSelectedEvents() {

        if (mReportStopReasonFragment != null) {

            removeReportStopReasonFragment();

        }
        if (mSelectStopReasonFragment != null) {

            removeSelectStopReasonFragment();

        }
    }

    @Override
    public void onJobActionItemClick() {

        Intent intent = new Intent(DashboardActivity.this, JobActionActivity.class);

        startActivityForResult(intent, JobActionActivity.EXTRA_ACTIVATE_JOB_CODE);

        ignoreFromOnPause = true;

        if (mActionBarAndEventsFragment != null) {

            mActionBarAndEventsFragment.setFromAnotherActivity(true);
        }
    }

    @Override
    public void onJoshProductSelected(Integer spinnerProductPosition, Integer jobID, String jobName) {

        mSpinnerProductPosition = spinnerProductPosition;

        mSelectProductJobId = jobID;

        dashboardDataStartPolling();

        ProgressDialogManager.show(this);
    }

    @Override
    public void onProductionStatusChanged(int id) {
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        SimpleRequests simpleRequests = new SimpleRequests();
        simpleRequests.postProductionMode(persistenceManager.getSiteUrl(), new PostProductionModeCallback() {
            @Override
            public void onPostProductionModeSuccess(ErrorResponseNewVersion response) {
                // TODO: 31/07/2018 display crouton
                if (response.isFunctionSucceed()) {
                    dashboardDataStartPolling();
                } else {
                    ProgressDialogManager.dismiss();
                    ShowCrouton.showSimpleCrouton(DashboardActivity.this, response.getmError().getErrorDesc(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);
                }
            }

            @Override
            public void onPostProductionModeFailed(ErrorObjectInterface reason) {
                ProgressDialogManager.dismiss();
                // TODO: 31/07/2018 set error message
                ShowCrouton.showSimpleCrouton(DashboardActivity.this, reason.getDetailedDescription(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);

            }
        }, NetworkManager.getInstance(), new SetProductionModeForMachineRequest(persistenceManager.getSessionId(), persistenceManager.getMachineId(), id), persistenceManager.getTotalRetries());

    }

    @Override
    public void onSplitEventPressed(int eventID) {
        // TODO: 05/07/2018 call server
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        SimpleRequests simpleRequests = new SimpleRequests();
        simpleRequests.postSplitEvent(persistenceManager.getSiteUrl(), new PostSplitEventCallback() {
            @Override
            public void onPostSplitEventSuccess(Object response) {
                Toast.makeText(DashboardActivity.this, "onPostSplitEventSuccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPostSplitEventFailed(ErrorObjectInterface reason) {
                Toast.makeText(DashboardActivity.this, "onPostSplitEventFailed", Toast.LENGTH_SHORT).show();

            }
        }, NetworkManager.getInstance(), new SplitEventRequest(persistenceManager.getSessionId(), String.valueOf(eventID)), persistenceManager.getTotalRetries(), persistenceManager.getRequestTimeout());

    }

    @Override
    public void onOpenSelectStopReasonFragmentNew(SelectStopReasonFragment selectStopReasonFragment) {

        mSelectStopReasonFragment = selectStopReasonFragment;

        try {

            getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), selectStopReasonFragment).commit();
        } catch (IllegalStateException ignored) {
        }

        if (mSelectStopReasonFragment != null) {

            mSelectStopReasonFragment.setSelectedEvents(mSelectedEvents);

        }

    }

    @Override
    public void onBackPressed() {
        if (mReportStopReasonFragment != null || mSelectStopReasonFragment != null) {

            if (mReportStopReasonFragment != null && mSelectStopReasonFragment == null) {

                removeReportStopReasonFragment();

            }
            if (mSelectStopReasonFragment != null) {

                removeSelectStopReasonFragment();

            }
            Fragment visible = getVisibleFragment();

            if (!(visible instanceof ActionBarAndEventsFragment
                    || visible instanceof WidgetFragment
                    || visible instanceof RecipeFragment
                    || visible instanceof SelectStopReasonFragment
                    || visible instanceof ReportStopReasonFragment)) {

                try {

                    getSupportFragmentManager().popBackStack();
                } catch (IllegalStateException ignored) {
                }
            }

        } else if (mReportFragment != null){

            getSupportFragmentManager().beginTransaction().remove(mReportFragment).commit();

            mReportFragment = null;

        }else {

            super.onBackPressed();

        }

    }

    private void removeSelectStopReasonFragment() {

        try {

            getSupportFragmentManager().beginTransaction().remove(mSelectStopReasonFragment).commit();

            mSelectStopReasonFragment = null;
        } catch (IllegalStateException ignored) {
        }

    }

    private void removeReportStopReasonFragment() {

        try {

            getSupportFragmentManager().beginTransaction().remove(mReportStopReasonFragment).commit();

            mReportStopReasonFragment = null;

            if (mActionBarAndEventsFragment != null) {

                mActionBarAndEventsFragment.disableSelectMode();
            }
            if (mSelectedEvents != null) {
                mSelectedEvents = null;
            }
        } catch (IllegalStateException ignored) {
        }
    }

    @Override
    public void onViewPagerCreated() {

        initRecipeFragment();
    }

    private void initRecipeFragment() {

        getAllRecipes(PersistenceManager.getInstance().getJobId(), false);
//        getAllRecipes(2, false);
    }

    private void getAllRecipes(Integer jobId, final boolean isUpdate) {

        PersistenceManager persistanceManager = PersistenceManager.getInstance();

        SimpleRequests simpleRequests = new SimpleRequests();

        simpleRequests.getAllRecipe(persistanceManager.getSiteUrl(), persistanceManager.getSessionId(),
                jobId, new GetAllRecipeCallback() {
                    @Override
                    public void onGetAllRecipeSuccess(Object response) {

                        if (isUpdate) {

                            showRecipeFragment((RecipeResponse) response);

                        } else {

                            addFragmentsToViewPager((RecipeResponse) response);

                        }
                    }

                    @Override
                    public void onGetAllRecipeFailed(ErrorObjectInterface reason) {

                        if (!isUpdate) {
                            addFragmentsToViewPager(null);
                        }
                    }
                }, NetworkManager.getInstance(), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());

    }

    private void addFragmentsToViewPager(RecipeResponse response) {

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {

            showRecipeFragment(response);

            mViewPagerFragment.addFragment(mWidgetFragment);

        } else {

            mViewPagerFragment.addFragment(mWidgetFragment);

            showRecipeFragment(response);

        }
    }

    private void showRecipeFragment(RecipeResponse recipeResponse) {

        if (mRecipeFragment == null) {

            mRecipeFragment = RecipeFragment.newInstance(recipeResponse);

            mViewPagerFragment.addFragment(mRecipeFragment);

        } else {

            mRecipeFragment.updateRecipeResponse(recipeResponse);
        }

    }

    @Override
    public void onImageProductClick(List<String> fileUrl, String name) {

        startGalleryActivity(fileUrl, name);
    }

    private void startGalleryActivity(List<String> fileUrl, String name) {

        if (fileUrl != null && fileUrl.size() > 0) {

            mGalleryIntent = new Intent(DashboardActivity.this, GalleryActivity.class);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_FILE_URL, (ArrayList<String>) fileUrl);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_FILES_TITLE, name);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_PDF_FILES, mPdfList);

            startActivityForResult(mGalleryIntent, GalleryActivity.EXTRA_GALLERY_CODE);

            ignoreFromOnPause = true;

            if (mActionBarAndEventsFragment != null) {

                mActionBarAndEventsFragment.setFromAnotherActivity(true);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mGalleryIntent = null;

        ChangeLang.changeLanguage(this);

        if (resultCode == RESULT_OK && requestCode == GalleryActivity.EXTRA_GALLERY_CODE) {
            ignoreFromOnPause = true;

            mPdfList = data.getParcelableArrayListExtra(GalleryActivity.EXTRA_RECIPE_PDF_FILES);

        }

        if (resultCode == RESULT_OK && requestCode == JobActionActivity.EXTRA_ACTIVATE_JOB_CODE) {
            ignoreFromOnPause = true;
            Object response = data.getParcelableExtra(JobActionActivity.EXTRA_ACTIVATE_JOB_RESPONSE);

            if (((Response) response).getError() == null) {

                mSelectJobId = data.getIntExtra(JobActionActivity.EXTRA_ACTIVATE_JOB_ID, PersistenceManager.getInstance().getJobId());

                startJob();

                ShowCrouton.jobsLoadingSuccessCrouton(DashboardActivity.this, getString(R.string.start_job_success));

                dashboardDataStartPolling();
            }

        }

    }

    @Override
    public void onIgnoreOnPauseFromAdvancedSettings() {

        ignoreFromOnPause = true;
    }

    @Override
    public void onShowCrouton(String errorResponse) {

        ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Retrofit, errorResponse);
        ShowCrouton.showSimpleCrouton(DashboardActivity.this, errorObject);

    }

    @Override
    public void onExecuteJob(JobBase.OnJobFinishedListener onJobFinishedListener) {

        mOnJobFinishedListener = onJobFinishedListener;

        getActiveJobs();
    }

    @Override
    public void onOpenNewFragmentInCentralDashboardContainer(String type) {

        try {
            mReportFragment = ReportRejectsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(),
                    mActiveJobsListForMachine, mSpinnerProductPosition, false);

            getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), mReportFragment).commit();

        } catch (IllegalStateException ignored) {
        }
//todo
//        switch (position) {
//            case 0: {
//                ZLogger.d(LOG_TAG, "New Job");
//
//                if (PersistenceManager.getInstance().getVersion() >= MINIMUM_VERSION_FOR_NEW_ACTIVATE_JOB) {
//
//                    mListener.onJobActionItemClick();
//
//                } else {
//
//                    mOnGoToScreenListener.goToFragment(new JobsFragment(), true);
//
//                }
//                break;
//            }
//            case 1: {
//                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
//                    mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true);
//                } else {
//                    mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true);
//                }
//                break;
//            }
//            case 2: {
//                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
//                    mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true);
//                } else {
//                    mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true);
//                }
//                break;
//            }
//            case 3: {
//                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
//                    mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true);
//                } else {
//                    mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true);
//                }
//                break;
//            }
//            case 4: {
//
//                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
//                    mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true);
//                } else {
//                    mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true);
//                }
//
//                break;
//            }
//        }

    }
}