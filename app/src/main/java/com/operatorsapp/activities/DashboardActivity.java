package com.operatorsapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.common.ErrorResponse;
import com.example.common.Event;
import com.example.common.MultipleRejectRequestModel;
import com.example.common.RejectForMultipleRequest;
import com.example.common.StandardResponse;
import com.example.common.StopLogs.StopLogsResponse;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.common.callback.GetStopLogCallback;
import com.example.common.callback.MachineJoshDataCallback;
import com.example.common.department.MachinesLineDetail;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.permissions.PermissionResponse;
import com.example.common.permissions.WidgetInfo;
import com.example.oppapplog.OppAppLogger;
import com.github.mikephil.charting.jobs.MoveViewJob;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.operators.activejobslistformachinecore.ActiveJobsListForMachineCore;
import com.operators.activejobslistformachinecore.interfaces.ActiveJobsListForMachineUICallbackListener;
import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.activejobslistformachinenetworkbridge.ActiveJobsListForMachineNetworkBridge;
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
import com.operators.reportrejectcore.ReportCallbackListener;
import com.operators.reportrejectcore.ReportCore;
import com.operators.reportrejectinfra.GetAllRecipeCallback;
import com.operators.reportrejectinfra.GetVersionCallback;
import com.operators.reportrejectinfra.PostActivateJobCallback;
import com.operators.reportrejectinfra.PostSplitEventCallback;
import com.operators.reportrejectinfra.SimpleCallback;
import com.operators.reportrejectnetworkbridge.ReportNetworkBridge;
import com.operators.reportrejectnetworkbridge.server.request.SplitEventRequest;
import com.operators.reportrejectnetworkbridge.server.response.IntervalAndTimeOutResponse;
import com.operators.reportrejectnetworkbridge.server.response.Recipe.RecipeResponse;
import com.operators.reportrejectnetworkbridge.server.response.activateJob.ActivateJobRequest;
import com.operators.shiftloginfra.model.ShiftForMachineResponse;
import com.operators.shiftlognetworkbridge.ShiftLogNetworkBridge;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.ShowDashboardCroutonListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.NextJobTimerDialog;
import com.operatorsapp.dialogs.SetupEndDialog;
import com.operatorsapp.dialogs.TitleAndSubWithSelectableListDialog;
import com.operatorsapp.fragments.ActionBarAndEventsFragment;
import com.operatorsapp.fragments.AdvancedSettingsFragment;
import com.operatorsapp.fragments.FixUnitsProducedFragment;
import com.operatorsapp.fragments.LenoxDashboardFragment;
import com.operatorsapp.fragments.RecipeFragment;
import com.operatorsapp.fragments.ReportCycleUnitsFragment;
import com.operatorsapp.fragments.ReportProductionFragment;
import com.operatorsapp.fragments.ReportRejectsFragment;
import com.operatorsapp.fragments.ReportShiftFragment;
import com.operatorsapp.fragments.ReportStopReasonFragment;
import com.operatorsapp.fragments.SelectMachineFragment;
import com.operatorsapp.fragments.SelectStopReasonFragment;
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
import com.operatorsapp.model.SendRejectObject;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.model.event.ActivateJobEvent;
import com.operatorsapp.model.event.QCTestEvent;
import com.operatorsapp.model.event.ReportProductionEvent;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.callback.PostProductionModeCallback;
import com.operatorsapp.server.pulling.AllDashboardDataCore;
import com.operatorsapp.server.pulling.interfaces.ActualBarExtraDetailsUICallback;
import com.operatorsapp.server.pulling.interfaces.MachineDataUICallback;
import com.operatorsapp.server.pulling.interfaces.MachinePermissionCallback;
import com.operatorsapp.server.pulling.interfaces.MachineStatusUICallback;
import com.operatorsapp.server.pulling.interfaces.OnTimeToEndChangedListener;
import com.operatorsapp.server.pulling.interfaces.ShiftForMachineUICallback;
import com.operatorsapp.server.pulling.interfaces.ShiftLogUICallback;
import com.operatorsapp.server.pulling.timecounter.TimeToEndCounter;
import com.operatorsapp.server.requests.PostDeleteTokenRequest;
import com.operatorsapp.server.requests.PostIncrementCounterRequest;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.server.responses.AppVersionResponse;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.server.responses.ResponseKPIS;
import com.operatorsapp.server.responses.StopReasonsGroup;
import com.operatorsapp.utils.ChangeLang;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.MyExceptionHandler;
import com.operatorsapp.utils.SaveAlarmsHelper;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SimpleRequests;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.utils.broadcast.RefreshPollingBroadcast;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.operatorsapp.view.SingleLineKeyboard;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;
import ravtech.co.il.publicutils.JobBase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static com.example.common.permissions.WidgetInfo.PermissionId.SHIFT_REPORT;
import static com.operatorsapp.activities.ActivateJobActivity.EXTRA_IS_NO_PRODUCTION;
import static com.operatorsapp.activities.ActivateJobActivity.EXTRA_LAST_ERP_JOB_ID;
import static com.operatorsapp.activities.ActivateJobActivity.EXTRA_LAST_JOB_ID;
import static com.operatorsapp.activities.ActivateJobActivity.EXTRA_LAST_PRODUCT_NAME;
import static com.operatorsapp.activities.QCActivity.QC_EDIT_MODE;
import static com.operatorsapp.activities.QCActivity.QC_TEST_ID;
import static com.operatorsapp.fragments.ActionBarAndEventsFragment.EXTRA_FIELD_FOR_MACHINE;
import static com.operatorsapp.fragments.ReportStopReasonFragment.IS_REPORTING_ON_SETUP_END;
import static com.operatorsapp.fragments.ReportStopReasonFragment.IS_REPORTING_ON_SETUP_EVENTS;
import static com.operatorsapp.fragments.ReportStopReasonFragment.IS_SETUP_MODE;
import static com.operatorsapp.utils.ClearData.cleanEvents;

//import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardActivity extends AppCompatActivity implements OnCroutonRequestListener,
        OnActivityCallbackRegistered, GoToScreenListener, JobsFragmentToDashboardActivityCallback,
        OperatorCoreToDashboardActivityCallback,
        ReportFieldsFragmentCallbackListener, SettingsInterface,
        CroutonRootProvider, ApproveFirstItemFragmentCallbackListener,
        RefreshPollingBroadcast.RefreshPollingListener, CroutonCreator.CroutonListener,
        ActionBarAndEventsFragment.ActionBarAndEventsFragmentListener,
        ReportStopReasonFragment.ReportStopReasonFragmentListener,
        ViewPagerFragment.OnViewPagerListener,
        RecipeFragment.OnRecipeFragmentListener,
        AdvancedSettingsFragment.AdvancedSettingsListener,
        ShowDashboardCroutonListener, AllDashboardDataCore.AllDashboardDataCoreListener,
        DashboardCentralContainerListener,
        OnReportFieldsUpdatedCallbackListener,
        EasyPermissions.PermissionCallbacks,
        SelectStopReasonFragment.SelectStopReasonFragmentListener,
        SignInOperatorFragment.SignInOperatorFragmentListener,
        SelectMachineFragment.SelectMachineFragmentListener,
        Thread.UncaughtExceptionHandler {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    public static final String REPORT_REJECT_TAG = "ReportRejects";
    public static final String REPORT_UNIT_CYCLE_TAG = "ReportUnitsInCycle";
    public static final String REPORT_FIX_PRODUCED_UNITS = "ReportFixProducedUnits";
    public static final String REPORT_PRODUCTION_TAG = "ReportProduction";
    private static final int POOLING_BACKUP_DELAY = 1000 * 60 * 5;
    private static final String INTERVAL_KEY = "OpAppPollingInterval";
    private static final String TIME_OUT_KEY = "OpAppRequestTimeout";
    private static final float MINIMUM_VERSION_FOR_INTERVAL_AND_TIME_OUT_FROM_API = 1.9f;
    private static final int CHECK_APP_VERSION_INTERVAL = 1000 * 60 * 60 * 3; //check every 3 hours
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private static final int QC_ACTIVITY_RESULT_CODE = 2506;
    private static final int TECH_CALL_ACTIVITY_RESULT_CODE = 2507;
    private static final int TASK_ACTIVITY_RESULT_CODE = 2508;
    private boolean ignoreFromOnPause = false;
    public static final String DASHBOARD_FRAGMENT = "dashboard_fragment";
    private CroutonCreator mCroutonCreator;
    private TimeToEndCounter mTimeToEndCounter;
    private DashboardActivityToJobsFragmentCallback mDashboardActivityToJobsFragmentCallback;
    private DashBoardActivityToSelectedJobFragmentCallback mDashboardActivityToSelectedJobFragmentCallback;
    private JobsCore mJobsCore;
    private ReportFieldsForMachineCore mReportFieldsForMachineCore;
    private ReportFieldsForMachine mReportFieldsForMachine;
    private WeakReference<OnReportFieldsUpdatedCallbackListener> mOnReportFieldsUpdatedCallbackListener;
    private AllDashboardDataCore mAllDashboardDataCore;
    private RefreshPollingBroadcast mRefreshBroadcast = null;
    private ArrayList<WeakReference<DashboardUICallbackListener>> mDashboardUICallbackListenerList = new ArrayList<WeakReference<DashboardUICallbackListener>>();
    private WeakReference<WidgetFragment> mWidgetFragment;
    private WeakReference<ActionBarAndEventsFragment> mActionBarAndEventsFragment;
    //    private View mContainer2;
    private ArrayList<Float> mSelectedEvents;
    private WeakReference<ReportStopReasonFragment> mReportStopReasonFragment;
    private WeakReference<SelectStopReasonFragment> mSelectStopReasonFragment;
    private View mContainer3;
    private WeakReference<ViewPagerFragment> mViewPagerFragment;
    private WeakReference<ReportShiftFragment> mReportShiftFragment;
    private WeakReference<RecipeFragment> mRecipeFragment;
    private Intent mGalleryIntent;
    //    private Integer mSelectJobId;
    private ArrayList<PdfObject> mPdfList = new ArrayList<>();
    private Integer mSelectProductJobId;
    private JobBase.OnJobFinishedListener mOnJobFinishedListener;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private MachineStatus mCurrentMachineStatus;
    private int mSpinnerProductPosition;
    //private Tracker mTracker;
    private Handler pollingBackupHandler = new Handler();
    private ArrayList<Machine> mMachines;
    private AlertDialog mLoadingDialog;
    private Timer mReportModeTimer;
    //    private AlertDialog mAlaramAlertDialog;
    private Runnable pollingBackupRunnable = new Runnable() {
        @Override
        public void run() {
            if (DashboardActivity.this != null && !DashboardActivity.this.isDestroyed()) {
                getActiveJobs();
                pollingBackup(true);
            }
        }
    };
    private ReportCore mReportCore;
    private SetupEndDialog mSetupEndDialog;
    private int mSelectedReasonId;
    private int mSelectedTechnicianId;
    private SendRejectObject mSendRejectObject;
    private ActualBarExtraResponse mActualBarExtraResponse;
    private ArrayList<RejectForMultipleRequest> mRejectForMultipleRequests;
    private boolean mCustomKeyBoardIsOpen;
    private static Handler mVersionCheckHandler;
    private Runnable mCheckAppVersionRunnable;
    private File outputFile;
    private MachineJoshDataResponse mMachineJoshDataResponse;
    private Integer mSelectProductJoshId;
    private View mReportBtn;
    private boolean mIsTimeLineOpen;
    private SparseArray<WidgetInfo> permissionForMachineHashMap;
    private NextJobTimerDialog mNextJobTimerDialog;
    private int mShowDialogJobId;
    private static Handler collapseNotificationHandler;
    private boolean mIsCollapse = true;
    private boolean mIsUpgrading = false;
    private AsyncTask<String, String, String> mDownloadFile;
    private boolean isNeedRecipeRefresh;
    private WeakReference<SelectMachineFragment> mSelectMachineFragment;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


        if (!hasFocus && PersistenceManager.getInstance().isStatusBarLocked()) {
            mIsCollapse = true;
            collapseNow(true);
        } else if (collapseNotificationHandler != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // cancel collapse loop after 1 minute
                    if (DashboardActivity.this != null && !DashboardActivity.this.isDestroyed()) {
                        collapseNotificationHandler.removeCallbacksAndMessages(null);
                        mIsCollapse = false;
                    }
                }
            }, 1000 * 60);
        }

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
                                            TimeUtils.getDateForNotification(not.getmSentTime()).getTime(), not.getmNotificationID(), not.getmTargetUserId(), not.getmEventID(), not.getmEventName()));
                                }
                            }
                        }
                        PersistenceManager.getInstance().setNotificationHistory(response.body().getmNotificationsList());
                    } else {
                        PersistenceManager.getInstance().setNotificationHistory(new ArrayList<Notification>());
                    }

                    PersistenceManager.getInstance().setCalledTechnicianList(techList);
                    if (techList.size() > 0) {
                        PersistenceManager.getInstance().setRecentTechCallId(techList.get(0).getmNotificationId());
                    } else {
                        PersistenceManager.getInstance().setRecentTechCallId(0);
                    }
//                    ProgressDialogManager.dismiss();
//                    finish();
                } else {
//                    ProgressDialogManager.dismiss();
                    PersistenceManager.getInstance().setNotificationHistory(null);
//                    finish();
                }

            }

            @Override
            public void onFailure(Call<NotificationHistoryResponse> call, Throwable t) {

//                ProgressDialogManager.dismiss();
                PersistenceManager.getInstance().setNotificationHistory(null);
//                finish();

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void collapseNow(final boolean isCollapse) {

        try {
            // Initialize 'collapseNotificationHandler'
            if (collapseNotificationHandler == null) {
                collapseNotificationHandler = new Handler();
            }

            // Post a Runnable with some delay - currently set to 300 ms
            collapseNotificationHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (DashboardActivity.this != null && !DashboardActivity.this.isDestroyed()) {
                        // Use reflection to trigger a method from 'StatusBarManager'
                        @SuppressLint("WrongConstant") Object statusBarService = getSystemService("statusbar");
                        Class<?> statusBarManager = null;

                        try {
                            statusBarManager = Class.forName("android.app.StatusBarManager");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        Method collapseStatusBar = null;
                        try {
                            // Prior to API 17, the method to call is 'collapse()'
                            // API 17 onwards, the method to call is `collapsePanels()`
                            if (Build.VERSION.SDK_INT > 16) {
                                collapseStatusBar = statusBarManager.getMethod(isCollapse ? "collapsePanels" : "expandNotificationsPanel");
                            } else {
                                collapseStatusBar = statusBarManager.getMethod(isCollapse ? "collapse" : "expand");
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }

                        collapseStatusBar.setAccessible(true);

                        try {
                            collapseStatusBar.invoke(statusBarService);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        // Currently, the delay is 10 ms. You can change this
                        // value to suit your needs.
                        if (mIsCollapse && PersistenceManager.getInstance().isStatusBarLocked()) {
                            collapseNotificationHandler.postDelayed(this, 10L);
                        }
                    }
                }
            }, 10L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OppAppLogger.d(TAG, "onCreate(), start ");

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        setContentView(R.layout.activity_dashboard);
        updateAndroidSecurityProvider(this);

//        // Analytics
//        OperatorApplication application = (OperatorApplication) ƒgetApplication();
//        mTracker = application.getDefaultTracker();

        getNotifications();

        mMachines = getIntent().getExtras().<Machine>getParcelableArrayList(MainActivity.MACHINE_LIST);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mCroutonCreator = new CroutonCreator();

        getIntervalAndTimeout();

        initDataListeners();

        mActionBarAndEventsFragment = new WeakReference<>(ActionBarAndEventsFragment.newInstance());

//        mContainer2 = findViewById(R.id.fragments_container_widget);

        mContainer3 = findViewById(R.id.fragments_container_central);

        initDashboardFragment();

        try {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, mActionBarAndEventsFragment.get()).commit();

            getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        } catch (IllegalStateException ignored) {
        }
        OppAppLogger.d(TAG, "onCreate(), end ");

        setupVersionCheck(false);

        setReportBtnListener();

    }

    private void setReportBtnListener() {

        mReportBtn = findViewById(R.id.AD_report_btn);
        mReportBtn.post(new Runnable() {
            @Override
            public void run() {
                if (DashboardActivity.this != null && !DashboardActivity.this.isDestroyed()) {
                    ViewGroup.LayoutParams params = mReportBtn.getLayoutParams();
                    params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.15);
                    params.width = params.height;
                    mReportBtn.setLayoutParams(params);
                    if (PersistenceManager.getInstance().getReportShiftBtnPositionX() > 0) {
                        mReportBtn.setX(PersistenceManager.getInstance().getReportShiftBtnPositionX());
                    }
                    if (PersistenceManager.getInstance().getReportShiftBtnPositionY() > 0) {
                        mReportBtn.setY(PersistenceManager.getInstance().getReportShiftBtnPositionY());
                    }
                }
            }
        });
        mReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProcessPhoenix.triggerRebirth(DashboardActivity.this);

                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.SHIFT_REPORT, true, "Shift Report pressed");
                initReportShiftFragment();
            }
        });
        final float[] downX = new float[1];
        final float[] downY = new float[1];
        final float[] dX = new float[1];
        final float[] dY = new float[1];
        final float[] moveToX = new float[1];
        final float[] moveToY = new float[1];
        mReportBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX[0] = view.getX();
                        downY[0] = view.getY();
                        moveToX[0] = view.getX();
                        moveToY[0] = view.getY();
                        dX[0] = view.getX() - motionEvent.getRawX();
                        dY[0] = view.getY() - motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs((motionEvent.getRawX() + dX[0]) - downX[0]) > 10 && motionEvent.getRawX() + dX[0] > 0
                                && motionEvent.getRawX() + dX[0] < mContainer3.getWidth() + mContainer3.getX() - (view.getWidth())) {
                            moveToX[0] = motionEvent.getRawX() + dX[0];
                        }
                        if (Math.abs((motionEvent.getRawY() + dY[0]) - downY[0]) > 10 && motionEvent.getRawY() + dY[0] > 0
                                && motionEvent.getRawY() + dY[0] < mContainer3.getHeight() - (view.getHeight() / 2)) {
                            moveToY[0] = motionEvent.getRawY() + dY[0];
                        }
                        if (moveToX[0] != 0 || moveToY[0] != 0) {
                            view.animate()
                                    .x(moveToX[0])
                                    .y(moveToY[0])
                                    .setDuration(0)
                                    .start();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(moveToX[0] - downX[0]) > 10 || Math.abs(moveToY[0] - downY[0]) > 10) {
                            PersistenceManager.getInstance().setReportShiftBtnPositionX(moveToX[0]);
                            PersistenceManager.getInstance().setReportShiftBtnPositionY(moveToY[0]);
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void initDataListeners() {

        GetMachineStatusNetworkBridge getMachineStatusNetworkBridge = new GetMachineStatusNetworkBridge();

        getMachineStatusNetworkBridge.inject(NetworkManager.getInstance());

        GetMachineDataNetworkBridge getMachineDataNetworkBridge = new GetMachineDataNetworkBridge();

        getMachineDataNetworkBridge.inject(NetworkManager.getInstance());

        ShiftLogNetworkBridge shiftLogNetworkBridge = new ShiftLogNetworkBridge();

        shiftLogNetworkBridge.inject(NetworkManager.getInstance());

        mAllDashboardDataCore = new AllDashboardDataCore(this, getMachineStatusNetworkBridge, PersistenceManager.getInstance(), getMachineDataNetworkBridge, PersistenceManager.getInstance(), PersistenceManager.getInstance(), shiftLogNetworkBridge);

        ReportFieldsForMachineNetworkBridge reportFieldsForMachineNetworkBridge = new ReportFieldsForMachineNetworkBridge();
        reportFieldsForMachineNetworkBridge.inject(NetworkManager.getInstance());

        mReportFieldsForMachineCore = new ReportFieldsForMachineCore(reportFieldsForMachineNetworkBridge, PersistenceManager.getInstance());

        checkUpdateNotificationToken();
    }

    private void checkUpdateNotificationToken() {

        final PersistenceManager pm = PersistenceManager.getInstance();

        if (pm.getNotificationToken() != null) {
            final boolean retry[] = {true};
            final String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            PostNotificationTokenRequest request = new PostNotificationTokenRequest(pm.getSessionId(), pm.getMachineId(), pm.getNotificationToken(), id);
            NetworkManager.getInstance().postNotificationToken(request, new Callback<StandardResponse>() {
                @Override
                public void onResponse(Call<StandardResponse> call, retrofit2.Response<StandardResponse> response) {
                    if (response != null && response.body() != null && response.errorBody() == null) {
                        Log.d(TAG, "token sent");
                    } else {
                        onFailure(call, new Throwable("failed "));
                    }
                }

                @Override
                public void onFailure(Call<StandardResponse> call, Throwable t) {
                    Log.d(TAG, "token failed");
                    if (retry[0]) {
                        retry[0] = false;
                        call.clone();
                    }
                }
            });
        } else {
            try {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        PersistenceManager.getInstance().setNotificationToken(task.getResult().getToken());
                        checkUpdateNotificationToken();
                    }
                });
            } catch (IllegalStateException e) {
            }
        }

//        if (pm.isNeedUpdateToken()) {
//
//            if (pm.getNotificationToken() != null) {
//                final boolean retry[] = {true};
//
//                final String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//
//                PostNotificationTokenRequest request = new PostNotificationTokenRequest(pm.getSessionId(), pm.getMachineId(), pm.getNotificationToken(), id);
//                NetworkManager.getInstance().postNotificationToken(request, new Callback<StandardResponse>() {
//                    @Override
//                    public void onResponse(Call<StandardResponse> call, retrofit2.Response<StandardResponse> response) {
//                        if (response != null && response.body() != null && response.errorBody() == null) {
//                            Log.d(TAG, "token sent");
//                            pm.setNeedUpdateToken(false);
//                            pm.tryToUpdateToken("success + android id: " + id);
//                        } else {
//                            onFailure(call, new Throwable("failed "));
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<StandardResponse> call, Throwable t) {
//                        pm.setNeedUpdateToken(true);
//                        pm.tryToUpdateToken("failed + android id: " + id);
//
//                        Log.d(TAG, "token failed");
//                        if (retry[0]) {
//                            retry[0] = false;
//                            call.clone();
//                        }
//                    }
//                });
//            } else {
//                try {
//
//                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                            if (!task.isSuccessful()) {
//                                Log.w(TAG, "getInstanceId failed", task.getException());
//                                return;
//                            }
//
//                            // Get new Instance ID token
//
//                            PersistenceManager.getInstance().setNotificationToken(task.getResult().getToken());
//                            checkUpdateNotificationToken();
//
//                        }
//                    });
//                } catch (IllegalStateException e) {
//                }
//            }
//        }

    }

    private void pollingBackup(boolean isActivate) {

        if (isActivate) {
            pollingBackupHandler.removeCallbacksAndMessages(null);
            pollingBackupHandler.postDelayed(pollingBackupRunnable, POOLING_BACKUP_DELAY);
            Log.d(TAG, "pollingBackupHandler reset");
        } else {
            pollingBackupHandler.removeCallbacksAndMessages(null);
            Log.d(TAG, "pollingBackupHandler removed");
        }
    }

    private WidgetFragment initWidgetFragment() {

        mWidgetFragment = new WeakReference<>(WidgetFragment.newInstance(mReportFieldsForMachine,
                findJoshId(mActiveJobsListForMachine, mSpinnerProductPosition)));

        return mWidgetFragment.get();
//        mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSpinnerProductPosition
    }

    private int findJoshId(ActiveJobsListForMachine mActiveJobsListForMachine, int mSpinnerProductPosition) {
        if (mActiveJobsListForMachine != null && mActiveJobsListForMachine.getActiveJobs() != null
                && mActiveJobsListForMachine.getActiveJobs().size() > 0 && mActiveJobsListForMachine.getActiveJobs().get(mSpinnerProductPosition) != null) {
            return mActiveJobsListForMachine.getActiveJobs().get(mSpinnerProductPosition).getJoshID();
        }
        return 0;
    }

    private void initDashboardFragment() {

        if (BuildConfig.FLAVOR.equals(getString(R.string.emerald_flavor_name))) {

            initWidgetFragment();

            initViewPagerFragment();

        } else if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {

            changeActionBarColor(R.color.lenox_action_bar_color);

//            setLenoxMachine(PersistenceManager.getInstance().getMachineId());
            //TODO Lenox uncomment

            initLenoxDashboardFragment();

            if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
                mActionBarAndEventsFragment.get().setMachines(mMachines);
            }
        }

    }

    public void changeActionBarColor(int resourseColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, resourseColor));
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(resourseColor)));

    }

    private void initLenoxDashboardFragment() {

        try {
            getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), LenoxDashboardFragment.newInstance()).commit();
        } catch (IllegalStateException ignored) {
        }
    }

    private void initViewPagerFragment() {

        mViewPagerFragment = new WeakReference<>(ViewPagerFragment.newInstance());

        try {

            getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), mViewPagerFragment.get()).commit();
        } catch (IllegalStateException ignored) {
        }
    }

    private void initReportShiftFragment() {

        mReportShiftFragment = new WeakReference<>(ReportShiftFragment.newInstance(mIsTimeLineOpen));

        try {
            getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), mReportShiftFragment.get()).addToBackStack(ReportShiftFragment.TAG).commit();
//            mReportBtn.setVisibility(View.GONE);
        } catch (IllegalStateException ignored) {
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mWidgetFragment != null && mWidgetFragment.get() != null) {//TODO check because viewpagerFragment

            try {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().remove(mWidgetFragment.get()).commit();
            } catch (IllegalStateException ignored) {
            }
        }
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            try {

                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().remove(mActionBarAndEventsFragment.get()).commit();
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

    private FragmentManager.OnBackStackChangedListener getListener() {

        return new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {

                Fragment fragment = getVisibleFragment();

                if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

                    mActionBarAndEventsFragment.get().setVisiblefragment(fragment);
                }

                if (fragment != null) {
                    setActionBarByFragment(fragment);
                    setFiltersByFragment();
                    checkShowReportBtn(fragment);
                }
            }
        };
    }

    public void setFiltersByFragment() {
        if (mCurrentMachineStatus != null && mCurrentMachineStatus.getAllMachinesData() != null
                && mCurrentMachineStatus.getAllMachinesData().size() > 0
                && mCurrentMachineStatus.getAllMachinesData().get(0) != null) {
            setWhiteFilter(mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() > 1);
            setFilterWarningText(mCurrentMachineStatus.getAllMachinesData().get(0).isProductionModeWarning());
//                        setFilterWarningText(true);
        }
    }

    public void setActionBarByFragment(Fragment fragment) {
        if (fragment instanceof ActionBarAndEventsFragment ||
                fragment instanceof RecipeFragment ||
                fragment instanceof WidgetFragment ||
                fragment instanceof ReportStopReasonFragment ||
                fragment instanceof SelectStopReasonFragment ||
                fragment instanceof ReportShiftFragment) {
            if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
                mActionBarAndEventsFragment.get().setActionBar();
            }
            first = !first;
        }
    }

    public void checkShowReportBtn(Fragment fragment) {
        if (fragment instanceof ActionBarAndEventsFragment ||
                fragment instanceof RecipeFragment ||
                fragment instanceof WidgetFragment) {
            showReportBtn(true);
        } else {
            showReportBtn(false);
        }
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {

        try {

            ProviderInstaller.installIfNeeded(this);

        } catch (GooglePlayServicesRepairableException e) {


            GoogleApiAvailability.getInstance().getErrorDialog(callingActivity, e.getConnectionStatusCode(), 0);

        } catch (GooglePlayServicesNotAvailableException e) {
            OppAppLogger.e("SecurityException", "Google Play Services not available.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);

        pollingBackup(false);

        if (!ignoreFromOnPause && mGalleryIntent == null) {

            mAllDashboardDataCore.stopPolling();

            mAllDashboardDataCore.unregisterListener();

            mAllDashboardDataCore.stopTimer();

            mReportFieldsForMachineCore.stopPolling();

            mReportFieldsForMachineCore.unregisterListener();

            if (mDownloadFile != null) {
                mDownloadFile.cancel(true);
            }
/*
            if (!mIsUpgrading && PersistenceManager.getInstance().isStatusBarLocked()) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }*/

        }
    }

    @Override
    protected void onResume() {
        OppAppLogger.d(TAG, "onResume()");

        EventBus.getDefault().register(this);

        pollingBackup(true);
        getKPIS();

        if (!ignoreFromOnPause) {

            if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

                mActionBarAndEventsFragment.get().setVisiblefragment(getVisibleFragment());
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

        ChangeLang.initLanguage(this);
    }

    @Subscribe
    public void onOpenQcById(QCTestEvent event) {
        onOpenQCActivity(event.getiD(), false);
    }

    @Subscribe
    public void openReportProduction(ReportProductionEvent event) {
        if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
            goToFragment(ReportProductionFragment.newInstance(0, mActiveJobsListForMachine, mSpinnerProductPosition), true, true);
        } else {
            goToFragment(ReportProductionFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSpinnerProductPosition), true, true);
        }
    }

    @Subscribe
    public void onActivateJob(ActivateJobEvent event) {
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        postActivateJob(new ActivateJobRequest(persistenceManager.getSessionId(),
                String.valueOf(persistenceManager.getMachineId()),
                "0",
                event.getErpJobId(),
                persistenceManager.getOperatorId(),
                false));
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
        MoveViewJob.getInstance(null, 0, 0, null, null);

        if (mReportModeTimer != null) {
            mReportModeTimer.purge();
            mReportModeTimer.cancel();
            mReportModeTimer = null;
        }
        if (mCroutonCreator != null) {
            mCroutonCreator.cancel();
            mCroutonCreator = null;
        }
        removeBroadcasts();

        clearPolling();
        clearInterfaces();

        if (mVersionCheckHandler != null) {
            mVersionCheckHandler.removeCallbacksAndMessages(null);
        }
        if (collapseNotificationHandler != null) {
            collapseNotificationHandler.removeCallbacksAndMessages(null);
            collapseNotificationHandler = null;
        }

        mIsCollapse = false;

        if (getSupportFragmentManager() != null && getSupportFragmentManager().getFragments() != null) {

            List<Fragment> fragments = getSupportFragmentManager().getFragments();

            for (Fragment fragment : fragments) {
                try {
                    getSupportFragmentManager().beginTransaction().remove(fragment);
                } catch (Exception e) {
                }
                fragment = null;
            }
        }
        if (!mIsUpgrading && PersistenceManager.getInstance().isStatusBarLocked()) {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private void removeBroadcasts() {

        try {

            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRefreshBroadcast);

        } catch (Exception e) {

            if (e.getMessage() != null)
                Log.e(TAG, e.getMessage());
        }

    }

    public void dashboardDataStartPolling() {

        mAllDashboardDataCore.registerListener(getMachineStatusUICallback(), getMachineDataUICallback(),
                getShiftLogUICallback(), getActualBarUICallback(), getMachineJoshDataCallback(), getPermissionForMachine(), getStopEventsLineCallback());

        mAllDashboardDataCore.stopPolling();

        NetworkManager.getInstance().clearPollingRequest();

        mAllDashboardDataCore.startPolling();

    }

    private GetStopLogCallback getStopEventsLineCallback() {
        return new GetStopLogCallback() {
            @Override
            public void onGetStopLogSuccess(StopLogsResponse response) {

            }

            @Override
            public void onGetStopLogFailed(StandardResponse reason) {

            }
        };
    }

    private MachinePermissionCallback getPermissionForMachine() {
        return new MachinePermissionCallback() {

            @Override
            public void onMachinePermissionCallbackSucceeded(PermissionResponse permissionResponse) {
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    permissionForMachineHashMap = new SparseArray<>();
                    for (WidgetInfo widgetInfo : permissionResponse.getWidgetInfo()) {
//                        widgetInfo.setHaspermission(false); for test
                        permissionForMachineHashMap.put(widgetInfo.getId(), widgetInfo);
                    }

                    for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {

                        if (dashboardUICallbackListener.get() != null && dashboardUICallbackListener.get() != null) {
                            dashboardUICallbackListener.get().onPermissionForMachinePolling(permissionForMachineHashMap);
                        }
                    }
                }
            }

            @Override
            public void onMachinePermissionCallbackFailed(StandardResponse reason) {
                OppAppLogger.w(TAG, "onMachinePermissionCallbackFailed");
            }
        };
    }

    private ActualBarExtraDetailsUICallback getActualBarUICallback() {
        return new ActualBarExtraDetailsUICallback() {
            @Override
            public void onActualBarExtraDetailsSucceeded(ActualBarExtraResponse actualBarExtraResponse) {
                Log.d(TAG, "onActualBarExtraDetailsSucceeded: ");
                mActualBarExtraResponse = actualBarExtraResponse;
            }

            @Override
            public void onActualBarExtraDetailsFailed(StandardResponse reason) {
                OppAppLogger.w(TAG, "onActualBarExtraDetailsFailed");
            }
        };
    }

    private MachineJoshDataCallback getMachineJoshDataCallback() {
        return new MachineJoshDataCallback() {
            @Override
            public void onMachineJoshDataCallbackSucceeded(MachineJoshDataResponse machineJoshDataResponse) {
                mMachineJoshDataResponse = machineJoshDataResponse;
            }

            @Override
            public void onMachineJoshDataCallbackFailed(StandardResponse reason) {

            }
        };
    }

    private void getActiveJobs() {
        mOnReportFieldsUpdatedCallbackListener = new WeakReference<OnReportFieldsUpdatedCallbackListener>(this);
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

                if (activeJobsListForMachine.getActiveJobs().size() > 0) {
                    mAllDashboardDataCore.sendRequestForPolling(mOnJobFinishedListener, activeJobsListForMachine.getActiveJobs().get(0).getJobID(),
                            mSelectProductJobId, PersistenceManager.getInstance().getShiftStart());
                    PersistenceManager.getInstance().setMaxUnitReport(mActiveJobsListForMachine.getActiveJobs().get(0).getCavitiesStandard());
                    if (activeJobsListForMachine.getActiveJobs().size() > mSpinnerProductPosition) {
                        PersistenceManager.getInstance().setJoshID(mActiveJobsListForMachine.getActiveJobs().get(mSpinnerProductPosition).getJoshID());
                    }
                }
                mReportFieldsForMachineCore.stopPolling();
                mReportFieldsForMachineCore.startPolling();

                if (activeJobsListForMachine.getActiveJobs().size() <= 1) {

                    mSelectProductJobId = null;
                }

                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                        if (dashboardUICallbackListener.get() != null && dashboardUICallbackListener.get() != null) {
                            dashboardUICallbackListener.get().onActiveJobsListForMachineUICallbackListener(activeJobsListForMachine);
                        }
                    }

                }


                OppAppLogger.i(TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
            } else {
                ProgressDialogManager.dismiss();
                OppAppLogger.w(TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
            }
        }

        @Override
        public void onActiveJobsListForMachineReceiveFailed(StandardResponse reason) {
            if (reason != null && reason.getError() != null && reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.SessionInvalid) {
                OppAppLogger.w(TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getError().getErrorDesc());
                silentLoginFromDashBoard(DashboardActivity.this, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        getActiveJobs();
                    }

                    @Override
                    public void onSilentLoginFailed(StandardResponse reason) {
                        ShowCrouton.operatorLoadingErrorCrouton(DashboardActivity.this, reason.getError().getErrorDesc());
                    }
                });
            } else {

                OppAppLogger.w(TAG, "onActiveJobsListForMachineReceiveFailed() " + reason.getError().getErrorDesc());
//            ShowCrouton.jobsLoadingErrorCrouton(mOnCroutonRequestListener);
                mAllDashboardDataCore.sendRequestForPolling(mOnJobFinishedListener, null, mSelectProductJobId, PersistenceManager.getInstance().getShiftStart());
            }

        }
    };

    @NonNull
    private MachineStatusUICallback getMachineStatusUICallback() {
        return new MachineStatusUICallback() {
            @Override
            public void onStatusReceivedSuccessfully(MachineStatus machineStatus) {

                boolean isNewShift = false;
                if (mCurrentMachineStatus != null && mCurrentMachineStatus.getAllMachinesData() != null &&
                        mCurrentMachineStatus.getAllMachinesData().size() > 0) {
                    isNewShift = (mCurrentMachineStatus.getAllMachinesData().get(0).getShiftEndingIn() - machineStatus.getAllMachinesData().get(0).getShiftEndingIn()) < 0;
                }
                mCurrentMachineStatus = machineStatus;
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    if (machineStatus != null && machineStatus.getAllMachinesData() != null &&
                            machineStatus.getAllMachinesData().size() > 0) {

                        PersistenceManager.getInstance().setJobId(machineStatus.getAllMachinesData().get(0).getCurrentJobID());
                        PersistenceManager.getInstance().setDisplayRejectFactor(machineStatus.getAllMachinesData().get(0).isDisplayRejectFactor());
                        PersistenceManager.getInstance().setAddRejectsOnSetupEnd(machineStatus.getAllMachinesData().get(0).isAddRejectsOnSetupEnd());
                        PersistenceManager.getInstance().setMinEventDuration(machineStatus.getAllMachinesData().get(0).getMinEventDuration());
                        PersistenceManager.getInstance().setDepartmentId(machineStatus.getAllMachinesData().get(0).getDepartmentID());
                        PersistenceManager.getInstance().setUnitsInCycleType(machineStatus.getAllMachinesData().get(0).getUnitsInCycleType());
                        PersistenceManager.getInstance().setMachineLineId(machineStatus.getAllMachinesData().get(0).getLineID());
                        PersistenceManager.getInstance().setReportRejectDefaultUnits(machineStatus.getAllMachinesData().get(0).getReportRejectDefaultUnits());
                        PersistenceManager.getInstance().setRequireWorkerSignIn(machineStatus.getAllMachinesData().get(0).isRequireWorkerSignIn());
                        PersistenceManager.getInstance().setActivateJobWidgetOnOpApp(machineStatus.getAllMachinesData().get(0).isActivateJobWidgetOnOpApp());

                        String opName = machineStatus.getAllMachinesData().get(0).getOperatorName();
                        String opId = machineStatus.getAllMachinesData().get(0).getOperatorId();
                        PersistenceManager.getInstance().setOperatorDBId(machineStatus.getAllMachinesData().get(0).getUserId());

                        if (opId != null && !opId.equals("") && opName != null && !opName.equals("")) {

                            PersistenceManager.getInstance().setOperatorName(opName);
                            PersistenceManager.getInstance().setOperatorId(opId);
                        } else {
                            PersistenceManager.getInstance().setOperatorName("");
                            PersistenceManager.getInstance().setOperatorId("");
                        }

                        if (mViewPagerFragment != null && mViewPagerFragment.get() != null && mViewPagerFragment.get().isRecipeShown()) {
                            getAllRecipes(machineStatus.getAllMachinesData().get(0).getCurrentJobID(), true, true);
                        } else if (mViewPagerFragment != null && isNeedRecipeRefresh && mRecipeFragment != null) {
                            getAllRecipes(PersistenceManager.getInstance().getJobId(), true, false);
                            mPdfList = new ArrayList<>();
                        }

                        pollingBackup(true);
                    }

                    for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {

                        if (dashboardUICallbackListener.get() != null && dashboardUICallbackListener.get() != null) {
                            dashboardUICallbackListener.get().onDeviceStatusChanged(machineStatus);
                        }
                    }
                    ProgressDialogManager.dismiss();
                } else {
                    ProgressDialogManager.dismiss();
                    OppAppLogger.w(TAG, " onStatusReceivedSuccessfully() - DashboardUICallbackListener is null");
                }
                if (machineStatus != null) {
                    setWhiteFilter(mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() > 1);
                    setFilterWarningText(mCurrentMachineStatus.getAllMachinesData().get(0).isProductionModeWarning());
                    checkShowReportBtn(getVisibleFragment());
//                    setFilterWarningText(true);
                    showTimeNextJobDialog(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentJobID(), mCurrentMachineStatus.getAllMachinesData().get(0).getmAutoActivateNextJob(),
                            mCurrentMachineStatus.getAllMachinesData().get(0).getmNextJobID(),
                            mCurrentMachineStatus.getAllMachinesData().get(0).getmAutoActivateNextJobTimer(),
                            mCurrentMachineStatus.getAllMachinesData().get(0).getmNextERPJobID(),
                            mCurrentMachineStatus.getAllMachinesData().get(0).getmAutoActivateNextJobTimerSec());
                    if (isNewShift && machineStatus.getAllMachinesData().get(0).isWorkerSigninOnShiftChange()
                            && !(getVisibleFragment() instanceof SignInOperatorFragment)) {
                        goToFragment(new SignInOperatorFragment(), true, true);
                    }
                }

            }

            @Override
            public void onTimerChanged(long millisUntilFinished) {
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {

                        if (dashboardUICallbackListener.get() != null && dashboardUICallbackListener.get() != null) {
                            Locale locale = getApplicationContext().getResources().getConfiguration().locale;

                            String countDownTimer = String.format(locale, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                            dashboardUICallbackListener.get().onTimerChanged(countDownTimer);
                        }
                    }
                } else {
                    OppAppLogger.w(TAG, "onTimerChanged() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onStatusReceiveFailed(StandardResponse reason) {

                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                        if (dashboardUICallbackListener.get() != null && dashboardUICallbackListener.get() != null) {
                            OppAppLogger.i(TAG, "onStatusReceiveFailed() reason: " + reason.getError().getErrorDesc());
                            dashboardUICallbackListener.get().onDataFailure(reason, DashboardUICallbackListener.CallType.Status);
                        }
                    }
                }
            }
        };
    }

    private void showReportBtn(boolean show) {
        boolean havePermission = true;
        if (permissionForMachineHashMap != null) {
            havePermission = WidgetInfo.getWidgetInfo(permissionForMachineHashMap, SHIFT_REPORT.getId()).getHaspermissionBoolean();
        }
        if (show && havePermission) {
            mReportBtn.setVisibility(View.VISIBLE);
        } else {
            mReportBtn.setVisibility(View.GONE);
        }
    }

    private void setBlackFilter(boolean show) {
        if (show) {
            findViewById(R.id.FAAE_black_filter).setVisibility(View.VISIBLE);
//            setWidgetItemInPager();
        } else {
            findViewById(R.id.FAAE_black_filter).setVisibility(View.GONE);
            onClearAllSelectedEvents();
        }
    }

    private void setWidgetItemInPager() {
        if (mViewPagerFragment != null && mViewPagerFragment.get() != null) {
            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                mViewPagerFragment.get().getPager().setCurrentItem(1);
            } else {
                mViewPagerFragment.get().getPager().setCurrentItem(0);
            }
        }
    }

    private void setWhiteFilter(boolean show) {
        Fragment fragment = getVisibleFragment();
        if (fragment instanceof ActionBarAndEventsFragment ||
                fragment instanceof RecipeFragment ||
                fragment instanceof WidgetFragment ||
                fragment instanceof ReportStopReasonFragment ||
                fragment instanceof SelectStopReasonFragment) {

            setWhiteFilterViews(show);
        } else {
            setWhiteFilterViews(false);
        }

    }

    private void setWhiteFilterViews(boolean show) {
        if (findViewById(R.id.FAAE_white_filter) != null) {
            if (show && !BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
                findViewById(R.id.FAAE_white_filter).setVisibility(View.VISIBLE);
                setWidgetItemInPager();
            } else {
                findViewById(R.id.FAAE_white_filter).setVisibility(View.GONE);
            }
        }
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
            mActionBarAndEventsFragment.get().setWhiteFilter(show);
        }
    }

    private void setFilterWarningText(boolean show) {
        Fragment fragment = getVisibleFragment();
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
            if (fragment instanceof ActionBarAndEventsFragment ||
                    fragment instanceof RecipeFragment ||
                    fragment instanceof WidgetFragment ||
                    fragment instanceof ReportStopReasonFragment ||
                    fragment instanceof SelectStopReasonFragment) {
                mActionBarAndEventsFragment.get().setCycleWarningViewShow(show);
            } else {
                mActionBarAndEventsFragment.get().setCycleWarningViewShow(false);
            }
        }
    }

    @NonNull
    private MachineDataUICallback getMachineDataUICallback() {
        return new MachineDataUICallback() {

            @Override
            public void onDataReceivedSuccessfully(ArrayList<Widget> widgetList) {
                ProgressDialogManager.dismiss();
//                if (mSelectJobId != null) {
//                    PersistenceManager.getInstance().setJobId(mSelectJobId);
//                }
                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                        if (dashboardUICallbackListener.get() != null && dashboardUICallbackListener.get() != null) {
                            dashboardUICallbackListener.get().onMachineDataReceived(widgetList);
                        }
                    }
                } else {

                    OppAppLogger.w(TAG, " onDataReceivedSuccessfully() - DashboardUICallbackListener is null");
                }
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }

                if (!ChangeLang.isLanguageSetOk(DashboardActivity.this)) {//todo nathan check this function
                    refreshApp();
                }
            }

            @Override
            public void onDataReceiveFailed(StandardResponse reason) {
                OppAppLogger.i(TAG, "onDataReceivedSuccessfully() reason: " + reason.getError().getErrorDesc());

                if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                    for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                        if (dashboardUICallbackListener.get() != null && dashboardUICallbackListener.get() != null) {
                            dashboardUICallbackListener.get().onDataFailure(reason, DashboardUICallbackListener.CallType.MachineData);
                        }
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
                PersistenceManager.getInstance().setShiftStart(TimeUtils.getNoTFromDateString(shiftForMachineResponse.getStartTime(), shiftForMachineResponse.getTimeFormat()));
                if (shiftForMachineResponse.getStartTime() != null && shiftForMachineResponse.getStartTime().length() > 0
                        && shiftForMachineResponse.getDuration() > 0) {
                    PersistenceManager.getInstance().setShiftEnd(TimeUtils.getDate(shiftForMachineResponse.getDuration() + new Date().getTime(),
                            TimeUtils.SQL_NO_T_FORMAT));
                }
                if (durationOfShift > 0) {
                    startShiftTimer(durationOfShift);
                } else {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (DashboardActivity.this != null && !DashboardActivity.this.isDestroyed()) {
                                startShiftTimer(durationOfShift);
                            }
                        }
                    }, PersistenceManager.getInstance().getPollingFrequency() * 1000);
                }
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }

            @Override
            public void onGetShiftForMachineFailed(StandardResponse reason) {
                OppAppLogger.w(TAG, "get shift for machine failed with reason: " + reason.getError() + " " + reason.getError().getErrorDesc());
                ShowCrouton.showSimpleCrouton(DashboardActivity.this, reason);

                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }
        };
    }

    private void startShiftTimer(long timeInSeconds) {
        if (mTimeToEndCounter == null) {
            mTimeToEndCounter = new TimeToEndCounter(new OnTimeToEndChangedListener() {
                @Override
                public void onTimeToEndChanged(long millisUntilFinished) {
                    shiftForMachineTimer();
                }
            });
        }
        mTimeToEndCounter.calculateShiftToEnd(timeInSeconds);

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
//TODO kuti
//                    events = updateList(events);

                    for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                        if (dashboardUICallbackListener.get() != null && dashboardUICallbackListener.get() != null) {
                            dashboardUICallbackListener.get().onShiftLogDataReceived(events, mActualBarExtraResponse, mMachineJoshDataResponse);
                        }
                    }
                    if (ProgressDialogManager.isShowing()) {
                        ProgressDialogManager.dismiss();
                    }
                } else {
                    if (ProgressDialogManager.isShowing()) {
                        ProgressDialogManager.dismiss();
                    }
                    OppAppLogger.w(TAG, " shiftLogStartPolling() - DashboardUICallbackListener is null");
                }
            }

            @Override
            public void onGetShiftLogFailed(StandardResponse reason) {

                if (reason != null) {

                    OppAppLogger.i(TAG, "shiftLogStartPolling() reason: " + reason.getError().getErrorDesc());

                    if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {

                        for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                            if (dashboardUICallbackListener.get() != null && dashboardUICallbackListener.get() != null) {
                                dashboardUICallbackListener.get().onDataFailure(reason, DashboardUICallbackListener.CallType.ShiftLog);
                            }
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
                OppAppLogger.d(TAG, "onReportFieldsReceivedSuccessfully()");
                mReportFieldsForMachine = reportFieldsForMachine;
                if (mOnReportFieldsUpdatedCallbackListener != null && mOnReportFieldsUpdatedCallbackListener.get() != null) {
                    mOnReportFieldsUpdatedCallbackListener.get().onReportUpdatedSuccess();
                }
                if (mWidgetFragment != null && mWidgetFragment.get() != null) {
                    mWidgetFragment.get().setReportFieldForMachine(mReportFieldsForMachine);
                }
                PersistenceManager.getInstance().setTechnicianList(reportFieldsForMachine.getTechnicians());
            } else {
                OppAppLogger.w(TAG, "reportFieldsForMachine is null");
                if (mOnReportFieldsUpdatedCallbackListener != null && mOnReportFieldsUpdatedCallbackListener.get() != null) {
                    mOnReportFieldsUpdatedCallbackListener.get().onReportUpdateFailure();
                }
            }
            if (ProgressDialogManager.isShowing()) {
                ProgressDialogManager.dismiss();
            }
        }

        int mRetries = 0;
        final int mMaxRetries = 3;

        @Override
        public void onReportFieldsReceivedSFailure(StandardResponse reason) {
            OppAppLogger.i(TAG, "onReportFieldsReceivedSFailure() reason: " + reason.getError().getErrorDesc());
            if (mOnReportFieldsUpdatedCallbackListener != null && mOnReportFieldsUpdatedCallbackListener.get() != null) {
                mOnReportFieldsUpdatedCallbackListener.get().onReportUpdateFailure();
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

    private void showSetUpEndDialog() {
        if (mActiveJobsListForMachine != null) {
            mSetupEndDialog = new SetupEndDialog(this, mReportFieldsForMachine, mActiveJobsListForMachine);
            mSetupEndDialog.showNoProductionAlarm(this, new SetupEndDialog.SetupEndDialogListener() {
                @Override
                public void sendReport(int selectedReasonId, int selectedTechnicianId, ArrayList<RejectForMultipleRequest> rejectForMultipleRequests) {
                    mRejectForMultipleRequests = rejectForMultipleRequests;
                    sendSetupEndReport(selectedReasonId, selectedTechnicianId);
                }

                @Override
                public void onDismissSetupEndDialog() {
                    mSetupEndDialog = null;
                    if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
                        mActionBarAndEventsFragment.get().SetupEndDialogShow(false);
                    }
                }
            });
            if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
                mActionBarAndEventsFragment.get().SetupEndDialogShow(true);
            }
        }
    }

    @Override
    public void onShowCroutonRequest(String croutonMessage, int croutonDurationInMilliseconds,
                                     int viewGroup, CroutonCreator.CroutonType croutonType) {

    }

    @Override
    public void onShowCroutonRequest(SpannableStringBuilder croutonMessage,
                                     int croutonDurationInMilliseconds, int viewGroup, CroutonCreator.CroutonType croutonType) {
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
//
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    public void onFragmentAttached(WeakReference<DashboardUICallbackListener> dashboardUICallbackListener) {
        if (mDashboardUICallbackListenerList != null) {

            mDashboardUICallbackListenerList.add(dashboardUICallbackListener);
        }
    }

    @Override
    public void onFragmentDetached(WeakReference<DashboardUICallbackListener> dashboardUICallbackListener) {
        if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {
            for (WeakReference<DashboardUICallbackListener> listener : mDashboardUICallbackListenerList) {
                mDashboardUICallbackListenerList.remove(listener);
                break;
            }
        }
    }

    @Override
    public void goToFragment(Fragment fragment, boolean isCentralContainer,
                             boolean addToBackStack) {

        try {
            if (isCentralContainer) {

//                if (fragment instanceof ApproveFirstItemFragment) {
//                    getSupportFragmentManager().beginTransaction().add(R.id.fragments_container_dialog, fragment).addToBackStack(DASHBOARD_FRAGMENT).commit();
//                } else {
                getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), fragment).addToBackStack(DASHBOARD_FRAGMENT).commit();
//                }

            } else {

                getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, fragment).addToBackStack(DASHBOARD_FRAGMENT).commit();

            }
        } catch (IllegalStateException ignored) {
        }

    }

    @Override
    public void goToDashboardActivity(ArrayList<Machine> machines) {

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
                OppAppLogger.i(TAG, "onJobListReceived()");
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
            public void onJobListReceiveFailed(StandardResponse reason) {
                OppAppLogger.w(TAG, "onJobListReceiveFailed() " + reason.getError());
                mDashboardActivityToJobsFragmentCallback.onJobsListReceiveFailed();
                if (ProgressDialogManager.isShowing()) {
                    ProgressDialogManager.dismiss();
                }
            }

            @Override
            public void onStartJobSuccess() {
                OppAppLogger.i(TAG, "onStartJobSuccess()");

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
            public void onStartJobFailed(StandardResponse reason) {
                if (reason.getError().getErrorDesc().contains("Job started successfully")) {
                    ShowCrouton.jobsLoadingAlertCrouton(DashboardActivity.this, reason.getError().getErrorDesc());
                } else {
                    OppAppLogger.i(TAG, "onStartJobFailed()");
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

        ProgressDialogManager.show(DashboardActivity.this);

        dashboardDataStartPolling();

//        PersistenceManager.getInstance().setJobId(mSelectJobId);

        isNeedRecipeRefresh = true;
    }

    @Override
    public void onJobFragmentAttached(DashboardActivityToJobsFragmentCallback
                                              dashboardActivityToJobsFragmentCallback) {
        mDashboardActivityToJobsFragmentCallback = dashboardActivityToJobsFragmentCallback;
    }

    @Override
    public void getJobsForMachineList() {
        mJobsCore.getJobsListForMachine();

    }

    @Override
    public void startJobForMachine(int jobId) {
        Log.i(TAG, "startJobForMachine(), Job Id: " + jobId);
//        PersistenceManager.getInstance().setJobId(jobId);

//        mSelectJobId = jobId;


        mJobsCore.startJobForMachine(jobId, PersistenceManager.getInstance().getMachineLineId() < 1);

    }

    @Override
    public void onSelectedJobFragmentAttached(DashBoardActivityToSelectedJobFragmentCallback
                                                      dashboardActivityToSelectedJobFragmentCallback) {
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

        OppAppLogger.i(TAG, "onSetOperatorForMachineSuccess(), operator Id: " + operatorId + " operator name: " + operatorName);

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

    public void silentLoginFromDashBoard(
            final OnCroutonRequestListener onCroutonRequestListener,
            final SilentLoginCallback silentLoginCallback) {

        LoginCore.getInstance().silentLoginFromDashBoard(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getUserName(), PersistenceManager.getInstance().getPassword(), new LoginUICallback<Machine>() {
            @Override
            public void onLoginSucceeded(ArrayList<Machine> machines, String siteName) {

                OppAppLogger.d(TAG, "login, onGetMachinesSucceeded(),  go Next");
                silentLoginCallback.onSilentLoginSucceeded();
                PersistenceManager.getInstance().setSiteName(siteName);
                checkUpdateNotificationToken();

            }

            @Override
            public void onLoginFailed(StandardResponse reason) {
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
            OppAppLogger.e(TAG, "getCurrentFragment(), error: " + ex.getMessage());
            return null;
        }
    }


    public Fragment getVisibleFragment() {
        Fragment f = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
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
        OppAppLogger.w(TAG, "mReportFieldsForMachine is null");
        return null;
    }

    @Override
    public void updateReportRejectFields() {
        mReportFieldsForMachineCore.stopPolling();
        mReportFieldsForMachineCore.startPolling();
    }

    @Override
    public void onClearAppDataRequest() {
        Log.i(TAG, "onClearAppDataRequest() command received from settings screen");
        clearData();
        refreshApp();
        deleteCache(this);
    }

    @Override
    public void onChangeMachineRequest() {
        Log.i(TAG, "onChangeMachineRequest() command received from settings screen");

        mSelectMachineFragment = new WeakReference<>(SelectMachineFragment.newInstance());
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
            mActionBarAndEventsFragment.get().setVisiblefragment(mSelectMachineFragment.get());
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, mSelectMachineFragment.get()).commit();
        showReportBtn(false);
    }

    @Override
    public void onCheckForAppUpdates(boolean isImmediate) {
        setupVersionCheck(isImmediate);
    }

    @Override
    public void onOpenQCActivity(int id, boolean editMode) {
        Intent intent = new Intent(DashboardActivity.this, QCActivity.class);
        intent.putExtra(QC_TEST_ID, id);
        intent.putExtra(QC_EDIT_MODE, editMode);
        ignoreFromOnPause = true;

        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            mActionBarAndEventsFragment.get().setFromAnotherActivity(true);
        }
        startActivityForResult(intent, QC_ACTIVITY_RESULT_CODE);
    }

    @Override
    public void onRefreshMachineLinePolling() {
        dashboardDataStartPolling();
        ProgressDialogManager.show(this);
    }

    @Override
    public void onViewLog() {
        if (mCurrentMachineStatus == null) {
            return;
        }
        Intent intent = new Intent(this, StopEventLogActivity.class);
        intent.putExtra(EXTRA_FIELD_FOR_MACHINE, getReportForMachine());
        intent.putExtra(IS_REPORTING_ON_SETUP_EVENTS, mCurrentMachineStatus.isAllowReportingOnSetupEvents());
        intent.putExtra(IS_REPORTING_ON_SETUP_END, mCurrentMachineStatus.isAllowReportingSetupAfterSetupEnd());
        intent.putExtra(IS_SETUP_MODE, mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() == 0);
        startActivity(intent);
        ignoreFromOnPause = true;

        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            mActionBarAndEventsFragment.get().setFromAnotherActivity(true);
        }
    }


    private void clearData() {

        PostDeleteTokenRequest request = new PostDeleteTokenRequest(PersistenceManager.getInstance().getMachineId(), PersistenceManager.getInstance().getSessionId(), PersistenceManager.getInstance().getNotificationToken());
        NetworkManager.getInstance().postDeleteToken(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, retrofit2.Response<StandardResponse> response) {

            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {

            }
        });

        DataSupport.deleteAll(Event.class);

        String tmpLanguage = PersistenceManager.getInstance().getCurrentLang();
        String tmpLanguageName = PersistenceManager.getInstance().getCurrentLanguageName();

        PersistenceManager.getInstance().clear();

        PersistenceManager.getInstance().items.clear();

        PersistenceManager.getInstance().setCurrentLang(tmpLanguage);
        PersistenceManager.getInstance().setCurrentLanguageName(tmpLanguageName);

        OppAppLogger.i(TAG, "PersistenceManager cleared");
        clearInterfaces();
    }

    private void clearInterfaces() {
        //Cores clear
        if (mReportFieldsForMachineCore != null) {
            mReportFieldsForMachineCore.stopPolling();
            mReportFieldsForMachineCore.unregisterListener();
            OppAppLogger.i(TAG, "mReportFieldsForMachineCore cleared");
        }
        if (mAllDashboardDataCore != null) {
            mAllDashboardDataCore.stopPolling();
            mAllDashboardDataCore.unregisterListener();
            OppAppLogger.i(TAG, "mAllDashboardDataCore cleared");
        }

        if (mJobsCore != null) {
            mJobsCore.unregisterListener();
            OppAppLogger.i(TAG, "mJobsCore cleared");
        }

        if (mReportFieldsForMachineCore != null) {
            mReportFieldsForMachine = null;
            OppAppLogger.i(TAG, "mReportFieldsForMachine cleared");
        }
        //Interfaces clear
        if (mOnReportFieldsUpdatedCallbackListener != null) {
            mOnReportFieldsUpdatedCallbackListener = null;
            OppAppLogger.i(TAG, "mOnReportFieldsUpdatedCallbackListener cleared");
        }
        if (mDashboardActivityToJobsFragmentCallback != null) {
            mDashboardActivityToJobsFragmentCallback = null;
            OppAppLogger.i(TAG, "mDashboardActivityToJobsFragmentCallback cleared");
        }
        if (mDashboardActivityToSelectedJobFragmentCallback != null) {
            mDashboardActivityToSelectedJobFragmentCallback = null;
            OppAppLogger.i(TAG, "mDashboardActivityToSelectedJobFragmentCallback cleared");
        }
        if (mDashboardUICallbackListenerList != null && mDashboardUICallbackListenerList.size() > 0) {
            mDashboardUICallbackListenerList.clear();
            mDashboardUICallbackListenerList = null;
            OppAppLogger.i(TAG, "mDashboardUICallbackListenerList cleared");
        }
        if (mReportFieldsForMachineUICallback != null) {
            mReportFieldsForMachineUICallback = null;
            OppAppLogger.i(TAG, "mReportFieldsForMachineUICallback cleared");
        }
        if (mCroutonCreator != null) {
            mCroutonCreator = null;
            OppAppLogger.i(TAG, "mCroutonCreator cleared");

        }
    }

    @Override
    public void onRefreshReportFieldsRequest(OnReportFieldsUpdatedCallbackListener
                                                     onReportFieldsUpdatedCallbackListener) {
//        mOnReportFieldsUpdatedCallbackListener = onReportFieldsUpdatedCallbackListener;
//        mReportFieldsForMachineCore.stopPolling();
//        mReportFieldsForMachineCore.startPolling();
        getActiveJobs();

        onRefreshPolling();
        OppAppLogger.i(TAG, "onRefreshReportFieldsRequest() command received from settings screen");
    }

    @Override
    public void onRefreshApplicationRequest() {
        OppAppLogger.i(TAG, "onRefreshApplicationRequest() command received from settings screen");
        refreshApp();
    }

    private void refreshApp() {
        cleanEvents();
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

                Log.e(TAG, e.getMessage());
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
//        Fragment currentFragment = getCurrentFragment();
//        if (currentFragment != null && currentFragment instanceof CroutonRootProvider) {
//            return ((CroutonRootProvider) currentFragment).getCroutonRoot();
//        }
//        Fragment currentFragment = getCurrentFragment();
//        if (currentFragment != null && currentFragment instanceof CroutonRootProvider) {
//            return ((CroutonRootProvider) currentFragment).getCroutonRoot();
//        }
        Fragment visible = getVisibleFragment();
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null &&
                visible != null && (visible instanceof ActionBarAndEventsFragment
                || visible instanceof WidgetFragment
                || visible instanceof RecipeFragment
                || visible instanceof SelectStopReasonFragment
                || visible instanceof ReportStopReasonFragment)) {

            return mActionBarAndEventsFragment.get().getCroutonRoot();

        }
        return R.id.parent_layouts;
    }

    @Override
    public void onRefreshPollingRequest() {
        OppAppLogger.i(TAG, "onRefreshPollingRequest() command received from settings screen");

//        mAllDashboardDataCore.stopPolling();
//
//        NetworkManager.getInstance().clearPollingRequest();
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

            for (WeakReference<DashboardUICallbackListener> dashboardUICallbackListener : mDashboardUICallbackListenerList) {
                if (dashboardUICallbackListener.get() != null)
                    dashboardUICallbackListener.get().onApproveFirstItemEnabledChanged(false); // disable the button at least until next polling cycle
            }
        }
    }

    @Override
    public void onApproveFirstItemShowFilter(boolean b) {
        setBlackFilter(b);
    }

    @Override
    public void onRefreshPolling() {

        Log.d(DavidVardi.DAVID_TAG_SPRINT_1_5, "onRefreshPolling");

        dashboardDataStartPolling();

//        if (getSupportFragmentManager() != null) {
//
//            try {
//
//                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//            } catch (IllegalStateException ignored) {
//
//            }
//
//        }

//        ProgressDialogManager.dismiss();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ignoreFromOnPause = false;

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            SendBroadcast.SendEmail(this);

        }

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onCroutonDismiss() {

//        mActionBarAndEventsFragment.openNextDialog();
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            mActionBarAndEventsFragment.get().setAlertChecked();
        }
    }

    @Override
    public void onWidgetChangeState(boolean state) {
        if (mWidgetFragment != null && mWidgetFragment.get() != null) {
            mWidgetFragment.get().setWidgetState(state);
        }
    }

    @Override
    public void onWidgetUpdateSpane(boolean open) {
        mIsTimeLineOpen = open;
        if (mWidgetFragment != null && mWidgetFragment.get() != null) {
            mWidgetFragment.get().setSpanCount(open);
        }
        if (mSelectStopReasonFragment != null && mSelectStopReasonFragment.get() != null) {
            mSelectStopReasonFragment.get().setSpanCount(!open);
        }
        if (mReportStopReasonFragment != null && mReportStopReasonFragment.get() != null) {
            mReportStopReasonFragment.get().setSpanCount(!open);
        }
        if (mReportShiftFragment != null && mReportShiftFragment.get() != null) {
            mReportShiftFragment.get().setIsOpenState(!open);
        }

    }

    @Override
    public void onResize(int width, int statusBarsHeight) {

        ViewGroup.MarginLayoutParams layoutParams3 = (ViewGroup.MarginLayoutParams) mContainer3.getLayoutParams();

        layoutParams3.setMarginStart(width);

        layoutParams3.topMargin = statusBarsHeight;

        mContainer3.setLayoutParams(layoutParams3);

    }


    @Override
    public void onResizeBottomMargin(int bottomMargin) {
        ViewGroup.MarginLayoutParams layoutParams3 = (ViewGroup.MarginLayoutParams) mContainer3.getLayoutParams();

        layoutParams3.bottomMargin = bottomMargin;

        mContainer3.setLayoutParams(layoutParams3);
    }

    @Override
    public void onOpenTaskActivity() {
        clearPolling();

        Intent intent = new Intent(DashboardActivity.this, TaskActivity.class);
        ignoreFromOnPause = true;

        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            mActionBarAndEventsFragment.get().setFromAnotherActivity(true);
        }
        startActivityForResult(intent, TASK_ACTIVITY_RESULT_CODE);
    }

    private void clearPolling() {
        pollingBackup(false);
        if (mAllDashboardDataCore != null) {
            mAllDashboardDataCore.stopPolling();
            mAllDashboardDataCore.unregisterListener();
            mAllDashboardDataCore.stopTimer();
        }
        if (mReportFieldsForMachineCore != null) {
            mReportFieldsForMachineCore.stopPolling();
            mReportFieldsForMachineCore.unregisterListener();
        }
        NetworkManager.getInstance().clearPollingRequest();
    }

    @Override
    public void onActionBarAndEventsFragmentCreated() {

        if (PersistenceManager.getInstance().getMachineId() == -1 && !(getVisibleFragment() instanceof SelectMachineFragment)) {
            mSelectMachineFragment = new WeakReference<>(SelectMachineFragment.newInstance());
            if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
                mActionBarAndEventsFragment.get().setVisiblefragment(mSelectMachineFragment.get());
            }
            getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, mSelectMachineFragment.get()).commit();
            showReportBtn(false);
        }
    }

    @Override
    public void onLockScreenForceLoginOperator() {

        if (!(getVisibleFragment() instanceof SignInOperatorFragment || getVisibleFragment() instanceof SelectMachineFragment)) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, new SignInOperatorFragment()).addToBackStack("SignInOperatorFragment").commitAllowingStateLoss();
        }
    }

    @Override
    public void onOpenReportStopReasonFragment(ReportStopReasonFragment reportStopReasonFragment) {

        startReportModeTimer();

        mReportStopReasonFragment = new WeakReference<>(reportStopReasonFragment);

        if (mReportStopReasonFragment != null) {

            mReportStopReasonFragment.get().setSelectedEvents(mSelectedEvents);
        }

        try {
            List<Fragment> frags = getSupportFragmentManager().getFragments();
            for (Fragment frag : frags) {
                if (frag instanceof ReportStopReasonFragment) {
                    getSupportFragmentManager().beginTransaction().remove(frag).commit();
                }
            }
            getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), reportStopReasonFragment).commit();
        } catch (IllegalStateException ignored) {
        }

    }

    private void startReportModeTimer() {

        final int[] timeCounter = new int[1];
        if (mReportModeTimer != null) {
            mReportModeTimer.cancel();
        }
        mReportModeTimer = new Timer();
        mReportModeTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (DashboardActivity.this != null && !DashboardActivity.this.isDestroyed()) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (DashboardActivity.this != null && !DashboardActivity.this.isDestroyed()) {
                                if (timeCounter[0] == 60) {
                                    onClearAllSelectedEvents();
                                    mReportModeTimer.cancel();
                                    return;
                                }

                                timeCounter[0]++;
                            }
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    @Override
    public void onEventSelected(Float event, boolean checked) {

        if (mSelectedEvents == null) {

            mSelectedEvents = new ArrayList<Float>();
        }

        if (checked) {
            if (!mSelectedEvents.contains(event)) {
                mSelectedEvents.add(event);
            }
        } else {

            ArrayList<Float> toDelete = new ArrayList<>();
            for (Float event1 : mSelectedEvents) {
                if (event.compareTo(event1) == 0) {
                    toDelete.add(event1);
                }
            }
            for (Float event1 : toDelete) {
                mSelectedEvents.remove(event1);
            }
        }

        if (mSelectStopReasonFragment != null && mSelectStopReasonFragment.get() != null) {

            mSelectStopReasonFragment.get().setSelectedEvents(mSelectedEvents);
        }
        if (mReportStopReasonFragment != null && mReportStopReasonFragment.get() != null) {

            mReportStopReasonFragment.get().setSelectedEvents(mSelectedEvents);
        }
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            mActionBarAndEventsFragment.get().setSelectedEvents(mSelectedEvents);
        }
        if (mSelectedEvents.size() == 0) {
            if (mSelectStopReasonFragment != null) {

                onBackPressed();
            }
            if (mReportStopReasonFragment != null) {

                onBackPressed();
            }
        } else {
            startReportModeTimer();
        }
    }

    @Override
    public void onClearAllSelectedEvents() {

        mSelectedEvents = null;

        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            mActionBarAndEventsFragment.get().disableSelectMode();
        }

        if (mReportStopReasonFragment != null) {

            removeReportStopReasonFragment();

        }
        if (mSelectStopReasonFragment != null) {

            removeSelectStopReasonFragment();

        }
    }

    @Override
    public void onJobActionItemClick() {

        startPendingJobsActivity();
    }


    public void startPendingJobsActivity() {
        Intent intent = new Intent(DashboardActivity.this, ActivateJobActivity.class);

        if (mCurrentMachineStatus != null && mCurrentMachineStatus.getAllMachinesData() != null
                && mCurrentMachineStatus.getAllMachinesData().size() > 0) {
            intent.putExtra(EXTRA_LAST_JOB_ID, mCurrentMachineStatus.getAllMachinesData().get(0).getLastJobId());
            intent.putExtra(EXTRA_LAST_ERP_JOB_ID, mCurrentMachineStatus.getAllMachinesData().get(0).getLastErpJobId());
            intent.putExtra(EXTRA_LAST_PRODUCT_NAME, mCurrentMachineStatus.getAllMachinesData().get(0).getLastProductName());
            intent.putExtra(EXTRA_IS_NO_PRODUCTION, mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() > 1);
        }

        startActivityForResult(intent, ActivateJobActivity.EXTRA_ACTIVATE_JOB_CODE);

        ignoreFromOnPause = true;

        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            mActionBarAndEventsFragment.get().setFromAnotherActivity(true);
        }
    }

    @Override
    public void onJoshProductSelected(Integer spinnerProductPosition, ActiveJob
            selectedJob, String jobName) {

        mSpinnerProductPosition = spinnerProductPosition;

        mSelectProductJobId = selectedJob.getJobID();
        mSelectProductJoshId = selectedJob.getJoshID();

        dashboardDataStartPolling();

        ProgressDialogManager.show(this);
    }

    @Override
    public void onProductionStatusChanged(final int productionModeId, final String newStatus, final List<MachinesLineDetail> machineLineItems) {

        MachinesLineDetail current = null;
        for (MachinesLineDetail machinesLineDetail : machineLineItems) {
            if (machinesLineDetail.getMachineID() == PersistenceManager.getInstance().getMachineId()) {
                current = machinesLineDetail;
            }
        }
        if (current != null) {
            machineLineItems.remove(current);
        }

        if (machineLineItems != null && machineLineItems.size() > 0) {

            TitleAndSubWithSelectableListDialog titleAndSubWithSelectableListDialog = new TitleAndSubWithSelectableListDialog(
                    new TitleAndSubWithSelectableListDialog.TitleAndSubWithSelectableListDialogListener() {
                        @Override
                        public void onClickPositiveBtn(ArrayList<MachinesLineDetail> machinesLineDetails) {
                            ProgressDialogManager.show(DashboardActivity.this);
                            postProductionMode(productionModeId, PersistenceManager.getInstance().getMachineId());
                            for (MachinesLineDetail machinesLineDetail : machinesLineDetails) {
                                postProductionMode(productionModeId, machinesLineDetail.getMachineID());
                            }
                        }

                        @Override
                        public void onClickNegativeBtn() {
                            ProgressDialogManager.show(DashboardActivity.this);
                            postProductionMode(productionModeId, PersistenceManager.getInstance().getMachineId());
                        }
                    }, getString(R.string.production_status),
                    String.format("%s:", getString(R.string.update_this_production_status_also_to)),
                    getString(R.string.apply), (ArrayList<MachinesLineDetail>) machineLineItems
            );
            android.app.AlertDialog dialog = titleAndSubWithSelectableListDialog.showTitleAndSubWithSelectableListDialog(getApplicationContext());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
                        mActionBarAndEventsFragment.get().setProductionStatusVisible();
                    }
                }
            });
        } else {
            ProgressDialogManager.show(DashboardActivity.this);
            postProductionMode(productionModeId, PersistenceManager.getInstance().getMachineId());
        }

        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
            mActionBarAndEventsFragment.get().blockStatusSpinner();
        }
    }

    public void postProductionMode(int productionModeId, int machineId) {
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        SimpleRequests simpleRequests = new SimpleRequests();
        simpleRequests.postProductionMode(persistenceManager.getSiteUrl(), new PostProductionModeCallback() {
            @Override
            public void onPostProductionModeSuccess(StandardResponse response) {
                // TODO: 31/07/2018 display crouton
                dashboardDataStartPolling();
                if (response.getFunctionSucceed()) {
                    //Analytics
                    new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.PRODUCTION_STATUS, true, "Production Status Changed");
                } else {
                    ShowCrouton.showSimpleCrouton(DashboardActivity.this, response.getError().getErrorDesc(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);

                }
            }

            @Override
            public void onPostProductionModeFailed(StandardResponse reason) {
                ProgressDialogManager.dismiss();
                // TODO: 31/07/2018 set error message
                ShowCrouton.showSimpleCrouton(DashboardActivity.this, reason.getError().getErrorDesc(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);
                //Analytics
                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.PRODUCTION_STATUS, false, "Error: " + reason.getError().getErrorDesc());
                dashboardDataStartPolling();
            }
        }, NetworkManager.getInstance(), new SetProductionModeForMachineRequest(persistenceManager.getSessionId(), machineId, productionModeId), persistenceManager.getTotalRetries());
    }

    @Override
    public void onLenoxMachineClicked(Machine machine) {

        SaveAlarmsHelper.saveAlarmsCheckedLocaly(this);

        PersistenceManager.getInstance().setShiftLogStartingFrom(com.operatorsapp.utils.TimeUtils.getDate(System.currentTimeMillis() - DAY_IN_MILLIS, "yyyy-MM-dd HH:mm:ss.SSS"));

        DataSupport.deleteAll(Event.class);

        onClearAllSelectedEvents();

        setLenoxMachine(machine.getId());
    }

    @Override
    public void onLastShiftItemUpdated() {

        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showBlackFilter(boolean show) {
//        if (mSetupEndDialog == null) {
        setBlackFilter(show);
//        }
    }

    @Override
    public void showWhiteFilter(boolean show) {
        setWhiteFilter(show);
    }

    @Override
    public void onShowSetupEndDialog() {
        showSetUpEndDialog();
    }

    @Override
    public void onTechnicianCalled() {
        dashboardDataStartPolling();
    }

    @Override
    public void setCycleWarningView(boolean cycleWarningViewShow) {
        if (mViewPagerFragment != null && mViewPagerFragment.get() != null) {
            mViewPagerFragment.get().setCycleWarningView(cycleWarningViewShow);
        }
    }

    @Override
    public void resetCycleWarningView(boolean wasShow, boolean show) {
        if (mViewPagerFragment != null && mViewPagerFragment.get() != null) {
            mViewPagerFragment.get().resetCycleWarningView(wasShow, show);
        }
    }

    public void setLenoxMachine(int machineId) {

        showLoadingDialog();

        PersistenceManager.getInstance().setMachineId(machineId);

        dashboardDataStartPolling();
    }

    private void showLoadingDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.loading_data));

        builder.setCancelable(false);

        mLoadingDialog = builder.create();

        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }


    @Override
    public void onSplitEventPressed(Float eventID) {
        // TODO: 05/07/2018 call server
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        SimpleRequests simpleRequests = new SimpleRequests();
        simpleRequests.postSplitEvent(persistenceManager.getSiteUrl(), new PostSplitEventCallback() {
            @Override
            public void onPostSplitEventSuccess(StandardResponse response) {
                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.SPLIT_STOP_EVENT, true, "Split stop event");
                ShowCrouton.jobsLoadingSuccessCrouton(DashboardActivity.this, getString(R.string.split_event_success));
                dashboardDataStartPolling();
            }

            @Override
            public void onPostSplitEventFailed(StandardResponse reason) {
                String msg = reason.getError().getErrorDesc();
                if (msg == null || msg.equals("")) {
                    msg = getString(R.string.split_event_failed);
                }
                ShowCrouton.jobsLoadingAlertCrouton(DashboardActivity.this, msg);
                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.SPLIT_STOP_EVENT, false, "Split stop event- " + msg);
            }
        }, NetworkManager.getInstance(), new SplitEventRequest(persistenceManager.getSessionId(), String.valueOf(eventID)), persistenceManager.getTotalRetries(), persistenceManager.getRequestTimeout());

    }

    @Override
    public void onOpenSelectStopReasonFragmentNew(SelectStopReasonFragment
                                                          selectStopReasonFragment, boolean isFromViewLogRoot) {

        mSelectStopReasonFragment = new WeakReference<>(selectStopReasonFragment);

        try {

            getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), selectStopReasonFragment).commit();
        } catch (IllegalStateException ignored) {
        }

        if (mSelectStopReasonFragment != null) {

            mSelectStopReasonFragment.get().setSelectedEvents(mSelectedEvents);

        }

    }

    @Override
    public void onReport(int position, StopReasonsGroup mSelectedSubreason) {

    }


    @Override
    public void onSuccess() {

    }

    @Override
    public void onBackPressed() {

        Fragment visible = getVisibleFragment();

        if (mSelectMachineFragment != null && mSelectMachineFragment.get() != null) {
            if (mSelectMachineFragment.get().isMultiSelectMode()) {
                mSelectMachineFragment.get().initView();
                return;
            }
            if (PersistenceManager.getInstance().getMachineId() == -1) {
                return;
            }
            onCloseSelectMachine();
            return;
        }
        if (SingleLineKeyboard.isKeyBoardOpen) {
            if (mWidgetFragment != null && mWidgetFragment.get() != null) {
                mWidgetFragment.get().onCloseKeyboard();
            }
            SingleLineKeyboard.isKeyBoardOpen = false;
        } else if (mReportStopReasonFragment != null || mSelectStopReasonFragment != null) {

            if (mReportStopReasonFragment != null && mSelectStopReasonFragment == null) {

                removeReportStopReasonFragment();

            }
            if (mSelectStopReasonFragment != null) {

                removeSelectStopReasonFragment();

            }

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

        } else {
            super.onBackPressed();
        }

    }

    private void removeSelectStopReasonFragment() {

        try {
            if (mSelectStopReasonFragment.get() != null) {
                getSupportFragmentManager().beginTransaction().remove(mSelectStopReasonFragment.get()).commit();

                mSelectStopReasonFragment = null;
            }
        } catch (IllegalStateException ignored) {
        }

    }

    private void removeReportStopReasonFragment() {

        try {

            if (mReportStopReasonFragment.get() != null) {
                getSupportFragmentManager().beginTransaction().remove(mReportStopReasonFragment.get()).commit();

                mReportStopReasonFragment = null;

                if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

                    mActionBarAndEventsFragment.get().disableSelectMode();
                }
                if (mSelectedEvents != null) {
                    mSelectedEvents = null;
                }
            }
        } catch (IllegalStateException ignored) {
        }
    }

    @Override
    public void onViewPagerCreated() {

        getAllRecipes(PersistenceManager.getInstance().getJobId(), false, false);
    }

    @Override
    public void onRecipeFragmentShown() {
        if (mRecipeFragment != null && mRecipeFragment.get() != null) {
            mRecipeFragment.get().showProgress(true);
        }
        getAllRecipes(PersistenceManager.getInstance().getJobId(), true, false);
    }

    private void getAllRecipes(Integer jobId, final boolean isUpdate, final boolean isRefresh) {

        isNeedRecipeRefresh = false;
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
                    public void onGetAllRecipeFailed(StandardResponse reason) {

                        if (!isUpdate) {
                            addFragmentsToViewPager(null);
                        } else if (isRefresh && mRecipeFragment != null && mRecipeFragment.get() != null) {
                            mRecipeFragment.get().updateRecipeResponse(null, reason);
                        }
                    }
                }, NetworkManager.getInstance(), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());

    }

    private void getIntervalAndTimeout() {

        if (PersistenceManager.getInstance().getVersion() < MINIMUM_VERSION_FOR_INTERVAL_AND_TIME_OUT_FROM_API) {
            return;
        }
        SimpleRequests simpleRequests = new SimpleRequests();

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        simpleRequests.getIntervalAndTimeout(persistanceManager.getSiteUrl(),
                persistanceManager.getSessionId(), new GetVersionCallback() {
                    @Override
                    public void onGetVersionSuccess(Object response) {

                        setIntervalAndTimeOut((IntervalAndTimeOutResponse) response);
                    }

                    @Override
                    public void onGetVersionFailed(StandardResponse reason) {

                    }

                }, NetworkManager.getInstance(), persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());

    }

    public void setIntervalAndTimeOut(IntervalAndTimeOutResponse response) {
        int poll = PersistenceManager.getInstance().getPollingFrequency();
        int pollNew;
        if (Integer.parseInt(response.getPollingIntervalData().get(0).getValue()) > 0) {
            if (response.getPollingIntervalData().get(0).getKey().equals(INTERVAL_KEY)) {
                pollNew = Integer.parseInt(response.getPollingIntervalData().get(0).getValue());
                PersistenceManager.getInstance().setPolingFrequency(pollNew);
                if (poll != pollNew) {
                    onRefreshPollingRequest();
                }
            }
            if (response.getPollingIntervalData().get(0).getKey().equals(TIME_OUT_KEY)) {
                PersistenceManager.getInstance().setRequestTimeOut(Integer.parseInt(response.getPollingIntervalData().get(0).getValue()));
            }
        }
        if (Integer.parseInt(response.getPollingIntervalData().get(1).getValue()) > 0) {
            if (response.getPollingIntervalData().get(1).getKey().equals(INTERVAL_KEY)) {
                pollNew = Integer.parseInt(response.getPollingIntervalData().get(1).getValue());
                PersistenceManager.getInstance().setPolingFrequency(pollNew);
                if (poll != pollNew) {
                    onRefreshPollingRequest();
                }
            }
            if (response.getPollingIntervalData().get(1).getKey().equals(TIME_OUT_KEY)) {
                PersistenceManager.getInstance().setRequestTimeOut(Integer.parseInt(response.getPollingIntervalData().get(1).getValue()));
            }
        }
    }

    private void addFragmentsToViewPager(RecipeResponse response) {

        if (mViewPagerFragment != null && mViewPagerFragment.get() != null) {
            WidgetFragment widgetFragment = initWidgetFragment();
            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {

                showRecipeFragment(response);

                mViewPagerFragment.get().addFragment(widgetFragment);

            } else {

                mViewPagerFragment.get().addFragment(widgetFragment);

                showRecipeFragment(response);

            }
        }
    }

    private void showRecipeFragment(RecipeResponse recipeResponse) {

        if (mViewPagerFragment != null && mViewPagerFragment.get() != null) {

            if (mRecipeFragment == null) {

//                recipeResponse.setPermission(WidgetInfo.getWidgetInfo(permissionForMachineHashMap,WidgetInfo.PermissionId.ENABLE_EDIT_JOB_RECIPE.getId()).getHaspermissionBoolean());
                mRecipeFragment = new WeakReference<>(RecipeFragment.newInstance(recipeResponse));

                mViewPagerFragment.get().addFragment(mRecipeFragment.get());

            } else {

                mRecipeFragment.get().updateRecipeResponse(recipeResponse, null);
            }
        }
    }

    @Override
    public void onImageProductClick(List<String> fileUrl, String name) {

        startGalleryActivity(fileUrl, name);
    }

    @Override
    public void onRefreshRecipe() {
        dashboardDataStartPolling();
        getAllRecipes(PersistenceManager.getInstance().getJobId(), true, true);
    }

    @Override
    public void onShowCrouton(String string, CroutonCreator.CroutonType type) {
        ShowCrouton.showSimpleCrouton(this, string, type);
    }

    private void startGalleryActivity(List<String> fileUrl, String name) {

        if (fileUrl != null && fileUrl.size() > 0) {

            mGalleryIntent = new Intent(DashboardActivity.this, GalleryActivity.class);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_FILE_URL, (ArrayList<String>) fileUrl);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_FILES_TITLE, name);

            mGalleryIntent.putExtra(GalleryActivity.EXTRA_RECIPE_PDF_FILES, mPdfList);

            startActivityForResult(mGalleryIntent, GalleryActivity.EXTRA_GALLERY_CODE);

            ignoreFromOnPause = true;

            if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

                mActionBarAndEventsFragment.get().setFromAnotherActivity(true);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mGalleryIntent = null;
        Log.d(TAG, "ChangeLang: ");
        ChangeLang.initLanguage(this);

        ignoreFromOnPause = true;
        if (resultCode == RESULT_OK && requestCode == GalleryActivity.EXTRA_GALLERY_CODE) {

            mPdfList = data.getParcelableArrayListExtra(GalleryActivity.EXTRA_RECIPE_PDF_FILES);

        }

        if (requestCode == QC_ACTIVITY_RESULT_CODE || requestCode == TASK_ACTIVITY_RESULT_CODE) {

            if (resultCode == RESULT_OK && requestCode == QC_ACTIVITY_RESULT_CODE) {
                ShowCrouton.showSimpleCrouton(this, getString(R.string.send_test_success), CroutonCreator.CroutonType.SUCCESS);
            }
            pollingBackup(true);
            dashboardDataStartPolling();
            mReportFieldsForMachineCore.startPolling();
            mReportFieldsForMachineCore.registerListener(mReportFieldsForMachineUICallback);
        }

        if (resultCode == RESULT_OK && requestCode == ActivateJobActivity.EXTRA_ACTIVATE_JOB_CODE) {

            Object response = data.getParcelableExtra(ActivateJobActivity.EXTRA_ACTIVATE_JOB_RESPONSE);

            if (((StandardResponse) response).getError().getErrorDesc() == null) {

                activateJob(Integer.parseInt(data.getStringExtra(ActivateJobActivity.EXTRA_ACTIVATE_JOB_ID)));
            }
        }

        if (resultCode == RESULT_OK && requestCode == TECH_CALL_ACTIVITY_RESULT_CODE) {
            if (data.getBooleanExtra(TechCallActivity.EXTRA_IS_MACHINE_CHANGED, false)) {
                mAllDashboardDataCore.startPolling();
            }
        }

    }

    private void activateJob(int jobId) {
//        mSelectJobId = jobId;

        startJob();

        ShowCrouton.jobsLoadingSuccessCrouton(DashboardActivity.this, getString(R.string.start_job_success));

//        dashboardDataStartPolling();
    }

    @Override
    public void onIgnoreOnPauseFromAdvancedSettings() {

        ignoreFromOnPause = true;
    }

    @Override
    public void onShowCrouton(String errorResponse, boolean isError) {

        if (isError) {
            if (errorResponse == null || errorResponse.length() == 0) {
                errorResponse = " ";
            }
            StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, errorResponse);
            ShowCrouton.showSimpleCrouton(DashboardActivity.this, errorObject);
        } else {
            if (errorResponse == null || errorResponse.length() == 0) {
                errorResponse = getString(R.string.success);
            }
            ShowCrouton.showSimpleCrouton(DashboardActivity.this, errorResponse, CroutonCreator.CroutonType.SUCCESS);
        }


    }

    @Override
    public void onExecuteJob(JobBase.OnJobFinishedListener onJobFinishedListener) {

        mOnJobFinishedListener = onJobFinishedListener;

        getActiveJobs();
    }

    @Override
    public void onNoInternetConnection() {
        Toast.makeText(this, R.string.no_connection_msg, Toast.LENGTH_SHORT).show();
//        onShowCrouton(getString(R.string.no_connection_msg), true);
    }

    @Override
    public void onCounterPressedInCentralDashboardContainer(int count) {
        PostIncrementCounterRequest request = new PostIncrementCounterRequest(PersistenceManager.getInstance().getMachineId(), PersistenceManager.getInstance().getSessionId());
        NetworkManager.getInstance().postIncrementCounter(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, retrofit2.Response<StandardResponse> response) {
                onRefreshPollingRequest();
                mCroutonCreator.showCrouton(DashboardActivity.this, getString(R.string.incremented_successfully), 0, getCroutonRoot(), CroutonCreator.CroutonType.SUCCESS);
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
                mCroutonCreator.showCrouton(DashboardActivity.this, getString(R.string.incremented_failure), 0, getCroutonRoot(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);
            }
        });
    }

    @Override
    public void onReportReject(String value, boolean isUnit, int selectedCauseId,
                               int selectedReasonId) {

        sendRejectReport(value, isUnit, selectedCauseId, selectedReasonId);
    }

    @Override
    public void onScrollToPosition(int position) {
        if (mWidgetFragment != null && mWidgetFragment.get() != null) {

            mWidgetFragment.get().scrollToPosition(position);
        }
    }

    @Override
    public void onReportCycleUnit(String value) {
        if (value == null || value.equals("")) {
            value = "0";
        }
        sendCycleUnitReport(Double.parseDouble(value));
    }

    @Override
    public void onOpenPendingJobs() {
        startPendingJobsActivity();
    }

    @Override
    public void onEndSetup() {
        onShowSetupEndDialog();
    }

    @Override
    public void onReportStopEvent() {
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
            mActionBarAndEventsFragment.get().startSelectMode(null, null);
        }
    }

    @Override
    public void onKeyboardEvent(boolean isOpen) {
        mCustomKeyBoardIsOpen = isOpen;
    }

    private void sendCycleUnitReport(double value) {
        ProgressDialogManager.show(this);
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.inject(NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackListener);
        OppAppLogger.i(TAG, "sendRejectReport units value is: " + String.valueOf(value) + " JobId: " + mSelectProductJobId);

        mReportCore.sendCycleUnitsReport(value, mSelectProductJobId);
    }

    private void sendRejectReport(String value, boolean isUnit, int selectedCauseId, int selectedReasonId) {
        if (mActiveJobsListForMachine == null || mActiveJobsListForMachine.getActiveJobs() == null) {
            return;
        }
        mSendRejectObject = new SendRejectObject(value, isUnit, selectedCauseId, selectedReasonId);
        ProgressDialogManager.show(this);
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.inject(NetworkManager.getInstance(), NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportRejectsCallbackListener);
        if (value.isEmpty()) {
            ShowCrouton.showSimpleCrouton(DashboardActivity.this, getResources().getString(R.string.invalid_value), CroutonCreator.CroutonType.NETWORK_ERROR);
            return;
        }
        try {
            if (isUnit) {
                mReportCore.sendReportReject(selectedReasonId, selectedCauseId, Double.parseDouble(value), (double) 0, mActiveJobsListForMachine.getActiveJobs().get(mSpinnerProductPosition).getJoshID());
            } else {
                mReportCore.sendReportReject(selectedReasonId, selectedCauseId, (double) 0, Double.parseDouble(value), mActiveJobsListForMachine.getActiveJobs().get(mSpinnerProductPosition).getJoshID());
            }
        } catch (NumberFormatException e) {
            ShowCrouton.showSimpleCrouton(DashboardActivity.this, getResources().getString(R.string.invalid_value), CroutonCreator.CroutonType.NETWORK_ERROR);
        } catch (IndexOutOfBoundsException e) {
            ShowCrouton.showSimpleCrouton(DashboardActivity.this,
                    getResources().getString(R.string.josh) + " " + getResources().getString(R.string.invalid_value), CroutonCreator.CroutonType.NETWORK_ERROR);
        }
//        SendBroadcast.refreshPolling(getContext());
    }

    private void sendMultipleRejectReport
            (ArrayList<RejectForMultipleRequest> rejectForMultipleRequests) {
        SimpleRequests simpleRequests = new SimpleRequests();
        PersistenceManager persistenceManager = PersistenceManager.getInstance();
        simpleRequests.reportMultipleRejects(persistenceManager.getSiteUrl(), new SimpleCallback() {
            @Override
            public void onRequestSuccess(StandardResponse response) {
                StandardResponse StandardResponse = objectToNewError(response);
                OppAppLogger.i(TAG, "sendMultipleReportSuccess()");
                if (StandardResponse.getFunctionSucceed()) {
                    ShowCrouton.showSimpleCrouton(DashboardActivity.this, StandardResponse.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
                } else {
                    ShowCrouton.showSimpleCrouton(DashboardActivity.this, StandardResponse.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
                }
                mRejectForMultipleRequests = null;

            }

            @Override
            public void onRequestFailed(StandardResponse reason) {
                mRejectForMultipleRequests = null;
                ShowCrouton.showSimpleCrouton(DashboardActivity.this, reason.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
            }
        }, NetworkManager.getInstance(), new MultipleRejectRequestModel(persistenceManager.getSessionId(),
                rejectForMultipleRequests), persistenceManager.getTotalRetries(), persistenceManager.getRequestTimeout());
    }

    private void sendSetupEndReport(int selectedReasonId, int selectedTechnicianId) {
        mSelectedReasonId = selectedReasonId;
        mSelectedTechnicianId = selectedTechnicianId;
        ProgressDialogManager.show(this);
        ReportNetworkBridge reportNetworkBridge = new ReportNetworkBridge();
        reportNetworkBridge.injectApproveFirstItem(NetworkManager.getInstance());
        mReportCore = new ReportCore(reportNetworkBridge, PersistenceManager.getInstance());
        mReportCore.registerListener(mReportCallbackEndSetupListener);
        mReportCore.sendApproveFirstItem(selectedReasonId, selectedTechnicianId, mSelectProductJobId);

    }


    ReportCallbackListener mReportCallbackListener = new ReportCallbackListener() {

        @Override
        public void sendReportSuccess(StandardResponse errorResponse) {
            StandardResponse response = objectToNewError(errorResponse);
//            ProgressDialogManager.dismiss();
            OppAppLogger.i(TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();

            String text = "";
            text = response.getError().getErrorDesc();
            if (response.getFunctionSucceed()) {
//                if (mReportCycleUnitValues[0].equals(mReportCycleUnitValues[1])) {
                ShowCrouton.showSimpleCrouton(DashboardActivity.this, text, CroutonCreator.CroutonType.SUCCESS);
            } else {
                ShowCrouton.showSimpleCrouton(DashboardActivity.this, text, CroutonCreator.CroutonType.NETWORK_ERROR);
            }
//            else if (Double.parseDouble(mReportCycleUnitValues[0]) > Double.parseDouble(mReportCycleUnitValues[1])) {
//                    text = String.format("%s %s %s %s", getString(R.string.report_cycle_failed_but_max_reported), getString(R.string.reported),
//                            getString(R.string.max_value_is), mReportCycleUnitValues[1]);
//                    ShowCrouton.showSimpleCrouton(DashboardActivity.this, text, CroutonCreator.CroutonType.NETWORK_ERROR);
//                } else if (Double.parseDouble(mReportCycleUnitValues[0]) < Double.parseDouble(mReportCycleUnitValues[1])) {
//                    text = String.format("%s %s %s %s", getString(R.string.report_cycle_failed_but_min_reported), getString(R.string.reported),
//                            getString(R.string.min_value_is), mReportCycleUnitValues[1]);
//                    ShowCrouton.showSimpleCrouton(DashboardActivity.this, text, CroutonCreator.CroutonType.NETWORK_ERROR);
//                }
//            } else {
//                ShowCrouton.showSimpleCrouton(DashboardActivity.this, response.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
//            }

            SendBroadcast.refreshPolling(DashboardActivity.this);

        }

        @Override
        public void sendReportFailure(StandardResponse reason) {
            ProgressDialogManager.dismiss();
            OppAppLogger.w(TAG, "sendReportFailure()");
            if (reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Credentials_mismatch) {
                silentLoginFromDashBoard(DashboardActivity.this, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendRejectReport(mSendRejectObject.getValue(), mSendRejectObject.isUnit(), mSendRejectObject.getSelectedCauseId(), mSendRejectObject.getSelectedReasonId());
                    }

                    @Override
                    public void onSilentLoginFailed(StandardResponse reason) {
                        OppAppLogger.w(TAG, "Failed silent login");
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
                        ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this, errorObject);
                        ProgressDialogManager.dismiss();
                    }
                });
            } else {

                StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, reason.getError().getErrorDesc());
                ShowCrouton.showSimpleCrouton(DashboardActivity.this, errorObject.getError().getErrorDesc(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);

            }
            SendBroadcast.refreshPolling(DashboardActivity.this);
        }
    };

    ReportCallbackListener mReportRejectsCallbackListener = new ReportCallbackListener() {

        @Override
        public void sendReportSuccess(StandardResponse errorResponse) {
            StandardResponse response = objectToNewError(errorResponse);
//            ProgressDialogManager.dismiss();
            OppAppLogger.i(TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();

            if (response.getFunctionSucceed()) {
                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.REJECT_REPORT, true, "Report Reject");

                ShowCrouton.showSimpleCrouton(DashboardActivity.this, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
            } else {
                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.REJECT_REPORT, false, "Report Reject- " + response.getError().getErrorDesc());

                ShowCrouton.showSimpleCrouton(DashboardActivity.this, response.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
            }

            SendBroadcast.refreshPolling(DashboardActivity.this);

        }

        @Override
        public void sendReportFailure(StandardResponse reason) {
            ProgressDialogManager.dismiss();
            OppAppLogger.w(TAG, "sendReportFailure()");
            if (reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Credentials_mismatch) {
                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.REJECT_REPORT, false, "Report Reject- Credentials_mismatch");
                silentLoginFromDashBoard(DashboardActivity.this, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendRejectReport(mSendRejectObject.getValue(), mSendRejectObject.isUnit(), mSendRejectObject.getSelectedCauseId(), mSendRejectObject.getSelectedReasonId());
                    }

                    @Override
                    public void onSilentLoginFailed(StandardResponse reason) {
                        OppAppLogger.w(TAG, "Failed silent login");
                        StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
                        ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this, errorObject);
                        ProgressDialogManager.dismiss();
                    }
                });
            } else {
                StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, reason.getError().getErrorDesc());
                ShowCrouton.showSimpleCrouton(DashboardActivity.this, errorObject.getError().getErrorDesc(), CroutonCreator.CroutonType.CREDENTIALS_ERROR);
                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.REJECT_REPORT, false, "Report Reject- " + errorObject.getError().getErrorDesc());
            }
            SendBroadcast.refreshPolling(DashboardActivity.this);
        }
    };

    ReportCallbackListener mReportCallbackEndSetupListener = new ReportCallbackListener() {
        @Override
        public void sendReportSuccess(StandardResponse o) {//TODO crouton error
            StandardResponse response = objectToNewError(o);
//            SendBroadcast.refreshPolling(DashboardActivity.this);
            dashboardDataStartPolling();
//            ProgressDialogManager.dismiss();

            if (response.getFunctionSucceed()) {
                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.END_SETUP, true, "End setup");
                if (mRejectForMultipleRequests != null && mRejectForMultipleRequests.size() > 0) {
                    sendMultipleRejectReport(mRejectForMultipleRequests);
                } else {
                    ShowCrouton.showSimpleCrouton(DashboardActivity.this, response.getError().getErrorDesc(), CroutonCreator.CroutonType.SUCCESS);
                    mRejectForMultipleRequests = null;
                }
            } else {
                new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.END_SETUP, false, "End setup");
                ShowCrouton.showSimpleCrouton(DashboardActivity.this, response.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
                mRejectForMultipleRequests = null;
            }
            OppAppLogger.i(TAG, "sendReportSuccess()");
            mReportCore.unregisterListener();
            onApproveFirstItemComplete();

        }

        @Override
        public void sendReportFailure(StandardResponse reason) {
            ProgressDialogManager.dismiss();
            OppAppLogger.w(TAG, "sendReportFailure()");
            mRejectForMultipleRequests = null;
            if (reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Credentials_mismatch) {
                silentLoginFromDashBoard(DashboardActivity.this, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {
                        sendSetupEndReport(mSelectedReasonId, mSelectedTechnicianId);
                    }

                    @Override
                    public void onSilentLoginFailed(StandardResponse reason) {
                        OppAppLogger.w(TAG, "Failed silent login");
                        StandardResponse errorObject = new StandardResponse(ErrorObjectInterface.ErrorCode.Missing_reports, "missing reports");
                        ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this, errorObject);
                        ProgressDialogManager.dismiss();
                    }
                });
            } else {

                StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Missing_reports, "missing reports");
                ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this, errorObject);
            }
            new GoogleAnalyticsHelper().trackEvent(DashboardActivity.this, GoogleAnalyticsHelper.EventCategory.END_SETUP, false, "End setup- " + reason.getError().getErrorDesc());
            SendBroadcast.refreshPolling(DashboardActivity.this);

        }
    };

    private StandardResponse objectToNewError(Object o) {
        StandardResponse responseNewVersion;
        if (o instanceof StandardResponse) {
            responseNewVersion = (StandardResponse) o;
        } else {
            Gson gson = new GsonBuilder().create();

            ErrorResponse er = gson.fromJson(new Gson().toJson(o), ErrorResponse.class);

            responseNewVersion = new StandardResponse(true, 0, er);
            if (responseNewVersion.getError().getErrorCode() != 0) {
                responseNewVersion.setFunctionSucceed(false);
            }
        }
//        if (responseNewVersion.getError() != null
//                && responseNewVersion.getError().getErrorDesc() != null
//                && responseNewVersion.getError().getErrorDesc().isEmpty()
//                && responseNewVersion.getError().get() != null
//                && !responseNewVersion.getError().getmErrorFunction().isEmpty()) {
//            responseNewVersion.getError().setmErrorDesc(responseNewVersion.getError().getmErrorFunction());
//        }
        return responseNewVersion;
    }

    @Override
    public void onOpenNewFragmentInCentralDashboardContainer(String type) {

        Fragment reportFragment = null;

        if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null || mCurrentMachineStatus.getAllMachinesData().size() == 0) {
            OppAppLogger.w(TAG, "missing machine status data in job spinner");
            return;
        }

        try {

            switch (type) {

                case REPORT_REJECT_TAG:
                    reportFragment = ReportRejectsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(),
                            mActiveJobsListForMachine, mSpinnerProductPosition, mCurrentMachineStatus.getAllMachinesData().get(0).getRejectMesuaring());

                    break;

                case REPORT_UNIT_CYCLE_TAG:
                    reportFragment = ReportCycleUnitsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSpinnerProductPosition);

                    break;

                case REPORT_FIX_PRODUCED_UNITS:
                    reportFragment = FixUnitsProducedFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSpinnerProductPosition);

                    break;

                case REPORT_PRODUCTION_TAG:
                    reportFragment = ReportProductionFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSpinnerProductPosition);

                    break;

            }

            if (reportFragment != null) {
                getSupportFragmentManager().beginTransaction().add(mContainer3.getId(), reportFragment).addToBackStack(DASHBOARD_FRAGMENT).commit();
            }

        } catch (IllegalStateException ignored) {
        }

    }

    @Override
    public void onReportUpdatedSuccess() {

    }

    @Override
    public void onReportUpdateFailure() {

    }

    private void getKPIS() {
        NetworkManager.getInstance().getKPIs(new Callback<ResponseKPIS>() {
            @Override
            public void onResponse(Call<ResponseKPIS> call, Response<ResponseKPIS> response) {
                PersistenceManager.getInstance().setTranslationForKPIS(response.body());
            }

            @Override
            public void onFailure(Call<ResponseKPIS> call, Throwable t) {
                PersistenceManager.getInstance().setTranslationForKPIS(null);
            }
        });
    }

    private void setupVersionCheck(boolean isImmediate) {
        int delay = isImmediate ? 0 : CHECK_APP_VERSION_INTERVAL;
        if (mVersionCheckHandler != null) {
            mVersionCheckHandler.removeCallbacksAndMessages(null);
        }
        mVersionCheckHandler = new Handler();

        mCheckAppVersionRunnable = new Runnable() {
            @Override
            public void run() {
                if (DashboardActivity.this != null && !DashboardActivity.this.isDestroyed()) {
                    getKPIS();
                    NetworkManager.getInstance().GetApplicationVersion(new Callback<AppVersionResponse>() {
                        @Override
                        public void onResponse(Call<AppVersionResponse> call, retrofit2.Response<AppVersionResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getError().getErrorDesc() == null) {

                                for (AppVersionResponse.ApplicationVersion item : response.body().getmAppVersion()) {

//                                String siteName = item.getmSite() != null ? item.getmSite().toLowerCase() : "";
                                    String siteName = item.getmSite();

                                    if (item.getmAppName().equals(Consts.APP_NAME) && item.getmAppVersion() > BuildConfig.VERSION_CODE
                                            && (siteName.isEmpty() || siteName.equals("all") || siteName.equals(PersistenceManager.getInstance().getSiteName().toLowerCase()))) {
                                        getFile(item.getmUrl());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AppVersionResponse> call, Throwable t) {

                        }
                    });
                    mVersionCheckHandler.postDelayed(mCheckAppVersionRunnable, CHECK_APP_VERSION_INTERVAL);
                }
            }
        };

        mVersionCheckHandler.postDelayed(mCheckAppVersionRunnable, delay);
    }

    private void getFile(String url) {
        Log.d(TAG, "getFile- " + url);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            //check if app has permission to write to the external storage.
            if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Get the URL entered
                mDownloadFile = new DownloadFile().execute(url);

            } else {
                //If permission is not present request for the same.
                EasyPermissions.requestPermissions(this, getString(R.string.storage_permission), REQUEST_WRITE_PERMISSION, Manifest.permission.READ_EXTERNAL_STORAGE, url);
            }


        } else {
            Toast.makeText(this, "SD Card not found", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Download the file once permission is granted
        Log.d(TAG, "onPermissionsGranted- " + requestCode);
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            //String url = "https://s3-eu-west-1.amazonaws.com/leadermes/opapp_35_update_test.apk";
            mDownloadFile = new DownloadFile().execute(perms.get(0));
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "Permission has been denied", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSuccess(StandardResponse standardResponse) {

    }

    @Override
    public void onSaveWorkers() {
        onBackPressed();
        ProgressDialogManager.show(this);
        dashboardDataStartPolling();
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            if (mActionBarAndEventsFragment.get().isVisible()) {
                mActionBarAndEventsFragment.get().blockOperatorsSpinner();
            }
        }
    }

    @Override
    public void onChangeFactory() {
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onQCTestSelected() {
        pollingBackup(false);
        mAllDashboardDataCore.stopPolling();
        mAllDashboardDataCore.unregisterListener();
        mAllDashboardDataCore.stopTimer();
        mReportFieldsForMachineCore.stopPolling();
        mReportFieldsForMachineCore.unregisterListener();
        NetworkManager.getInstance().clearPollingRequest();

        Intent intent = new Intent(DashboardActivity.this, QCActivity.class);
        intent.putExtra(QCActivity.QC_IS_FROM_SELECT_MACHINE_SCREEN, true);
        ignoreFromOnPause = true;

        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            mActionBarAndEventsFragment.get().setFromAnotherActivity(true);
        }
        startActivityForResult(intent, QC_ACTIVITY_RESULT_CODE);
    }

    @Override
    public void onCloseSelectMachine() {
        if (PersistenceManager.getInstance().getMachineId() == -1) {
            return;
        }
        if (mSelectMachineFragment != null && mSelectMachineFragment.get() != null) {
            try {
                getSupportFragmentManager().beginTransaction().remove(mSelectMachineFragment.get()).commit();
                mSelectMachineFragment = null;
            } catch (NullPointerException e) {
                onBackPressed();
            }
        }
        showReportBtn(true);
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {

            mActionBarAndEventsFragment.get().setActionBar();
        }
    }

    @Override
    public void onMachineSelected() {
        cleanEvents();
        onCloseSelectMachine();
        if (mActionBarAndEventsFragment != null && mActionBarAndEventsFragment.get() != null) {
            mActionBarAndEventsFragment.get().clearEventsRecycler();
        }
        ProgressDialogManager.show(this);
        dashboardDataStartPolling();
        shiftForMachineTimer();

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof SignInOperatorFragment) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(OperatorApplication.getAppContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) OperatorApplication.getAppContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        finish();
        System.exit(2);
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isCancelled = false;
        private File directory;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            if (!DashboardActivity.this.isFinishing()) {
                Log.d(TAG, "DownloadFile- onPreExecute");
                super.onPreExecute();
                this.progressDialog = new ProgressDialog(DashboardActivity.this);
                this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                this.progressDialog.setCancelable(true);
                this.progressDialog.setTitle(getResources().getString(R.string.update_version_title));
                this.progressDialog.setMessage(getResources().getString(R.string.update_version_messege));
                this.progressDialog.setIcon(getResources().getDrawable(R.drawable.logo));
                this.progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isCancelled = true;
                    }
                });
                this.progressDialog.show();
            }
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            if (!DashboardActivity.this.isFinishing()) {
                Log.d(TAG, "DownloadFile- doInBackground - " + f_url[0]);
                int count;
                try {
                    URL url = new URL(f_url[0]);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    // getting file length
                    int lengthOfFile = connection.getContentLength();


                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                    //Extract file name from URL
                    fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                    //Append timestamp to file name
                    fileName = "bbbbbb.apk";

                    //External directory path to save file
                    folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

                    //Create androiddeft folder if it does not exist
                    directory = new File(folder);

                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    outputFile = new File(directory, fileName);
                    // Output stream to write file
                    OutputStream output = new FileOutputStream(outputFile);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lengthOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                    return "Downloaded at: " + folder + fileName;

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }

            }
            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            if (!DashboardActivity.this.isFinishing()) {
                progressDialog.setProgress(Integer.parseInt(progress[0]));
            }
        }


        @Override
        protected void onPostExecute(String message) {
            if (isCancelled || DashboardActivity.this.isFinishing()) {
                return;
            }
            Log.d(TAG, "DownloadFile- onPostExecute - " + message);
            // dismiss the dialog after the file was downloaded
//            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_LONG).show();
            try {
                Uri apkUri = FileProvider.getUriForFile(DashboardActivity.this, BuildConfig.APPLICATION_ID + ".provider", outputFile);
                mIsUpgrading = true;
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                install.normalizeMimeType("application/vnd.android.package-archive");

                if (install.resolveActivity(getPackageManager()) != null) {
                    startActivity(install);
                } else {
                    ShowCrouton.showSimpleCrouton(DashboardActivity.this, getString(R.string.install_activity_not_found), CroutonCreator.CroutonType.NETWORK_ERROR);
                }

//                finish();
            } catch (Exception e) {

            }
            this.progressDialog.dismiss();
        }
    }

    private void showTimeNextJobDialog(int currentJobID, boolean autoActivateNextJob, final long nextJobID, boolean autoActivateNextJobTimer, String erpJobId, int counter) {

        if (mShowDialogJobId != 0 && mShowDialogJobId == currentJobID) {
            return;
        }
        try {
            if (autoActivateNextJob && nextJobID > 0) {
                mShowDialogJobId = currentJobID;
                mNextJobTimerDialog = new NextJobTimerDialog(
                        new NextJobTimerDialog.NextJobTimerDialogListener() {
                            @Override
                            public void onClickPositiveBtn() {
                                PersistenceManager persistenceManager = PersistenceManager.getInstance();
                                postActivateJob(new ActivateJobRequest(persistenceManager.getSessionId(),
                                        String.valueOf(persistenceManager.getMachineId()),
                                        String.valueOf(nextJobID),
                                        persistenceManager.getOperatorId(),
                                        false));
                            }

                            @Override
                            public void onClickNegativeBtn() {
                            }
                        }, getString(R.string.you_ve_reached_the_production_target), getString(R.string.next_job_will_start_in),
                        erpJobId, getString(R.string.start_job_now), getString(R.string.cancel_job), counter, autoActivateNextJobTimer
                );

                mNextJobTimerDialog.showNextJobTimerDialog(getApplicationContext()).show();
            }
        } catch (Exception e) {

        }
    }

    private void postActivateJob(final ActivateJobRequest activateJobRequest) {

        final PersistenceManager persistanceManager = PersistenceManager.getInstance();

        SimpleRequests simpleRequests = new SimpleRequests();

        ProgressDialogManager.show(this);

        simpleRequests.postActivateJob(persistanceManager.getSiteUrl(), new PostActivateJobCallback() {

            @Override
            public void onPostActivateJobSuccess(StandardResponse response) {

                if (response == null) {
                    ProgressDialogManager.dismiss();
                    StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, "PostActivateJob Failed");
                    ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this, errorObject);

                } else if (((StandardResponse) response).getError() != null) {
                    ProgressDialogManager.dismiss();
                    StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, ((StandardResponse) response).getError().getErrorDesc());
                    ShowCrouton.showSimpleCrouton(DashboardActivity.this, errorObject);

                }
                if (((StandardResponse) response).getFunctionSucceed()) {
                    activateJob(Integer.parseInt(activateJobRequest.getJobID()));
                }
            }

            @Override
            public void onPostActivateJobFailed(StandardResponse reason) {

                ProgressDialogManager.dismiss();

                StandardResponse errorObject = new StandardResponse(ErrorResponse.ErrorCode.Retrofit, reason.getError().getErrorDesc());
                ShowCrouton.jobsLoadingErrorCrouton(DashboardActivity.this, errorObject);
            }
        }, NetworkManager.getInstance(), activateJobRequest, persistanceManager.getTotalRetries(), persistanceManager.getRequestTimeout());

    }
}