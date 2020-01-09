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
import android.util.SparseArray;
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
import android.widget.Toast;

import com.app.operatorinfra.Operator;
import com.example.common.Event;
import com.example.common.StandardResponse;
import com.example.common.actualBarExtraResponse.ActualBarExtraResponse;
import com.example.common.callback.ErrorObjectInterface;
import com.example.common.callback.GetMachineLineCallback;
import com.example.common.department.MachineLineResponse;
import com.example.common.department.MachinesLineDetail;
import com.example.common.machineJoshDataResponse.MachineJoshDataResponse;
import com.example.common.permissions.WidgetInfo;
import com.example.oppapplog.OppAppLogger;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
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
import com.operatorsapp.adapters.MachineLineAdapter;
import com.operatorsapp.adapters.OperatorSpinnerAdapter;
import com.operatorsapp.adapters.ProductionSpinnerAdapter;
import com.operatorsapp.adapters.ShiftLogSqlAdapter;
import com.operatorsapp.adapters.TechnicianSpinnerAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.DialogFragment;
import com.operatorsapp.dialogs.GenericDialog;
import com.operatorsapp.dialogs.LauncherDialog;
import com.operatorsapp.dialogs.LegendDialog;
import com.operatorsapp.dialogs.LockStatusBarDialog;
import com.operatorsapp.dialogs.NotificationsDialog;
import com.operatorsapp.dialogs.TechCallDialog;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.managers.CroutonCreator;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.JobActionsSpinnerItem;
import com.operatorsapp.model.TechCallInfo;
import com.operatorsapp.server.NetworkManager;
import com.operatorsapp.server.requests.PostNotificationTokenRequest;
import com.operatorsapp.server.requests.PostTechnicianCallRequest;
import com.operatorsapp.server.requests.RespondToNotificationRequest;
import com.operatorsapp.server.requests.SendNotificationRequest;
import com.operatorsapp.server.responses.Notification;
import com.operatorsapp.server.responses.NotificationHistoryResponse;
import com.operatorsapp.utils.ClearData;
import com.operatorsapp.utils.Consts;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.GoogleAnalyticsHelper;
import com.operatorsapp.utils.ResizeWidthAnimation;
import com.operatorsapp.utils.SaveAlarmsHelper;
import com.operatorsapp.utils.SaveHelperNew;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.format.DateUtils.DAY_IN_MILLIS;
import static com.operatorsapp.managers.PersistenceManager.setMachineData;
import static com.operatorsapp.utils.SimpleRequests.getMachineLine;
import static com.operatorsapp.utils.TimeUtils.convertDateToMillisecond;


public class ActionBarAndEventsFragment extends Fragment implements DialogFragment.OnDialogButtonsListener,
        DashboardUICallbackListener,
        OnStopClickListener, CroutonRootProvider,
        SelectStopReasonBroadcast.SelectStopReasonListener,
        View.OnClickListener, LenoxMachineAdapter.LenoxMachineAdapterListener,
        EmeraldSpinner.OnSpinnerEventsListener {

    private static final String LOG_TAG = ActionBarAndEventsFragment.class.getSimpleName();
    private static final int ANIM_DURATION_MILLIS = 200;
    public static final int TYPE_ALERT = 20;
    public static final double MINIMUM_VERSION_FOR_NEW_ACTIVATE_JOB = 1.8f;
    private static final int PRODUCTION_ID = 1;
    public static final String EXTRA_FIELD_FOR_MACHINE = "EXTRA_FIELD_FOR_MACHINE";


    private View mToolBarView;
    private GoToScreenListener mOnGoToScreenListener;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private OperatorCore mOperatorCore;
    private OnCroutonRequestListener mCroutonCallback;
    private RecyclerView mShiftLogRecycler;
    private LinearLayout mShiftLogLayout;
    private TextView mNoNotificationsText;
    private ProgressBar mLoadingDataText;
    private RelativeLayout mStatusLayout;
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
    //    private ViewGroup mMachineStatusLayout;
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
    List<MachinesLineDetail> machineLineItems = new ArrayList<>();
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
    private SparseArray<WidgetInfo> permissionResponseHashmap;
    private View mShowAlarmCheckBoxLy;
    private MachineLineAdapter mMachineLineAdapter;
    private TextView mLineNameTv;
    private View mLineLy;
    private View mLineProgress;
    private View mBottomRl;


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

        //Analytics
        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Main dashboard");

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
        displayViewByServerSettings(permissionResponseHashmap);
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
        mDatabaseHelper = DatabaseHelper.getInstance(getContext());
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
        statusBarParams.height = (int) (mTollBarsHeight * 0.28);
        mStatusLayout.requestLayout();

        mMinDurationText = view.findViewById(R.id.fragment_dashboard_min_duration_tv);
        mMinDurationLil = view.findViewById(R.id.fragment_dashboard_min_duration_lil);

        mShiftLogLayout = view.findViewById(R.id.fragment_dashboard_shiftlog);
        mShiftLogParams = mShiftLogLayout.getLayoutParams();
        mShiftLogParams.width = mCloseWidth;
        mShiftLogLayout.requestLayout();

        mShiftLogSwipeRefresh = view.findViewById(R.id.shift_log_swipe_refresh);
        mShiftLogSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SendBroadcast.refreshPolling(getActivity());
            }
        });

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
        mShowAlarmCheckBoxLy = view.findViewById(R.id.FAAE_alarm_chekbox_ly);
        mShowAlarmCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowAlarms = isChecked;
                setShiftLogAdapter(getCursorByType());
            }
        });

        initMachineLine(view);
        mBottomRl = getView().findViewById(R.id.FAAE_bottom_rl);

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
        mNoNotificationsText = view.findViewById(R.id.fragment_dashboard_no_notif);
        mLoadingDataText = view.findViewById(R.id.fragment_dashboard_loading_data_shiftlog);
        mLoadingDataText.setVisibility(View.VISIBLE);
        final ImageView shiftLogHandle = view.findViewById(R.id.fragment_dashboard_left_btn);

        mTimeLineType = view.findViewById(R.id.FAAE_shift_type_checkbox);
        mTimeLineType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsTimeLine = isChecked;
                if (isChecked) {
                    new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.TOGGLE_SHIFT_LOG_VIEW, true, "Change shift log view- Events View");
                    mEventsRecycler.setVisibility(View.VISIBLE);
                    mShiftLogRecycler.setVisibility(View.GONE);
                    mShowAlarmCheckBoxLy.setVisibility(View.GONE);
                    mMinDurationLil.setVisibility(View.GONE);
                    mFilterLy.setVisibility(View.VISIBLE);
                    if (mIsOpen) {
                        showLegendBtnVisibility(true);
                    }
//                    if (mEventsAdapter != null) {
//                        mEventsAdapter.notifyDataSetChanged();
//                    } else {
//                        initEvents(mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()));
                    onShiftLogDataReceived(null, mActualBarExtraResponse, null);
//                    }
                    PersistenceManager.getInstance().setIsNewShiftLog(true);
                } else {
                    new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.TOGGLE_SHIFT_LOG_VIEW, true, "Change shift log view- Alarms View");
                    mEventsRecycler.setVisibility(View.GONE);
                    mShiftLogRecycler.setVisibility(View.VISIBLE);
                    mFilterLy.setVisibility(View.GONE);
                    showLegendBtnVisibility(false);
                    if (!mIsSelectionMode) {
                        mShowAlarmCheckBoxLy.setVisibility(View.VISIBLE);
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
                                    mListener.onWidgetUpdateSpane(true);
                                } else {
                                    mIsOpen = true;
                                    toggleWoopList(mShiftLogParams, mOpenWidth, true);
                                    mListener.onWidgetChangeState(false);
                                    mListener.onWidgetUpdateSpane(false);

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
        initBottomNotificationLayout(view);

//        initCycleAlarmView(view);

        return statusBarParams;
    }

    private void initMachineLine(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.FAAE_machine_line_rv);
        mLineLy = view.findViewById(R.id.FAAE_machine_line_ly);
        mLineNameTv = view.findViewById(R.id.FAAE_machine_line_tv);
        mLineProgress = view.findViewById(R.id.FAAE_line_progress);
//        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
//            view.findViewById(R.id.FAAE_line_arrow).setRotationY(180);
//        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);

        mMachineLineAdapter = new MachineLineAdapter(machineLineItems, new MachineLineAdapter.MachineLineAdapterListener() {
            @Override
            public void onMachineSelected(MachinesLineDetail departmentMachineValue) {
                ClearData.clearMachineData();
                setMachineData(departmentMachineValue.getMachineID(), departmentMachineValue.getMachineName());
                mListener.onRefreshMachineLinePolling();
                mLineProgress.setVisibility(View.VISIBLE);
                disableSelectMode();
            }
        });
        recyclerView.setAdapter(mMachineLineAdapter);

        getMachinesLineData();

        view.findViewById(R.id.FAAE_machine_line_log_btn_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onViewLog();
            }
        });

    }

    private void getMachinesLineData() {
        PersistenceManager pm = PersistenceManager.getInstance();
        getMachineLine(pm.getSiteUrl(), new GetMachineLineCallback() {
            @Override
            public void onGetMachineLineSuccess(MachineLineResponse response) {
                if (isDetached()) {
                    return;
                }
                mLineProgress.setVisibility(View.GONE);
                if (response.getLineID() != 0) {
                    mLineLy.setVisibility(View.VISIBLE);
                    machineLineItems.clear();
                    machineLineItems.addAll(response.getMachinesData());
                    mMachineLineAdapter.notifyDataSetChanged();
                    if (response.getLineName() != null && !response.getLineName().isEmpty()) {
                        mLineNameTv.setText(response.getLineName());
                    } else {
                        mLineNameTv.setText(getResources().getString(R.string.production_line));
                    }
                } else {
                    mLineLy.setVisibility(View.GONE);
                }
                setDashboardContainer3MarginBottom();
            }

            @Override
            public void onGetMachineLineFailed(StandardResponse reason) {
                if (isDetached()) {
                    return;
                }
                ShowCrouton.showSimpleCrouton(mCroutonCallback, reason.getError().getErrorDesc(), CroutonCreator.CroutonType.NETWORK_ERROR);
                mLineProgress.setVisibility(View.GONE);
            }
        }, NetworkManager.getInstance(), pm.getTotalRetries(), pm.getRequestTimeout());
    }

    private void setDashboardContainer3MarginBottom() {

        if (mBottomRl != null) {

            mBottomRl.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onResizeBottomMargin(mBottomRl.getHeight());
                }
            });
        }
    }

    private void initBottomNotificationLayout(View view) {

        if (view != null) {
            LinearLayout bottomNotifLil = view.findViewById(R.id.bottom_notification_lil);
            ImageView bottomNotifIv = view.findViewById(R.id.bottom_notification_iv);
            TextView bottomNotifTv = view.findViewById(R.id.bottom_notification_tv);
            TextView bottomNotifSenderTv = view.findViewById(R.id.bottom_notification_sender_tv);
            TextView bottomNotifTimeTv = view.findViewById(R.id.bottom_notification_time_tv);

            bottomNotifLil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openNotificationsList();
                }
            });

            ArrayList<Notification> notList = PersistenceManager.getInstance().getNotificationHistoryNoTech();
            if (notList == null || notList.size() == 0) {
                bottomNotifIv.setVisibility(View.INVISIBLE);
                bottomNotifTv.setText("");
                bottomNotifTimeTv.setText("");
                bottomNotifSenderTv.setText("");
            } else {
                Notification notification = notList.get(0);
                bottomNotifIv.setVisibility(View.VISIBLE);
                bottomNotifIv.setImageResource(notification.getResponseIcon(getActivity()));
                bottomNotifTv.setText(notification.getmBody(getActivity()));
                bottomNotifTimeTv.setText(notification.getmResponseDate());
                bottomNotifSenderTv.setText(PersistenceManager.getInstance().getCurrentLang().equals("en") ? notification.getmOriginalSenderName() : notification.getmOriginalSenderHName());

            }
        }

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
//        cursor = mDatabaseHelper.getCursorOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration());
        cursor = mDatabaseHelper.getCursorOrderByTimeFilterByDuration(0);
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

                }
//                else if (type == Consts.NOTIFICATION_TYPE_FROM_WEB) {
//
//                setNotificationNeedResponse();
//                }
                openNotificationPopUp(intent.getIntExtra(Consts.NOTIFICATION_ID, 0));

                initBottomNotificationLayout(getView());
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
                //check if notification was sent to self
//                if (notification.getmTargetUserId() == notification.getmSourceUserID()) {
                if (PersistenceManager.getInstance().isSelfNotification(notificationId + "")) {
                    if (mPopUpDialog != null && mPopUpDialog instanceof NotificationsDialog && mPopUpDialog.isShowing()) {
                        openNotificationsList();
                    }
                    return;
                }

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
                ImageView btnApprove = mPopUpDialog.findViewById(R.id.notification_popup_approve_btn);
                ImageView btnDecline = mPopUpDialog.findViewById(R.id.notification_popup_decline_btn);
                ImageView btnClarify = mPopUpDialog.findViewById(R.id.notification_popup_clarify_btn);
                TextView tvSender = mPopUpDialog.findViewById(R.id.notification_popup_tv_sender);
                TextView tvBody = mPopUpDialog.findViewById(R.id.notification_popup_tv_body);

                if (notification.getmResponseType() != 0) {
                    btnApprove.setVisibility(View.GONE);
                    btnClarify.setVisibility(View.GONE);
                    btnDecline.setVisibility(View.GONE);
                }

                if (notification.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN) {

                    tvSender.setText(getResources().getString(R.string.technician) + " " + notification.getmTargetName());
                    String str;
                    switch (notification.getmResponseType()) {

                        case Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE:
                            str = getResources().getString(R.string.call_approved2);
                            tvBody.setText(String.format(str, notification.getmSender()));
                            break;

                        case Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE:
                            str = getResources().getString(R.string.call_declined2);
                            tvBody.setText(String.format(str, notification.getmSender()));
                            break;


                        case Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE:
                            str = getResources().getString(R.string.started_service2);
                            tvBody.setText(String.format(str, notification.getmSender()));
                            break;


                        case Consts.NOTIFICATION_RESPONSE_TYPE_END_SERVICE:
                            str = getResources().getString(R.string.service_completed2);
                            tvBody.setText(String.format(str, notification.getmSender()));
                            break;

                        case Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED:
                            str = getResources().getString(R.string.call_cancelled2);
                            tvBody.setText(String.format(str, notification.getmSender()));
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
                                sendNotificationResponse(notificationId, Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE, false);
                                mPopUpDialog.dismiss();
                                break;

                            case R.id.notification_popup_decline_btn:
                                sendNotificationResponse(notificationId, Consts.NOTIFICATION_RESPONSE_TYPE_DECLINE, false);
                                mPopUpDialog.dismiss();
                                break;

                            case R.id.notification_popup_clarify_btn:
                                sendNotificationResponse(notificationId, Consts.NOTIFICATION_RESPONSE_TYPE_MORE_DETAILS, false);
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
                        mListener.onWidgetUpdateSpane(false);

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
                mListener.onWidgetUpdateSpane(true);

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
        public void onOperatorDataReceiveFailure(StandardResponse reason) {

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
            //            Zloger.clearPollingRequest(TAG, "onSetOperatorSuccess() ");
        }

        @Override
        public void onSetOperatorFailed(StandardResponse reason) {
            OppAppLogger.getInstance().d(LOG_TAG, "onSetOperatorFailed() " + reason.getError().getErrorDesc());
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

//        initEvents(mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()));
        onShiftLogDataReceived(null, mActualBarExtraResponse, null);


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

        //        OppAppLogger.getInstance().clearPollingRequest(TAG, "setActionBar(),  " + " toolBar: " + mToolBarView.getHeight() + " -- " + mTollBarsHeight * 0.65);
        //        OppAppLogger.getInstance().clearPollingRequest(TAG, "setActionBar(),  " + " status: " + mStatusLayout.getHeight() + " -- " + mTollBarsHeight * 0.35);
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

                    PersistenceManager.getInstance().setJoshID(mActiveJobsListForMachine.getActiveJobs().get(mSelectedPosition).getJoshID());

                    mListener.onJoshProductSelected(position, mActiveJobs.get(position),
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
                    || mVisiblefragment instanceof ReportStopReasonFragment
                    || mVisiblefragment instanceof ReportShiftFragment)) {
                return;
            }

            LayoutInflater inflator = initActionBar(actionBar);

            // TODO: 08/07/2018 update to new toolbar
            mToolBarView = inflator.inflate(R.layout.actionbar_title_and_tools_view, null);

            final ImageView statusLock = mToolBarView.findViewById(R.id.toolbar_status_lock_iv);
            ImageView notificationIv = mToolBarView.findViewById(R.id.toolbar_notification_button);
            technicianRl = mToolBarView.findViewById(R.id.toolbar_technician_button);
            ImageView tutorialIv = mToolBarView.findViewById(R.id.toolbar_tutorial_iv);
            mTechnicianIconIv = mToolBarView.findViewById(R.id.toolbar_technician_iv);
            mTechOpenCallsIv = mToolBarView.findViewById(R.id.toolbar_technician_open_calls_tv);
//            mNotificationIndicatorCircleFl = mToolBarView.findViewById(R.id.toolbar_notification_counter_circle);
//            mNotificationIndicatorNumTv = mToolBarView.findViewById(R.id.toolbar_notification_counter_tv);
            mStatusTimeMinTv = mToolBarView.findViewById(R.id.ATATV_status_time_min);
            mToolBarView.findViewById(R.id.ATATV_machine_ly).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onChangeMachineRequest();
                }
            });

            statusLock.setImageDrawable(getResources().getDrawable(PersistenceManager.getInstance().isStatusBarLocked() ? R.drawable.lock : R.drawable.lock_open));
            statusLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PersistenceManager.getInstance().isStatusBarLocked()) {
                        final LockStatusBarDialog dialog = new LockStatusBarDialog(getActivity(), new LockStatusBarDialog.LockStatusBarListener() {
                            @Override
                            public void unlockSuccess() {
                                PersistenceManager.getInstance().setStatusBarLocked(false);
                                statusLock.setImageDrawable(getResources().getDrawable(R.drawable.lock_open));
                            }
                        });
                        dialog.show();
                    } else {
                        PersistenceManager.getInstance().setStatusBarLocked(true);
                        statusLock.setImageDrawable(getResources().getDrawable(R.drawable.lock));
                    }
                }
            });

            getNotificationsFromServer(false);
            setTechnicianCallStatus();

            tutorialIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PersistenceManager.getInstance().isStatusBarLocked()) {
                        Toast.makeText(getActivity(), "Please unlock app...", Toast.LENGTH_SHORT).show();
                    } else {
                        openOtherApps();
                    }
//                    startToolbarTutorial();
                }
            });

//            notificationIv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openNotificationsList();
//                }
//            });

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
                                    mOnGoToScreenListener.goToFragment(ReportProductionFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true, true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ReportProductionFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true, true);
                                }
                                break;
                            }
                            case 4: {
                                mListener.onOpenQCActivity();
                                break;
                            }
                            case 5: {
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
//            mToolBarView.findViewById(R.id.ATATV_job_indicator_ly).setOnClickListener(this);

//            if (mMachineStatusLayout == null) {
//                mMachineStatusLayout = mToolBarView.findViewById(R.id.linearLayout2);
//                mMachineStatusLayout.setVisibility(View.INVISIBLE);
//            }
            ImageView settingsButton = mToolBarView.findViewById(R.id.settings_button);
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnGoToScreenListener.goToFragment(SettingsFragment.newInstance(), false, true);
                }
            });
            actionBar.setCustomView(mToolBarView);

//            setToolBarHeight(mToolBarView);

            if (mCurrentMachineStatus != null && mCurrentMachineStatus.getAllMachinesData().size() > 0) {

                onDeviceStatusChanged(mCurrentMachineStatus);
            }

            initLenoxView(notificationIv, technicianRl, tutorialIv, mJobsSpinner);

        }

        if ((!BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) && PersistenceManager.getInstance().isDisplayToolbarTutorial()) {
//            startToolbarTutorial();
        }
        if (permissionResponseHashmap != null) {
            displayViewByServerSettings(permissionResponseHashmap);
        }
    }

    private void openOtherApps() {
        LauncherDialog dialog = new LauncherDialog(getActivity());
        dialog.show();
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
        List<Technician> techniciansList = new ArrayList<Technician>();
        if (((DashboardActivity) getActivity()).getReportForMachine() != null && ((DashboardActivity) getActivity()).getReportForMachine().getTechnicians() != null) {
            techniciansList = ((DashboardActivity) getActivity()).getReportForMachine().getTechnicians();
        }
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
//        if (sourceUserId == null || sourceUserId.equals("")) {
////            sourceUserId = "0";
//            sourceUserId = pm.getUserId() + "";
//        }


        PostTechnicianCallRequest request = new PostTechnicianCallRequest(pm.getSessionId(), pm.getMachineId(), title, technician.getID(), body, operatorName, technician.getEName(), sourceUserId);
        NetworkManager.getInstance().postTechnicianCall(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                if (response.body() != null && response.body().getError().getErrorDesc() == null) {

                    PersistenceManager.getInstance().setTechnicianCallTime(Calendar.getInstance().getTimeInMillis());
                    PersistenceManager.getInstance().setCalledTechnicianName(techName);

                    TechCallInfo techCall = new TechCallInfo(0, techName, getString(R.string.called_technician) + "\n" + techName, Calendar.getInstance().getTimeInMillis(), response.body().getLeaderRecordID(), technician.getID());
                    PersistenceManager.getInstance().setCalledTechnician(techCall);
                    PersistenceManager.getInstance().setRecentTechCallId(techCall.getmNotificationId());
                    setTechnicianCallStatus();
                    getNotificationsFromServer(false);
                    ProgressDialogManager.dismiss();
                    mListener.onTechnicianCalled();

                    new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.TECH_CALL, true, "technician name: " + techName);

//                    Tracker tracker = ((OperatorApplication) getActivity().getApplication()).getDefaultTracker();
//                    tracker.setHostname(PersistenceManager.getInstance().getSiteName());
//                    tracker.send(new HitBuilders.EventBuilder()
//                            .setCategory("Technician Call")
//                            .setAction("Technician was called Successfully")
//                            .setLabel("technician name: " + techName)
//                            .build());
                } else {
                    String msg = "failed";
                    if (response.body() != null && response.body().getError() != null) {
                        msg = response.body().getError().getErrorDesc();
                    }
                    onFailure(call, new Throwable(msg));
                }
                mPopUpDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                ProgressDialogManager.dismiss();
                PersistenceManager.getInstance().setCalledTechnicianName("");

                String m = "";
                if (t != null && t.getMessage() != null) {
                    m = t.getMessage();
                }

                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.TECH_CALL, false, "reason: " + m);

//                Tracker tracker = ((OperatorApplication) getActivity().getApplication()).getDefaultTracker();
//                tracker.setHostname(PersistenceManager.getInstance().getSiteName());
//                tracker.send(new HitBuilders.EventBuilder()
//                        .setCategory("Technician Call")
//                        .setAction("Call for Technician failed")
//                        .setLabel("reason: " + m)
//                        .build());

                final GenericDialog dialog = new GenericDialog(getActivity(), t.getMessage(), getString(R.string.call_technician_title), getString(R.string.ok), true);
                final AlertDialog alertDialog = dialog.showNoProductionAlarm();
                dialog.setListener(new GenericDialog.OnGenericDialogListener() {
                    @Override
                    public void onActionYes() {
                        alertDialog.cancel();
                    }

                    @Override
                    public void onActionNo() {
                    }

                    @Override
                    public void onActionAnother() {
                    }
                });
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
//            notificationIv.setVisibility(View.INVISIBLE);
            technicianIv.setVisibility(View.INVISIBLE);
            tutorialIv.setVisibility(View.INVISIBLE);
            jobsSpinner.setVisibility(View.GONE);
            mMainView.findViewById(R.id.FAAE_product_ly).setVisibility(View.GONE);
            mMainView.findViewById(R.id.FAAE_product_ly_separator1).setVisibility(View.GONE);
            mMainView.findViewById(R.id.FAAE_josh_id_ly).setVisibility(View.GONE);
            mMainView.findViewById(R.id.FAAE_josh_id_ly_separator2).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_production_spinner).setVisibility(View.GONE);
            displayOperatorView(View.GONE);
            displayTechnicianView(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_production_text).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_job_spinner_separator).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.toolbar_after_job_spinner_separator).setVisibility(View.GONE);
            mToolBarView.findViewById(R.id.ATATV_language_spinner).setVisibility(View.GONE);
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

    private void displayViewByServerSettings(SparseArray<WidgetInfo> permissionResponse) {
        if (permissionResponse == null) {
            return;
        }
        displayTechnicianView(WidgetInfo.getWidgetInfo(permissionResponse, WidgetInfo.PermissionId.SERVICE_CALLS.getId()).getHaspermission());
        displayOperatorView(WidgetInfo.getWidgetInfo(permissionResponse, WidgetInfo.PermissionId.OPERATOR_SIGN_IN.getId()).getHaspermission());
        if (getView() != null) {
            getView().findViewById(R.id.bottom_notification_lil).setVisibility(WidgetInfo.getWidgetInfo(permissionResponse, WidgetInfo.PermissionId.MESSAGES.getId()).getHaspermission());
        }
        if (mToolBarView != null) {
            mToolBarView.findViewById(R.id.toolbar_production_status_rl).setVisibility(WidgetInfo.getWidgetInfo(permissionResponse, WidgetInfo.PermissionId.PRODUCTION_STATUS.getId()).getHaspermission());
        }
        if (mTimeLineType != null) {
            if (WidgetInfo.getWidgetInfo(permissionResponse, WidgetInfo.PermissionId.EVENT_LIST.getId()).getHaspermissionBoolean()
                    && WidgetInfo.getWidgetInfo(permissionResponse, WidgetInfo.PermissionId.SHIFT_LOG.getId()).getHaspermissionBoolean()) {
                mTimeLineType.setVisibility(View.VISIBLE);
            } else {
                mTimeLineType.setVisibility(View.GONE);
                mTimeLineType.setChecked(WidgetInfo.getWidgetInfo(permissionResponse, WidgetInfo.PermissionId.SHIFT_LOG.getId()).getHaspermissionBoolean());//id only one is true
            }
        }
    }

    public void displayOperatorView(int visibility) {
        if (mToolBarView != null) {
            mToolBarView.findViewById(R.id.toolbar_operator_fl).setVisibility(visibility);
            mToolBarView.findViewById(R.id.iv_user_icon).setVisibility(visibility);
        }
    }

    public void displayTechnicianView(int visibility) {
        if (mToolBarView != null) {
            mToolBarView.findViewById(R.id.toolbar_technician_button).setVisibility(visibility);
            mToolBarView.findViewById(R.id.toolbar_technician_separator).setVisibility(visibility);
        }
    }

    private void setTechnicianCallStatus() {

        if (isAdded()) {
            ArrayList<TechCallInfo> techList = PersistenceManager.getInstance().getCalledTechnician();
            if (techList != null && techList.size() > 0) {
                mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.technician_blue_svg));
                mTechOpenCallsIv.setVisibility(View.VISIBLE);
                int counter = 0;
                for (TechCallInfo call : techList) {
                    if (call.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_APPROVE || call.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET
                            || call.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_START_SERVICE) {
                        counter++;
                    }
                }
                mTechOpenCallsIv.setText(counter + "");
            } else {
                mTechOpenCallsIv.setVisibility(View.INVISIBLE);
                mTechnicianIconIv.setImageDrawable(getResources().getDrawable(R.drawable.technician_white));
                PersistenceManager.getInstance().setRecentTechCallId(0);
            }
        }
    }

//    private void setNotificationNeedResponse() {
////        int counter = 0;
//        for (Notification item : PersistenceManager.getInstance().getNotificationHistory()) {
//            if (item.getmNotificationType() != Consts.NOTIFICATION_TYPE_TECHNICIAN && item.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET) {
//                mNotificationIndicatorCircleFl.setVisibility(View.VISIBLE);
//                return;
//            }
////            if (item.getmNotificationType() != Consts.NOTIFICATION_TYPE_TECHNICIAN && item.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_UNSET) {
////                counter++;
////            }
//        }
//
//        mNotificationIndicatorCircleFl.setVisibility(View.INVISIBLE);
//
////        if (counter > 0) {
////            mNotificationIndicatorCircleFl.setVisibility(View.VISIBLE);
////            mNotificationIndicatorNumTv.setText(counter + "");
////        } else {
////            mNotificationIndicatorCircleFl.setVisibility(View.INVISIBLE);
////            mNotificationIndicatorNumTv.setText(counter + "");
////        }
//    }

    private void openTechniciansList() {
        if ((getActivity()) == null || !(getActivity() instanceof DashboardActivity)) {
            return;
        }

        final List<Technician> techniciansList = ((DashboardActivity) getActivity()).getReportForMachine().getTechnicians();

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
                NetworkManager.getInstance().postTechnicianCall(request, new Callback<StandardResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {
                        if (response.body() != null && response.body().getError().getErrorDesc() == null) {

                            PersistenceManager.getInstance().setTechnicianCallTime(Calendar.getInstance().getTimeInMillis());
                            PersistenceManager.getInstance().setCalledTechnicianName(techName);

                            TechCallInfo techCall = new TechCallInfo(0, techName, getString(R.string.called_technician) + "\n" + techName, Calendar.getInstance().getTimeInMillis(), response.body().getLeaderRecordID(), technicianId);
                            PersistenceManager.getInstance().setCalledTechnician(techCall);
                            PersistenceManager.getInstance().setRecentTechCallId(techCall.getmNotificationId());
                            setTechnicianCallStatus();
                            getNotificationsFromServer(false);
                            ProgressDialogManager.dismiss();


                            new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.TECH_CALL, true, "technician name: " + techName);
//                            Tracker tracker = ((OperatorApplication) getActivity().getApplication()).getDefaultTracker();
//                            tracker.setHostname(PersistenceManager.getInstance().getSiteName());
//                            tracker.send(new HitBuilders.EventBuilder()
//                                    .setCategory("Technician Call")
//                                    .setAction("Technician was called Successfully")
//                                    .setLabel("technician name: " + techName)
//                                    .build());
                        } else {
                            String msg = "failed";
                            if (response.body() != null && response.body().getError() != null) {
                                msg = response.body().getError().getErrorDesc();
                            }
                            onFailure(call, new Throwable(msg));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                        ProgressDialogManager.dismiss();
                        PersistenceManager.getInstance().setCalledTechnicianName("");

                        String m = "";
                        if (t != null && t.getMessage() != null) {
                            m = t.getMessage();
                        }

                        new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.TECH_CALL, false, "reason: " + m);

//                        Tracker tracker = ((OperatorApplication) getActivity().getApplication()).getDefaultTracker();
//                        tracker.setHostname(PersistenceManager.getInstance().getSiteName());
//                        tracker.send(new HitBuilders.EventBuilder()
//                                .setCategory("Technician Call")
//                                .setAction("Call for Technician failed")
//                                .setLabel("reason: " + m)
//                                .build());

                        final GenericDialog dialog = new GenericDialog(getActivity(), t.getMessage(), getString(R.string.call_technician_title), getString(R.string.ok), true);
                        final AlertDialog alertDialog = dialog.showNoProductionAlarm();
                        dialog.setListener(new GenericDialog.OnGenericDialogListener() {
                            @Override
                            public void onActionYes() {
                                alertDialog.cancel();
                            }

                            @Override
                            public void onActionNo() {
                            }

                            @Override
                            public void onActionAnother() {
                            }
                        });

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

        mPopUpDialog = new NotificationsDialog(getActivity(), new NotificationsDialog.NotificationsDialogListener() {
            @Override
            public void onSendNewNotification(String text) {
                sendNewNotification(text);
            }

            @Override
            public void onNotificationResponse(int notificationId, int responseType) {
                sendNotificationResponse(notificationId, responseType, true);
            }

            public void onGetNotificationsFromServer(boolean openNotificationDialog) {
                getNotificationsFromServer(openNotificationDialog);
            }
        });

        mPopUpDialog.setCanceledOnTouchOutside(true);
        mPopUpDialog.show();

        new GoogleAnalyticsHelper().trackScreen(getActivity(), "Notifications dialog");
    }

    private void sendNewNotification(String text) {
        int machineId = PersistenceManager.getInstance().getMachineId();
        String sessionId = PersistenceManager.getInstance().getSessionId();
        String title = "notification from opapp";
        SendNotificationRequest request = new SendNotificationRequest(machineId, text, title, sessionId);
        ProgressDialogManager.show(getActivity());

        NetworkManager.getInstance().postSendNotification(request, new Callback<NotificationHistoryResponse>() {
            @Override
            public void onResponse(Call<NotificationHistoryResponse> call, Response<NotificationHistoryResponse> response) {
                ProgressDialogManager.dismiss();
                if (mPopUpDialog != null && mPopUpDialog.isShowing()) {
                    mPopUpDialog.dismiss();
                }
                PersistenceManager.getInstance().setSelfNotificationsId(response.body().getLeaderRecordID() + "");
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.SEND_NOTIFICATION, true, "Send New Notification");
            }

            @Override
            public void onFailure(Call<NotificationHistoryResponse> call, Throwable t) {
                ProgressDialogManager.dismiss();
                new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.SEND_NOTIFICATION, false, "Send New Notification");
            }
        });
    }

    private void sendNotificationResponse(final int notificationId, final int responseType, final boolean isRefresh) {

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
                    notification[0].getmSourceUserID() + "",
                    notification[0].getmSender(),
                    pm.getOperatorName(),
                    "",
                    notification[0].getmTargetUserId() + "");

//            RespondToNotificationRequest request = new RespondToNotificationRequest(pm.getSessionId(),
//                    getResources().getString(R.string.respond_notification_title),
//                    notification[0].getmBody(getActivity()),
//                    pm.getMachineId() + "",
//                    notification[0].getmNotificationID() + "",
//                    responseType,
//                    Consts.NOTIFICATION_TYPE_FROM_WEB,
//                    Consts.NOTIFICATION_RESPONSE_TARGET_WEB,
//                    pm.getUserId() + "",
//                    pm.getOperatorName(),
//                    notification[0].getmSender(),
//                    opId,
//                    notification[0].getmTargetUserId() + "");

            ProgressDialogManager.show(getActivity());
            NetworkManager.getInstance().postResponseToNotification(request, new Callback<StandardResponse>() {
                @Override
                public void onResponse(@NonNull Call<StandardResponse> call, @NonNull Response<StandardResponse> response) {

                    new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.RESPOND_TO_NOTIFICATION, true, "Respond to Notification- ID: " + notification[0].getmNotificationID());

                    ArrayList<Notification> nList = pm.getNotificationHistory();
                    notification[0].setmResponseType(responseType);
                    nList.add(notification[0]);
                    pm.setNotificationHistory(nList);
//                    setNotificationNeedResponse();
                    if (isRefresh) {
                        openNotificationsList();
                    }
                    if (ProgressDialogManager.isShowing()) {
                        ProgressDialogManager.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StandardResponse> call, @NonNull Throwable t) {
                    new GoogleAnalyticsHelper().trackEvent(getActivity(), GoogleAnalyticsHelper.EventCategory.RESPOND_TO_NOTIFICATION, false, "Respond to Notification- ID: " + notification[0].getmNotificationID());
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
        textview.setVisibility(View.VISIBLE);
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
                    mListener.onProductionStatusChanged(finalStatusList.get(position).getId(), finalStatusList.get(position).getEName(), machineLineItems);
                    productionStatusSpinner.setVisibility(View.INVISIBLE);
                    textview.setVisibility(View.INVISIBLE);
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

    public void setProductionStatusVisible() {
        if (mToolBarView != null) {
            mToolBarView.findViewById(R.id.toolbar_production_spinner).setVisibility(View.VISIBLE);
            mToolBarView.findViewById(R.id.toolbar_production_text).setVisibility(View.VISIBLE);
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

            mOperatorsSpinnerArray.add(getResources().getString(R.string.edit_operators));
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

        if (getContext() == null) {
            return;
        }
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

//        Cursor cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), eventReasonId);
        Cursor cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(0, eventReasonId);
        ArrayList<Event> events = mDatabaseHelper.getListFromCursor(cursor);

        if (event == null) {
            if (events.size() > 0) {
                event = events.get(0);
            } else {
                return;
            }
        }

        if (mListener != null && events != null && events.size() > 0) {
            mListener.onOpenReportStopReasonFragment(ReportStopReasonFragment.newInstance(mIsOpen, mActiveJobsListForMachine, mSelectedPosition));
        } else {
            return;
        }

        mIsSelectionMode = true;
        mFirstSeletedEvent = event;

        if (myTaskListener == null) {
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
            setShiftLogAdapter(cursor);

            initEvents(events);

            onStopEventSelected(event.getEventID(), true);

            mShowAlarmCheckBoxLy.setVisibility(View.GONE);
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
    public void onPermissionForMachinePolling(SparseArray<WidgetInfo> permissionResponse) {
        permissionResponseHashmap = permissionResponse;
        displayViewByServerSettings(permissionResponse);

    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {
        OppAppLogger.getInstance().i(LOG_TAG, "onDeviceStatusChanged()");

        mCurrentMachineStatus = machineStatus;

        initStatusLayout(mCurrentMachineStatus);

        mMinDurationText.setText(String.format(Locale.getDefault(), "%d %s", machineStatus.getAllMachinesData().get(0).getMinEventDuration(), getString(R.string.minutes)));

        getMachinesLineData();

//        if (mSwipeToRefresh.isRefreshing()){
//            mSwipeToRefresh.setRefreshing(false);
//        }

        if (!BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {

            setupProductionStatusSpinner();

            setupOperatorSpinner();

            enableActionSpinner();
        }

    }

    public void enableActionSpinner() {
        //Attention mJobActionsSpinnerItems.get(0).getUniqueID() is dependant to  R.array.jobs_spinner_array order
        disableActionInSpinner(WidgetInfo.getWidgetInfo(permissionResponseHashmap, WidgetInfo.PermissionId.ACTIVATE_JOB.getId()).getHaspermissionBoolean(), mJobActionsSpinnerItems.get(0).getUniqueID());
        disableActionInSpinner(mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                && WidgetInfo.getWidgetInfo(permissionResponseHashmap, WidgetInfo.PermissionId.ADD_REJECTS.getId()).getHaspermissionBoolean(), mJobActionsSpinnerItems.get(1).getUniqueID());
        disableActionInSpinner(mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                && WidgetInfo.getWidgetInfo(permissionResponseHashmap, WidgetInfo.PermissionId.CHANGE_UNITS_IN_CYCLE.getId()).getHaspermissionBoolean(), mJobActionsSpinnerItems.get(2).getUniqueID());
        disableActionInSpinner(mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                && WidgetInfo.getWidgetInfo(permissionResponseHashmap, WidgetInfo.PermissionId.REPORT_PRODUCTION.getId()).getHaspermissionBoolean(), mJobActionsSpinnerItems.get(3).getUniqueID());
        disableActionInSpinner(mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                && WidgetInfo.getWidgetInfo(permissionResponseHashmap, WidgetInfo.PermissionId.QC.getId()).getHaspermissionBoolean(), mJobActionsSpinnerItems.get(4).getUniqueID());
        if (!mEndSetupDisable) {
            disableActionInSpinner(mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                            && mCurrentMachineStatus.getAllMachinesData().get(0).canReportApproveFirstItem()
                            && WidgetInfo.getWidgetInfo(permissionResponseHashmap, WidgetInfo.PermissionId.END_SETUP.getId()).getHaspermissionBoolean()
                    , mJobActionsSpinnerItems.get(mJobActionsSpinnerItems.size() - 1).getUniqueID());
        } else {
            mEndSetupDisable = false;
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
    public void onShiftLogDataReceived(ArrayList<Event> events, ActualBarExtraResponse actualBarExtraResponse, MachineJoshDataResponse machineJoshDataResponse) {

        if (events == null) {
            events = new ArrayList<>();
        }
        if (actualBarExtraResponse != null && machineJoshDataResponse != null && machineJoshDataResponse.getDepMachine().size() > 0
                && machineJoshDataResponse.getDepMachine().get(0).getDepartmentMachines().size() > 0) {
            actualBarExtraResponse.setJobData(machineJoshDataResponse.getDepMachine().get(0).getDepartmentMachines().get(0).getJobData());
        }
        mActualBarExtraResponse = actualBarExtraResponse;

        if (mShiftLogSwipeRefresh.isRefreshing()) {
            mShiftLogSwipeRefresh.setRefreshing(false);
        }

//        if (!mIsSelectionMode) {
        mLoadingDataText.setVisibility(View.GONE);
        Event finalLatestEvent = null;
        for (Event event : events) {
            if (event.getEventGroupID() != TYPE_ALERT) {
                finalLatestEvent = event;
                break;
            }
        }
        if (isAdded()) {
            final Event finalLatestEvent1 = finalLatestEvent;
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
            }
            mAsyncTask = new MyTask(events, actualBarExtraResponse, new MyTaskListener() {
                @Override
                public void onComplete() {
                    if (isAdded()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
// in all polling befor the request i update the getshiftlogstartingfrom to current time (for the request i used the previous value of getshiftlogstartingfrom , make the update after that)
                                // if the conditions bellow is completed the endtime of last event is seted to shiftlogstartingfrom in place of time of last polling request
                                if (mIsTimeLine) {
                                    if (finalLatestEvent1 != null && finalLatestEvent1.getEventEndTime() != null
                                            && finalLatestEvent1.getEventEndTime().length() > 0 && mCurrentMachineStatus != null &&
                                            mCurrentMachineStatus.getAllMachinesData() != null && mCurrentMachineStatus.getAllMachinesData().size() > 0) {

                                        PersistenceManager.getInstance().setShiftLogStartingFrom(TimeUtils.getDate(convertDateToMillisecond(finalLatestEvent1.getEventEndTime()), "yyyy-MM-dd HH:mm:ss.SSS"));
                                    }
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
                                if (newEvents != null) {
                                    initEvents(newEvents);
                                }
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

                                mShowAlarmCheckBoxLy.setVisibility(View.GONE);
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
            if (isAdded() && !isCancelled()) {
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

                if (mAutoSelectMode && event.getEventEndTime() != null && event.getEventEndTime().length() > 0 &&
                        mFirstSeletedEvent != null && mSelectedEvents != null
                        && mSelectedEvents.size() == 1) {

                    mustBeClosed = true;
                }

                if (DataSupport.count(Event.class) == 0 || !DataSupport.isExist(Event.class, DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()))) {

                    event.save();

                    mEventsQueue.add(event);

                    if (BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
                        addCheckedAlarms(checkedAlarms, event);
                    }
                } else {

                    addCheckedAlarms(checkedAlarms, event);
                }
            }

            checkedAlarmsHashMap.remove(PersistenceManager.getInstance().getMachineId());

            PersistenceManager.getInstance().setCheckedAlarms(checkedAlarmsHashMap);

            myTaskListener.onShowNotificationText(false);

            Cursor cursor;
            if (mIsSelectionMode) {
                cursor = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), mFirstSeletedEvent.getEventReasonID());
                myTaskListener.onUpdateEventsRecyclerViews(cursor, mDatabaseHelper.getListFromCursor(cursor));
            } else {
                cursor = getCursorByType();
                ArrayList<Event> eventArrayList = mDatabaseHelper.getListFromCursor(cursor);
                if (mIsTimeLine) {
                    eventArrayList = new SaveHelperNew().updateList(mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()), mActualBarExtraResponse, getString(R.string.working));
                }
                myTaskListener.onUpdateEventsRecyclerViews(cursor, eventArrayList);
            }

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
            if (deletedEvents > 0 || isActualBarExtraResponse(actualBarExtraResponse)) {
//                Cursor cursorSelec = null;
//                ArrayList<Event> eventSelectArrayList = new ArrayList<>();
//                if (mIsSelectionMode) {
//                    cursorSelec = mDatabaseHelper.getStopByReasonIdShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration(), mFirstSeletedEvent.getEventReasonID());
//                    eventSelectArrayList = mDatabaseHelper.getListFromCursor(cursorSelec);
//                }
                Cursor cursorNoSelect = getCursorByType();
                ArrayList<Event> eventArrayList = mDatabaseHelper.getListFromCursor(cursorNoSelect);
                ;
                if (mIsTimeLine) {
                    eventArrayList = new SaveHelperNew().updateList(mDatabaseHelper.getListFromCursor(getCursorByTypeTimeLine()), mActualBarExtraResponse, getString(R.string.working));
                }
                if (mIsSelectionMode) {
//                    myTaskListener.onUpdateEventsRecyclerViews(cursorSelec, eventSelectArrayList);
                } else {
                    myTaskListener.onUpdateEventsRecyclerViews(cursorNoSelect, eventArrayList);
                }
                if (eventArrayList.size() == 0) {
                    mNoData = true;
                    myTaskListener.onShowNotificationText(true);
                }
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

    private void initEventRecycler(View view) {
        mEventsRecycler = view.findViewById(R.id.FAAE_events_recycler);
        mEventsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mEventsAdapter = new EventsAdapter(getContext(), this, mIsSelectionMode, mIsOpen);
        mEventsRecycler.setAdapter(mEventsAdapter);

        final boolean[] isZooming = {false};
        mEventsRecycler.addListener(new PinchRecyclerView.PinchRecyclerViewListener() {
            @Override
            public void onScale(float factor) {
                if (mEventsAdapter != null) {
                    mEventsAdapter.setFactor(factor);
                }
            }

            @Override
            public void onScaleBegin() {
            }

            @Override
            public void onScaleEnd() {
            }
        });
    }


    private void initEvents(ArrayList<Event> events) {

        if (mLoadingDataText != null) {
            mLoadingDataText.setVisibility(View.GONE);
        }

        if (events.size() < 1) {
            mNoNotificationsText.setVisibility(View.VISIBLE);
        } else {
            mNoNotificationsText.setVisibility(View.GONE);
        }
        if (events.size() < 1) {
            return;
        }
        mNoData = false;

        if (mEventsAdapter == null && getView() != null) {
            initEventRecycler(getView());
        }
        mEventsAdapter.setIsSelectionMode(mIsSelectionMode);
        mEventsAdapter.setEvents(events);
        mEventsAdapter.setSelectedEvents(mSelectedEvents);
        mEventsAdapter.notifyDataSetChanged();

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

        ArrayList<Event> events = mDatabaseHelper.getAlEvents();
        for (Event event : events) {

            if (event.getEventEndTime() != null && event.getEventEndTime().length() > 0 && TimeUtils.getLongFromDateString(event.getEventEndTime(), "dd/MM/yyyy HH:mm:ss")
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
        disableActionInSpinner(enabled
                && WidgetInfo.getWidgetInfo(permissionResponseHashmap, WidgetInfo.PermissionId.END_SETUP.getId()).getHaspermissionBoolean(), mApproveItemID);
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
    public void onDataFailure(final StandardResponse reason, CallType callType) {

        if (mShiftLogSwipeRefresh.isRefreshing()) {
            mShiftLogSwipeRefresh.setRefreshing(false);
        }

        Log.e(DavidVardi.DAVID_TAG_SPRINT_1_5, "onDataFailure");


        if (callType == CallType.Status) {
//            mMachineStatusLayout.setVisibility(View.VISIBLE);
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

    private void silentLogin(StandardResponse reason) {

        if (!thereAlreadyRequest && getActivity() != null) {

            thereAlreadyRequest = true;

            if (reason.getError().getErrorCodeConstant() == ErrorObjectInterface.ErrorCode.Retrofit) {

                ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mCroutonCallback, new SilentLoginCallback() {
                    @Override
                    public void onSilentLoginSucceeded() {

                        SendBroadcast.refreshPolling(getContext());

                        openSilentLoginOption();
                    }

                    @Override
                    public void onSilentLoginFailed(StandardResponse reason) {

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
        if (nameByLang.length() > 31) {
            nameByLang = nameByLang.substring(0, 31);
        }
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
//        mMachineStatusLayout.setVisibility(View.VISIBLE);

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
        if (mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }
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

        } else if (status == MachineStatus.MachineServerStatus.DOWN_TIME.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.down_time));

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

            }

        }
        //todo
        onShiftLogDataReceived(null, mActualBarExtraResponse, null);
    }

    public void disableSelectMode() {

        mIsSelectionMode = false;

        mFirstSeletedEvent = null;

        mSelectedEvents = null;

        mAutoSelectMode = false;

        Cursor cursor;
        cursor = getCursorByType();
        setShiftLogAdapter(cursor);
        onShiftLogDataReceived(null, mActualBarExtraResponse, null);

        mSelectedNumberLy.setVisibility(View.GONE);

        if (!mIsTimeLine) {
            mShowAlarmCheckBoxLy.setVisibility(View.VISIBLE);
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

//            case R.id.ATATV_job_indicator_ly:
//
//                if (!mEndSetupDisable &&
//                        mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
//                        && mCurrentMachineStatus.getAllMachinesData().get(0).canReportApproveFirstItem()) {
//                    openSetupEndFragment();
//                }
//                break;
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
        mListener.setCycleWarningView(false);
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
        mListener.setCycleWarningView(mCycleWarningViewShow);
    }

    public void hideBlackFilters() {
        mStatusBlackFilter.setVisibility(View.GONE);
        mListener.showBlackFilter(false);
    }

    private void setLanguageSpinner(View view) {

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

        EmeraldSpinner.setSpinnerSize((int) (getResources().getDisplayMetrics().heightPixels * 0.75), mLanguagesSpinner);
    }

    public void setCycleWarningViewShow(boolean show) {
        boolean wasShow = mCycleWarningViewShow;
        if (show && !BuildConfig.FLAVOR.equals(getString(R.string.lenox_flavor_name))) {
            mCycleWarningViewShow = true;
        } else {
            mCycleWarningViewShow = false;
        }
//        if ((wasShow && mCycleWarningView.getVisibility() == View.GONE)) {
//            mListener.resetCycleWarningView(false);
//        } else {
        if (mListener != null) {
            mListener.resetCycleWarningView(wasShow, show);
//        }
        }
    }

    private void sendTokenWithSessionIdToServer() {
        final PersistenceManager pm = PersistenceManager.getInstance();
        final String id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        PostNotificationTokenRequest request = new PostNotificationTokenRequest(pm.getSessionId(), pm.getMachineId(), pm.getNotificationToken(), id);
        NetworkManager.getInstance().postNotificationToken(request, new Callback<StandardResponse>() {
            @Override
            public void onResponse(Call<StandardResponse> call, Response<StandardResponse> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    Log.d(LOG_TAG, "token sent");
                    if (mListener != null) {
                        mListener.onRefreshApplicationRequest();
                    }
                } else {
                    Log.d(LOG_TAG, "token failed");
                }
            }

            @Override
            public void onFailure(Call<StandardResponse> call, Throwable t) {
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

                if (response != null && response.body() != null && response.body().getError().getErrorDesc() == null) {

                    // TODO: 28/03/2019 update tech list for new calls
                    ArrayList<TechCallInfo> techList = PersistenceManager.getInstance().getCalledTechnician();
                    ArrayList<TechCallInfo> techListCopy = new ArrayList<>();
                    techListCopy = techList;
                    for (Notification not : response.body().getmNotificationsList()) {

                        not.setmSentTime(TimeUtils.getStringNoTFormatForNotification(not.getmSentTime()));
                        not.setmResponseDate(TimeUtils.getStringNoTFormatForNotification(not.getmResponseDate()));

                        if (not.getmNotificationType() == Consts.NOTIFICATION_TYPE_TECHNICIAN) {
                            boolean isNew = true;
                            if (techList != null && techListCopy.size() > 0) {
                                for (int i = 0; i < techListCopy.size(); i++) {
                                    TechCallInfo tech = techListCopy.get(i);
                                    if (tech.getmNotificationId() == not.getmNotificationID() && tech.getmResponseType() == not.getmResponseType()
                                            && tech.getmTechnicianId() == not.getmTargetUserId()) {
                                        isNew = false;
                                        tech.setmCallTime(TimeUtils.getLongFromDateString(not.getmResponseDate(), TimeUtils.SIMPLE_FORMAT_FORMAT));
                                        tech.setmResponseType(not.getmResponseType());
                                        techList.set(i, tech);
                                    } else if (tech.getmTechnicianId() == not.getmTargetUserId() && not.getmNotificationID() > tech.getmNotificationId()) {
                                        techList.remove(i);
                                    } else if (tech.getmNotificationId() == not.getmNotificationID() && not.getmResponseType() == Consts.NOTIFICATION_RESPONSE_TYPE_CANCELLED) {
                                        techList.remove(i);
                                    }
                                }

                                if (isNew) {
                                    techList.add(new TechCallInfo(not.getmResponseType(), not.getmTargetName(), not.getmResponseType() + "",
                                            TimeUtils.getLongFromDateString(not.getmResponseDate(), TimeUtils.SIMPLE_FORMAT_FORMAT),
                                            not.getmNotificationID(), not.getmTargetUserId()));
                                }
                            }
                        }
                    }

                    PersistenceManager.getInstance().setNotificationHistory(response.body().getmNotificationsList());
                    PersistenceManager.getInstance().setCalledTechnicianList(techList);
                    if (openNotifications || (mPopUpDialog != null && mPopUpDialog.isShowing() && mPopUpDialog instanceof NotificationsDialog)) {
                        openNotificationsList();
                    }

                } else {
                    PersistenceManager.getInstance().setNotificationHistory(null);
                }

//                setNotificationNeedResponse();
                setTechnicianCallStatus();
                initBottomNotificationLayout(getView());
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

        void onWidgetUpdateSpane(boolean open);

        void onResize(int width, int statusBarsHeight);

        void onOpenReportStopReasonFragment(ReportStopReasonFragment reportStopReasonFragment);

        void onEventSelected(Float event, boolean b);

        void onClearAllSelectedEvents();

        void onJobActionItemClick();

        void onSplitEventPressed(Float eventID);

        void onJoshProductSelected(Integer spinnerProductPosition, ActiveJob jobID, String jobName);

        void onProductionStatusChanged(int id, String newStatus, List<MachinesLineDetail> machineLineItems);

        void onLenoxMachineClicked(Machine machine);

        void onLastShiftItemUpdated();

        void onRefreshApplicationRequest();

        void showBlackFilter(boolean show);

        void showWhiteFilter(boolean show);

        void onShowSetupEndDialog();

        void onTechnicianCalled();

        void setCycleWarningView(boolean cycleWarningViewShow);

        void resetCycleWarningView(boolean wasShow, boolean show);

        void onChangeMachineRequest();

        void onOpenQCActivity();

        void onRefreshMachineLinePolling();

        void onViewLog();

        void onResizeBottomMargin(int bottomMargin);
    }

}
