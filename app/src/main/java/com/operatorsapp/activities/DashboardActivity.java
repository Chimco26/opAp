package com.operatorsapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;

import com.operators.getmachinesstatusnetworkbridge.GetMachineStatusNetworkBridge;
import com.operators.infra.MachineStatus;
import com.operators.machinestatuscore.MachineStatusCore;
import com.operators.machinestatuscore.interfaces.MachineStatusUICallback;
import com.operatorsapp.R;
import com.operatorsapp.fragments.DashboardFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.FragmentUiListener;
import com.operatorsapp.interfaces.MSDUIListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.polling.EmeraldJobBase;
import com.operatorsapp.server.NetworkManager;
import com.zemingo.logrecorder.ZLogger;
import com.zemingo.pollingmachanaim.JobBase;
import com.zemingo.pollingmachanaim.PollingManager;

import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardActivity extends AppCompatActivity implements OnCroutonRequestListener, FragmentUiListener
{

    private static final String LOG_TAG = DashboardActivity.class.getSimpleName();
    private CroutonCreator mCroutonCreator;
    private MSDUIListener mMSDUIListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCroutonCreator = new CroutonCreator();

        GetMachineStatusNetworkBridge getMachineStatusNetworkBridge = new GetMachineStatusNetworkBridge();
        getMachineStatusNetworkBridge.inject(NetworkManager.getInstance());
        final MachineStatusCore machineStatusCore = new MachineStatusCore(getMachineStatusNetworkBridge, PersistenceManager.getInstance());

        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, DashboardFragment.newInstance()).commit();

        EmeraldJobBase job = new EmeraldJobBase()
        {
            @Override
            protected void executeJob(final OnJobFinishedListener onJobFinishedListener)
            {
                machineStatusCore.getMachineStatus(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getSessionId(), String.valueOf(PersistenceManager.getInstance().getMachineId()), new MachineStatusUICallback()
                {
                    @Override
                    public void onStatusReceivedSuccessfully(MachineStatus machineStatus)
                    {
                        ZLogger.i(LOG_TAG, "received");
                        mMSDUIListener.onDeviceStatusChanged(machineStatus);
                        onJobFinishedListener.onJobFinished();
                    }

                    @Override
                    public void onTimerChanged(String timeToEndInHours)
                    {
                        mMSDUIListener.onTimerChanged(timeToEndInHours);
                    }

                    @Override
                    public void onStatusReceiveFailed()
                    {
                        ZLogger.i(LOG_TAG, "onStatusReceiveFailed()");

                    }
                });
            }
        };


        PollingManager.getInstance().register(job, 0, TimeUnit.SECONDS);
        job.startJob(0, 5, TimeUnit.SECONDS);

    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType)
    {

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType)
    {

    }

    @Override
    public void onHideConnectivityCroutonRequest()
    {

    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onFragmentAttached(MSDUIListener msduiListener)
    {
        mMSDUIListener = msduiListener;
    }
}