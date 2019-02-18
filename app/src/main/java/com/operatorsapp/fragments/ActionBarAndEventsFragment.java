package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.app.operatorinfra.Operator;
import com.example.oppapplog.OppAppLogger;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.infra.Machine;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.AllMachinesData;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operators.reportfieldsformachineinfra.PackageTypes;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.Technician;
import com.operators.reportrejectnetworkbridge.server.response.ErrorResponseNewVersion;
import com.operatorsapp.BuildConfig;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.EventsAdapter;
import com.operatorsapp.adapters.JobsSpinnerAdapter;
import com.operatorsapp.adapters.JoshProductNameSpinnerAdapter;
import com.operatorsapp.adapters.LanguagesSpinnerAdapterActionBar;
import com.operatorsapp.adapters.LenoxMachineAdapter;
import com.operatorsapp.adapters.NotificationHistoryAdapter;
import com.operatorsapp.adapters.NotificationsPagerAdapter;
import com.operatorsapp.adapters.OperatorSpinnerAdapter;
import com.operatorsapp.adapters.ProductionSpinnerAdapter;
import com.operatorsapp.adapters.ShiftLogSqlAdapter;
import com.operatorsapp.adapters.TechnicianSpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.DialogFragment;
import com.operatorsapp.dialogs.GenericDialog;
import com.operatorsapp.dialogs.TechCallDialog;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.JobActionsSpinnerItem;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.server.requests.PostTechnicianCallRequest;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.ResizeWidthAnimation;
import com.operatorsapp.utils.SaveAlarmsHelper;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.operatorsapp.view.EmeraldSpinner;
import com.operatorsapp.view.TimeLineView;
import com.ravtech.david.sqlcore.DatabaseHelper;
import com.ravtech.david.sqlcore.Event;

import org.litepal.crud.DataSupport;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.format.DateUtils.DAY_IN_MILLIS;


public class ActionBarAndEventsFragment extends Fragment implements DialogFragment.OnDialogButtonsListener,
        DashboardUICallbackListener,
        OnStopClickListener, CroutonRootProvider, SelectStopReasonBroadcast.SelectStopReasonListener,
        View.OnClickListener, LenoxMachineAdapter.LenoxMachineAdapterListener, EmeraldSpinner.OnSpinnerEventsListener {

    private static final String LOG_TAG = ActionBarAndEventsFragment.class.getSimpleName();
    private static final int ANIM_DURATION_MILLIS = 200;
    public static final int TYPE_ALERT = 20;
    public static final double MINIMUM_VERSION_FOR_NEW_ACTIVATE_JOB = 1.8f;
    private static final long TECHNICIAN_CALL_WAITING_RESPONSE = 1000 * 60 * 5;
    private static final long TECHNICIAN_CALL_CLEAN_ALL = 1000 * 60 * 20;
    private static final long ONE_HOUR = 1000 * 60 * 60;
    private static final long HALF_HOUR = 1000 * 60 * 30;
    private static final int PRODUCTION_ID = 1;
    private static final String SQL_NO_T_FORMAT = "dd/MM/yyyy HH:mm:ss";


    private View mToolBarView;
    private GoToScreenListener mOnGoToScreenListener;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private OperatorCore mOperatorCore;
    private OnCroutonRequestListener mCroutonCallback;
    private RecyclerView mShiftLogRecycler;
    private LinearLayout mShiftLogLayout;
    private TextView mNoNotificationsText;
    private TextView mLoadingDataText;
    private LinearLayout mStatusLayout;
    private int mDownX;
    private ShiftLogSqlAdapter mShiftLogAdapter;
    private ArrayDeque<Event> mEventsQueue = new ArrayDeque<>();
    private boolean mIsOpen = false;
    private int mCloseWidth;
    private int mOpenWidth;
    private float mTollBarsHeight;
    private ArrayList<String> mOperatorsSpinnerArray = new ArrayList<>();
    private TextView mProductNameTextView;
    private TextView mJobIdTextView;
    private TextView mShiftIdTextView;
    private TextView mTimerTextView;
    private TextView mMachineIdStatusBarTextView;
    private TextView mMachineStatusStatusBarTextView;
    private ImageView mStatusIndicatorImageView;
    private ViewGroup.LayoutParams mShiftLogParams;
    private RelativeLayout.LayoutParams mSwipeParams;
    private boolean mIsNewShiftLogs;
    private MachineStatus mCurrentMachineStatus;
    private JobsSpinnerAdapter mJobsSpinnerAdapter;
    private List<JobActionsSpinnerItem> mJobActionsSpinnerItems;
    private int mApproveItemID;
    private ViewGroup mMachineStatusLayout;
    public static final int REASON_UNREPORTED = 0;
    private SelectStopReasonBroadcast mReasonBroadcast = null;
    private boolean thereAlreadyRequest = false;
    public DatabaseHelper mDatabaseHelper;
    private boolean mNoData;
    private ActionBarAndEventsFragmentListener mListener;
    private int mRecyclersHeight;
    private boolean mIsSelectionMode;
    private ArrayList<Integer> mSelectedEvents;
    private TextView mSelectedNumberTv;
    private View mSelectedNumberLy;
    private Event mLastEvent;
    private Fragment mVisiblefragment;
    private boolean mFromAnotherActivity;
    private TextView mMinDurationText;
    private LinearLayout mMinDurationLil;
    private SwipeRefreshLayout mShiftLogSwipeRefresh;
    private ActiveJobsListForMachine mActiveJobsListForMachine;
    private Spinner mProductSpinner;
    private View mMultipleProductImg;
    private JoshProductNameSpinnerAdapter mJoshProductNameSpinnerAdapter;
    private List<ActiveJob> mActiveJobs;
    private int mSelectedPosition;
    private int mLenoxMachineLyWidth;
    private View mLenoxMachineLy;
    private ArrayList<Machine> mMachines;
    private TextView mTechnicianIndicatorTv;
    private BroadcastReceiver mNotificationsReceiver = null;
    private Handler mHandlerTechnicianCall = new Handler();
    private boolean isShowAlarms;
    private Event mFirstSeletedEvent;
    private FrameLayout mNotificationIndicatorCircleFl;
    private TextView mNotificationIndicatorNumTv;
    private Dialog mPopUpDialog;
    private Switch mShowAlarmCheckBox;
    private View mStatusBlackFilter;
    private EmeraldSpinner mLanguagesSpinner;
    private View mStatusWhiteFilter;
    private RelativeLayout technicianRl;
    private TextView mStatusTimeMinTv;
    private boolean mAutoSelectMode;
    private View mMainView;
    private boolean mEndSetupDisable;
    private ImageView mTechnicianIconIv;
    private TextView mTechnicianTimerTv;
    private View mCycleWarningView;
    private boolean mSetupEndDialogShow;
    private boolean mCycleWarningViewShow;
    private EmeraldSpinner mJobsSpinner;
    private Handler mCallCounterHandler;
    private LinearLayout mScrollView;
    private TimeLineView mTimeView;
    private ArrayList<String> mTimes;
    public static final int PIXEL_FOR_MINUTE = 10;
    private boolean mIsSelectionEventsMode = false;
    private ImageView mCloseSelectEvents;
    private RecyclerView mEventsRecycler;
    private EventsAdapter mEventsAdapter;
    private Switch mTimeLineType;


    public static ActionBarAndEventsFragment newInstance() {
        return new ActionBarAndEventsFragment();
    }


    private void registerReceiver() {

        if (mReasonBroadcast == null) {

            mReasonBroadcast = new SelectStopReasonBroadcast(this);

            IntentFilter filter = new IntentFilter();

            filter.addAction(SelectStopReasonBroadcast.ACTION_SELECT_REASON);

            if (getActivity() != null) {
                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReasonBroadcast, filter);
            }
        }

        if (mNotificationsReceiver == null) {
            setNotificationsReceiver();
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mNotificationsReceiver), new IntentFilter(Consts.NOTIFICATION_BROADCAST_NAME));
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ProgressDialogManager.show(getActivity()); TODO in dismiss ther are conflict with widgetFragment
// TODO because ProgessDialogManager support only one progress management in same time
        View inflate = inflater.inflate(R.layout.fragment_actionbar_and_events, container, false);
        SoftKeyboardUtil.hideKeyboard(this);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        OppAppLogger.getInstance().d(LOG_TAG, "onViewCreated(), start ");
        super.onViewCreated(view, savedInstanceState);

        mMainView = view;
        final ViewGroup.LayoutParams statusBarParams = initView(view);

//        if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
//
//            mLenoxMachineLy = view.findViewById(R.id.FAAE_lenox_machines_ly);
//
//            mLenoxMachineLy.setVisibility(View.VISIBLE);
//
//            mLenoxMachineLy.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//
//                    mLenoxMachineLyWidth = mLenoxMachineLy.getWidth();
//
//                    mSwipeParams = (RelativeLayout.LayoutParams) mShiftLogSwipeRefresh.getLayoutParams();
//                    mSwipeParams.width = mShiftLogParams.width + mLenoxMachineLyWidth;
//                    mShiftLogSwipeRefresh.setLayoutParams(mSwipeParams);
//                    mShiftLogSwipeRefresh.requestLayout();
//
//                    mListener.onResize(mCloseWidth + mLenoxMachineLyWidth, statusBarParams.height);
//
//                    if (mLenoxMachineLyWidth != 0) {
//                        mLenoxMachineLy.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }
//                }
//            });
//
//        } else {
        mSwipeParams = (RelativeLayout.LayoutParams) mShiftLogSwipeRefresh.getLayoutParams();
        mSwipeParams.width = mShiftLogParams.width;
        mShiftLogSwipeRefresh.setLayoutParams(mSwipeParams);
        mShiftLogSwipeRefresh.requestLayout();
        mListener.onResize(mCloseWidth, statusBarParams.height);
        //TODO Lenox uncomment

        //        }
    }

    private void initLenoxMachineRv(View view) {

        RecyclerView lenoxMachineLy = view.findViewById(R.id.FAAE_lenox_machines_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        lenoxMachineLy.setLayoutManager(linearLayoutManager);
        lenoxMachineLy.setAdapter(new LenoxMachineAdapter(getContext(), mMachines, this));

    }

    public ViewGroup.LayoutParams initView(@NonNull View view) {
        mIsOpen = false;
        mIsNewShiftLogs = PersistenceManager.getInstance().isNewShiftLogs();
        // get screen parameters
        Point size = new Point();

        if (getActivity() != null) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            display.getSize(size);
        }

        int width = size.x;
        float height = size.y;
        mOpenWidth = (int) (width * 0.33);
        mCloseWidth = (int) (width * 0.16);
        mTollBarsHeight = (int) (height * 0.25);

        if (height == 0) {
            height = 1;
        }
        mRecyclersHeight = (int) ((1 - (mTollBarsHeight / height)) * height);

        final int middleWidth = (int) (width * 0.3);

        mStatusLayout = view.findViewById(R.id.status_layout);
        mStatusBlackFilter = view.findViewById(R.id.FAAE_status_black_filter);
        mStatusWhiteFilter = view.findViewById(R.id.FAAE_status_white_filter);
        ViewGroup.LayoutParams statusBarParams;
        statusBarParams = mStatusLayout.getLayoutParams();
        statusBarParams.height = (int) (mTollBarsHeight * 0.35);
        mStatusLayout.requestLayout();

        mShowAlarmCheckBox = view.findViewById(R.id.FAAE_alarm_chekbox);
        mShowAlarmCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowAlarms = isChecked;
                setShiftLogAdapter(getCursorByType());
            }
        });

        mTimeLineType = view.findViewById(R.id.FAAE_shift_type_checkbox);
        mTimeLineType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mEventsRecycler.setVisibility(View.VISIBLE);
                    mShiftLogRecycler.setVisibility(View.GONE);
                    mShowAlarmCheckBox.setVisibility(View.GONE);
                }else {
                    mEventsRecycler.setVisibility(View.GONE);
                    mShiftLogRecycler.setVisibility(View.VISIBLE);
                    mShowAlarmCheckBox.setVisibility(View.VISIBLE);
                }
            }
        });

        //mSwipeToRefresh = view.findViewById(R.id.swipe_refresh_actionbar_events);
        mProductNameTextView = view.findViewById(R.id.text_view_product_name_and_id);
        mMultipleProductImg = view.findViewById(R.id.FAAE_multiple_product_img);
        mProductSpinner = view.findViewById(R.id.FAAE_product_spinner);
        mJobIdTextView = view.findViewById(R.id.text_view_job_id);
        mShiftIdTextView = view.findViewById(R.id.text_view_shift_id);
        mTimerTextView = view.findViewById(R.id.text_view_timer);
        mSelectedNumberTv = view.findViewById(R.id.FAAE_selected_nmbr);

        mSelectedNumberLy = view.findViewById(R.id.FAAE_event_selected_ly);

        mShiftLogLayout = view.findViewById(R.id.fragment_dashboard_shiftlog);
        mShiftLogParams = mShiftLogLayout.getLayoutParams();
        mShiftLogParams.width = mCloseWidth;
        mShiftLogLayout.requestLayout();

        mShiftLogRecycler = view.findViewById(R.id.fragment_dashboard_shift_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mShiftLogRecycler.setLayoutManager(linearLayoutManager);


        mCloseSelectEvents = view.findViewById(R.id.FAAE_close_select_events);
        mCloseSelectEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCloseSelectEvents.setVisibility(View.GONE);

                mIsSelectionEventsMode = false;
                initEvents(mDatabaseHelper.getListFromCursor(getCursorByType()));

            }
        });

        initEventRecycler(view);

        mScrollView = view.findViewById(R.id.FAAE_scroll_container);
        mScrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//todo kuti

                if (!mIsSelectionEventsMode) {
                    mIsSelectionEventsMode = true;

                    mCloseSelectEvents.setVisibility(View.VISIBLE);
                    initEvents(mDatabaseHelper.getListFromCursor(getCursorByType()));
                }


            }
        });
        mTimeView = view.findViewById(R.id.FAAE_time_container);


        initLenoxMachineRv(view);

        mShiftLogSwipeRefresh = view.findViewById(R.id.shift_log_swipe_refresh);
        mShiftLogSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SendBroadcast.refreshPolling(getActivity());
            }
        });

        mNoNotificationsText = view.findViewById(R.id.fragment_dashboard_no_notif);

        mLoadingDataText = view.findViewById(R.id.fragment_dashboard_loading_data_shiftlog);
        mMinDurationText = view.findViewById(R.id.fragment_dashboard_min_duration_tv);
        mMinDurationLil = view.findViewById(R.id.fragment_dashboard_min_duration_lil);

        final ImageView shiftLogHandle = view.findViewById(R.id.fragment_dashboard_left_btn);

        view.findViewById(R.id.FAAE_unselect_all).setOnClickListener(this);

        View mDividerView = view.findViewById(R.id.fragment_dashboard_divider);
        mDividerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = (int) event.getRawX();
                        shiftLogHandle.setImageResource(R.drawable.left_panel_btn_pressed);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mNoData) {
                            int currentX;

                            if (PersistenceManager.getInstance().getCurrentLang().equals("iw")) {
                                currentX = mShiftLogLayout.getLayoutParams().width - (int) event.getRawX() + mDownX;
                            } else {
                                currentX = mShiftLogLayout.getLayoutParams().width + (int) event.getRawX() - mDownX;
                            }
                            if (currentX >= mCloseWidth && currentX <= mOpenWidth) {
                                mShiftLogLayout.getLayoutParams().width = currentX;
                                mShiftLogLayout.requestLayout();
                                mSwipeParams.width = mShiftLogParams.width + mLenoxMachineLyWidth;
                                mShiftLogSwipeRefresh.setLayoutParams(mSwipeParams);
                                mShiftLogSwipeRefresh.requestLayout();
                                mDownX = (int) event.getRawX();
                                if (mShiftLogAdapter != null) {
                                    mShiftLogAdapter.changeState(true);
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        shiftLogHandle.setImageResource(R.drawable.left_panel_btn);
                        if (!mNoData) {
                            if (event.getRawX() - mDownX > 5 || event.getRawX() - mDownX < -5) {
                                if (mShiftLogParams.width < middleWidth) {
                                    mIsOpen = false;
                                    toggleWoopList(mShiftLogParams, mCloseWidth, false);
                                    mListener.onWidgetChangeState(true);
                                    mListener.onWidgetUpdateSpane(3);
                                } else {
                                    mIsOpen = true;
                                    toggleWoopList(mShiftLogParams, mOpenWidth, true);
                                    mListener.onWidgetChangeState(false);
                                    mListener.onWidgetUpdateSpane(2);

                                }

                            } else {
                                onButtonClick(mShiftLogParams);
                            }
                        }
                        v.performClick();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    default:
                        shiftLogHandle.setImageResource(R.drawable.left_panel_btn);
                        break;
                }
                return false;
            }
        });


        mOperatorCore.registerListener(mOperatorForMachineUICallbackListener);

        OppAppLogger.getInstance().d(LOG_TAG, "onViewCreated(), end ");

        initProductsSpinner();

        initCycleAlarmView(view);

        return statusBarParams;
    }

    private void initCycleAlarmView(View view) {
        mCycleWarningView = view.findViewById(R.id.FAAE_cycle_alarm_view);
        view.findViewById(R.id.FAAE_cycle_alarm_view).findViewById(R.id.NPAD_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivateJobScreen();
            }
        });
    }

    @Nullable
    public Cursor getCursorByType() {
        Cursor cursor;
         if (isShowAlarms) {
            cursor = mDatabaseHelper.getCursorOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration());
        } else {
            cursor = mDatabaseHelper.getStopTypeShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration());
        }
        return cursor;
    }

    private void setNotificationsReceiver() {
        mNotificationsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int type = intent.getIntExtra(Consts.NOTIFICATION_TYPE, 0);
                //String technicianName = intent.getStringExtra(Consts.NOTIFICATION_TECHNICIAN_NAME);

                if (type == Consts.NOTIFICATION_TYPE_TECHNICIAN) {

                    mHandlerTechnicianCall.removeCallbacksAndMessages(null);
                    int notificationId = intent.getIntExtra(Consts.NOTIFICATION_ID, 0);
                    int responseType = intent.getIntExtra(Consts.NOTIFICATION_RESPONSE_TYPE, 0);
                    PersistenceManager.getInstance().setRecentTechCallId(notificationId);
                    ArrayList<TechCallInfo> list = PersistenceManager.getInstance().getCalledTechnician();
                    for (TechCallInfo call : list) {
                        if (call.getmNotificationId() == notificationId) {
                            call.setmResponseType(responseType);
                            call.setmCallTime(new Date().getTime());
                            switch (responseType) {
                                case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                                    call.setmStatus(getString(R.string.message_received) + "\n" + call.getmName());
                                    break;

                                case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                                    call.setmStatus(getString(R.string.message_declined) + "\n" + call.getmName());
                                    break;

                                case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                                    call.setmStatus(getString(R.string.at_work) + "\n" + call.getmName());
                                    break;

                                case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                                    call.setmStatus(getString(R.string.work_comlpeted) + "\n" + call.getmName());
                                    break;

                            }
                            PersistenceManager.getInstance().setCalledTechnicianList(list);
                            break;
                        }
                    }
                    setTechnicianCallStatus();

                } else if (type == Consts.NOTIFICATION_TYPE_FROM_WEB) {

                    setNotificationNeedResponse();
                }
                openNotificationPopUp(intent.getIntExtra(Consts.NOTIFICATION_ID, 0));
            }
        };
    }

    private void openNotificationPopUp(final int notificationId) {

        final ArrayList<Notification> notificationList = PersistenceManager.getInstance().getNotificationHistory();
        if (notificationList != null && notificationList.size() > 0) {

            Notification notification = null;

            for (int i = 0; i < notificationList.size(); i++) {
                if (notificationList.get(i).getmNotificationID() == notificationId) {
                    notification = notificationList.get(i);
                    break;
                }
            }

            if (notification != null) {
                if (mPopUpDialog != null && mPopUpDialog.isShowing()) {
                    mPopUpDialog.dismiss();
                }
                mPopUpDialog = new Dialog(getActivity());
                mPopUpDialog.setContentView(R.layout.notification_popup);
                mPopUpDialog.setCanceledOnTouchOutside(true);

                Window window = mPopUpDialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.TOP;
                wlp.y = 0;
                Point point = new Point(1, 1);
                ((WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
                window.setLayout((point.x * 1) / 2, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setAttributes(wlp);
                mPopUpDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                ImageView btnClose = mPopUpDialog.findViewById(R.id.notification_popup_iv);
                ImageView icon = mPopUpDialog.findViewById(R.id.notification_popup_icon_iv);
                Button btnApprove = mPopUpDialog.findViewById(R.id.notification_popup_approve_btn);
                Button btnDecline = mPopUpDialog.findViewById(R.id.notification_popup_decline_btn);
                Button btnClarify = mPopUpDialog.findViewById(R.id.notification_popup_clarify_btn);
                TextView tvSender = mPopUpDialog.findViewById(R.id.notification_popup_tv_sender);
                TextView tvBody = mPopUpDialog.findViewById(R.id.notification_popup_tv_body);

                if (notification.getmResponseType() != 0) {
                    btnApprove.setVisibility(View.GONE);
                    btnClarify.setVisibility(View.GONE);
                    btnDecline.setVisibility(View.GONE);
                }

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN) {

                    tvSender.setText(getResources().getString(R.string.technician) + " " + notification.getmTargetName());

                    switch (notification.getmResponseType()) {

                        case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                            tvBody.setText(String.format(getResources().getString(R.string.call_approved2), notification.getmSender()));
                            break;

                        case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                            tvBody.setText(String.format(getResources().getString(R.string.call_declined2), notification.getmSender()));
                            break;


                        case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                            tvBody.setText(String.format(getResources().getString(R.string.started_service2), notification.getmSender()));
                            break;


                        case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                            tvBody.setText(String.format(getResources().getString(R.string.service_completed2), notification.getmSender()));
                            break;

                        case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                            tvBody.setText(String.format(getResources().getString(R.string.call_cancelled2), notification.getmSender()));
                            break;

                        default:
                            tvBody.setText(notification.getmBody(getActivity()));
                            break;
                    }

                    icon.setImageDrawable(getResources().getDrawable(R.drawable.technician_dark));
                } else {
                    tvSender.setText(getString(R.string.message_from) + " " + notification.getmSender());
                    tvBody.setText(notification.getmBody(getActivity()));
                }


                View.OnClickListener thisDialogListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.notification_popup_iv:
                                mPopUpDialog.dismiss();
                                break;

                            case R.id.notification_popup_approve_btn:
                                sendNotificationResponse(notificationId, Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE);
                                mPopUpDialog.dismiss();
                                break;

                            case R.id.notification_popup_decline_btn:
                                sendNotificationResponse(notificationId, Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE);
                                mPopUpDialog.dismiss();
                                break;

                            case R.id.notification_popup_clarify_btn:
                                sendNotificationResponse(notificationId, Consts.NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS);
                                mPopUpDialog.dismiss();
                                break;
                        }
                    }
                };

                btnApprove.setOnClickListener(thisDialogListener);
                btnDecline.setOnClickListener(thisDialogListener);
                btnClarify.setOnClickListener(thisDialogListener);
                btnClose.setOnClickListener(thisDialogListener);

                mPopUpDialog.show();
            }
        }
    }

    private void onButtonClick(final ViewGroup.LayoutParams leftLayoutParams) {
        if (!mIsOpen) {
            if (!mNoData) {
                final ResizeWidthAnimation anim = new ResizeWidthAnimation(mShiftLogLayout, mOpenWidth);
                anim.setDuration(ANIM_DURATION_MILLIS);
                mShiftLogLayout.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toggleWoopList(leftLayoutParams, mOpenWidth, true);
                        if (mShiftLogAdapter != null) {
                            mShiftLogAdapter.notifyDataSetChanged();
                        }
                        mListener.onWidgetChangeState(false);
                        mListener.onWidgetUpdateSpane(2);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mIsOpen = true;
            }
        } else {

            closeWoopList(leftLayoutParams);
        }

    }

    private void closeWoopList(final ViewGroup.LayoutParams leftLayoutParams) {
        final ResizeWidthAnimation anim = new ResizeWidthAnimation(mShiftLogLayout, mCloseWidth);
        anim.setDuration(ANIM_DURATION_MILLIS);
        mShiftLogLayout.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mShiftLogAdapter != null) {
                    mShiftLogAdapter.changeState(true);
                }
                if (mEventsAdapter != null) {
                    mEventsAdapter.setClosedState(true);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toggleWoopList(leftLayoutParams, mCloseWidth, false);
                mListener.onWidgetChangeState(true);
                mListener.onWidgetUpdateSpane(3);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mIsOpen = false;
    }

    OperatorForMachineUICallbackListener mOperatorForMachineUICallbackListener = new OperatorForMachineUICallbackListener() {
        @Override
        public void onOperatorDataReceived(Operator operator) {

        }

        @Override
        public void onOperatorDataReceiveFailure(ErrorObjectInterface reason) {

        }

        @Override
        public void onSetOperatorSuccess(String operatorId) {
            if (operatorId == null || operatorId.isEmpty()) {
                PersistenceManager.getInstance().setOperatorId("");
                PersistenceManager.getInstance().setOperatorName("");

                // TODO: 25/07/2018 sign out success
                //ShowCrouton.jobsLoadingAlertCrouton(mCroutonCallback, message);
                ShowCrouton.jobsLoadingSuccessCrouton(mCroutonCallback, getString(R.string.signed_out_successfully));
            } else {
                // TODO: 25/07/2018 sign in success
                ShowCrouton.jobsLoadingSuccessCrouton(mCroutonCallback, getString(R.string.signed_in_successfully));
            }
            SendBroadcast.refreshPolling(getActivity());
            setupOperatorSpinner();
            //            mOperatorCoreToDashboardActivityCallback.onSetOperatorForMachineSuccess(mSelectedOperator.getOperatorId(), mSelectedOperator.getOperatorName());
            //            Zloger.clearPollingRequest(LOG_TAG, "onSetOperatorSuccess() ");
        }

        @Override
        public void onSetOperatorFailed(ErrorObjectInterface reason) {
            OppAppLogger.getInstance().d(LOG_TAG, "onSetOperatorFailed() " + reason.getError());
        }

    };


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onResume() {
        OppAppLogger.getInstance().d(LOG_TAG, "onResume(), Start ");
        super.onResume();

        if (mFromAnotherActivity) {

            mFromAnotherActivity = false;

            return;
        }

        mDatabaseHelper = DatabaseHelper.getInstance(getContext());

        Cursor tempCursor = getCursorByType();

        if (tempCursor != null && tempCursor.moveToFirst()) {

            setShiftLogAdapter(tempCursor);

        }
        //todo kuti

        initEvents(mDatabaseHelper.getListFromCursor(getCursorByType()));


        if (DataSupport.count(Event.class) > 0) {
            mNoData = false;
            mNoNotificationsText.setVisibility(View.GONE);
            mLoadingDataText.setVisibility(View.GONE);
        } else {
            mNoData = true;
            mNoNotificationsText.setVisibility(View.VISIBLE);
            mLoadingDataText.setVisibility(View.VISIBLE);
        }

        registerReceiver();
        setActionBar();
        if (mCurrentMachineStatus != null) {
            initStatusLayout(mCurrentMachineStatus);
        }
        if (DataSupport.count(Event.class) > 0) {

            for (Event event : DataSupport.where(DatabaseHelper.KEY_IS_DISMISS + " = 0").find(Event.class)) {

                Log.d(LOG_TAG, "EVENT " + event.isIsDismiss());
                event.setTimeOfAdded(System.currentTimeMillis());
                mEventsQueue.add(event);

            }
        } else {
            mNoData = true;
            mNoNotificationsText.setVisibility(View.VISIBLE);
            mLoadingDataText.setVisibility(View.VISIBLE);
        }

        OppAppLogger.getInstance().d(LOG_TAG, "onResume(), end ");


    }


    private void toggleWoopList(ViewGroup.LayoutParams mLeftLayoutParams, int newWidth,
                                boolean isOpen) {
        if (mShiftLogAdapter == null) {
            return;
        }
        mLeftLayoutParams.width = newWidth;
        mShiftLogLayout.requestLayout();
        mSwipeParams.width = mShiftLogParams.width + mLenoxMachineLyWidth;
        mShiftLogSwipeRefresh.setLayoutParams(mSwipeParams);
        mShiftLogSwipeRefresh.requestLayout();
//        mListener.onWidgetUpdatemargine(newWidth);
        mListener.onResize(newWidth + mLenoxMachineLyWidth, mStatusLayout.getLayoutParams().height);
        mShiftLogAdapter.changeState(!isOpen);

        if (mEventsAdapter != null) {
            mEventsAdapter.setClosedState(isOpen);
            mEventsAdapter.notifyDataSetChanged();

        }

        mListener.onWidgetChangeState(!isOpen);

        //        OppAppLogger.getInstance().clearPollingRequest(LOG_TAG, "setActionBar(),  " + " toolBar: " + mToolBarView.getHeight() + " -- " + mTollBarsHeight * 0.65);
        //        OppAppLogger.getInstance().clearPollingRequest(LOG_TAG, "setActionBar(),  " + " status: " + mStatusLayout.getHeight() + " -- " + mTollBarsHeight * 0.35);
    }

    @Override
    public void onAttach(Context context) {
        OppAppLogger.getInstance().d(LOG_TAG, "onAttach(), start ");
        super.onAttach(context);
        try {
            mCroutonCallback = (OnCroutonRequestListener) getActivity();
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
            mOnActivityCallbackRegistered.onFragmentAttached(this);
            mListener = (ActionBarAndEventsFragmentListener) context;
            OperatorCoreToDashboardActivityCallback operatorCoreToDashboardActivityCallback = (OperatorCoreToDashboardActivityCallback) getActivity();
            if (operatorCoreToDashboardActivityCallback != null) {
                mOperatorCore = operatorCoreToDashboardActivityCallback.onSignInOperatorFragmentAttached();
            }
//            getActiveJobs();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
        OppAppLogger.getInstance().d(LOG_TAG, "onAttach(), end ");
    }

    @Override
    public void onDetach() {
        OppAppLogger.getInstance().d(LOG_TAG, "onDetach(), start ");
        super.onDetach();
        mCroutonCallback = null;
        mOnGoToScreenListener = null;
        mOnActivityCallbackRegistered.onFragmentDetached(this);
        mOnActivityCallbackRegistered = null;
        mOperatorCore.unregisterListener();
        mOperatorCore = null;
        mListener = null;
        OppAppLogger.getInstance().d(LOG_TAG, "onDetach(), end ");
    }

    public void initProductView(ActiveJobsListForMachine activeJobsListForMachine) {
        mActiveJobsListForMachine = activeJobsListForMachine;
        if (mActiveJobsListForMachine.getActiveJobs() != null && mActiveJobsListForMachine.getActiveJobs().size() > 1) {

            mProductNameTextView.setVisibility(View.GONE);
            mProductSpinner.setVisibility(View.VISIBLE);
            mMultipleProductImg.setVisibility(View.VISIBLE);
            mProductSpinner.setVisibility(View.VISIBLE);

            if (mJoshProductNameSpinnerAdapter != null) {
                mActiveJobs.clear();
                mActiveJobs.addAll(mActiveJobsListForMachine.getActiveJobs());
                mJoshProductNameSpinnerAdapter.notifyDataSetChanged();
            }
        } else {

            mSelectedPosition = 0;

            mProductSpinner.setSelection(0);

            mProductNameTextView.setVisibility(View.VISIBLE);
            mProductSpinner.setVisibility(View.GONE);
            mMultipleProductImg.setVisibility(View.GONE);

            initStatusLayout(mCurrentMachineStatus);

        }
    }


    private void initProductsSpinner() {
        //TODO
        mActiveJobs = new ArrayList<>();
        if (getActivity() != null) {
            mJoshProductNameSpinnerAdapter = new JoshProductNameSpinnerAdapter(getActivity(), R.layout.item_product_spinner, mActiveJobs);
            mJoshProductNameSpinnerAdapter.setDropDownViewResource(R.layout.item_product_spinner_list);
            mProductSpinner.setAdapter(mJoshProductNameSpinnerAdapter);
            mProductSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.status_bar), PorterDuff.Mode.SRC_ATOP);

            mProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    mJoshProductNameSpinnerAdapter.setTitle(position);

                    mSelectedPosition = position;

                    mListener.onJoshProductSelected(position, mActiveJobs.get(position).getJobID(),
                            mActiveJobs.get(position).getJobName());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    @SuppressLint("InflateParams")
    public void setActionBar() {

        ActionBar actionBar = null;
        if ((getActivity()) != null) {
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        if (actionBar != null) {

            if (mVisiblefragment != null && !(mVisiblefragment instanceof ActionBarAndEventsFragment
                    || mVisiblefragment instanceof WidgetFragment
                    || mVisiblefragment instanceof RecipeFragment
                    || mVisiblefragment instanceof SelectStopReasonFragment
                    || mVisiblefragment instanceof ReportStopReasonFragment)) {
                return;
            }

            LayoutInflater inflator = initActionBar(actionBar);

            // TODO: 08/07/2018 update to new toolbar
            mToolBarView = inflator.inflate(R.layout.actionbar_title_and_tools_view, null);

            ImageView notificationIv = mToolBarView.findViewById(R.id.toolbar_notification_button);
            technicianRl = mToolBarView.findViewById(R.id.toolbar_technician_button);
            ImageView tutorialIv = mToolBarView.findViewById(R.id.toolbar_tutorial_iv);
            mTechnicianIconIv = mToolBarView.findViewById(R.id.toolbar_technician_iv);
            mTechnicianTimerTv = mToolBarView.findViewById(R.id.toolbar_technician_chronometer);
            mTechnicianIndicatorTv = mToolBarView.findViewById(R.id.toolbar_technician_tv);
            mNotificationIndicatorCircleFl = mToolBarView.findViewById(R.id.toolbar_notification_counter_circle);
            mNotificationIndicatorNumTv = mToolBarView.findViewById(R.id.toolbar_notification_counter_tv);
            mStatusTimeMinTv = mToolBarView.findViewById(R.id.ATATV_status_time_min);

            getNotificationsFromServer(false);
            setTechnicianCallStatus();

            tutorialIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startToolbarTutorial();
                }
            });

            notificationIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNotificationsList();
                }
            });

            technicianRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkTechCalls();

//                    long technicianCallTime = PersistenceManager.getInstance().getTechnicianCallTime();
//                    long now = new Date().getTime();
//                    if (!(technicianCallTime > 0 && technicianCallTime > (now - TECHNICIAN_CALL_WAITING_RESPONSE))) {
//                        openTechniciansList();
//                    }
                }
            });

            mJobsSpinner = mToolBarView.findViewById(R.id.toolbar_job_spinner);

            if (mJobsSpinnerAdapter == null) {
                // we generate the data for the spinner, approve button can be disabled so created seperatly
                mJobActionsSpinnerItems = new ArrayList<>();

                String[] options = getResources().getStringArray(R.array.jobs_spinner_array);

                for (int i = 0; i < options.length; i++) {
                    mJobActionsSpinnerItems.add(new JobActionsSpinnerItem(i, options[i]));
                }
                // add approve first item with unique ID;
                mApproveItemID = mJobActionsSpinnerItems.size();
                mJobActionsSpinnerItems.add(new JobActionsSpinnerItem(mApproveItemID, getString(R.string.approve_first_item)));

                mJobsSpinnerAdapter = new JobsSpinnerAdapter(getActivity(), R.layout.spinner_job_item, mJobActionsSpinnerItems);
            }

            mJobsSpinner.setAdapter(mJobsSpinnerAdapter);

            mJobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), PorterDuff.Mode.SRC_ATOP);

            mJobsSpinner.setSpinnerEventsListener(this);

            mJobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null || mCurrentMachineStatus.getAllMachinesData().size() == 0) {
                        OppAppLogger.getInstance().w(LOG_TAG, "missing machine status data in job spinner");
                        return;
                    }
                    mJobsSpinnerAdapter.updateSelectedId(mJobActionsSpinnerItems.get(position).getUniqueID());
                    if (mJobActionsSpinnerItems.get(position).isEnabled()) {
                        switch (position) {
                            case 0: {
                                openActivateJobScreen();
                                break;
                            }
                            case 1: {
                                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                    mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition, mCurrentMachineStatus.getAllMachinesData().get(0).getRejectMesuaring()), true, true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition, mCurrentMachineStatus.getAllMachinesData().get(0).getRejectMesuaring()), true, true);
                                }
                                break;
                            }
                            case 2: {
                                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                    mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true, true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true, true);
                                }
                                break;
                            }
                            case 3: {
                                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                    mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true, true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true, true);
                                }
                                break;
                            }
                            case 4: {
                                openSetupEndFragment();
                                break;
                            }
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            setupOperatorSpinner();
            //setupProductionStatusSpinner();
            setLanguageSpinner(mToolBarView);

            mMachineIdStatusBarTextView = mToolBarView.findViewById(R.id.text_view_machine_id_name);
            mMachineIdStatusBarTextView.setSelected(true);
            mMachineStatusStatusBarTextView = mToolBarView.findViewById(R.id.text_view_machine_status);
            mMachineStatusStatusBarTextView.setSelected(true);
            mStatusIndicatorImageView = mToolBarView.findViewById(R.id.job_indicator);
            mToolBarView.findViewById(R.id.ATATV_job_indicator_ly).setOnClickListener(this);

            if (mMachineStatusLayout == null) {
                mMachineStatusLayout = mToolBarView.findViewById(R.id.linearLayout2);
                mMachineStatusLayout.setVisibility(View.INVISIBLE);
            }
            ImageView settingsButton = mToolBarView.findViewById(R.id.settings_button);
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnGoToScreenListener.goToFragment(SettingsFragment.newInstance(), false, true);
                }
            });
            actionBar.setCustomView(mToolBarView);

            setToolBarHeight(mToolBarView);

            if (mCurrentMachineStatus != null && mCurrentMachineStatus.getAllMachinesData().size() > 0) {

                onDeviceStatusChanged(mCurrentMachineStatus);
            }

            initLenoxView(notificationIv, technicianRl, tutorialIv, mJobsSpinner);

        }

        if ((!BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) && PersistenceManager.getInstance().isDisplayToolbarTutorial()) {
            startToolbarTutorial();
        }

    }

    public void openSetupEndFragment() {
//        if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
//            mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true, true);
//        } else {
//            mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true, true);
//        }
        mListener.onShowSetupEndDialog();
    }

    private void checkTechCalls() {
        ArrayList<TechCallInfo> tech = PersistenceManager.getInstance().getCalledTechnician();
        if (tech != null && tech.size() > 0) {
            for (TechCallInfo call : tech) {
                if (call.isOpenCall()) {
                    openDeleteTechCallDialog(tech);
                    return;
                }
            }

        }

        openTechniciansList();
    }

    private void openDeleteTechCallDialog(ArrayList<TechCallInfo> tech) {
        mPopUpDialog = new TechCallDialog(getActivity(), tech, new TechCallDialog.TechDialogListener() {
            @Override
            public void onNewCallPressed() {
                mPopUpDialog.dismiss();
                openTechniciansList();
            }

            @Override
            public void onCancelPressed() {
                mPopUpDialog.dismiss();
            }

            @Override
            public void onCleanTech(TechCallInfo techCallInfo) {
                cleanTech(0, techCallInfo);
                getNotificationsFromServer(false);
                setTechnicianCallStatus();
            }
        });
        mPopUpDialog.show();
    }

    public void openActivateJobScreen() {
        OppAppLogger.getInstance().d(LOG_TAG, "New Job");
        if (PersistenceManager.getInstance().getVersion() >= MINIMUM_VERSION_FOR_NEW_ACTIVATE_JOB) {
            mListener.onJobActionItemClick();
        } else {
            mOnGoToScreenListener.goToFragment(new JobsFragment(), true, true);
        }
    }

    public LayoutInflater initActionBar(ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        SpannableString spannableString = new SpannableString(getActivity().getString(R.string.screen_title));
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.white)), 0, spannableString.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.T12_color)), spannableString.length() - 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return LayoutInflater.from(getActivity());
    }

    public void initLenoxView(ImageView notificationIv, View technicianIv, ImageView
            tutorialIv, EmeraldSpinner jobsSpinner) {
        if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
            notificationIv.setVisibility(View.INVISIBLE);
            technicianIv.setVisibility(View.INVISIBLE);
            tutorialIv.setVisibility(View.INVISIBLE);
            jobsSpinner.setVisibility(View.GONE);
            mMainView.findViewById(R.id.FAAE_product_ly).setVisibility(View.GONE);
            mMainView.findViewById(R.id.FAAE_product_ly_separator1).setVisibility(View.GONE);
            mMainView.findViewById(R.id.FAAE_josh_id_ly).setVisibility(View.GONE);
            mMainView.findViewById(R.id.FAAE_josh_id_ly_separator2).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_production_spinner).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_operator_fl).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_technician_button).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_production_spinner_separator).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_production_text).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_job_spinner_separator).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_after_job_spinner_separator).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.ATATV_language_spinner).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.iv_user_icon).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_machine_separator).setVisibility(View.GONE);
            View userIcon = mToolBarView.findViewById(R.id.settings_button);
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) userIcon.getLayoutParams();
            param.setMarginEnd((int) (-10 * getActivity().getResources().getDisplayMetrics().density));
            userIcon.setLayoutParams(param);
            ((ImageView) mToolBarView.findViewById(R.id.ATATV_flavor_logo)).setImageDrawable(getResources().getDrawable(R.drawable.lenox_logo_new_medium));
            mToolBarView.findViewById(R.id.ATATV_company_name_separator).setVisibility(View.VISIBLE);
            ((TextView) mToolBarView.findViewById(R.id.ATATV_company_name_tv)).setText(PersistenceManager.getInstance().getSiteName());
        }
    }

    private void setTechnicianCallStatus() {

        ArrayList<TechCallInfo> techList = PersistenceManager.getInstance().getCalledTechnician();
        int callId = PersistenceManager.getInstance().getRecentTechCallId();
        if (techList != null && techList.size() > 0) {
            TechCallInfo techCallInfo = techList.get(0);
            for (TechCallInfo call : techList) {
                if (callId > 0 && call.getmNotificationId() == callId) {
                    techCallInfo = call;
                }
            }

            long technicianCallTime = techCallInfo.getmCallTime();
            long now = new Date().getTime();

            setTimeForTechTimer(techCallInfo);

            long delay = -1;
            switch (techCallInfo.getmResponseType()) {
                case Consts.NOTIFICATION_RESPONSE_TYPE_UNSET:
                    mTechnicianIndicatorTv.setText(getResources().getString(R.string.called_technician) + "\n" + techCallInfo.getmName());
                    mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.called));
                    //calculate 5 minutes from call and display a message
                    if (techCallInfo.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET && technicianCallTime > 0 && technicianCallTime > (now - TECHNICIAN_CALL_WAITING_RESPONSE)) {
                        mHandlerTechnicianCall.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<TechCallInfo> techList2 = PersistenceManager.getInstance().getCalledTechnician();
                                if (techList2 != null && techList2.size() > 0) {
                                    TechCallInfo techCallInfo2 = techList2.get(techList2.size() - 1);
                                    //if (PersistenceManager.getInstance().getTechnicianCallTime() > 0) {
                                    if (techCallInfo2.getmCallTime() > 0 && techCallInfo2.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET) {
                                        final AlertDialog.Builder builder;
                                        builder = new AlertDialog.Builder(getActivity());
                                        builder.setCancelable(true);
                                        final AlertDialog alertDialog = builder.setTitle(getResources().getString(R.string.call_technician_title))
                                                .setMessage(getResources().getString(R.string.call_for_technician_unresponsive))
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setIcon(R.drawable.technician_dark)
                                                .show();
                                    }
                                }
                            }
                        }, TECHNICIAN_CALL_WAITING_RESPONSE - (now - technicianCallTime));
                    }
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                    mTechnicianIndicatorTv.setText(getResources().getString(R.string.call_approved) + "\n" + techCallInfo.getmName());
                    mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.message_recieved));
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                    delay = 1000 * 60 * 10;
                    mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.message_declined));
                    mTechnicianIndicatorTv.setText(getResources().getString(R.string.call_declined) + "\n" + techCallInfo.getmName());
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                    mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.at_work));
                    mTechnicianIndicatorTv.setText(getResources().getString(R.string.started_service) + "\n" + techCallInfo.getmName());
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                    delay = 0;
                    mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.work_completed));
                    mTechnicianIndicatorTv.setText(getResources().getString(R.string.service_completed) + "\n" + techCallInfo.getmName());
                    break;
                case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                    delay = 0;
                    mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.cancel));
                    mTechnicianIndicatorTv.setText(getResources().getString(R.string.call_cancelled) + "\n" + techCallInfo.getmName());
                    break;
                default:
                    mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.technicaian));
                    mTechnicianIndicatorTv.setText("");
                    break;
            }
            cleanTech((int) delay, techCallInfo);
        } else {
            mTechnicianIconIv.setImageResource(R.drawable.technicaian);
            mTechnicianIndicatorTv.setText("");
            setTimeForTechTimer(null);
            PersistenceManager.getInstance().setRecentTechCallId(0);
        }

    }

    @SuppressLint("DefaultLocale")
    private void setTimeForTechTimer(final TechCallInfo techCallInfo) {
        if (mCallCounterHandler == null) {
            mCallCounterHandler = new Handler();
        }
        mCallCounterHandler.removeCallbacksAndMessages(null);
        if (techCallInfo == null) {
            mTechnicianTimerTv.setText("");
        } else {
            mCallCounterHandler.post(new Runnable() {
                @Override
                public void run() {
                    long callDuration = new Date().getTime() - techCallInfo.getmCallTime();
                    int postDelay;
                    String callTime;

                    if (callDuration < (60 * 60 * 1000)) {
                        postDelay = 1000 * 60;
                        callTime = String.format("%d Min.", TimeUnit.MILLISECONDS.toMinutes(callDuration));
                    } else if (callDuration < 1000 * 60 * 60 * 24) {
                        postDelay = 1000 * 60;
                        callTime = String.format("%d Hrs., %d Min.", TimeUnit.MILLISECONDS.toHours(callDuration),
                                TimeUnit.MILLISECONDS.toMinutes(callDuration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(callDuration)));
                    } else {
                        postDelay = 1000 * 60 * 60;
                        callTime = String.format("%d Days, %d Hrs.", TimeUnit.MILLISECONDS.toDays(callDuration),
                                TimeUnit.MILLISECONDS.toHours(callDuration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(callDuration)));
                    }

                    mTechnicianTimerTv.setText(callTime);
                    if (mCallCounterHandler == null) {
                        mCallCounterHandler = new Handler();
                    }
                    mCallCounterHandler.removeCallbacksAndMessages(null);
                    mCallCounterHandler.postDelayed(this, postDelay);
                }
            });
        }
    }

    private void cleanTech(int delay, final TechCallInfo techCallInfo) {

        mHandlerTechnicianCall.removeCallbacksAndMessages(null);
        if (delay >= 0) {
            mHandlerTechnicianCall.postDelayed(new Runnable() {
                @Override
                public void run() {

                    ArrayList<TechCallInfo> list = PersistenceManager.getInstance().getCalledTechnician();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getmNotificationId() == techCallInfo.getmNotificationId()) {
                                list.remove(i);
                                break;
                            }
                        }
                        PersistenceManager.getInstance().setCalledTechnicianList(list);
                    }
                    if (list.size() > 0) {
                        PersistenceManager.getInstance().setRecentTechCallId(list.get(0).getmNotificationId());
                        setTechnicianCallStatus();
                    } else {
                        PersistenceManager.getInstance().setRecentTechCallId(-1);
                        mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.technicaian));
                        mTechnicianIndicatorTv.setText("");
                    }
                }
            }, delay);
        }
    }

    private void setNotificationNeedResponse() {
        int counter = 0;
        for (Notification item : PersistenceManager.getInstance().getNotificationHistory()) {
            if (item.getmNotificationType() != Consts.NOTIFICATION_TYPE_TECHNICIAN && item.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET) {
                counter++;
            }
        }

        if (counter > 0) {
            mNotificationIndicatorCircleFl.setVisibility(View.VISIBLE);
            mNotificationIndicatorNumTv.setText(counter + "");
        } else {
            mNotificationIndicatorCircleFl.setVisibility(View.INVISIBLE);
            mNotificationIndicatorNumTv.setText(counter + "");
        }
    }

    private void openTechniciansList() {
        if ((getActivity()) == null || !(getActivity() instanceof DashboardActivity)) {
            return;
        }

        final List<Technician> techniciansList = ((DashboardActivity) getActivity()).getReportForMachine().getTechnicians();

        Collections.sort(techniciansList, new Comparator<Technician>() {
            @Override
            public int compare(Technician o1, Technician o2) {
                if (OperatorApplication.isEnglishLang()) {
                    return o1.getEName().compareTo(o2.getEName());
                } else {
                    return o1.getLName().compareTo(o2.getLName());
                }
            }
        });

        if (mPopUpDialog != null && mPopUpDialog.isShowing()) {
            mPopUpDialog.dismiss();
        }

        mPopUpDialog = new Dialog(getActivity());
        mPopUpDialog.setContentView(R.layout.dialog_list);
        mPopUpDialog.setCanceledOnTouchOutside(true);

        Window window = mPopUpDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.START | Gravity.TOP;
        if (!OperatorApplication.isEnglishLang()) {
            wlp.x = Gravity.START | Gravity.TOP;
        }
        wlp.y = (int) mTollBarsHeight;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        final ListView technicianLv = mPopUpDialog.findViewById(R.id.dialog_list_scrollview);
        TextView technicianTv = mPopUpDialog.findViewById(R.id.dialog_list_tv);
        technicianTv.setText(R.string.technician_list);

        final TechnicianSpinnerAdapter technicianSpinnerAdapter = new TechnicianSpinnerAdapter(getActivity(), R.layout.base_spinner_item, techniciansList);
        technicianLv.setAdapter(technicianSpinnerAdapter);

        technicianLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PersistenceManager pm = PersistenceManager.getInstance();

                ProgressDialogManager.show(getActivity());

                String body;
                String operatorName = "";
                String title = getString(R.string.operator) + " ";
                if (pm.getOperatorName() != null && pm.getOperatorName().length() > 0) {
                    operatorName = pm.getOperatorName();
                } else {
                    operatorName = pm.getUserName();
                }
                body = getString(R.string.service_call_made_new);
                body = String.format(body, operatorName);
                body += " " + pm.getMachineName();
                title += operatorName;

                final String techName = OperatorApplication.isEnglishLang() ? techniciansList.get(position).getEName() : techniciansList.get(position).getLName();
                String sourceUserId = PersistenceManager.getInstance().getOperatorId();
                if (sourceUserId == null || sourceUserId.equals("")) {
                    sourceUserId = "0";
                }
                final int technicianId = techniciansList.get(position).getID();

                PostTechnicianCallRequest request = new PostTechnicianCallRequest(pm.getSessionId(), pm.getMachineId(), title, technicianId, body, operatorName, techniciansList.get(position).getEName(), sourceUserId);
                NetworkManager.getInstance().postTechnicianCall(request, new Callback<ErrorResponseNewVersion>() {
                    @Override
                    public void onResponse(@NonNull Call<ErrorResponseNewVersion> call, @NonNull Response<ErrorResponseNewVersion> response) {
                        if (response.body() != null && response.body().getmError() == null) {

                            mCallCounterHandler.removeCallbacksAndMessages(null);
                            PersistenceManager.getInstance().setTechnicianCallTime(Calendar.getInstance().getTimeInMillis());
                            PersistenceManager.getInstance().setCalledTechnicianName(techName);

                            TechCallInfo techCall = new TechCallInfo(0, techName, getString(R.string.called_technician) + "\n" + techName, Calendar.getInstance().getTimeInMillis(), response.body().getmLeaderRecordID(), technicianId);
                            PersistenceManager.getInstance().setCalledTechnician(techCall);
                            PersistenceManager.getInstance().setRecentTechCallId(techCall.getmNotificationId());
                            setTechnicianCallStatus();
                            getNotificationsFromServer(false);
                            ProgressDialogManager.dismiss();


                            Tracker tracker = ((OperatorApplication) getActivity().getApplication()).getDefaultTracker();
                            tracker.setHostname(PersistenceManager.getInstance().getSiteName());
                            tracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("Technician Call")
                                    .setAction("Technician was called Successfully")
                                    .setLabel("technician name: " + techName)
                                    .build());
                        } else {
                            String msg = "failed";
                            if (response.body() != null && response.body().getmError() != null) {
                                msg = response.body().getmError().getErrorDesc();
                            }
                            onFailure(call, new Throwable(msg));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ErrorResponseNewVersion> call, @NonNull Throwable t) {
                        ProgressDialogManager.dismiss();
                        PersistenceManager.getInstance().setCalledTechnicianName("");

                        String m = "";
                        if (t != null && t.getMessage() != null) {
                            m = t.getMessage();
                        }
                        Tracker tracker = ((OperatorApplication) getActivity().getApplication()).getDefaultTracker();
                        tracker.setHostname(PersistenceManager.getInstance().getSiteName());
                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Technician Call")
                                .setAction("Call for Technician failed")
                                .setLabel("reason: " + m)
                                .build());

                        final GenericDialog dialog = new GenericDialog(getActivity(), t.getMessage(), getString(R.string.call_technician_title), getString(R.string.ok), true);
                        dialog.setListener(new GenericDialog.OnGenericDialogListener() {
                            @Override
                            public void onActionYes() {
                                dialog.cancel();
                            }

                            @Override
                            public void onActionNo() {
                            }

                            @Override
                            public void onActionAnother() {
                            }
                        });
                        dialog.show();

                        //ShowCrouton.showSimpleCrouton(((DashboardActivity) getActivity()), "Call for Technician failed", CroutonCreator.CroutonType.ALERT_DIALOG);
                    }
                });

                mPopUpDialog.dismiss();
            }
        });

        mPopUpDialog.show();

    }

    private void openNotificationsList() {

        if (mPopUpDialog != null && mPopUpDialog.isShowing()) {
            mPopUpDialog.dismiss();
        }

        mPopUpDialog = new Dialog(getActivity());
        mPopUpDialog.setCanceledOnTouchOutside(true);

        Window window = mPopUpDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.END | Gravity.TOP;
        if (!OperatorApplication.isEnglishLang()) {
            wlp.x = Gravity.END | Gravity.TOP;
        }
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        final ArrayList<Notification> notificationList = PersistenceManager.getInstance().getNotificationHistory();

        if (notificationList.size() > 0) {

            mPopUpDialog.setContentView(R.layout.notification_view_pager);

            final ViewPager vpDialog = mPopUpDialog.findViewById(R.id.NVP_view_pager);
            final TextView rightTab = mPopUpDialog.findViewById(R.id.NVP_view_pager_tv_right_tab);
            final TextView leftTab = mPopUpDialog.findViewById(R.id.NVP_view_pager_tv_left_tab);
            final View rightTabUnderline = mPopUpDialog.findViewById(R.id.NVP_view_pager_right_tab_underline);
            final View leftTabUnderline = mPopUpDialog.findViewById(R.id.NVP_view_pager_left_tab_underline);
            final SwipeRefreshLayout swipeRefresh = mPopUpDialog.findViewById(R.id.NVP_swipe);


            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getNotificationsFromServer(true);
                }
            });
            vpDialog.setAdapter(new NotificationsPagerAdapter(getActivity(), notificationList, new NotificationHistoryAdapter.OnNotificationResponseSelected() {
                @Override
                public void onNotificationResponse(int notificationId, int responseType) {
                    sendNotificationResponse(notificationId, responseType);
                    mPopUpDialog.dismiss();
                }
            }));

            vpDialog.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            leftTab.setTextColor(getResources().getColor(R.color.tabNotificationColor));
                            leftTabUnderline.setVisibility(View.VISIBLE);
                            rightTab.setTextColor(getResources().getColor(R.color.dark_indigo));
                            rightTabUnderline.setVisibility(View.INVISIBLE);
                            break;
                        case 1:
                            rightTab.setTextColor(getResources().getColor(R.color.tabNotificationColor));
                            rightTabUnderline.setVisibility(View.VISIBLE);
                            leftTab.setTextColor(getResources().getColor(R.color.dark_indigo));
                            leftTabUnderline.setVisibility(View.INVISIBLE);
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (swipeRefresh != null) {
                        swipeRefresh.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
                    }
                }
            });

            leftTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpDialog.setCurrentItem(0);
                }
            });

            rightTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vpDialog.setCurrentItem(1);
                }
            });

            Point point = new Point(1, 1);
            ((WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
            mPopUpDialog.getWindow().setLayout((int) (point.x / 3), point.y - 50);

            vpDialog.setCurrentItem(0);


        } else {
            TextView tv = new TextView(getActivity());
            tv.setPadding(20, 50, 20, 50);
            tv.setText(getString(R.string.no_notification_to_show));
            tv.setTextSize(16);
            tv.setGravity(Gravity.CENTER);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            mPopUpDialog.setContentView(tv);

            mPopUpDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        mPopUpDialog.show();


        Tracker tracker = ((OperatorApplication) getActivity().getApplication()).getDefaultTracker();
        tracker.setHostname(PersistenceManager.getInstance().getSiteName());
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Notifications dialog")
                .setAction("Notifications dialog was opened")
                .setLabel("Notifications dialog was opened")
                .build());
    }

    private void sendNotificationResponse(final int notificationId, final int responseType) {

        final PersistenceManager pm = PersistenceManager.getInstance();
        final Notification[] notification = new Notification[1];
        for (Notification item : pm.getNotificationHistory()) {
            if (item.getmNotificationID() == notificationId) {
                notification[0] = item;
                break;
            }
        }

        if (notification[0] != null) {

            String opId = pm.getOperatorId();
            if (opId == null || opId.equals("")) {
                opId = "0";
            }
            RespondToNotificationRequest request = new RespondToNotificationRequest(pm.getSessionId(),
                    getResources().getString(R.string.respond_notification_title),
                    notification[0].getmBody(getActivity()),
                    pm.getMachineId() + "",
                    notification[0].getmNotificationID() + "",
                    responseType,
                    Consts.NOTIFICATION_TYPE_FROM_WEB,
                    Consts.NOTIFICATION_RESPONSE_TARGET_WEB,
                    opId,
                    pm.getOperatorName(),
                    notification[0].getmSender(),
                    opId,
                    notification[0].getmTargetUserId() + "");

            ProgressDialogManager.show(getActivity());
            NetworkManager.getInstance().postResponseToNotification(request, new Callback<ErrorResponseNewVersion>() {
                @Override
                public void onResponse(@NonNull Call<ErrorResponseNewVersion> call, @NonNull Response<ErrorResponseNewVersion> response) {

                    ArrayList<Notification> nList = pm.getNotificationHistory();
                    notification[0].setmResponseType(responseType);
                    nList.add(notification[0]);
                    pm.setNotificationHistory(nList);
                    setNotificationNeedResponse();
                    if (ProgressDialogManager.isShowing()) {
                        ProgressDialogManager.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ErrorResponseNewVersion> call, @NonNull Throwable t) {
                    if (ProgressDialogManager.isShowing()) {
                        ProgressDialogManager.dismiss();
                    }
                }
            });
        }
    }

    public void setupProductionStatusSpinner() {

        if (getActivity() == null) {
            return;
        }

        int selected = 0;

        final EmeraldSpinner productionStatusSpinner = mToolBarView.findViewById(R.id.toolbar_production_spinner);
        final LinearLayout productionStatusSpinnerLil = mToolBarView.findViewById(R.id.toolbar_production_spinner_lil);
        productionStatusSpinnerLil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productionStatusSpinner.performClick();
            }
        });

        productionStatusSpinner.setVisibility(View.VISIBLE);
        final TextView textview = mToolBarView.findViewById(R.id.toolbar_production_text);
        ReportFieldsForMachine reportForMachine = ((DashboardActivity) getActivity()).getReportForMachine();
        List<PackageTypes> statusList = new ArrayList<>();
        if (reportForMachine != null && reportForMachine.getProductionStatus() != null) {
            statusList = reportForMachine.getProductionStatus();
            for (int i = 0; i < statusList.size(); i++) {
                if (statusList.get(i).getId() == mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID()) {
                    selected = i;
                }
            }
        }

        if (statusList.size() == 0) {
            productionStatusSpinner.setVisibility(View.GONE);
            return;
        }
        textview.setText(OperatorApplication.isEnglishLang() ? statusList.get(selected).getEName() : statusList.get(selected).getLName());
        final ArrayAdapter<PackageTypes> productionStatusSpinnerAdapter = new ProductionSpinnerAdapter(getActivity(), R.layout.spinner_production_item, statusList, selected);
        productionStatusSpinner.setAdapter(productionStatusSpinnerAdapter);
        productionStatusSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), PorterDuff.Mode.SRC_ATOP);

        final List<PackageTypes> finalStatusList = statusList;
        productionStatusSpinner.setSpinnerEventsListener(this);
        productionStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mCurrentMachineStatus.getAllMachinesData().get(0).ismCustomerIsActivateJobs() &&
                        ((PackageTypes) productionStatusSpinner.getItemAtPosition(productionStatusSpinner.getSelectedItemPosition())).getId() == PRODUCTION_ID) {

                    openActivateJobScreen();

                } else if (mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() != finalStatusList.get(position).getId()) {
                    ProgressDialogManager.show(getActivity());
                    mListener.onProductionStatusChanged(finalStatusList.get(position).getId(), finalStatusList.get(position).getEName());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupOperatorSpinner() {

        if (getActivity() == null) {
            return;
        }

        mOperatorsSpinnerArray.clear();
        EmeraldSpinner operatorsSpinner = mToolBarView.findViewById(R.id.toolbar_operator_spinner);
        TextView operatorTitle = mToolBarView.findViewById(R.id.toolbar_operator_tv);

        if (PersistenceManager.getInstance().getOperatorName() == null || PersistenceManager.getInstance().getOperatorName().isEmpty()) {

            FrameLayout operatorFl = mToolBarView.findViewById(R.id.toolbar_operator_fl);

            operatorTitle.setVisibility(View.VISIBLE);
            operatorsSpinner.setVisibility(View.GONE);

            operatorFl.setMinimumWidth(operatorTitle.getWidth());

            operatorFl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnGoToScreenListener.goToFragment(new SignInOperatorFragment(), true, true);
                    mListener.showWhiteFilter(false);
                }
            });
        } else {
            operatorTitle.setVisibility(View.GONE);
            operatorsSpinner.setVisibility(View.VISIBLE);

            mOperatorsSpinnerArray.add(getResources().getString(R.string.switch_user));
            mOperatorsSpinnerArray.add(getResources().getString(R.string.sign_out));

            final ArrayAdapter<String> operatorSpinnerAdapter = new OperatorSpinnerAdapter(getActivity(), R.layout.spinner_operator_item, mOperatorsSpinnerArray.toArray(new String[mOperatorsSpinnerArray.size()]), PersistenceManager.getInstance().getOperatorName());
            operatorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            operatorsSpinner.setAdapter(operatorSpinnerAdapter);
            operatorsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), PorterDuff.Mode.SRC_ATOP);
            operatorsSpinner.setSpinnerEventsListener(this);
            operatorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        mOnGoToScreenListener.goToFragment(new SignInOperatorFragment(), true, true);
                        mListener.showWhiteFilter(false);
                    } else if (position == 1) {
                        mOperatorCore.setOperatorForMachine("");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void startToolbarTutorial() {
        //if (PersistenceManager.getInstance().isDisplayToolbarTutorial()){
        List<TapTarget> targets = new ArrayList<>();

        if (getView() != null) {

            targets.add(TapTarget.forView(getView().findViewById(R.id.parent_layouts), getString(R.string.tutorial_swipe)).targetRadius(100).icon(getResources().getDrawable(R.drawable.swipe_arrows)));

        }

        targets.add(TapTarget.forView(mToolBarView.findViewById(R.id.toolbar_job_spinner), getString(R.string.tutorial_job_actions), getString(R.string.tutorial_job_actions_desc)).targetRadius(100));
        targets.add(TapTarget.forView(mMinDurationLil, getString(R.string.stop_event), getString(R.string.tutorial_event_log_desc)).targetRadius(100));

        if (getActivity() == null) {
            return;
        }
        TapTargetSequence tapSeq = new TapTargetSequence(getActivity()).targets(targets).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                PersistenceManager.getInstance().setDisplayToolbarTutorial(false);
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                PersistenceManager.getInstance().setDisplayToolbarTutorial(false);
            }
        });

        tapSeq.start();

    }

    private void setToolBarHeight(final View view) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                OppAppLogger.getInstance().d(LOG_TAG, "onPreDraw(), start ");
                if (view.getViewTreeObserver().isAlive()) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                ViewGroup.LayoutParams toolBarParams;
                toolBarParams = view.getLayoutParams();
                toolBarParams.height = (int) (mTollBarsHeight * 0.65);
                OppAppLogger.getInstance().d(LOG_TAG, "onPreDraw(), pre request layout ");
                view.requestLayout();
                OppAppLogger.getInstance().d(LOG_TAG, "onPreDraw(), end ");
//        TODO        dismissProgressDialog();
                return true;
            }
        });
    }

    @Override
    public void onDismissClick(DialogInterface dialog, int requestCode) {

    }

    public void setAlertChecked() {

        if (mLastEvent != null && mLastEvent.getEventGroupID() == TYPE_ALERT) {

            mLastEvent.setTreated(true);

            mLastEvent.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(mLastEvent.getEventID()));

            Cursor cursor = getCursorByType();
            setShiftLogAdapter(cursor);

            if (mShiftLogAdapter != null) {
                mShiftLogRecycler.setAdapter(mShiftLogAdapter);
            }
        }
    }

    private void openDialog(Event event) {

        String message;

        if (PersistenceManager.getInstance().getCurrentLang().equals("en")) {

            message = event.getSubtitleEname();

        } else {

            message = event.getSubtitleLname();
        }


        message += " " + getString(R.string.alarm);

        message += " " + event.getAlarmValue();

        ShowCrouton.jobsLoadingAlertCrouton(mCroutonCallback, message);

    }

    @Override
    public void onDismissAllClick(DialogInterface dialog, int eventGroupId, int requestCode) {
        dialog.dismiss();
        Iterator<Event> iterator = mEventsQueue.iterator();
        while (iterator.hasNext()) {
            Event next = iterator.next();
            if (next.getEventGroupID() == eventGroupId) {
                //                event.setAlarmDismissed(true);
                next.setIsDismiss(true);
                iterator.remove();
            }
        }
//        openNextDialog();
    }

    @Override
    public void onReportClick(Event event) {
        //TODO    openStopReportScreen(eventId, start, end, duration);mEvent.getEventID(), mEvent.getTime(), mEvent.getEventEndTime(), mEvent.getDuration()
        startSelectMode(event);
    }

    @Override
    public void onStopClicked(int eventId, String startTime, String endTime, long duration) {

        //TODO    openStopReportScreen(eventId, startTime, endTime, duration);
    }

    @Override
    public void onStopEventSelected(Integer event, boolean b) {//todo kuti
        mListener.onEventSelected(event, b);
    }

    @Override
    public void onSplitEventPressed(int eventID) {
        // TODO: 05/07/2018 call server split event


        mListener.onSplitEventPressed(eventID);

    }

    @Override
    public void onLastItemUpdated() {

        mListener.onLastShiftItemUpdated();
    }

    @Override
    public void onSelectMode(Event event) {

        startSelectMode(event);
    }

    public void startSelectMode(Event event) {
        if (event == null) {
            ArrayList<Event> events = mDatabaseHelper.getListFromCursor(getCursorByType());
            if (events.size() > 0) {
                event = events.get(events.size() - 1);
            }
        }

        mListener.onOpenReportStopReasonFragment(ReportStopReasonFragment.newInstance(mIsOpen, mActiveJobsListForMachine, mSelectedPosition));

        mIsSelectionMode = true;
        mFirstSeletedEvent = event;

        Cursor cursor;
        cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), mFirstSeletedEvent.getEventReasonID());
        setShiftLogAdapter(cursor);
//        initEvents(mDatabaseHelper.getListFromCursor(getCursorByType()));
        onStopEventSelected(event.getEventID(), true);

        mShowAlarmCheckBox.setVisibility(View.GONE);
    }

    public void setShiftLogAdapter(Cursor cursor) {
        mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), cursor,
                !mIsOpen, mCloseWidth, this, mOpenWidth, mRecyclersHeight,
                mIsSelectionMode, mSelectedEvents);

        mShiftLogRecycler.setAdapter(mShiftLogAdapter);
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {
        OppAppLogger.getInstance().i(LOG_TAG, "onDeviceStatusChanged()");

        mCurrentMachineStatus = machineStatus;

        initStatusLayout(mCurrentMachineStatus);

        mMinDurationText.setText(String.format(Locale.getDefault(), "%d %s", machineStatus.getAllMachinesData().get(0).getMinEventDuration(), getString(R.string.minutes)));


//        if (mSwipeToRefresh.isRefreshing()){
//            mSwipeToRefresh.setRefreshing(false);
//        }

        if (!BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {

            setupProductionStatusSpinner();

            setupOperatorSpinner();

            disableActionInSpinner(machineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1, mJobActionsSpinnerItems.get(1).getUniqueID());
            disableActionInSpinner(machineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1, mJobActionsSpinnerItems.get(2).getUniqueID());
            disableActionInSpinner(machineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1, mJobActionsSpinnerItems.get(3).getUniqueID());

            if (!mEndSetupDisable) {
                disableActionInSpinner(machineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                                && machineStatus.getAllMachinesData().get(0).canReportApproveFirstItem()
                        , mJobActionsSpinnerItems.get(4).getUniqueID());
            } else {
                mEndSetupDisable = false;
            }
        }

    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {

        if (mShiftLogSwipeRefresh.isRefreshing()) {
            mShiftLogSwipeRefresh.setRefreshing(false);
        }
    }


    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events) {

        int deletedEvents = clearOver24HShift();

        if (mShiftLogSwipeRefresh.isRefreshing()) {
            mShiftLogSwipeRefresh.setRefreshing(false);
        }

//        if (!mIsSelectionMode) {
        mLoadingDataText.setVisibility(View.GONE);

        if (events != null && events.size() > 0) {

            HashMap<Integer, ArrayList<Integer>> checkedAlarmsHashMap = PersistenceManager.getInstance().getCheckedAlarms();

            ArrayList<Integer> checkedAlarms = checkedAlarmsHashMap.get(PersistenceManager.getInstance().getMachineId());

            mEventsQueue.clear();

            PersistenceManager.getInstance().setShiftLogStartingFrom(com.operatorsapp.utils.TimeUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));

            mNoData = false;

            for (Event event : events) {
                event.setTimeOfAdded(System.currentTimeMillis());

                if (!mIsNewShiftLogs) {
                    event.setIsDismiss(true);
                }

            }
            boolean mustBeClosed = false;
            for (Event event : events) {

                if (mAutoSelectMode && event.getEventEndTime() != null && event.getEventEndTime().length() > 0 &&
                        mFirstSeletedEvent != null && mSelectedEvents != null
                        && mSelectedEvents.size() == 1) {

                    mustBeClosed = true;
                }

                if (DataSupport.count(Event.class) == 0 || !DataSupport.isExist(Event.class, DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()))) {

                    event.save();

                    if (mIsNewShiftLogs) {
                        mEventsQueue.add(event);
                    }
                    if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
                        addCheckedAlarms(checkedAlarms, event);
                    }
                } else {

                    addCheckedAlarms(checkedAlarms, event);
                }
            }

            checkedAlarmsHashMap.remove(PersistenceManager.getInstance().getMachineId());

            PersistenceManager.getInstance().setCheckedAlarms(checkedAlarmsHashMap);

            mNoNotificationsText.setVisibility(View.GONE);

            Cursor cursor;
            if (mIsSelectionMode) {

                cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), mFirstSeletedEvent.getEventReasonID());

            } else {

                cursor = getCursorByType();
            }
            setShiftLogAdapter(cursor);

            initEvents(mDatabaseHelper.getListFromCursor(getCursorByType()));


            if (mEventsQueue.size() > 0) {
                Event event = mEventsQueue.peek();
                // we only need to show the stop report when the event is open (no end time) and hase no event reason ( == 0).
                if (event.getEventGroupID() != TYPE_ALERT && TextUtils.isEmpty(event.getEventEndTime()) && event.getEventReasonID() == REASON_UNREPORTED) {

                    if (!mIsSelectionMode) {
                        startSelectMode(event);
                        mAutoSelectMode = true;
                    } else if (mustBeClosed) {
                        mListener.onClearAllSelectedEvents();
                    }
                    mEventsQueue.pop();

                } else if (event.getEventGroupID() == TYPE_ALERT) {
                    openDialog(event);
                    mLastEvent = event;
                    mEventsQueue.pop();
                    if (mustBeClosed) {
                        mListener.onClearAllSelectedEvents();
                    }
                }

            } else if (mustBeClosed) {
                mListener.onClearAllSelectedEvents();
            }
        } else {
            if (DataSupport.count(Event.class) == 0) {
                mNoData = true;
                mNoNotificationsText.setVisibility(View.VISIBLE);
            }
            if (deletedEvents > 0) {

                Cursor cursor;
                if (mIsSelectionMode) {

                    cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), mFirstSeletedEvent.getEventReasonID());

                } else {

                    cursor = getCursorByType();
                }
                setShiftLogAdapter(cursor);
                initEvents(mDatabaseHelper.getListFromCursor(getCursorByType()));
            }
        }

        mIsNewShiftLogs = true;
        PersistenceManager.getInstance().setIsNewShiftLogs(true);

        if (mShiftLogAdapter != null)
            mShiftLogAdapter.notifyDataSetChanged();


    }

    private void initEventRecycler(View view) {
        mEventsRecycler = view.findViewById(R.id.FAAE_events_recycler);
        mEventsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

//        mEventsAdapter = new EventsAdapter(getContext(), this, mIsSelectionMode, mIsOpen);
//
//        mEventsRecycler.setAdapter(mEventsAdapter);
    }

    private void initEvents(ArrayList<Event> events) {//todo kuti

        Collections.reverse(events);

        if (events.size() < 1) {
            return;
        }

        mEventsAdapter = new EventsAdapter(getContext(), this, mIsSelectionMode, mIsOpen, events);

        mEventsRecycler.setAdapter(mEventsAdapter);

//        if (mEventsAdapter != null)
//            mEventsAdapter.setEvents(events);


    }


    public void addCheckedAlarms(ArrayList<Integer> checkedAlarms, Event event) {
        if (checkedAlarms != null) {

            for (Integer integer : checkedAlarms) {

                if (integer == event.getEventID()) {

                    event.setTreated(true);

                    mEventsQueue.remove(event);

                }

            }
        }

        event.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()));
    }

    private int clearOver24HShift() {

        ArrayList<Event> toDelete = new ArrayList<>();

        for (Event event : mDatabaseHelper.getAlEvents()) {

            if (TimeUtils.getLongFromDateString(event.getEventTime(), "dd/MM/yyyy HH:mm:ss")
                    < System.currentTimeMillis() - DAY_IN_MILLIS) {

                toDelete.add(event);

            }
        }

        for (Event event : toDelete) {

            mDatabaseHelper.deleteEvent(event.getEventID());
        }

        return toDelete.size();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {
        disableActionInSpinner(enabled, mApproveItemID);
        mEndSetupDisable = true;

    }

    public void disableActionInSpinner(boolean enabled, int itemId) {
        if (mJobsSpinnerAdapter != null) {
            for (JobActionsSpinnerItem item : mJobActionsSpinnerItems) {
                if (item.getUniqueID() == itemId) {
                    item.setEnabled(enabled);
                }
            }
            mJobsSpinnerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine
                                                                     activeJobsListForMachine) {

        if (activeJobsListForMachine != null) {
            initProductView(activeJobsListForMachine);
            OppAppLogger.getInstance().i(LOG_TAG, "onActiveJobsListForMachineReceived() list size is: " + activeJobsListForMachine.getActiveJobs().size());
        } else {
            OppAppLogger.getInstance().w(LOG_TAG, "onActiveJobsListForMachineReceived() activeJobsListForMachine is null");
        }
    }

    @Override
    public void onDataFailure(final ErrorObjectInterface reason, CallType callType) {

        if (mShiftLogSwipeRefresh.isRefreshing()) {
            mShiftLogSwipeRefresh.setRefreshing(false);
        }

        Log.e(DavidVardi.DAVID_TAG_SPRINT_1_5, "onDataFailure");


        if (callType == CallType.Status) {
            mMachineStatusLayout.setVisibility(View.VISIBLE);
            clearStatusLayout();
        }

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, reason);
                }
            });
        }

        silentLogin(reason);
    }

    private void silentLogin(ErrorObjectInterface reason) {

        if (!thereAlreadyRequest && getActivity() != null) {

            thereAlreadyRequest = true;

            if (reason.getError() == ErrorObjectInterface.ErrorCode.Retrofit) {

                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mCroutonCallback, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {

                        SendBroadcast.refreshPolling(getContext());

                        openSilentLoginOption();
                    }

                    @Override
                    public void onSilentLoginFailed(ErrorObjectInterface reason) {

                        ShowCrouton.operatorLoadingErrorCrouton(mCroutonCallback, reason.getError().toString());
                    }
                });
            }
        }
    }

    private void openSilentLoginOption() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                thereAlreadyRequest = false;
            }
        }, 15000);

    }

    private void initStatusLayout(MachineStatus machineStatus) {

        if (machineStatus == null) {

            return;
        }
        AllMachinesData machinesData = machineStatus.getAllMachinesData().get(0);
        String nameByLang = OperatorApplication.isEnglishLang() ? machinesData.getCurrentProductEname() : machinesData.getCurrentProductName();
        mProductNameTextView.setText(new StringBuilder(nameByLang).append(",").append(" ID - ").append(String.valueOf(machinesData.getCurrentProductID())));
        mJobIdTextView.setText((String.valueOf(machinesData.getCurrentJobName())));
        mShiftIdTextView.setText(String.valueOf(machinesData.getShiftIDString()));
        String machineName = OperatorApplication.isEnglishLang() ? machinesData.getMachineEName() : machinesData.getMachineLname();
        if (TextUtils.isEmpty(machineName)) {
            machineName = getString(R.string.dashes);
        }

        mMachineIdStatusBarTextView.setText(machineName);
        String statusNameByLang = OperatorApplication.isEnglishLang() ? machinesData.getMachineStatusEname() : machinesData.getMachineStatusLName();
        mMachineStatusStatusBarTextView.setText(statusNameByLang);
        statusAggregation(machineStatus);
        mMachineStatusLayout.setVisibility(View.VISIBLE);

        if (machineStatus.getAllMachinesData().get(0).getCurrentStatusTimeMin() > 0) {

            mStatusTimeMinTv.setVisibility(View.VISIBLE);
            mStatusTimeMinTv.setText(TimeUtils.getTimeFromMinute(machineStatus.getAllMachinesData().get(0).getCurrentStatusTimeMin()));
        } else {
            mStatusTimeMinTv.setVisibility(View.GONE);
        }

    }

    private void clearStatusLayout() {
        mProductNameTextView.setText("");
        mJobIdTextView.setText("");
        mShiftIdTextView.setText("");
        mMachineIdStatusBarTextView.setText("");
        mMachineStatusStatusBarTextView.setText("");
        mTimerTextView.setText("");

        if (getActivity() != null) {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_no_data));
        }
    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {
        mTimerTextView.setText(timeToEndInHours);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeBroadcasts();
        if (mHandlerTechnicianCall != null) {
            mHandlerTechnicianCall.removeCallbacksAndMessages(null);
        }
        if (mCallCounterHandler != null) {
            mCallCounterHandler.removeCallbacksAndMessages(null);
        }
    }


    private void removeBroadcasts() {

        if (getActivity() != null) {
            try {

                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReasonBroadcast);
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mNotificationsReceiver);

            } catch (Exception e) {

                if (e.getMessage() != null)
                    Log.e(LOG_TAG, e.getMessage());
            }
        }
    }


    private void statusAggregation(MachineStatus machineStatus) {

        if (getActivity() == null) {

            return;
        }

        int status = machineStatus.getAllMachinesData().get(0).getMachineStatusID();


        if (status == MachineStatus.MachineServerStatus.WORKING_OK.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.working));

        } else if (status == MachineStatus.MachineServerStatus.STOPPED.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.stopped));

        } else if (status == MachineStatus.MachineServerStatus.COMMUNICATION_FAILURE.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.communication_failure));

        } else if (status == MachineStatus.MachineServerStatus.SETUP_COMMUNICATION_FAILURE.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.communication_failure));

        } else if (status == MachineStatus.MachineServerStatus.NO_JOB.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.no_job));

        } else if (status == MachineStatus.MachineServerStatus.SETUP_WORKING.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_indicator_setup));

        } else if (status == MachineStatus.MachineServerStatus.SETUP_STOPPED.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.setup_stopped));

        } else if (status == MachineStatus.MachineServerStatus.PARAMETER_DEVIATION.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.parameter_deviation));

        } else if (status == MachineStatus.MachineServerStatus.STOP_IDLE.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.stop_idle));

        } else {
            OppAppLogger.getInstance().w(LOG_TAG, "Undefined parameter");

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_no_data));
        }

    }

    @Override
    public int getCroutonRoot() {

        return R.id.parent_layouts;

    }

    @Override
    public void onSelectStopReason(int eventId, int reasonId, String en, String il, String
            eSubTitle, String lSubtitle) {

        List<Event> events = DataSupport.where(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(eventId)).find(Event.class);


        if (events != null && events.size() > 0) {
            {

                for (Event event : events) {

                    event.setTreated(true);
                    event.setEventGroupID(reasonId);
                    event.setEventGroupLname(il);
                    event.setmEventGroupEname(en);

                    if (OperatorApplication.isEnglishLang()) {

                        event.setEventSubTitleEname(eSubTitle);

                    } else {

                        event.setEventSubTitleEname(lSubtitle);
                    }


                    event.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(eventId));
                }


//                if (mShiftLogAdapter != null) {
//
//                    mShiftLogAdapter.notifyDataSetChanged();
//
//
//                }
            }

        }
    }

    public void disableSelectMode() {

        mIsSelectionMode = false;

        mFirstSeletedEvent = null;

        mSelectedEvents = null;

        mAutoSelectMode = false;

        Cursor cursor;
        cursor = getCursorByType();
        setShiftLogAdapter(cursor);

        mSelectedNumberLy.setVisibility(View.GONE);

        mShowAlarmCheckBox.setVisibility(View.VISIBLE);


        if (mEventsAdapter != null) {
            mEventsAdapter.setIsSelectionMode(mIsSelectionMode);
            mEventsAdapter.notifyDataSetChanged();
        }

    }

    public void setSelectedEvents(ArrayList<Integer> selectedEvents) {
        mSelectedEvents = selectedEvents;

        if (mShiftLogAdapter != null) {

            mShiftLogAdapter.setSelectedEvents(mSelectedEvents);
        }

        if (mSelectedEvents != null && mSelectedEvents.size() > 0) {

            if (mSelectedNumberLy.getVisibility() == View.GONE) {

                mSelectedNumberLy.setVisibility(View.VISIBLE);

            }

            mSelectedNumberTv.setText(String.format(Locale.getDefault(), "%s %d", getString(R.string.events_selected), mSelectedEvents.size()));

        } else {

            mSelectedNumberLy.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.FAAE_unselect_all:

                mListener.onClearAllSelectedEvents();

                break;
            case R.id.ATATV_job_indicator_ly:

                if (!mEndSetupDisable &&
                        mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                        && mCurrentMachineStatus.getAllMachinesData().get(0).canReportApproveFirstItem()) {
                    openSetupEndFragment();
                }
                break;
        }
    }

    public void setVisiblefragment(Fragment visibleFragment) {

        mVisiblefragment = visibleFragment;

        if (mToolBarView != null) {

            if (mVisiblefragment instanceof ActionBarAndEventsFragment ||
                    mVisiblefragment instanceof RecipeFragment ||
                    mVisiblefragment instanceof WidgetFragment) {

                mToolBarView.findViewById(R.id.toolbar_job_spinner).setVisibility(View.VISIBLE);

            } else {

                mToolBarView.findViewById(R.id.toolbar_job_spinner).setVisibility(View.GONE);
            }
        }
    }

    public void setFromAnotherActivity(boolean fromAnotherActivity) {
        this.mFromAnotherActivity = fromAnotherActivity;
    }

    public void setMachines(final ArrayList<Machine> machines) {

        mMachines = machines;
    }

    @Override
    public void onLenoxMachineClicked(Machine machine) {

        mListener.onLenoxMachineClicked(machine);
    }

    @Override
    public void onSpinnerOpened(Spinner spinner) {
        showBlackFilters();
        setCycleWarningView(false);
    }

    public void showBlackFilters() {
        if (!mSetupEndDialogShow) {
            mStatusBlackFilter.setVisibility(View.VISIBLE);
            mListener.showBlackFilter(true);
        }
    }

    @Override
    public void onSpinnerClosed(Spinner spinner) {
        hideBlackFilters();
        setCycleWarningView(mCycleWarningViewShow);
    }

    public void hideBlackFilters() {
        mStatusBlackFilter.setVisibility(View.GONE);
        mListener.showBlackFilter(false);
    }

    private void setLanguageSpinner(View view) {
//        String currentLanguage = null;
//        for (int i = 0; i < getResources().getStringArray(R.array.language_codes_array).length; i++) {
//            if (getResources().getStringArray(R.array.language_codes_array)[i].equals(PersistenceManager.getInstance().getCurrentLang())) {
//                currentLanguage = getResources().getStringArray(R.array.language_codes_array)[i];
//            }
//        }
        final String[] languagesNames = getResources().getStringArray(R.array.languages_spinner_array);
        mLanguagesSpinner = view.findViewById(R.id.ATATV_language_spinner);
        LanguagesSpinnerAdapterActionBar spinnerArrayAdapter = new LanguagesSpinnerAdapterActionBar(getActivity(), R.layout.spinner_language_item, languagesNames);
        mLanguagesSpinner.setAdapter(spinnerArrayAdapter);

        if (getActivity() != null) {
            mLanguagesSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        for (int i = 0; i < languagesNames.length; i++) {

            if (languagesNames[i].equals(PersistenceManager.getInstance().getCurrentLanguageName())) {
                mLanguagesSpinner.setSelection(i);
            }
        }

        mLanguagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SaveAlarmsHelper.saveAlarmsCheckedLocaly(getActivity());
                PersistenceManager.getInstance().setCurrentLang(getResources().getStringArray(R.array.language_codes_array)[position]);
                PersistenceManager.getInstance().setCurrentLanguageName(languagesNames[position]);
                mListener.onRefreshApplicationRequest();
                sendTokenWithSessionIdToServer();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCycleWarningView(boolean show) {
        if (show && !BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
            mCycleWarningView.setVisibility(View.VISIBLE);
        } else {
            mCycleWarningView.setVisibility(View.GONE);
        }
    }

    public void setCycleWarningViewShow(boolean show) {
        if (show && !BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
            mCycleWarningViewShow = true;
        } else {
            mCycleWarningViewShow = false;
        }
        setCycleWarningView(show);
    }

    private void sendTokenWithSessionIdToServer() {
        final PersistenceManager pm = PersistenceManager.getInstance();
        final String id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        PostNotificationTokenRequest request = new PostNotificationTokenRequest(pm.getSessionId(), pm.getMachineId(), pm.getNotificationToken(), id);
        NetworkManager.getInstance().postNotificationToken(request, new Callback<ErrorResponseNewVersion>() {
            @Override
            public void onResponse(Call<ErrorResponseNewVersion> call, Response<ErrorResponseNewVersion> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    Log.d(LOG_TAG, "token sent");
                    pm.tryToUpdateToken("success + android id: " + id);
                    if (mListener != null) {
                        mListener.onRefreshApplicationRequest();
                    }
                } else {
                    pm.tryToUpdateToken("failed + android id: " + id);
                    Log.d(LOG_TAG, "token failed");
                }
            }

            @Override
            public void onFailure(Call<ErrorResponseNewVersion> call, Throwable t) {
                pm.tryToUpdateToken("failed + android id: " + id);
                pm.setNeedUpdateToken(true);
                Log.d(LOG_TAG, "token failed");
            }
        });

    }


    public void setWhiteFilter(boolean show) {
        if (show && !BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
            mStatusWhiteFilter.setVisibility(View.VISIBLE);
        } else {
            mStatusWhiteFilter.setVisibility(View.GONE);
        }
    }

    private void getNotificationsFromServer(final boolean openNotifications) {

        NetworkManager.getInstance().getNotificationHistory(new Callback<NotificationHistoryResponse>() {
            @Override
            public void onResponse(Call<NotificationHistoryResponse> call, Response<NotificationHistoryResponse> response) {

                if (response != null && response.body() != null && response.body().getmError() == null) {

                    ArrayList<TechCallInfo> techList = PersistenceManager.getInstance().getCalledTechnician();
                    for (Notification not : response.body().getmNotificationsList()) {
                        not.setmSentTime(TimeUtils.getStringNoTFormatForNotification(not.getmSentTime()));
                        not.setmResponseDate(TimeUtils.getStringNoTFormatForNotification(not.getmResponseDate()));
                        if (techList != null && techList.size() > 0) {
                            for (TechCallInfo tech : techList) {
                                if (tech.getmNotificationId() == not.getmNotificationID()) {
                                    tech.setmCallTime(TimeUtils.getLongFromDateString(not.getmResponseDate(), TimeUtils.SIMPLE_FORMAT_FORMAT));
                                    break;
                                }
                            }
                        }
                    }

                    PersistenceManager.getInstance().setNotificationHistory(response.body().getmNotificationsList());
                    PersistenceManager.getInstance().setCalledTechnicianList(techList);
                    if (openNotifications) {
                        openNotificationsList();
                    }

                } else {
                    PersistenceManager.getInstance().setNotificationHistory(null);
                }

                setNotificationNeedResponse();

            }

            @Override
            public void onFailure(Call<NotificationHistoryResponse> call, Throwable t) {

                PersistenceManager.getInstance().setNotificationHistory(null);

            }
        });

    }

    public void SetupEndDialogShow(boolean show) {
        mSetupEndDialogShow = show;
        hideBlackFilters();
    }

    public interface ActionBarAndEventsFragmentListener {
        void onWidgetChangeState(boolean state);

        void onWidgetUpdateSpane(int span);

        void onResize(int width, int statusBarsHeight);

        void onOpenReportStopReasonFragment(ReportStopReasonFragment reportStopReasonFragment);

        void onEventSelected(Integer event, boolean b);

        void onClearAllSelectedEvents();

        void onJobActionItemClick();

        void onSplitEventPressed(int eventID);

        void onJoshProductSelected(Integer spinnerProductPosition, Integer jobID, String jobName);

        void onProductionStatusChanged(int id, String newStatus);

        void onLenoxMachineClicked(Machine machine);

        void onLastShiftItemUpdated();

        void onRefreshApplicationRequest();

        void showBlackFilter(boolean show);

        void showWhiteFilter(boolean show);

        void onShowSetupEndDialog();
    }

}
