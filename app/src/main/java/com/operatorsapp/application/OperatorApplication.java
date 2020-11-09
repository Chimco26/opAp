package com.operatorsapp.application;

import android.content.Context;
import android.content.Intent;
import androidx.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.operators.getmachinesnetworkbridge.GetMachinesNetworkBridge;
import com.operators.logincore.LoginCore;
import com.operators.loginnetworkbridge.LoginNetworkBridge;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;
import org.litepal.LitePal;

//import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.operatorsapp.utils.SendReportUtil.IS_APP_CRASH;
import static com.operatorsapp.utils.SendReportUtil.MACHINE_ID;
import static com.operatorsapp.utils.SendReportUtil.METHOD_NAME;
import static com.operatorsapp.utils.SendReportUtil.SESSION_ID;

@ReportsCrashes(
        formUri = "https://leaders.my.leadermes.com/LeaderMESApi/ReportApplicationCrash",
        httpMethod = HttpSender.Method.POST,
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.REPORT_ID,
                ReportField.STACK_TRACE,
                ReportField.PHONE_MODEL,
                ReportField.BUILD,
                ReportField.CUSTOM_DATA
        },
        mode = ReportingInteractionMode.SILENT,
        reportType = HttpSender.Type.JSON

)
public class OperatorApplication extends MultiDexApplication {

    private static final String LOG_TAG = OperatorApplication.class.getSimpleName();

    private static Context msApplicationContext;
    private FirebaseAnalytics sAnalytics;

//    private Tracker sTracker;
    @Override
    public void onCreate() {
        super.onCreate();

//        Fabric.with(this, new Crashlytics());

        LitePal.initialize(this);

        try {
            ACRA.init(this);
        }catch (IllegalStateException ignored){}catch (NullPointerException e){}

        msApplicationContext = getApplicationContext();
//        LeakCanary.install(this);

        sAnalytics = FirebaseAnalytics.getInstance(this);

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidSans.ttf").setFontAttrId(R.attr.fontPath).build());

        PersistenceManager.initInstance(msApplicationContext);

        GetMachinesNetworkBridge getMachinesNetworkBridge = new GetMachinesNetworkBridge();
        getMachinesNetworkBridge.inject(NetworkManager.initInstance());

        LoginNetworkBridge loginNetworkBridge = new LoginNetworkBridge();
        loginNetworkBridge.inject(NetworkManager.getInstance());
        LoginCore.getInstance().inject(PersistenceManager.getInstance(), loginNetworkBridge, getMachinesNetworkBridge);

//        ShiftLogNetworkBridge shiftLogNetworkBridge = new ShiftLogNetworkBridge();
//        shiftLogNetworkBridge.inject(NetworkManager.getInstance());
//
//        ShiftLogCore.getInstance().inject(PersistenceManager.getInstance(), shiftLogNetworkBridge);

//        exceptionHandler();

        initImageLoading();

    }


    synchronized public FirebaseAnalytics getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//        if (sTracker == null) {
//            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
//        }

        return sAnalytics;
    }

    private void initImageLoading() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options).build();

        ImageLoader.getInstance().init(config);

    }


    private void exceptionHandler() {

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                //Catch your exception
                // Without System.exit() this will not work.
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                if (i != null) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }

                if (paramThrowable.getMessage() != null)

                    Log.e(LOG_TAG, paramThrowable.getMessage());


                ACRA.getErrorReporter().putCustomData(IS_APP_CRASH, "true");

                ACRA.getErrorReporter().putCustomData(METHOD_NAME, "exception handler");

                try {

                    ACRA.getErrorReporter().putCustomData(MACHINE_ID, String.valueOf(PersistenceManager.getInstance().getMachineId()));

                    ACRA.getErrorReporter().putCustomData(SESSION_ID, String.valueOf(PersistenceManager.getInstance().getSessionId()));

                    ACRA.getErrorReporter().handleException(paramThrowable);


                } catch (Exception ignored) {
                }

                startActivity(i);

                System.exit(2);
            }
        });
    }

    public static Context getAppContext() {
        return msApplicationContext;
    }

    public static boolean isEnglishLang() {

        return PersistenceManager.getInstance().getCurrentLang().equals("en");
    }

    public static void showNoInternetMsg() {
        Toast.makeText(msApplicationContext, R.string.no_connection_msg, Toast.LENGTH_SHORT).show();
    }
}
