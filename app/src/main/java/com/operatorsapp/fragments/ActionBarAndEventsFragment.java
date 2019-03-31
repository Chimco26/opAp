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
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.app.operatorinfra.Operator;
import com.example.common.Event;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.actualBarExtraResponse.Inventory;
import com.example.common.actualBarExtraResponse.Reject;
import com.example.common.actualBarExtraResponse.WorkingEvent;
import com.example.common.callback.ErrorObjectInterface;
import com.example.common.machineJoshDataResponse.JobDataItem;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.oppapplog.OppAppLogger;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.infra.Machine;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.AllMachinesData;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operators.reportfieldsformachineinfra.PackageTypes;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operators.reportfieldsformachineinfra.Technician;
import com.operators.reportrejectnetworkbridge.server.response.ResponseStatus;
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
import com.operatorsapp.dialogs.LegendDialog;
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
import com.operatorsapp.view.PinchRecyclerView;
import com.operatorsapp.view.TimeLineView;
import com.ravtech.david.sqlcore.DatabaseHelper;

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
import static com.operatorsapp.utils.TimeUtils.SIMPLE_FORMAT_FORMAT;
import static com.operatorsapp.utils.TimeUtils.SQL_T_FORMAT;
import static com.operatorsapp.utils.TimeUtils.convertDateToMillisecond;
import static com.operatorsapp.utils.TimeUtils.getDateFromFormat;


public class ActionBarAndEventsFragment extends Fragment implements DialogFragment.OnDialogButtonsListener,
        DashboardUICallbackListener,
        OnStopClickListener, CroutonRootProvider,
        SelectStopReasonBroadcast.SelectStopReasonListener,
        View.OnClickListener, LenoxMachineAdapter.LenoxMachineAdapterListener,
        EmeraldSpinner.OnSpinnerEventsListener{

    private static final String LOG_TAG = ActionBarAndEventsFragment.class.getSimpleName();
    private static final int ANIM_DURATION_MILLIS = 200;
    public static final int TYPE_ALERT = 20;
    public static final double MINIMUM_VERSION_FOR_NEW_ACTIVATE_JOB = 1.8f;
    private static final int PRODUCTION_ID = 1;


    private View mToolBarView;
    private GoToScreenListener mOnGoToScreenListener;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private OperatorCore mOperatorCore;
    private OnCroutonRequestListener mCroutonCallback;
    private RecyclerView mShiftLogRecycler;
    private LinearLayout mShiftLogLayout;
    private TextView mNoNotificationsText;
    private ProgressBar mLoadingDataText;
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
    private ArrayList<Float> mSelectedEvents;
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
    private View mCycleWarningView;
    private boolean mSetupEndDialogShow;
    private boolean mCycleWarningViewShow;
    private EmeraldSpinner mJobsSpinner;
    private LinearLayout mScrollView;
    private TimeLineView mTimeView;
    private ArrayList<String> mTimes;
    public static final int PIXEL_FOR_MINUTE = 10;
    private PinchRecyclerView mEventsRecycler;
    private EventsAdapter mEventsAdapter;
    private TextView mTechOpenCallsIv;
    private Switch mTimeLineType;
    private boolean mIsTimeLine;
    private ActualBarExtraResponse mActualBarExtraResponse;
    private CheckBox mSelectAll, mServiceCalls, mMessages, mRejects, mProductionReport;
    private View mFilterLy;
    private View mFilterBtn;
    private View mFiltersView;
    private AsyncTask<Void, Void, String> mAsyncTask;
    private ImageView mLegendBtn;
    private LegendDialog mLegendDialog;
    private Event mOpenEvent;


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
        //TODO Lenox uncomment

        //        }
        mSwipeParams = (RelativeLayout.LayoutParams) mShiftLogSwipeRefresh.getLayoutParams();
        mSwipeParams.width = mShiftLogParams.width;
        mShiftLogSwipeRefresh.setLayoutParams(mSwipeParams);
        mShiftLogSwipeRefresh.requestLayout();
        mListener.onResize(mCloseWidth, statusBarParams.height);

        initFilterEvents(view);
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

        mMinDurationText = view.findViewById(R.id.fragment_dashboard_min_duration_tv);
        mMinDurationLil = view.findViewById(R.id.fragment_dashboard_min_duration_lil);

        mShiftLogLayout = view.findViewById(R.id.fragment_dashboard_shiftlog);
        mShiftLogParams = mShiftLogLayout.getLayoutParams();
        mShiftLogParams.width = mCloseWidth;
        mShiftLogLayout.requestLayout();

        mShiftLogRecycler = view.findViewById(R.id.fragment_dashboard_shift_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mShiftLogRecycler.setLayoutManager(linearLayoutManager);
        initEventRecycler(view);
        initLegendDialog(view);

        mFilterLy = view.findViewById(R.id.FAAE_filter_ly);
        mFilterBtn = view.findViewById(R.id.FAAE_filter_btn);
        mFiltersView = view.findViewById(R.id.FAAE_filters_view);

        mFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(mShiftLogParams);
            }
        });
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
                mIsTimeLine = isChecked;
                if (isChecked) {
                    mEventsRecycler.setVisibility(View.VISIBLE);
                    mShiftLogRecycler.setVisibility(View.GONE);
                    mShowAlarmCheckBox.setVisibility(View.GONE);
                    mMinDurationLil.setVisibility(View.GONE);
                    mFilterLy.setVisibility(View.VISIBLE);
                    if (mIsOpen) {
                        showLegendBtnVisibility(true);
                    }
                    if (mEventsAdapter != null) {
                        mEventsAdapter.notifyDataSetChanged();
                    } else {
                        initEvents(mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()));
                    }
                    PersistenceManager.getInstance().setIsNewShiftLog(true);
                } else {
                    mEventsRecycler.setVisibility(View.GONE);
                    mShiftLogRecycler.setVisibility(View.VISIBLE);
                    mFilterLy.setVisibility(View.GONE);
                    showLegendBtnVisibility(false);
                    if (!mIsSelectionMode) {
                        mShowAlarmCheckBox.setVisibility(View.VISIBLE);
                        mMinDurationLil.setVisibility(View.VISIBLE);
                    }
                    if (mIsSelectionMode && mFirstSeletedEvent != null) {
                        Cursor cursor;
                        cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), mFirstSeletedEvent.getEventReasonID());
                        setShiftLogAdapter(cursor);
                    }
                    PersistenceManager.getInstance().setIsNewShiftLog(false);
                }
            }
        });

        if (PersistenceManager.getInstance().getIsNewShiftLog()) {
            mTimeLineType.setChecked(true);
        }
        //mSwipeToRefresh = view.findViewById(R.id.swipe_refresh_actionbar_events);
        mProductNameTextView = view.findViewById(R.id.text_view_product_name_and_id);
        mMultipleProductImg = view.findViewById(R.id.FAAE_multiple_product_img);
        mProductSpinner = view.findViewById(R.id.FAAE_product_spinner);
        mJobIdTextView = view.findViewById(R.id.text_view_job_id);
        mShiftIdTextView = view.findViewById(R.id.text_view_shift_id);
        mTimerTextView = view.findViewById(R.id.text_view_timer);
        mSelectedNumberTv = view.findViewById(R.id.FAAE_selected_nmbr);

        mSelectedNumberLy = view.findViewById(R.id.FAAE_event_selected_ly);

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
        mLoadingDataText.setVisibility(View.VISIBLE);
        final ImageView shiftLogHandle = view.findViewById(R.id.fragment_dashboard_left_btn);

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

    private void initLegendDialog(View view) {
        mLegendBtn = view.findViewById(R.id.FAAE_legend_btn);
        mLegendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLegendDialog == null)
                    mLegendDialog = LegendDialog.newInstants();
                if (getFragmentManager() != null)
                    mLegendDialog.show(getFragmentManager(), null);
            }
        });
    }

    private void showLegendBtnVisibility(boolean show) {
        if (mLegendBtn != null) {
            if (show) {
                mLegendBtn.setVisibility(View.VISIBLE);
            } else {
                mLegendBtn.setVisibility(View.GONE);
            }
        }
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
            cursor = mDatabaseHelper.getCursorOrderByTimeFilterByDurationWithoutWork(PersistenceManager.getInstance().getMinEventDuration());
        } else {
            cursor = mDatabaseHelper.getStopTypeShiftOrderByTimeFilterByDurationWithoutWork(PersistenceManager.getInstance().getMinEventDuration());
        }
        return cursor;
    }

    @Nullable
    public Cursor getCursorByTypeTimeLine() {
        Cursor cursor;
//         if (isShowAlarms) {
        cursor = mDatabaseHelper.getCursorOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration());
//        } else {
//            cursor = mDatabaseHelper.getStopTypeShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration());
//        }
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
                if (mIsTimeLine) {
                    mFiltersView.setVisibility(View.VISIBLE);
                    showLegendBtnVisibility(true);
                }
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
        if (mIsTimeLine) {
            mFiltersView.setVisibility(View.GONE);
            showLegendBtnVisibility(false);
        }
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

        initEvents(mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()));


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
            mTechOpenCallsIv = mToolBarView.findViewById(R.id.toolbar_technician_open_calls_tv);
            mNotificationIndicatorCircleFl = mToolBarView.findViewById(R.id.toolbar_notification_counter_circle);
            mNotificationIndicatorNumTv = mToolBarView.findViewById(R.id.toolbar_notification_counter_tv);
            mStatusTimeMinTv = mToolBarView.findViewById(R.id.ATATV_status_time_min);

            getNotificationsFromServer(false);
            setTechnicianCallStatus();

            tutorialIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startToolbarTutorial();
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
                    openDeleteTechCallDialog(PersistenceManager.getInstance().getCalledTechnician());

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
//            startToolbarTutorial();
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

    private void openDeleteTechCallDialog(ArrayList<TechCallInfo> tech) {
        final List<Technician> techniciansList = ((DashboardActivity) getActivity()).getReportForMachine().getTechnicians();
        mPopUpDialog = new TechCallDialog(getActivity(), tech, techniciansList, new TechCallDialog.TechDialogListener() {
            @Override
            public void onNewCallPressed() {
                mPopUpDialog.dismiss();
                //getFile();
                openTechniciansList();
            }

            @Override
            public void onCancelPressed() {
                mPopUpDialog.dismiss();
            }

            @Override
            public void onCleanTech() {
                getNotificationsFromServer(false);
                setTechnicianCallStatus();
            }

            @Override
            public void onTechnicianSelected(Technician technician) {
                createNewTechCall(technician);
            }
        });
        mPopUpDialog.show();
    }

    private void createNewTechCall(final Technician technician) {
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

        final String techName = OperatorApplication.isEnglishLang() ? technician.getEName() : technician.getLName();
        String sourceUserId = PersistenceManager.getInstance().getOperatorId();
        if (sourceUserId == null || sourceUserId.equals("")) {
//            sourceUserId = "0";
            sourceUserId = pm.getUserId() + "";
        }


        PostTechnicianCallRequest request = new PostTechnicianCallRequest(pm.getSessionId(), pm.getMachineId(), title, technician.getID(), body, operatorName, technician.getEName(), sourceUserId);
        NetworkManager.getInstance().postTechnicianCall(request, new Callback<ResponseStatus>() {
            @Override
            public void onResponse(@NonNull Call<ResponseStatus> call, @NonNull Response<ResponseStatus> response) {
                if (response.body() != null && response.body().getmError() == null) {

                    PersistenceManager.getInstance().setTechnicianCallTime(Calendar.getInstance().getTimeInMillis());
                    PersistenceManager.getInstance().setCalledTechnicianName(techName);

                    TechCallInfo techCall = new TechCallInfo(0, techName, getString(R.string.called_technician) + "\n" + techName, Calendar.getInstance().getTimeInMillis(), response.body().getmLeaderRecordID(), technician.getID());
                    PersistenceManager.getInstance().setCalledTechnician(techCall);
                    PersistenceManager.getInstance().setRecentTechCallId(techCall.getmNotificationId());
                    setTechnicianCallStatus();
                    getNotificationsFromServer(false);
                    ProgressDialogManager.dismiss();
                    mListener.onTechnicianCalled();

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
                mPopUpDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseStatus> call, @NonNull Throwable t) {
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
                mPopUpDialog.dismiss();
                //ShowCrouton.showSimpleCrouton(((DashboardActivity) getActivity()), "Call for Technician failed", CroutonCreator.CroutonType.ALERT_DIALOG);
            }
        });
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
        if (techList != null && techList.size() > 0) {
            mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.technician_blue_svg));
            mTechOpenCallsIv.setVisibility(View.VISIBLE);
            mTechOpenCallsIv.setText(techList.size() + "");
        } else {
            mTechOpenCallsIv.setVisibility(View.INVISIBLE);
            mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.technician_white));
            PersistenceManager.getInstance().setRecentTechCallId(0);
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
                NetworkManager.getInstance().postTechnicianCall(request, new Callback<ResponseStatus>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseStatus> call, @NonNull Response<ResponseStatus> response) {
                        if (response.body() != null && response.body().getmError() == null) {

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
                    public void onFailure(@NonNull Call<ResponseStatus> call, @NonNull Throwable t) {
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
            //final TextView rightTab = mPopUpDialog.findViewById(R.id.NVP_view_pager_tv_right_tab);
            final TextView leftTab = mPopUpDialog.findViewById(R.id.NVP_view_pager_tv_left_tab);
            //final View rightTabUnderline = mPopUpDialog.findViewById(R.id.NVP_view_pager_right_tab_underline);
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
//                            rightTab.setTextColor(getResources().getColor(R.color.dark_indigo));
//                            rightTabUnderline.setVisibility(View.INVISIBLE);
                            break;
                        case 1:
//                            rightTab.setTextColor(getResources().getColor(R.color.tabNotificationColor));
//                            rightTabUnderline.setVisibility(View.VISIBLE);
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

//            rightTab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    vpDialog.setCurrentItem(1);
//                }
//            });

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
            if (opId == null) {
                opId = "";
            }
            RespondToNotificationRequest request = new RespondToNotificationRequest(pm.getSessionId(),
                    getResources().getString(R.string.respond_notification_title),
                    notification[0].getmBody(getActivity()),
                    pm.getMachineId() + "",
                    notification[0].getmNotificationID() + "",
                    responseType,
                    Consts.NOTIFICATION_TYPE_FROM_WEB,
                    Consts.NOTIFICATION_RESPONSE_TARGET_WEB,
                    pm.getUserId() + "",
                    pm.getOperatorName(),
                    notification[0].getmSender(),
                    opId,
                    notification[0].getmTargetUserId() + "");

            ProgressDialogManager.show(getActivity());
            NetworkManager.getInstance().postResponseToNotification(request, new Callback<ResponseStatus>() {
                @Override
                public void onResponse(@NonNull Call<ResponseStatus> call, @NonNull Response<ResponseStatus> response) {

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
                public void onFailure(@NonNull Call<ResponseStatus> call, @NonNull Throwable t) {
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
        mToolBarView.findViewById(R.id.toolbar_production_spinner).setVisibility(View.VISIBLE);
        final EmeraldSpinner productionStatusSpinner = mToolBarView.findViewById(R.id.toolbar_production_spinner);
        mToolBarView.findViewById(R.id.ATATV_spinner_disable_view).setVisibility(View.GONE);
        final LinearLayout productionStatusSpinnerLil = mToolBarView.findViewById(R.id.toolbar_production_spinner_lil);
        productionStatusSpinnerLil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productionStatusSpinner.performClick();
            }
        });

        productionStatusSpinner.setVisibility(View.VISIBLE);
        productionStatusSpinner.setBackground(getResources().getDrawable(R.drawable.spinner_bckgrnd));
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
        if (!mCurrentMachineStatus.getAllMachinesData().get(0).isAllowProductionModeOnOpApp()) {
            mToolBarView.findViewById(R.id.ATATV_spinner_disable_view).setVisibility(View.VISIBLE);
            productionStatusSpinner.setBackground(null);
        }
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
        startSelectMode(event, null);
    }

    @Override
    public void onStopClicked(int eventId, String startTime, String endTime, long duration) {

        //TODO    openStopReportScreen(eventId, startTime, endTime, duration);
    }

    @Override
    public void onStopEventSelected(float event, boolean b) {//todo kuti
        mListener.onEventSelected(event, b);
    }

    @Override
    public void onSplitEventPressed(Float eventID) {
        // TODO: 05/07/2018 call server split event

        mListener.onSplitEventPressed(eventID);

    }

    @Override
    public void onLastItemUpdated() {

        mListener.onLastShiftItemUpdated();
    }

    @Override
    public void onSelectMode(Event event) {

        startSelectMode(event, null);
    }

    public void startSelectMode(Event event, MyTaskListener myTaskListener) {
        int eventReasonId;
        if (event == null) {
            eventReasonId = 0;
        } else {
            eventReasonId = event.getEventReasonID();
        }

        Cursor cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), eventReasonId);
        ArrayList<Event> events = mDatabaseHelper.getListFromCursor(cursor);

        if (event == null) {
            if (events.size() > 0) {
                event = events.get(0);
            } else {
                return;
            }
        }

        mListener.onOpenReportStopReasonFragment(ReportStopReasonFragment.newInstance(mIsOpen, mActiveJobsListForMachine, mSelectedPosition));

        mIsSelectionMode = true;
        mFirstSeletedEvent = event;

        if (myTaskListener == null) {
            setShiftLogAdapter(cursor);

            initEvents(events);

            onStopEventSelected(event.getEventID(), true);

            mShowAlarmCheckBox.setVisibility(View.GONE);
            mMinDurationLil.setVisibility(View.GONE);
        } else {
            myTaskListener.onUpdateEventsRecyclerViews(cursor, events);
            myTaskListener.onStartSelectMode(event);
        }
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
        getNotificationsFromServer(false);
    }


    @Override
    public void onShiftLogDataReceived(final ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse, MachineJoshDataResponse machineJoshDataResponse) {

        if (machineJoshDataResponse != null && machineJoshDataResponse.getDepMachine().size() > 0
                && machineJoshDataResponse.getDepMachine().get(0).getDepartmentMachines().size() > 0) {
            actualBarExtraResponse.setJobData(machineJoshDataResponse.getDepMachine().get(0).getDepartmentMachines().get(0).getJobData());
        }
        mActualBarExtraResponse = actualBarExtraResponse;

        if (mShiftLogSwipeRefresh.isRefreshing()) {
            mShiftLogSwipeRefresh.setRefreshing(false);
        }

//        if (!mIsSelectionMode) {
        mLoadingDataText.setVisibility(View.GONE);
        final Event finalLatestEvent;
        if (events.size() > 0) {
            finalLatestEvent = events.get(0);
        } else {
            finalLatestEvent = null;
        }
        if (isAdded()) {
            mAsyncTask = new MyTask(events, actualBarExtraResponse, new MyTaskListener() {
                @Override
                public void onComplete() {
                    if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (finalLatestEvent != null && finalLatestEvent.getEventEndTime() != null
                                        && finalLatestEvent.getEventEndTime().length() > 0 && mCurrentMachineStatus != null &&
                                        mCurrentMachineStatus.getAllMachinesData() != null && mCurrentMachineStatus.getAllMachinesData().size() > 0) {

                                    PersistenceManager.getInstance().setShiftLogStartingFrom(TimeUtils.getDate(convertDateToMillisecond(finalLatestEvent.getEventEndTime()), "yyyy-MM-dd HH:mm:ss.SSS"));
                                } else if (finalLatestEvent != null) {
//                                    PersistenceManager.getInstance().setShiftLogStartingFrom(TimeUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));
                                }
                            }
                        });
                    }
                }

                @Override
                public void onUpdateEventsRecyclerViews(final Cursor oldCursor, final ArrayList<Event> newEvents) {
                    if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setShiftLogAdapter(oldCursor);
                                initEvents(newEvents);
                            }
                        });
                    }
                }

                @Override
                public void onStartSelectMode(final Event event) {
                    if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onStopEventSelected(event.getEventID(), true);

                                mShowAlarmCheckBox.setVisibility(View.GONE);
                                mMinDurationLil.setVisibility(View.GONE);
                            }
                        });
                    }
                }

                @Override
                public void onShowNotificationText(final boolean show) {
                    if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (show) {
                                    mNoNotificationsText.setVisibility(View.VISIBLE);
                                } else {
                                    mNoNotificationsText.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onOpenDialog(final Event event) {
                    if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                openDialog(event);
                            }
                        });
                    }
                }

                @Override
                public void onClearAllSelectedEvents() {
                    if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onClearAllSelectedEvents();
                            }
                        });
                    }
                }
            }).execute();
        }
    }

    private class MyTask extends AsyncTask<Void, Void, String> {

        private final ArrayList<Event> events;
        private final ActualBarExtraResponse actualBarExtraResponse;
        private final MyTaskListener myTaskListener;

        public MyTask(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse, MyTaskListener myTaskListener) {
            this.events = events;
            this.actualBarExtraResponse = actualBarExtraResponse;
            this.myTaskListener = myTaskListener;
        }

        @Override
        protected String doInBackground(Void... params) {
            if (isAdded()) {
                updateEvents(events, actualBarExtraResponse, myTaskListener);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // do something with result
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }
    }

    public interface MyTaskListener {

        void onComplete();

        void onUpdateEventsRecyclerViews(Cursor oldCursor, ArrayList<Event> newEvents);

        void onStartSelectMode(Event event);

        void onShowNotificationText(boolean show);

        void onOpenDialog(Event event);

        void onClearAllSelectedEvents();
    }

    public void updateEvents(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse, MyTaskListener myTaskListener) {

        if (getActivity() == null || !isAdded()) {
            return;
        }
        removeOldUpdatedExtras(actualBarExtraResponse);

        int deletedEvents = clearOver24HShift();

        if (events != null && events.size() > 0) {

            HashMap<Integer, ArrayList<Integer>> checkedAlarmsHashMap = PersistenceManager.getInstance().getCheckedAlarms();

            ArrayList<Integer> checkedAlarms = checkedAlarmsHashMap.get(PersistenceManager.getInstance().getMachineId());

            mEventsQueue.clear();

            mNoData = false;

            for (Event event : events) {
                event.setTimeOfAdded(System.currentTimeMillis());

                if (!mIsNewShiftLogs) {
                    event.setIsDismiss(true);
                }

            }
            boolean mustBeClosed = false;
            for (Event event : events) {

                if (event.getEventGroupID() == TYPE_ALERT) {
                    mActualBarExtraResponse.getAlarmsEvents().add(event);
                }

                if (mAutoSelectMode && event.getEventEndTime() != null && event.getEventEndTime().length() > 0 &&
                        mFirstSeletedEvent != null && mSelectedEvents != null
                        && mSelectedEvents.size() == 1) {

                    mustBeClosed = true;
                }

                if (DataSupport.count(Event.class) == 0 || !DataSupport.isExist(Event.class, DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()))) {

                    if (event.getEventGroupID() != TYPE_ALERT) {
                        addDetailsToEvents(event, actualBarExtraResponse);
                    }
                    if (mOpenEvent != null && (event.getEventGroupID() != TYPE_ALERT
                            || convertDateToMillisecond(event.getEventTime()) >= convertDateToMillisecond(mOpenEvent.getEventEndTime()))) {
                        mOpenEvent.setType(1);
                        mOpenEvent.setEventEndTime(TimeUtils.getDateFromFormat(new Date(), SIMPLE_FORMAT_FORMAT));
                        addDetailsToEvents(mOpenEvent, actualBarExtraResponse);//test
                        mOpenEvent.save();
                    }
                    mOpenEvent = null;
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

            updateList(mDatabaseHelper.getListFromCursor(mDatabaseHelper.getCursorOrderByTimeFilterByDurationStartFromOneEvent(0, events.get(events.size() - 1).getEventID())), actualBarExtraResponse);

            myTaskListener.onShowNotificationText(false);

            Cursor cursor;
            if (mIsSelectionMode) {

                cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), mFirstSeletedEvent.getEventReasonID());
//                initEvents(mDatabaseHelper.getListFromCursor(cursor));
                myTaskListener.onUpdateEventsRecyclerViews(cursor, mDatabaseHelper.getListFromCursor(cursor));

            } else {

//                initEvents(mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()));
                cursor = getCursorByType();
                myTaskListener.onUpdateEventsRecyclerViews(cursor, mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()));
            }
//            setShiftLogAdapter(cursor);

            if (mEventsQueue.size() > 0) {
                Event event = mEventsQueue.peek();
                // we only need to show the stop report when the event is open (no end time) and hase no event reason ( == 0).
                if (event.getEventGroupID() != TYPE_ALERT && TextUtils.isEmpty(event.getEventEndTime()) && event.getEventReasonID() == REASON_UNREPORTED) {

                    if (!mIsSelectionMode) {
                        startSelectMode(event, myTaskListener);
                        mAutoSelectMode = true;
                    } else if (mustBeClosed) {
                        myTaskListener.onClearAllSelectedEvents();
                    }
                    mEventsQueue.pop();

                } else if (event.getEventGroupID() == TYPE_ALERT) {
                    myTaskListener.onOpenDialog(event);
                    mLastEvent = event;
                    mEventsQueue.pop();
                    if (mustBeClosed) {
                        myTaskListener.onClearAllSelectedEvents();
                    }
                }

            } else if (mustBeClosed) {
                myTaskListener.onClearAllSelectedEvents();
            }
        } else {
            if (DataSupport.count(Event.class) == 0) {
                mNoData = true;
                myTaskListener.onShowNotificationText(true);
            }
            if (deletedEvents > 0 || isActualBarExtraResponse(actualBarExtraResponse)) {

                Cursor cursor;
                if (mIsSelectionMode) {

                    cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), mFirstSeletedEvent.getEventReasonID());

                } else {

                    cursor = getCursorByType();
                }
//                setShiftLogAdapter(cursor);
//                initEvents(mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()));
                myTaskListener.onUpdateEventsRecyclerViews(cursor, mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()));
            }
        }

        mIsNewShiftLogs = true;
        PersistenceManager.getInstance().setIsNewShiftLogs(true);

        myTaskListener.onComplete();
    }

    private boolean isActualBarExtraResponse(ActualBarExtraResponse actualBarExtraResponse) {
        return actualBarExtraResponse != null && (actualBarExtraResponse.getNotification() != null ||
                actualBarExtraResponse.getRejects() != null || actualBarExtraResponse.getInventory() != null);
    }

    private ArrayList<Event> updateList(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse) {

        addNotificationsToPasteEvents(actualBarExtraResponse);

        for (int i = 0; i < events.size() - 1; i++) {

            if (events.get(i).getEventGroupID() != TYPE_ALERT) {
                Event event = events.get(i);

                Long eventStartMilli = convertDateToMillisecond(events.get(i + 1).getEventEndTime());
                Long eventEndMilli = convertDateToMillisecond(event.getEventTime());

                if (eventStartMilli < eventEndMilli) {

                    String color = "#1aa917";
                    if (getActivity() != null) {
                        color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.new_green));
                    }
                    Event workingEvent = createIntermediateEvent(events.get(i + 1).getEventEndTime(),
                            event.getEventTime(), event.getEventID(), eventStartMilli, eventEndMilli, "עובד", "Working",
                            -0.5f, color, 1);

                    if (DataSupport.count(Event.class) == 0 || !DataSupport.isExist(Event.class, DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(workingEvent.getEventID()))) {

                        addDetailsToEvents(workingEvent, actualBarExtraResponse);

                        workingEvent.save();
                    }
                }
            }

        }

        return events;
    }

    private Event addFirstEvent(Event event, ActualBarExtraResponse actualBarExtraResponse) {
        Long eventEndMilli = convertDateToMillisecond(event.getEventTime());
        Long minusDayTime = new Date().getTime() - DAY_IN_MILLIS;

        if (minusDayTime < eventEndMilli) {
            String color = "#1aa917";
            if (getActivity() != null) {
                color = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.new_green));
            }
            Event workingEvent = createIntermediateEvent(TimeUtils.getDateFromFormat(new Date(minusDayTime), SIMPLE_FORMAT_FORMAT),
                    event.getEventTime(), event.getEventID(), minusDayTime, eventEndMilli, "עובד", "Working",
                    -0.5f, color, 1);

            if (DataSupport.count(Event.class) == 0 || !DataSupport.isExist(Event.class, DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(workingEvent.getEventID()))) {

                addDetailsToEvents(workingEvent, actualBarExtraResponse);

                workingEvent.save();

                return workingEvent;
            }

        }
        return null;
    }

    @NonNull
    private Event createIntermediateEvent(String eventTime, String eventEndTime, float id, Long eventStartMilli,
                                          Long eventEndMilli, String lName, String eName,
                                          float differenceForNewId, String color, int type) {
        Event workingEvent = new Event();
        workingEvent.setEventTime(eventTime);
        workingEvent.setEventEndTime(eventEndTime);

        workingEvent.setEventSubTitleLname(lName);
        workingEvent.setEventSubTitleEname(eName);
        workingEvent.setColor(color);

        workingEvent.setEventID(id + differenceForNewId);
        workingEvent.setType(type);

        long duration = eventEndMilli - eventStartMilli;
        if (duration > DAY_IN_MILLIS) {
            duration = DAY_IN_MILLIS;
        }
        long minute = TimeUnit.MILLISECONDS.toMinutes(duration);

        workingEvent.setDuration(minute);
        return workingEvent;
    }

    private void removeOldUpdatedExtras(ActualBarExtraResponse actualBarExtraResponse) {

        ArrayList<Event> events = mDatabaseHelper.getListFromCursor(mDatabaseHelper.getCursorIfHaveExtra());

        for (Event event : events) {

            ArrayList<Inventory> eventInventories = event.getInventories();
            ArrayList<Inventory> inventories = actualBarExtraResponse.getInventory();
            if (eventInventories != null && eventInventories.size() > 0
                    && inventories != null && inventories.size() > 0) {
                ArrayList<Inventory> toDelete = new ArrayList<>();
                for (Inventory inventory : inventories) {
                    for (Inventory eventInventory : eventInventories) {
                        if (inventory.getID().equals(eventInventory.getID())) {
                            toDelete.add(eventInventory);
                        }
                    }
                }
                eventInventories.removeAll(toDelete);
                if (eventInventories.size() == 0) {
                    eventInventories = null;
                }
                event.setInventories(eventInventories);
            }
            ArrayList<Reject> eventRejects = event.getRejects();
            ArrayList<Reject> rejects = actualBarExtraResponse.getRejects();
            if (eventRejects != null && eventRejects.size() > 0
                    && rejects != null && rejects.size() > 0) {
                ArrayList<Reject> toDelete = new ArrayList<>();
                for (Reject reject : rejects) {
                    for (Reject eventReject : eventRejects) {
                        if (reject.getID().equals(eventReject.getID())) {
                            toDelete.add(eventReject);
                        }
                    }
                }
                eventRejects.removeAll(toDelete);
                if (eventRejects.size() == 0) {
                    eventRejects = null;
                }
                event.setRejects(eventRejects);
            }
            ArrayList<com.example.common.actualBarExtraResponse.Notification> eventNotifications = event.getNotifications();
            ArrayList<com.example.common.actualBarExtraResponse.Notification> notifications = actualBarExtraResponse.getNotification();
            if (eventNotifications != null && eventNotifications.size() > 0
                    && notifications != null && notifications.size() > 0) {
                ArrayList<com.example.common.actualBarExtraResponse.Notification> toDelete = new ArrayList<>();
                for (com.example.common.actualBarExtraResponse.Notification notification : notifications) {
                    for (com.example.common.actualBarExtraResponse.Notification eventsNotification : eventNotifications) {
                        if (notification.getID().equals(eventsNotification.getID())) {
                            toDelete.add(eventsNotification);
                        }
                    }
                }
                eventNotifications.removeAll(toDelete);
                if (eventNotifications.size() == 0) {
                    eventNotifications = null;
                }
                event.setNotifications(eventNotifications);
            }
            event.setHaveExtra(event.getNotifications() != null || event.getInventories() != null || event.getRejects() != null);

            event.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()));
        }
    }

    private void addNotificationsToPasteEvents(ActualBarExtraResponse actualBarExtraResponse) {

        if (actualBarExtraResponse.getInventory() != null || actualBarExtraResponse.getNotification() != null ||
                actualBarExtraResponse.getRejects() != null) {
            ArrayList<Event> events = mDatabaseHelper.getListFromCursor(mDatabaseHelper.getCursorOrderByTime());

            for (Event event : events) {
                if (actualBarExtraResponse.getInventory() != null || actualBarExtraResponse.getNotification() != null ||
                        actualBarExtraResponse.getRejects() != null) {
                    if (addDetailsToEvents(event, actualBarExtraResponse)) {
                        event.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()));
                    }
                }
            }
        }
    }

    private boolean isDuringAnEvent(Event event, String notifTime) {
        Long notificationSentTime = convertDateToMillisecond(notifTime, SIMPLE_FORMAT_FORMAT);
        Long eventStart = convertDateToMillisecond(event.getEventTime(), SIMPLE_FORMAT_FORMAT);
        Long eventEnd = convertDateToMillisecond(event.getEventEndTime(), SIMPLE_FORMAT_FORMAT);
        return eventStart <= notificationSentTime && notificationSentTime <= eventEnd;
    }

    private boolean addDetailsToEvents(Event event, ActualBarExtraResponse actualBarExtraResponse) {

        if (actualBarExtraResponse != null) {
            Long eventStart = convertDateToMillisecond(event.getEventTime(), SIMPLE_FORMAT_FORMAT);
            Long eventEnd = new Date().getTime();
            if (event.getEventEndTime() != null && event.getEventEndTime().length() > 0) {
                eventEnd = convertDateToMillisecond(event.getEventEndTime(), SIMPLE_FORMAT_FORMAT);
            }
            addDetailsToWorking(eventStart, eventEnd, event, actualBarExtraResponse);
            addStartProductToEvents(eventStart, eventEnd, event, actualBarExtraResponse);
            if (addAlarmEvents(eventStart, eventEnd, event, actualBarExtraResponse)
                    || addNotificationsToEvents(eventStart, eventEnd, event, actualBarExtraResponse)
                    || addRejectsToEvents(eventStart, eventEnd, event, actualBarExtraResponse)
                    || addInventoryToEvents(eventStart, eventEnd, event, actualBarExtraResponse)) {
                event.setHaveExtra(true);
                return true;
            }
        }
        return false;
    }

    private void addDetailsToWorking(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {

        if (event.getType() != 1) {
            return;
        }
        ArrayList<WorkingEvent> workingEvents = actualBarExtraResponse.getWorkingEvents();
        if (workingEvents != null && workingEvents.size() > 0) {

            for (WorkingEvent workingEvent : workingEvents) {
                Long workingEventSentTime = convertDateToMillisecond(workingEvent.getStartTime(), SQL_T_FORMAT);

                if (eventStart <= workingEventSentTime && workingEventSentTime <= eventEnd) {

                    if (workingEvent.getEventReason() != null && workingEvent.getEventReason().length() > 0) {
                        event.setEventSubTitleLname(workingEvent.getEventReason());
                        event.setEventSubTitleEname(workingEvent.getEventReason());
                    } else if (isAdded()) {
                        event.setEventSubTitleEname(getWorkingSubTitle(workingEvent));
                        event.setEventSubTitleLname(getWorkingSubTitle(workingEvent));
                    } else {
                        event.setEventSubTitleEname("Working");
                        event.setEventSubTitleLname("Working");
                    }
                    event.setColor(workingEvent.getColor());
                }
            }
        }
    }

    private String getWorkingSubTitle(WorkingEvent workingEvent) {
        switch (workingEvent.getEventDistributionID()) {
            case 1:
                return getString(R.string.working);
            case 2:
                return getString(R.string.stop);
            case 3:
                return getString(R.string.working_setup);
            case 4:
                return getString(R.string.stop_setup);
            case 5:
                return getString(R.string.idle);
            case 6:
                return getString(R.string.no_communication_p);
            case 7:
                return getString(R.string.parameter_deviation);
            case 8:
                return getString(R.string.working_no_production);
            default:
                return getString(R.string.working);
        }
    }

    private void addStartProductToEvents(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {

        ArrayList<JobDataItem> jobDataItems = (ArrayList<JobDataItem>) actualBarExtraResponse.getJobData();
        ArrayList<JobDataItem> toDelete = new ArrayList<>();
        if (jobDataItems != null && jobDataItems.size() > 0) {

            ArrayList<JobDataItem> jobDataItemArrayList = event.getJobDataItems();

            for (JobDataItem jobDataItem : jobDataItems) {
                Long jobDataItemSentTime = convertDateToMillisecond(jobDataItem.getStartTime(), SIMPLE_FORMAT_FORMAT);

                if (eventStart <= jobDataItemSentTime && jobDataItemSentTime <= eventEnd) {

                    if (jobDataItemArrayList == null) {
                        jobDataItemArrayList = new ArrayList<>();
                    }

                    jobDataItemArrayList.add(jobDataItem);
                    toDelete.add(jobDataItem);
                }
            }
            if (jobDataItemArrayList != null) {
                event.setJobDataItems(jobDataItemArrayList);
                jobDataItems.removeAll(toDelete);
                if (jobDataItems.size() == 0) {
                    jobDataItems = null;
                }
            }
        }
    }

    private boolean addNotificationsToEvents(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {

        ArrayList<com.example.common.actualBarExtraResponse.Notification> notifications = actualBarExtraResponse.getNotification();
        ArrayList<com.example.common.actualBarExtraResponse.Notification> toDelete = new ArrayList<>();
        if (notifications != null && notifications.size() > 0) {

            ArrayList<com.example.common.actualBarExtraResponse.Notification> notificationArrayList = event.getNotifications();

            for (com.example.common.actualBarExtraResponse.Notification notification : notifications) {
                Long notificationSentTime = convertDateToMillisecond(notification.getSentTime(), SQL_T_FORMAT);

                if (eventStart <= notificationSentTime && notificationSentTime <= eventEnd) {

                    if (notificationArrayList == null) {
                        notificationArrayList = new ArrayList<>();
                    }

                    notificationArrayList.add(notification);
                    toDelete.add(notification);
                }
            }
            if (notificationArrayList != null) {
                event.setNotifications(notificationArrayList);
                notifications.removeAll(toDelete);
                if (notifications.size() == 0) {
                    notifications = null;
                }
                return true;
            }
        }
        return false;
    }

    private boolean addAlarmEvents(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {

        ArrayList<Event> alarmEvents = actualBarExtraResponse.getAlarmsEvents();
        ArrayList<Event> toDelete = new ArrayList<>();
        if (alarmEvents != null && alarmEvents.size() > 0
                && event.getType() == 0) {

            ArrayList<Event> EventAlarmArrayList = event.getAlarmsEvents();

            for (Event event1 : alarmEvents) {
                Long eventTime = convertDateToMillisecond(event1.getEventTime(), SIMPLE_FORMAT_FORMAT);

                if (eventStart <= eventTime && eventTime <= eventEnd) {

                    if (EventAlarmArrayList == null) {
                        EventAlarmArrayList = new ArrayList<>();
                    }

                    EventAlarmArrayList.add(event1);
                    toDelete.add(event1);
                }
            }
            if (EventAlarmArrayList != null) {
                event.setAlarmsEvents(EventAlarmArrayList);
                alarmEvents.removeAll(toDelete);
                if (alarmEvents.size() == 0) {
                    alarmEvents = null;
                }
                return true;
            }
        }
        return false;
    }

    private boolean addRejectsToEvents(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {

        ArrayList<Reject> rejects = actualBarExtraResponse.getRejects();
        ArrayList<Reject> toDelete = new ArrayList<>();
        if (rejects != null && rejects.size() > 0) {

            ArrayList<Reject> rejectArrayList = event.getRejects();

            for (Reject reject : rejects) {
                Long rejectTime = convertDateToMillisecond(reject.getTime(), SIMPLE_FORMAT_FORMAT);

                if (eventStart <= rejectTime && rejectTime <= eventEnd) {
                    if (rejectArrayList == null) {
                        rejectArrayList = new ArrayList<>();
                    }
                    rejectArrayList.add(reject);
                    toDelete.add(reject);
                }
            }
            if (rejectArrayList != null) {
                event.setRejects(rejectArrayList);
                rejects.removeAll(toDelete);
                if (rejects.size() == 0) {
                    rejects = null;
                }
                return true;
            }
        }
        return false;
    }

    private boolean addInventoryToEvents(Long eventStart, Long eventEnd, Event event, ActualBarExtraResponse actualBarExtraResponse) {

        ArrayList<Inventory> inventories = actualBarExtraResponse.getInventory();
        ArrayList<Inventory> toDelete = new ArrayList<>();

        if (inventories != null && inventories.size() > 0) {

            ArrayList<Inventory> inventoriesArrayList = event.getInventories();

            for (Inventory inventory : inventories) {
                Long inventoryTime = convertDateToMillisecond(inventory.getTime(), SIMPLE_FORMAT_FORMAT);

                if (eventStart <= inventoryTime && inventoryTime <= eventEnd) {
                    if (inventoriesArrayList == null) {
                        inventoriesArrayList = new ArrayList<>();
                    }
                    inventoriesArrayList.add(inventory);
                    toDelete.add(inventory);
                }
            }
            if (inventoriesArrayList != null) {
                event.setInventories(inventoriesArrayList);
                inventories.removeAll(toDelete);
                if (inventories.size() == 0) {
                    inventories = null;
                }
                return true;
            }
        }
        return false;
    }

    private void initEventRecycler(View view) {
        mEventsRecycler = view.findViewById(R.id.FAAE_events_recycler);
        mEventsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mEventsAdapter = new EventsAdapter(getContext(), this, mIsSelectionMode, mIsOpen);
        mEventsRecycler.setAdapter(mEventsAdapter);

        mEventsRecycler.addListener(new PinchRecyclerView.PinchRecyclerViewListener() {
            @Override
            public void onScale(float factor) {
                if (mEventsAdapter != null) {
                    mEventsAdapter.setFactor(factor);
                }
            }
        });
    }

    private void initEvents(ArrayList<Event> events) {

        if (mLoadingDataText != null) {
            mLoadingDataText.setVisibility(View.GONE);
        }
        if (events.size() > 0) {
            Event event = addFirstEvent(events.get(events.size() - 1), mActualBarExtraResponse);
            if (event != null) {
                events.add(event);
            }
        }
        Event event = addCurrentEvent(events);
        if (event != null) {
            events.add(0, event);
        }
        if (events.size() < 1) {
            return;
        }
//        mEventsAdapter = new EventsAdapter(getContext(), this, mIsSelectionMode, mIsOpen, events, mSelectedEvents);
//        mEventsRecycler.setAdapter(mEventsAdapter);

        if (mEventsAdapter == null && getView() != null) {
            initEventRecycler(getView());
        }
        mEventsAdapter.setIsSelectionMode(mIsSelectionMode);
        mEventsAdapter.setEvents(events);
        mEventsAdapter.setSelectedEvents(mSelectedEvents);
        mEventsAdapter.notifyDataSetChanged();

    }

    private Event addCurrentEvent(ArrayList<Event> events) {
        Event event = null;
        if (!mIsSelectionMode && events.size() > 0 && events.get(0).getEventEndTime() != null
                && events.get(0).getEventEndTime().length() > 0 && mCurrentMachineStatus != null &&
                mCurrentMachineStatus.getAllMachinesData() != null && mCurrentMachineStatus.getAllMachinesData().size() > 0) {

            event = events.get(0);
        }
        if (events.size() < 1 && mCurrentMachineStatus != null &&
                mCurrentMachineStatus.getAllMachinesData() != null && mCurrentMachineStatus.getAllMachinesData().size() > 0) {
            event = new Event();
            event.setEventID(1);
            event.setEventEndTime(TimeUtils.getDateFromFormat(new Date(new Date().getTime() - DAY_IN_MILLIS), SIMPLE_FORMAT_FORMAT));
        }
        if (event != null) {
            Event intermediateEvent = new Event();
            setEventTextByMachineStatus(mCurrentMachineStatus.getAllMachinesData().get(0).getMachineStatusID(), intermediateEvent);
            intermediateEvent = createIntermediateEvent(event.getEventEndTime(),
                    getDateFromFormat(new Date(), SIMPLE_FORMAT_FORMAT), event.getEventID(), convertDateToMillisecond(event.getEventEndTime()), new Date().getTime(),
                    intermediateEvent.getSubtitleLname(), intermediateEvent.getSubtitleEname(),
                    +0.3f, getColorByMachineStatus(mCurrentMachineStatus.getAllMachinesData().get(0).getMachineStatusID()), 2);
            if (mOpenEvent != null) {
                intermediateEvent.setNotifications(mOpenEvent.getNotifications());
                intermediateEvent.setRejects(mOpenEvent.getRejects());
                intermediateEvent.setInventories(mOpenEvent.getInventories());
            }
            addDetailsToEvents(intermediateEvent, mActualBarExtraResponse);
            mOpenEvent = intermediateEvent;
            return intermediateEvent;
        }
        mOpenEvent = null;
        return null;
    }

    private String getColorByMachineStatus(int machineStatusID) {
        if (getActivity() != null) {
            switch (machineStatusID) {
                case 1:
                    return "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.new_green));
                case 2:
                    return "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.C7));
                case 5:
                    return "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.green_dark_2));
                default:
                    return "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.new_green));

            }
        } else {
            return "#1aa917";
        }
    }

    private void setEventTextByMachineStatus(int machineStatusID, Event event) {
        switch (machineStatusID) {
            case 1:
                event.setEventSubTitleEname("Working");
                event.setEventSubTitleLname("עבודה");
                break;
            case 2:
                event.setEventSubTitleEname("Parameter Deviation");
                event.setEventSubTitleLname("חריגה מפרמטר");
                break;
            case 5:
                event.setEventSubTitleEname("Working Setup");
                event.setEventSubTitleLname("עבודה ב setup");
                break;
            default:
                event.setEventSubTitleEname("Working");
                event.setEventSubTitleLname("עבודה");
        }
    }

    public void initFilterEvents(View view) {

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.FAAE_select_all:
                        boolean checked = mSelectAll.isChecked();
                        mServiceCalls.setChecked(checked);
                        mMessages.setChecked(checked);
                        mRejects.setChecked(checked);
                        mProductionReport.setChecked(checked);
                        break;
                }
                mSelectAll.setChecked(mServiceCalls.isChecked() && mMessages.isChecked() && mRejects.isChecked() && mProductionReport.isChecked());
                mEventsAdapter.setCheckedFilters(mServiceCalls.isChecked(), mMessages.isChecked(), mRejects.isChecked(), mProductionReport.isChecked());
            }
        };
        mSelectAll = view.findViewById(R.id.FAAE_select_all);
        mSelectAll.setOnClickListener(onClickListener);
//        mSelectAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean checked = mSelectAll.isChecked();
//                mWorkingEvents.setChecked(checked);
//                mEventDetails.setChecked(checked);
//                mServiceCalls.setChecked(checked);
//                mMessages.setChecked(checked);
//                mRejects.setChecked(checked);
//                mProductionReport.setChecked(checked);
//            }
//        });
//        mWorkingEvents.setOnCheckedChangeListener(this);
        mServiceCalls = view.findViewById(R.id.FAAE_service_alls);
        mServiceCalls.setOnClickListener(onClickListener);
        mMessages = view.findViewById(R.id.FAAE_messages);
        mMessages.setOnClickListener(onClickListener);
        mRejects = view.findViewById(R.id.FAAE_rejects);
        mRejects.setOnClickListener(onClickListener);
        mProductionReport = view.findViewById(R.id.FAAE_production_report);
        mProductionReport.setOnClickListener(onClickListener);


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
        initEvents(mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()));

        mSelectedNumberLy.setVisibility(View.GONE);

        if (!mIsTimeLine) {
            mShowAlarmCheckBox.setVisibility(View.VISIBLE);
            mMinDurationLil.setVisibility(View.VISIBLE);
        }

        if (mEventsAdapter != null) {
            mEventsAdapter.setIsSelectionMode(mIsSelectionMode);
            mEventsAdapter.notifyDataSetChanged();
        }
    }

    public void setSelectedEvents(ArrayList<Float> selectedEvents) {
        mSelectedEvents = selectedEvents;

        if (mShiftLogAdapter != null) {

            mShiftLogAdapter.setSelectedEvents(mSelectedEvents);
        }

        if (mEventsAdapter != null) {

            mEventsAdapter.setSelectedEvents(mSelectedEvents);
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
        boolean wasShow = mCycleWarningViewShow;
        if (show && !BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
            mCycleWarningViewShow = true;
        } else {
            mCycleWarningViewShow = false;
        }
        if ((wasShow && mCycleWarningView.getVisibility() == View.GONE)) {
            setCycleWarningView(false);
        } else {
            setCycleWarningView(show);
        }
    }

    private void sendTokenWithSessionIdToServer() {
        final PersistenceManager pm = PersistenceManager.getInstance();
        final String id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        PostNotificationTokenRequest request = new PostNotificationTokenRequest(pm.getSessionId(), pm.getMachineId(), pm.getNotificationToken(), id);
        NetworkManager.getInstance().postNotificationToken(request, new Callback<ResponseStatus>() {
            @Override
            public void onResponse(Call<ResponseStatus> call, Response<ResponseStatus> response) {
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
            public void onFailure(Call<ResponseStatus> call, Throwable t) {
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

                    // TODO: 28/03/2019 update tech list for new calls
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
                setTechnicianCallStatus();
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

        void onEventSelected(Float event, boolean b);

        void onClearAllSelectedEvents();

        void onJobActionItemClick();

        void onSplitEventPressed(Float eventID);

        void onJoshProductSelected(Integer spinnerProductPosition, Integer jobID, String jobName);

        void onProductionStatusChanged(int id, String newStatus);

        void onLenoxMachineClicked(Machine machine);

        void onLastShiftItemUpdated();

        void onRefreshApplicationRequest();

        void showBlackFilter(boolean show);

        void showWhiteFilter(boolean show);

        void onShowSetupEndDialog();

        void onTechnicianCalled();
    }

}
