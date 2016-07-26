package com.operatorsapp.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.app.operatorinfra.Operator;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.operators.getmachinesstatusnetworkbridge.GetMachineStatusNetworkBridge;
import com.operators.jobscore.JobsCore;
import com.operators.jobscore.interfaces.JobsForMachineUICallbackListener;
import com.operators.jobsinfra.JobListForMachine;
import com.operators.jobsnetworkbridge.JobsNetworkBridge;
import com.operators.machinestatuscore.MachineStatusCore;
import com.operators.machinestatuscore.interfaces.MachineStatusUICallback;
import com.operators.machinestatusinfra.ErrorObjectInterface;
import com.operators.machinestatusinfra.MachineStatus;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatornetworkbridge.OperatorNetworkBridge;
import com.operators.shiftlogcore.ShiftLogCore;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.fragments.DashboardFragment;
import com.operatorsapp.fragments.interfaces.DialogsShiftLogListener;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.DashboardActivityToJobsFragmentCallback;
import com.operatorsapp.interfaces.DashboardActivityToSelectedJobFragmentCallback;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.JobsFragmentToDashboardActivityCallback;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.SignedInOperatorsManager;
import com.operatorsapp.server.NetworkManager;
import com.zemingo.logrecorder.ZLogger;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardActivityCore extends AppCompatActivity implements OnCroutonRequestListener, OnActivityCallbackRegistered, GoToScreenListener, JobsFragmentToDashboardActivityCallback, OperatorCoreToDashboardActivityCallback, DialogsShiftLogListener {

    private static final String LOG_TAG = DashboardActivityCore.class.getSimpleName();
    private CroutonCreator mCroutonCreator;

    private DashboardUICallbackListener mDashboardUICallbackListener;
    private MachineStatusCore mMachineStatusCore;
    private DashboardActivityToJobsFragmentCallback mDashboardActivityToJobsFragmentCallback;
    private DashboardActivityToSelectedJobFragmentCallback mDashboardActivityToSelectedJobFragmentCallback;
    private JobsCore mJobsCore;

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

        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, DashboardFragment.newInstance()).commit();


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMachineStatusCore.registerListener(this, new MachineStatusUICallback() {
            @Override
            public void onStatusReceivedSuccessfully(MachineStatus machineStatus) {
                if (mDashboardUICallbackListener != null) {
                    mDashboardUICallbackListener.onDeviceStatusChanged(machineStatus);
                }
                else {
                    Log.w(LOG_TAG, " onStatusReceivedSuccessfully() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onTimerChanged(String timeToEndInHours) {
                if (mDashboardUICallbackListener != null) {
                    mDashboardUICallbackListener.onTimerChanged(timeToEndInHours);
                }
                else {
                    Log.w(LOG_TAG, "onTimerChanged() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onStatusReceiveFailed(ErrorObjectInterface reason) {
                ZLogger.i(LOG_TAG, "onStatusReceiveFailed() reason: " + reason.getDetailedDescription());
            }
        });


        mMachineStatusCore.startPolling();
    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, croutonMessage, croutonDurationInMilliseconds, viewGroup, croutonType);
        }
    }

    @Override
    public void onHideConnectivityCroutonRequest() {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public ShiftLogCore getShiftLogCore() {
        return ShiftLogCore.getInstance();
    }

    public void onFragmentAttached(DashboardUICallbackListener dashboardUICallbackListener) {
        mDashboardUICallbackListener = dashboardUICallbackListener;
    }

    @Override
    public void goToFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).addToBackStack("").commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).commit();
        }
    }

    @Override
    public void goToDashboardActivity(int machine) {

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
                if( jobListForMachine.getJobs().size()!=0){
                    mDashboardActivityToJobsFragmentCallback.onJobReceived(jobListForMachine);
                }else {
                    mDashboardActivityToJobsFragmentCallback.onJobReceiveFailed();
                }

            }

            @Override
            public void onJobListReceiveFailed(com.operators.jobsinfra.ErrorObjectInterface reason) {
                Log.i(LOG_TAG, "onJobListReceiveFailed()");
                mDashboardActivityToJobsFragmentCallback.onJobReceiveFailed();
            }

            @Override
            public void onStartJobSuccess() {
                Log.i(LOG_TAG, "onStartJobSuccess()");
                mDashboardActivityToSelectedJobFragmentCallback.onStartJobSuccess();
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                mMachineStatusCore.stopPolling();
                mMachineStatusCore.startPolling();
            }

            @Override
            public void onStartJobFailed(com.operators.jobsinfra.ErrorObjectInterface reason) {
                Log.i(LOG_TAG, "onStartJobFailed()");
                mDashboardActivityToSelectedJobFragmentCallback.onStartJobFailure();
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

        SignedInOperatorsManager signedInOperatorsManager = new SignedInOperatorsManager();
        signedInOperatorsManager.addOperator(new Operator(operatorId,operatorName));
        Log.i(LOG_TAG, "onSetOperatorForMachineSuccess(), operator Id: " + operatorId + " operator name: " + operatorName);

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mMachineStatusCore.stopPolling();
        mMachineStatusCore.startPolling();
    }
}