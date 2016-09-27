package com.operatorsapp.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.getmachinesstatusnetworkbridge.GetMachineStatusNetworkBridge;
import com.operators.infra.Machine;
import com.operators.jobscore.JobsCore;
import com.operators.jobscore.interfaces.JobsForMachineUICallbackListener;
import com.operators.jobsinfra.JobListForMachine;
import com.operators.jobsnetworkbridge.JobsNetworkBridge;
import com.operators.logincore.LoginCore;
import com.operators.logincore.interfaces.LoginUICallback;
import com.operators.machinedatacore.MachineDataCore;
import com.operators.machinedatacore.interfaces.MachineDataUICallback;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinedatanetworkbridge.GetMachineDataNetworkBridge;
import com.operators.machinestatuscore.MachineStatusCore;
import com.operators.machinestatuscore.interfaces.MachineStatusUICallback;
import com.operators.machinestatuscore.interfaces.OnTimeToEndChangedListener;
import com.operators.machinestatuscore.timecounter.TimeToEndCounter;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatornetworkbridge.OperatorNetworkBridge;
import com.operators.reportfieldsformachinecore.ReportFieldsForMachineCore;
import com.operators.reportfieldsformachinecore.interfaces.ReportFieldsForMachineUICallback;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachinenetworkbridge.ReportFieldsForMachineNetworkBridge;
import com.operators.shiftlogcore.ShiftLogCore;
import com.operators.shiftlogcore.interfaces.ShiftForMachineUICallback;
import com.operators.shiftlogcore.interfaces.ShiftLogUICallback;
import com.operators.shiftloginfra.Event;
import com.operators.shiftloginfra.ShiftForMachineResponse;
import com.operators.shiftlognetworkbridge.ShiftLogNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.fragments.DashboardFragment;
import com.operatorsapp.fragments.SignInOperatorFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.fragments.interfaces.OnReportFieldsUpdatedCallbackListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.SettingsInterface;
import com.operatorsapp.interfaces.DashboardActivityToJobsFragmentCallback;
import com.operatorsapp.interfaces.DashboardActivityToSelectedJobFragmentCallback;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.interfaces.ReportFieldsFragmentCallbackListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.TimeUtils;
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardActivity extends AppCompatActivity implements OnCroutonRequestListener, OnActivityCallbackRegistered, GoToScreenListener,
        JobsFragmentToDashboardActivityCallback, OperatorCoreToDashboardActivityCallback, /*DialogsShiftLogListener,*/ ReportFieldsFragmentCallbackListener, SettingsInterface, OnTimeToEndChangedListener, CroutonRootProvider
{

    private static final String LOG_TAG = DashboardActivity.class.getSimpleName();
    public static final String DASHBOARD_FRAGMENT = "dashboard_fragment";
    private CroutonCreator mCroutonCreator;
    private TimeToEndCounter mTimeToEndCounter;
    private DashboardUICallbackListener mDashboardUICallbackListener;
    private MachineStatusCore mMachineStatusCore;
    private DashboardActivityToJobsFragmentCallback mDashboardActivityToJobsFragmentCallback;
    private DashboardActivityToSelectedJobFragmentCallback mDashboardActivityToSelectedJobFragmentCallback;
    private JobsCore mJobsCore;
    private MachineDataCore mMachineDataCore;
    private ShiftLogCore mShiftLogCore;
    private ReportFieldsForMachineCore mReportFieldsForMachineCore;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private OnReportFieldsUpdatedCallbackListener mOnReportFieldsUpdatedCallbackListener;
    private DashboardFragment mDashboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        updateAndroidSecurityProvider(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCroutonCreator = new CroutonCreator();

        GetMachineStatusNetworkBridge getMachineStatusNetworkBridge = new GetMachineStatusNetworkBridge();
        getMachineStatusNetworkBridge.inject(NetworkManager.getInstance());
        mMachineStatusCore = new MachineStatusCore(getMachineStatusNetworkBridge, PersistenceManager.getInstance());

        GetMachineDataNetworkBridge getMachineDataNetworkBridge = new GetMachineDataNetworkBridge();
        getMachineDataNetworkBridge.inject(NetworkManager.getInstance());
        mMachineDataCore = new MachineDataCore(getMachineDataNetworkBridge, PersistenceManager.getInstance());

        ShiftLogNetworkBridge shiftLogNetworkBridge = new ShiftLogNetworkBridge();
        shiftLogNetworkBridge.inject(NetworkManager.getInstance());
        mShiftLogCore = new ShiftLogCore(PersistenceManager.getInstance(), shiftLogNetworkBridge);

        mDashboardFragment = DashboardFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mDashboardFragment).commit();
        ReportFieldsForMachineNetworkBridge reportFieldsForMachineNetworkBridge = new ReportFieldsForMachineNetworkBridge();
        reportFieldsForMachineNetworkBridge.inject(NetworkManager.getInstance());

        mReportFieldsForMachineCore = new ReportFieldsForMachineCore(reportFieldsForMachineNetworkBridge, PersistenceManager.getInstance());

        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mDashboardFragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mDashboardFragment != null) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().remove(mDashboardFragment).commit();
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GoogleApiAvailability.getInstance().getErrorDialog(callingActivity, e.getConnectionStatusCode(), 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            ZLogger.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMachineStatusCore.stopPolling();
        mMachineStatusCore.unregisterListener();
        mMachineStatusCore.stopTimer();

        mMachineDataCore.stopPolling();
        mMachineDataCore.unregisterListener();

        mShiftLogCore.stopPolling();
        mShiftLogCore.unregisterListener();

        mReportFieldsForMachineCore.stopPolling();
        mReportFieldsForMachineCore.unregisterListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        machineStatusStartPolling();
        shiftForMachineTimer();
        machineDataStartPolling();
        shiftLogStartPolling();

        mReportFieldsForMachineCore.registerListener(mReportFieldsForMachineUICallback);
        mReportFieldsForMachineCore.startPolling();
    }

    private void machineStatusStartPolling() {
        mMachineStatusCore.registerListener(new MachineStatusUICallback() {
            @Override
            public void onStatusReceivedSuccessfully(MachineStatus machineStatus) {
                if (mDashboardUICallbackListener != null) {
                    mDashboardUICallbackListener.onDeviceStatusChanged(machineStatus);
                } else {
                    Log.w(LOG_TAG, " onStatusReceivedSuccessfully() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onTimerChanged(long millisUntilFinished) {
                if (mDashboardUICallbackListener != null) {
                    Locale locale = getApplicationContext().getResources().getConfiguration().locale;

                    String countDownTimer = String.format(locale, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    mDashboardUICallbackListener.onTimerChanged(countDownTimer);
                } else {
                    Log.w(LOG_TAG, "onTimerChanged() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onStatusReceiveFailed(ErrorObjectInterface reason) {
                ZLogger.i(LOG_TAG, "onStatusReceiveFailed() reason: " + reason.getDetailedDescription());
                mDashboardUICallbackListener.onDataFailure(reason, DashboardUICallbackListener.CallType.Status);
                mMachineStatusCore.stopTimer();
                mMachineStatusCore.stopPolling();
            }
        });

        mMachineStatusCore.startPolling();

    }

    private void machineDataStartPolling() {
        mMachineDataCore.registerListener(new MachineDataUICallback() {

            @Override
            public void onDataReceivedSuccessfully(ArrayList<Widget> widgetList) {
                if (mDashboardUICallbackListener != null) {
                    mDashboardUICallbackListener.onMachineDataReceived(widgetList);
                } else {
                    Log.w(LOG_TAG, " onDataReceivedSuccessfully() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onDataReceiveFailed(ErrorObjectInterface reason) {
                ZLogger.i(LOG_TAG, "onDataReceivedSuccessfully() reason: " + reason.getDetailedDescription());
                mDashboardUICallbackListener.onDataFailure(reason, DashboardUICallbackListener.CallType.MachineData);
                mMachineDataCore.stopPolling();
            }
        });

        mMachineDataCore.startPolling();
    }

    private void shiftForMachineTimer() {
        mShiftLogCore.getShiftForMachine(new ShiftForMachineUICallback() {
            @Override
            public void onGetShiftForMachineSucceeded(ShiftForMachineResponse shiftForMachineResponse) {
                final int durationOfShift = (int) ((TimeUtils.getLongFromDateString(shiftForMachineResponse.getStartTime(), shiftForMachineResponse.getTimeFormat()) + shiftForMachineResponse.getDuration()) - System.currentTimeMillis());
//                durationOfShift = 900000;
                if (durationOfShift > 0) {
                    startShiftTimer(durationOfShift);
                    //shiftLogStartPolling();
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShiftTimer(durationOfShift);
                        }
                    }, PersistenceManager.getInstance().getPollingFrequency() * 1000);
                }
            }

            @Override
            public void onGetShiftForMachineFailed(ErrorObjectInterface reason) {
                Log.w(LOG_TAG,"get shift for machine failed with reason: " + reason.getError() + " " + reason.getDetailedDescription());
                ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this, reason);
            }
        });
    }

    private void startShiftTimer(int timeInSeconds) {
        if (mTimeToEndCounter == null) {
            mTimeToEndCounter = new TimeToEndCounter(this);
        }
        mTimeToEndCounter.calculateShiftToEnd(timeInSeconds);
    }

    @Override
    public void onTimeToEndChanged(long millisUntilFinished) {
        if (mShiftLogCore != null) {
            mShiftLogCore.stopPolling();
            mShiftLogCore.unregisterListener();
            Log.i(LOG_TAG, "mShiftLogCore cleared");
        }
        if (mDashboardUICallbackListener != null) {
            mDashboardUICallbackListener.onShiftForMachineEnded();
        }
        shiftForMachineTimer();
    }

    private void shiftLogStartPolling() {

        mShiftLogCore.registerListener(new ShiftLogUICallback() {

            @Override
            public void onGetShiftLogSucceeded(ArrayList<Event> events) {
                if (mDashboardUICallbackListener != null) {
                    mDashboardUICallbackListener.onShiftLogDataReceived(events);
                } else {
                    Log.w(LOG_TAG, " onDataReceivedSuccessfully() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onGetShiftLogFailed(ErrorObjectInterface reason) {
                ZLogger.i(LOG_TAG, "onDataReceivedSuccessfully() reason: " + reason.getDetailedDescription());
                mDashboardUICallbackListener.onDataFailure(reason, DashboardUICallbackListener.CallType.ShiftLog);
                mShiftLogCore.stopPolling();
            }
        });

        mShiftLogCore.startPolling();
    }

    ReportFieldsForMachineUICallback mReportFieldsForMachineUICallback = new ReportFieldsForMachineUICallback() {
        @Override
        public void onReportFieldsReceivedSuccessfully(ReportFieldsForMachine reportFieldsForMachine) {
            if (reportFieldsForMachine != null) {
                Log.d(LOG_TAG, "onReportFieldsReceivedSuccessfully()");
                mReportFieldsForMachine = reportFieldsForMachine;
                if (mOnReportFieldsUpdatedCallbackListener != null) {
                    mOnReportFieldsUpdatedCallbackListener.onReportUpdatedSuccess();
                }
            } else {
                Log.w(LOG_TAG, "reportFieldsForMachine is null");
                if (mOnReportFieldsUpdatedCallbackListener != null) {
                    mOnReportFieldsUpdatedCallbackListener.onReportUpdateFailure();
                }
            }
        }

        @Override
        public void onReportFieldsReceivedSFailure(ErrorObjectInterface reason) {
            ZLogger.i(LOG_TAG, "onReportFieldsReceivedSFailure() reason: " + reason.getDetailedDescription());
            if (mOnReportFieldsUpdatedCallbackListener != null) {
                mOnReportFieldsUpdatedCallbackListener.onReportUpdateFailure();
            }
            mReportFieldsForMachineCore.stopPolling();
        }
    };

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, croutonMessage, croutonDurationInMilliseconds, getCroutonRoot(), croutonType);
        }
    }

    @Override
    public void onHideConnectivityCroutonRequest() {
        if (mCroutonCreator != null) {
            mCroutonCreator.hideConnectivityCrouton();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onFragmentAttached(DashboardUICallbackListener dashboardUICallbackListener) {
        mDashboardUICallbackListener = dashboardUICallbackListener;
    }

    @Override
    public void goToFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).addToBackStack(DASHBOARD_FRAGMENT).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).commit();
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
                Log.i(LOG_TAG, "onJobListReceived()");
                if (jobListForMachine != null) {
                    if (jobListForMachine.getData() != null || jobListForMachine.getHeaders() != null) {
                        mDashboardActivityToJobsFragmentCallback.onJobsListReceived(jobListForMachine);
                    } else {
                        mDashboardActivityToJobsFragmentCallback.onJobsListReceiveFailed();
                    }
                } else {
                    mDashboardActivityToJobsFragmentCallback.onJobsListReceiveFailed();
                }
            }

            @Override
            public void onJobListReceiveFailed(ErrorObjectInterface reason) {
                Log.w(LOG_TAG, "onJobListReceiveFailed() " + reason.getError());
                mDashboardActivityToJobsFragmentCallback.onJobsListReceiveFailed();
            }

            @Override
            public void onStartJobSuccess() {
                Log.i(LOG_TAG, "onStartJobSuccess()");
                mDashboardActivityToSelectedJobFragmentCallback.onStartJobSuccess();
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mMachineStatusCore.stopPolling();
                mMachineStatusCore.startPolling();

                mMachineDataCore.stopPolling();
                mMachineDataCore.startPolling();

                mShiftLogCore.stopPolling();
                mShiftLogCore.startPolling();
            }


            @Override
            public void onStartJobFailed(ErrorObjectInterface reason) {
                Log.i(LOG_TAG, "onStartJobFailed()");
                mDashboardActivityToSelectedJobFragmentCallback.onStartJobFailure();
                ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this);
            }
        });
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
        mJobsCore.startJobForMachine(jobId);
    }

    @Override
    public void onSelectedJobFragmentAttached(DashboardActivityToSelectedJobFragmentCallback dashboardActivityToSelectedJobFragmentCallback) {
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

        Log.i(LOG_TAG, "onSetOperatorForMachineSuccess(), operator Id: " + operatorId + " operator name: " + operatorName);

        mMachineStatusCore.stopPolling();
        mMachineStatusCore.startPolling();

        mMachineDataCore.stopPolling();
        mMachineDataCore.startPolling();

        mShiftLogCore.stopPolling();
        mShiftLogCore.startPolling();

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    // Silent - setUsername & password from preferences, It is only when preferences.isSelectedMachine().
    public void silentLoginFromDashBoard(final OnCroutonRequestListener onCroutonRequestListener, final SilentLoginCallback silentLoginCallback) {
        LoginCore.getInstance().silentLoginFromDashBoard(PersistenceManager.getInstance().getSiteUrl(),
                PersistenceManager.getInstance().getUserName(),
                PersistenceManager.getInstance().getPassword(), new LoginUICallback<Machine>() {
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

                            ShowCrouton.jobsLoadingErrorCrouton(onCroutonRequestListener,reason);
                            //ShowCrouton.operatorLoadingErrorCrouton(onCroutonRequestListener, "credentials mismatch");
                        }
                    }
                });
    }


    private Fragment getCurrentFragment() {
        try {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            int fragmentBackStackSize = fragments.size();
            return fragments.get(fragmentBackStackSize - 1);
        } catch (NullPointerException ex) {
            ZLogger.e(LOG_TAG, "getCurrentFragment(), error: " + ex.getMessage());
            return null;
        }
    }

    @Override
    public ReportFieldsForMachine getReportForMachine() {
        if (mReportFieldsForMachine != null) {

            return mReportFieldsForMachine;
        }
        Log.w(LOG_TAG, "mReportFieldsForMachine is null");
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
    }


    private void clearData() {
        //Persistence storage clear
        String tmpUrl = PersistenceManager.getInstance().getSiteUrl();
        String tmpLanguage = PersistenceManager.getInstance().getCurrentLang();
        PersistenceManager.getInstance().clear();
        PersistenceManager.getInstance().setSiteUrl(tmpUrl);
        PersistenceManager.getInstance().setCurrentLang(tmpLanguage);
        Log.i(LOG_TAG, "PersistenceManager cleared");
        //Cores clear
        if (mReportFieldsForMachineCore != null) {
            mReportFieldsForMachineCore.stopPolling();
            mReportFieldsForMachineCore.unregisterListener();
            Log.i(LOG_TAG, "mReportFieldsForMachineCore cleared");
        }
        if (mMachineStatusCore != null) {
            mMachineStatusCore.stopPolling();
            mMachineStatusCore.unregisterListener();
            Log.i(LOG_TAG, "mMachineStatusCore cleared");
        }
        if (mShiftLogCore != null) {
            mShiftLogCore.stopPolling();
            mShiftLogCore.unregisterListener();
            Log.i(LOG_TAG, "mShiftLogCore cleared");
        }
        if (mJobsCore != null) {
            mJobsCore.unregisterListener();
            Log.i(LOG_TAG, "mJobsCore cleared");
        }
        if (mMachineDataCore != null) {
            mMachineDataCore.stopPolling();
            mMachineDataCore.unregisterListener();
            Log.i(LOG_TAG, "mMachineDataCore cleared");
        }
        //Objects clear
        if (mReportFieldsForMachineCore != null) {
            mReportFieldsForMachine = null;
            Log.i(LOG_TAG, "mReportFieldsForMachine cleared");
        }
        //Interfaces clear
        if (mOnReportFieldsUpdatedCallbackListener != null) {
            mOnReportFieldsUpdatedCallbackListener = null;
            Log.i(LOG_TAG, "mOnReportFieldsUpdatedCallbackListener cleared");
        }
        if (mDashboardActivityToJobsFragmentCallback != null) {
            mDashboardActivityToJobsFragmentCallback = null;
            Log.i(LOG_TAG, "mDashboardActivityToJobsFragmentCallback cleared");
        }
        if (mDashboardActivityToSelectedJobFragmentCallback != null) {
            mDashboardActivityToSelectedJobFragmentCallback = null;
            Log.i(LOG_TAG, "mDashboardActivityToSelectedJobFragmentCallback cleared");
        }
        if (mDashboardUICallbackListener != null) {
            mDashboardUICallbackListener = null;
            Log.i(LOG_TAG, "mDashboardUICallbackListener cleared");
        }
        if (mReportFieldsForMachineUICallback != null) {
            mReportFieldsForMachineUICallback = null;
            Log.i(LOG_TAG, "mReportFieldsForMachineUICallback cleared");
        }
        if (mCroutonCreator != null) {
            mCroutonCreator = null;
            Log.i(LOG_TAG, "mCroutonCreator cleared");

        }
    }

    @Override
    public void onRefreshReportFieldsRequest(OnReportFieldsUpdatedCallbackListener onReportFieldsUpdatedCallbackListener) {
        mOnReportFieldsUpdatedCallbackListener = onReportFieldsUpdatedCallbackListener;
        mReportFieldsForMachineCore.stopPolling();
        mReportFieldsForMachineCore.startPolling();
        Log.i(LOG_TAG, "onRefreshReportFieldsRequest() command received from settings screen");
    }

    @Override
    public void onRefreshApplicationRequest() {
        Log.i(LOG_TAG, "onRefreshApplicationRequest() command received from settings screen");
        refreshApp();
    }

    private void refreshApp() {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(myIntent);
        finish();
        //startActivity(myIntent);
    }

    public int getCroutonRoot()
    {
        Fragment currentFragment = getCurrentFragment();
        if(currentFragment != null && currentFragment instanceof CroutonRootProvider)
        {
            return ((CroutonRootProvider)currentFragment).getCroutonRoot();
        }
        return R.id.parent_layouts;
    }

    @Override
    public void onRefreshPollingRequest() {
        Log.i(LOG_TAG, "onRefreshPollingRequest() command received from settings screen");

        mMachineStatusCore.stopPolling();
        mShiftLogCore.stopPolling();
        mMachineDataCore.stopPolling();

        mMachineStatusCore.startPolling();
        mShiftLogCore.startPolling();
        mMachineDataCore.startPolling();
    }
}