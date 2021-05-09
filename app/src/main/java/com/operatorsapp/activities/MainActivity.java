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
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.oppapplog.OppAppLogger;
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
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.utils.ChangeLang;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.MyExceptionHandler;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.utils.broadcast.BroadcastAlarmManager;

import org.acra.ACRA;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.app.AlarmManager.INTERVAL_DAY;

public class MainActivity extends AppCompatActivity implements GoToScreenListener, OnCroutonRequestListener, Thread.UncaughtExceptionHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int STORAGE_REQUEST_CODE = 1;
    public static final String MACHINE_LIST = "MACHINE_LIST";
    private CroutonCreator mCroutonCreator;
    private WeakReference<Fragment> mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        Log.d(TAG, "ChangeLang: ");
        ChangeLang.initLanguage(this);
        try {
            ACRA.init(getApplication());
        } catch (IllegalStateException ignored) {
        } catch (NullPointerException e) {
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.main_restart_app_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersistenceManager.getInstance().clear();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        initDataStorage();

        setupAlarm();

        mCroutonCreator = new CroutonCreator();

        goToFragment(LoginFragment.newInstance(), true, false);

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
        if (mCurrentFragment != null && mCurrentFragment.get() != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().remove(mCurrentFragment.get()).commit();
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void goToFragment(Fragment fragment, boolean centralContainer, boolean addToBackStack) {
        if (isFinishing()) {
            return;
        }
        try {
            OppAppLogger.d(TAG, "goToFragment(), " + fragment.getClass().getSimpleName());
            mCurrentFragment = new WeakReference<>(fragment);
            if (addToBackStack) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mCurrentFragment.get()).addToBackStack("").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mCurrentFragment.get()).commit();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void goToDashboardActivity(ArrayList<Machine> machines) {
        if (isFinishing()) {
            return;
        }

//        if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name)) &&
//                PersistenceManager.getInstance().getMachineId() == -1) {
//
//            PersistenceManager.getInstance().setMachineId(machines.get(0).getId());
//        }
        //TODO Lenox uncomment

        Intent intent = new Intent(this, DashboardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MACHINE_LIST, machines);
        intent.putExtras(bundle);
        startActivity(intent);

        //now we have machineID and can get notifications history
        getNotifications();
    }

    private void getNotifications() {

//        ProgressDialogManager.show(this);
        NetworkManager.getInstance().getNotificationHistory(new Callback<NotificationHistoryResponse>() {
            @Override
            public void onResponse(Call<NotificationHistoryResponse> call, Response<NotificationHistoryResponse> response) {

                if (response.body() != null && response.body().getError().getErrorDesc() == null) {

                    ArrayList<TechCallInfo> techList = new ArrayList<>();

                    if (response.body().getmNotificationsList() != null) {
                        for (Notification not : response.body().getmNotificationsList()) {
                            not.setmSentTime(TimeUtils.getStringNoTFormatForNotification(not.getmSentTime()));
                            not.setmResponseDate(TimeUtils.getStringNoTFormatForNotification(not.getmResponseDate()));

                            if (not.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN && not.isOpenCall()) {
                                boolean isNew = true;
                                for (TechCallInfo techCall : techList) {
                                    if (techCall.getmMachineId() == 0) {
                                        techCall.setmMachineId(not.getMachineID());
                                    }
                                    if (not.getmNotificationID() == techCall.getmNotificationId()) {
                                        isNew = false;
                                        break;
                                    }
                                }
                                if (isNew) {
                                    techList.add(new TechCallInfo(not.getMachineID(), not.getmResponseType(), not.getmTargetName(), not.getmTitle(), not.getmAdditionalText(),
                                            TimeUtils.getDateForNotification(not.getmSentTime()).getTime(), not.getmNotificationID(), not.getmTargetUserId(), not.getmEventID()));
                                }
                            }
                        }
                        PersistenceManager.getInstance().setNotificationHistory(response.body().getmNotificationsList());
                    }else {
                        PersistenceManager.getInstance().setNotificationHistory(new ArrayList<Notification>());
                    }

                    PersistenceManager.getInstance().setCalledTechnicianList(techList);
                    if (techList.size() > 0) {
                        PersistenceManager.getInstance().setRecentTechCallId(techList.get(0).getmNotificationId());
                    } else {
                        PersistenceManager.getInstance().setRecentTechCallId(0);
                    }
//                    ProgressDialogManager.dismiss();
                    finish();
                } else {
//                    ProgressDialogManager.dismiss();
                    PersistenceManager.getInstance().setNotificationHistory(null);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<NotificationHistoryResponse> call, Throwable t) {

//                ProgressDialogManager.dismiss();
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
            OppAppLogger.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
        if (mCroutonCreator != null) {
            mCroutonCreator.showCrouton(this, croutonMessage, croutonDurationInMilliseconds, viewGroup, croutonType);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCroutonCreator = null;
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

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    public void isTryToLogin(boolean isTryToLogin) {
    }


    public void initDataStorage() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES}, STORAGE_REQUEST_CODE);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        long intendedTime = firingCal.getTimeInMillis();
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, INTERVAL_DAY, pendingIntent);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(OperatorApplication.getAppContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) OperatorApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        finish();
        System.exit(2);
    }
}
