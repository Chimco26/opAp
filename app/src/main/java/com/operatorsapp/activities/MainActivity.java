package com.operatorsapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.example.oppapplog.OppAppLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.operators.infra.Machine;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.fragments.LoginFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.utils.ChangeLang;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.utils.broadcast.BroadcastAlarmManager;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.app.AlarmManager.INTERVAL_DAY;

public class MainActivity extends AppCompatActivity implements GoToScreenListener, OnCroutonRequestListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int STORAGE_REQUEST_CODE = 1;
    public static final String MACHINE_LIST = "MACHINE_LIST";
    private CroutonCreator mCroutonCreator;
    private boolean mIsTryToLogin;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Analytics
        OperatorApplication application = (OperatorApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName(this.getLocalClassName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        ChangeLang.changeLanguage(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLoggerAndDataStorage();

        setupAlarm();

        mCroutonCreator = new CroutonCreator();
        goToFragment(LoginFragment.newInstance(), true, false);

        updateAndroidSecurityProvider(this);

        checkFlavor();
    }

    private void checkFlavor() {
        if(BuildConfig.FLAVOR.equals(getString(R.string.emerald_flavor_name))) {

            Log.d(TAG, "onCreate: emerald");

        }else if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))){

            Log.d(TAG, "onCreate: Lenox");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mCurrentFragment != null) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().remove(mCurrentFragment).commit();
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        if (!mIsTryToLogin) {
            super.onBackPressed();
        }
    }

    @Override
    public void goToFragment(Fragment fragment, boolean centralContainer, boolean addToBackStack) {
        OppAppLogger.getInstance().d(TAG, "goToFragment(), " + fragment.getClass().getSimpleName());
        mCurrentFragment = fragment;
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mCurrentFragment).addToBackStack("").commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mCurrentFragment).commit();
        }
    }

    @Override
    public void goToDashboardActivity(int machineId, ArrayList<Machine> machines) {
        //now we have machineID and can get notifications history
        getNotifications();

//        if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name)) &&
//                PersistenceManager.getInstance().getMachineId() == -1) {
//
//            PersistenceManager.getInstance().setMachineId(machines.get(0).getId());
//        }
        //TODO Lenox uncomment

        Intent intent = new Intent(this, DashboardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("machineId", machineId);
        bundle.putParcelableArrayList(MACHINE_LIST, machines);
        intent.putExtras(bundle);
        startActivity(intent);


    }

    private void getNotifications(){

        NetworkManager.getInstance().getNotificationHistory(new Callback<NotificationHistoryResponse>() {
            @Override
            public void onResponse(Call<NotificationHistoryResponse> call, Response<NotificationHistoryResponse> response) {

                if (response != null && response.body() != null && response.body().getmError() == null) {

                    for (Notification not : response.body().getmNotificationsList()) {
                        not.setmSentTime(TimeUtils.getStringNoTFormatForNotification(not.getmSentTime()));
                        not.setmResponseDate(TimeUtils.getStringNoTFormatForNotification(not.getmResponseDate()));
                    }

                    PersistenceManager.getInstance().setNotificationHistory(response.body().getmNotificationsList());
                    finish();
                }else {
                    PersistenceManager.getInstance().setNotificationHistory(null);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<NotificationHistoryResponse> call, Throwable t) {

                PersistenceManager.getInstance().setNotificationHistory(null);
                finish();

            }
        });

    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GoogleApiAvailability.getInstance().getErrorDialog(callingActivity, e.getConnectionStatusCode(), 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            OppAppLogger.getInstance().e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, croutonMessage, croutonDurationInMilliseconds, viewGroup, croutonType);
        }
    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, croutonMessage, croutonDurationInMilliseconds, viewGroup, croutonType);
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

    @Override
    public void isTryToLogin(boolean isTryToLogin) {
        mIsTryToLogin = isTryToLogin;
    }


    public void initLoggerAndDataStorage() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                initZLogger();

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_REQUEST_CODE) {
            initZLogger();
        }
    }

    public void initZLogger() {

        OppAppLogger.setStorageGranted(this, true);

        OppAppLogger.initInstance(this);

        goToFragment(LoginFragment.newInstance(), false, false);
    }

    private void setupAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), BroadcastAlarmManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar firingCal= Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 0); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, 0); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second

        if(firingCal.compareTo(currentCal) < 0) {
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Long intendedTime = firingCal.getTimeInMillis();
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, INTERVAL_DAY, pendingIntent);
        }
    }

}
