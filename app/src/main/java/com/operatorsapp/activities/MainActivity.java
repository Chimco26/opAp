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
import android.view.View;

import com.example.oppapplog.OppAppLogger;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.operators.infra.Machine;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.fragments.LoginFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.utils.ChangeLang;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.utils.broadcast.BroadcastAlarmManager;

import org.acra.ACRA;

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
    public static final String GO_TO_SELECT_MACHINE_FRAGMENT = "GO_TO_SELECT_MACHINE_FRAGMENT";
    private CroutonCreator mCroutonCreator;
    private boolean mIsTryToLogin;
    private boolean mGoToSelectMachine = false;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "ChangeLang: ");
        ChangeLang.changeLanguage(this);
        try {
            ACRA.init(getApplication());
        }catch (IllegalStateException ignored){}


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.main_restart_app_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        initLoggerAndDataStorage();

        setupAlarm();

        mCroutonCreator = new CroutonCreator();

        Bundle intent = getIntent().getExtras();
        if (intent != null){
            mGoToSelectMachine = intent.getBoolean(GO_TO_SELECT_MACHINE_FRAGMENT);
        }
        if (mGoToSelectMachine) {
            goToFragment(LoginFragment.newInstance(true), true, false);
        } else {

            goToFragment(LoginFragment.newInstance(false), true, false);
        }

        updateAndroidSecurityProvider(this);

        checkFlavor();



    }

    private void checkFlavor() {
        if (BuildConfig.FLAVOR.equals(getString(R.string.emerald_flavor_name))) {

            Log.d(TAG, "onCreate: emerald");

        } else if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {

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
//        if (!mIsTryToLogin) {
//
//            if (mCurrentFragment instanceof SelectMachineFragment && !mGoToSelectMachine) {
//                cleanData();
//                goToFragment(LoginFragment.newInstance(false), false, false);
//            } else {
//               finish();
//            }
//        }else {
            finish();
//        }
    }

    @Override
    public void goToFragment(Fragment fragment, boolean centralContainer, boolean addToBackStack) {
        if (isFinishing()){
            return;
        }
        try {
            OppAppLogger.getInstance().d(TAG, "goToFragment(), " + fragment.getClass().getSimpleName());
            mCurrentFragment = fragment;
            if (addToBackStack) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mCurrentFragment).addToBackStack("").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mCurrentFragment).commit();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void goToDashboardActivity(int machineId, ArrayList<Machine> machines) {
        if (isFinishing()){
            return;
        }
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

    private void getNotifications() {

        NetworkManager.getInstance().getNotificationHistory(new Callback<NotificationHistoryResponse>() {
            @Override
            public void onResponse(Call<NotificationHistoryResponse> call, Response<NotificationHistoryResponse> response) {

                if (response != null && response.body() != null && response.body().getError().getErrorDesc() == null) {

                    ArrayList<TechCallInfo> techList = PersistenceManager.getInstance().getCalledTechnician();

                    for (Notification not : response.body().getmNotificationsList()) {
                        not.setmSentTime(TimeUtils.getStringNoTFormatForNotification(not.getmSentTime()));
                        not.setmResponseDate(TimeUtils.getStringNoTFormatForNotification(not.getmResponseDate()));

                        if (not.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN && not.isOpenCall()) {
                            boolean isNew = true;
                            for (TechCallInfo techCall : techList) {
                                if (not.getmNotificationID() == techCall.getmNotificationId()) {
                                    isNew = false;
                                    break;
                                }
                            }
                            if (isNew) {
                                techList.add(new TechCallInfo(not.getmResponseType(), not.getmTargetName(), not.getmTitle(), TimeUtils.getDateForNotification(not.getmSentTime()).getTime(), not.getmNotificationID(), not.getmTargetUserId()));
                            }
                        }
                    }

                    PersistenceManager.getInstance().setNotificationHistory(response.body().getmNotificationsList());
                    PersistenceManager.getInstance().setCalledTechnicianList(techList);
                    if (techList.size() > 0) {
                        PersistenceManager.getInstance().setRecentTechCallId(techList.get(0).getmNotificationId());
                    } else {
                        PersistenceManager.getInstance().setRecentTechCallId(0);
                    }

                    finish();
                } else {
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

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES}, STORAGE_REQUEST_CODE);

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

        // TODO: 2/10/2019 way to go to LoginFragment from here 

        //     goToFragment(LoginFragment.newInstance(false), false, false);
    }

    private void setupAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), BroadcastAlarmManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 0); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, 0); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second

        if (firingCal.compareTo(currentCal) < 0) {
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Long intendedTime = firingCal.getTimeInMillis();
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, INTERVAL_DAY, pendingIntent);
        }
    }

//
//    public static void cleanData() {
//        PostDeleteTokenRequest request = new PostDeleteTokenRequest(PersistenceManager.getInstance().getMachineId(), PersistenceManager.getInstance().getSessionId(), PersistenceManager.getInstance().getNotificationToken());
//        NetworkManager.getInstance().postDeleteToken(request, new Callback<ErrorResponseNewVersion>() {
//            @Override
//            public void onResponse(Call<ErrorResponseNewVersion> call, retrofit2.Response<ErrorResponseNewVersion> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<ErrorResponseNewVersion> call, Throwable t) {
//
//            }
//        });
//
//        DataSupport.deleteAll(Event.class);
//
//        String tmpLanguage = PersistenceManager.getInstance().getCurrentLang();
//        String tmpLanguageName = PersistenceManager.getInstance().getCurrentLanguageName();
//
//        PersistenceManager.getInstance().clear();
//
//        PersistenceManager.getInstance().items.clear();
//
//        PersistenceManager.getInstance().setCurrentLang(tmpLanguage);
//        PersistenceManager.getInstance().setCurrentLanguageName(tmpLanguageName);
//    }

}
