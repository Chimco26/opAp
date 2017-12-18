package com.operatorsapp.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.operators.getmachinesnetworkbridge.GetMachinesNetworkBridge;
import com.operators.logincore.LoginCore;
import com.operators.loginnetworkbridge.LoginNetworkBridge;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.server.NetworkManager;
import com.zemingo.logrecorder.LogRecorder;
import com.zemingo.logrecorder.ZLogger;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.operatorsapp.utils.SendReportUtil.IS_APP_CRASH;

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
    private static Context msApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        ACRA.init(this);

        ACRA.getErrorReporter().putCustomData(IS_APP_CRASH, "true");

        msApplicationContext = getApplicationContext();
//        LeakCanary.install(this);

        LogRecorder.initInstance(msApplicationContext);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidSans.ttf").setFontAttrId(R.attr.fontPath).build());

        PersistenceManager.initInstance(msApplicationContext);
        NetworkManager.initInstance();

        GetMachinesNetworkBridge getMachinesNetworkBridge = new GetMachinesNetworkBridge();
        getMachinesNetworkBridge.inject(NetworkManager.getInstance());

        LoginNetworkBridge loginNetworkBridge = new LoginNetworkBridge();
        loginNetworkBridge.inject(NetworkManager.getInstance());
        LoginCore.getInstance().inject(PersistenceManager.getInstance(), loginNetworkBridge, getMachinesNetworkBridge);

//        ShiftLogNetworkBridge shiftLogNetworkBridge = new ShiftLogNetworkBridge();
//        shiftLogNetworkBridge.inject(NetworkManager.getInstance());
//
//        ShiftLogCore.getInstance().inject(PersistenceManager.getInstance(), shiftLogNetworkBridge);


        if (BuildConfig.DEBUG) {
            ZLogger.DEBUG = true;
        }
    }

    public static Context getAppContext() {
        return msApplicationContext;
    }

    public static boolean isEnglishLang() {
        return PersistenceManager.getInstance().getCurrentLang().equals("en");
    }
}
