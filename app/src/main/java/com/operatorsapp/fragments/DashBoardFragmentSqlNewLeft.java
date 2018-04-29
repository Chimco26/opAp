package com.operatorsapp.fragments;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.AllMachinesData;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.JobsSpinnerAdapter;
import com.operatorsapp.adapters.OperatorSpinnerAdapter;
import com.operatorsapp.adapters.ShiftLogSqlAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.JobActionsSpinnerItem;
import com.operatorsapp.utils.ResizeWidthAnimation;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast;
import com.operatorsapp.view.EmeraldSpinner;
import com.ravtech.david.sqlcore.DatabaseHelper;
import com.ravtech.david.sqlcore.Event;
import com.zemingo.logrecorder.ZLogger;

import org.litepal.crud.DataSupport;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static com.operatorsapp.fragments.DashboardFragmentSql.REASON_UNREPORTED;
import static com.operatorsapp.fragments.DashboardFragmentSql.TYPE_ALERT;
import static com.operatorsapp.fragments.DashboardFragmentSql.TYPE_STOP;
import static org.acra.ACRA.LOG_TAG;

/**
 * Created by nathanb on 4/29/2018.
 */

public class DashBoardFragmentSqlNewLeft extends Fragment implements CroutonRootProvider, View.OnTouchListener,
        DashboardUICallbackListener, OnStopClickListener, SelectStopReasonBroadcast.SelectStopReasonListener {


    private static final int ANIM_DURATION_MILLIS = 200;
    private LinearLayout mShiftLogLayout;
    private ViewGroup.LayoutParams mShiftLogParams;
    private int mOpenWidth;
    private int mCloseWidth;
    private int mWidgetsLayoutWidth;
    private int mTollBarsHeight;
    private int mRecyclersHeight;
    private TextView mNoNotificationsText;
    private TextView mLoadingDataText;
    private RecyclerView mShiftLogRecycler;
    private View mDividerView;
    private ImageView mShiftLogHandle;
    private int mMiddleWidth;
    private OperatorCore mOperatorCore;
    private OperatorForMachineUICallbackListener mOperatorForMachineUICallbackListener;
    private int mDownX;
    private ShiftLogSqlAdapter mShiftLogAdapter;
    private boolean mNoData;
    private boolean mIsOpen;
    private boolean mIsNewShiftLogs;
    private ArrayDeque<Event> mEventsQueue = new ArrayDeque<>();
    private ArrayList<Event> mNewEventsList = new ArrayList<>();
    private DatabaseHelper mDatabaseHelper;
    private LinearLayout mLoadingDataView;
    private GoToScreenListener mOnGoToScreenListener;
    private SelectStopReasonBroadcast mReasonBroadcast;
    private View mToolBarView;
    private JobsSpinnerAdapter mJobsSpinnerAdapter;
    private List<JobActionsSpinnerItem> mJobActionsSpinnerItems;
    private int mApproveItemID;
    private MachineStatus mCurrentMachineStatus;
    private String[] mOperatorsSpinnerArray;
    private TextView mMachineIdStatusBarTextView;
    private TextView mMachineStatusStatusBarTextView;
    private ImageView mStatusIndicatorImageView;
    private ViewGroup mMachineStatusLayout;
    private TextView mProductNameTextView;
    private TextView mJobIdTextView;
    private TextView mShiftIdTextView;
    private View mConfigView;
    private View mConfigLayout;
    private TextView mConfigTextView;

    public static DashBoardFragmentSqlNewLeft newInstance() {
        return new DashBoardFragmentSqlNewLeft();
    }

    private void registerReceiver() {

        if (mReasonBroadcast == null) {

            mReasonBroadcast = new SelectStopReasonBroadcast(this);

            IntentFilter filter = new IntentFilter();

            filter.addAction(SelectStopReasonBroadcast.ACTION_SELECT_REASON);

            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReasonBroadcast, filter);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        ProgressDialogManager.show(getActivity());

        View inflate = inflater.inflate(R.layout.fragment_dashboard_new, container, false);

        SoftKeyboardUtil.hideKeyboard(this);

        initVars(inflate);

        return inflate;
    }

    private void initVars(View view) {

        mShiftLogLayout = (LinearLayout) view.findViewById(R.id.fragment_dashboard_shiftlog_new);
        mNoNotificationsText = (TextView) view.findViewById(R.id.fragment_dashboard_no_notif_new);
        mLoadingDataText = (TextView) view.findViewById(R.id.fragment_dashboard_loading_data_shiftlog_new);
        mLoadingDataView = (LinearLayout) view.findViewById(R.id.fragment_dashboard_loading_data_widgets_new);

        mShiftLogRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_shift_log_new);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mShiftLogRecycler.setLayoutManager(linearLayoutManager);

        mDividerView = view.findViewById(R.id.fragment_dashboard_divider_new);
        mShiftLogHandle = (ImageView) view.findViewById(R.id.fragment_dashboard_left_btn_new);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ZLogger.d(LOG_TAG, "onViewCreated(), start ");
        super.onViewCreated(view, savedInstanceState);

        initView();
        initListener();
    }

    private void initListener() {

        mDividerView.setOnTouchListener(this);
    }

    private void initView() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        mOpenWidth = (int) (width * 0.4);
        mCloseWidth = (int) (width * 0.2);
        mWidgetsLayoutWidth = (int) (width * 0.8);
        mTollBarsHeight = (int) (height * 0.25);
        mRecyclersHeight = (int) (height * 0.75);
        mMiddleWidth = (int) (width * 0.3);


        mShiftLogParams = mShiftLogLayout.getLayoutParams();
        mShiftLogParams.width = mCloseWidth;
        mShiftLogLayout.requestLayout();

        mOperatorCore.registerListener(mOperatorForMachineUICallbackListener);

        ZLogger.d(LOG_TAG, "onViewCreated(), end ");

    }

    @Override
    public void onAttach(Context context) {
        ZLogger.d(LOG_TAG, "onAttach(), start ");
        super.onAttach(context);
        try {
//            mReportFieldsFragmentCallbackListener = (ReportFieldsFragmentCallbackListener) getActivity();
//            mCroutonCallback = (OnCroutonRequestListener) getActivity();
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
//            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
//            mOnActivityCallbackRegistered.onFragmentAttached(this);
            OperatorCoreToDashboardActivityCallback operatorCoreToDashboardActivityCallback = (OperatorCoreToDashboardActivityCallback) getActivity();
            if (operatorCoreToDashboardActivityCallback != null) {
                mOperatorCore = operatorCoreToDashboardActivityCallback.onSignInOperatorFragmentAttached();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
        ZLogger.d(LOG_TAG, "onAttach(), end ");
    }

    @Override
    public void onDetach() {
        ZLogger.d(LOG_TAG, "onDetach(), start ");
        super.onDetach();
//        mReportFieldsFragmentCallbackListener = null;
//        mCroutonCallback = null;
        mOnGoToScreenListener = null;
//        mOnActivityCallbackRegistered = null;
        mOperatorCore.unregisterListener();
        mOperatorCore = null;
        ZLogger.d(LOG_TAG, "onDetach(), end ");
    }

    private void dismissProgressDialog() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialogManager.dismiss();
                }
            });
        }

    }

    @Override
    public int getCroutonRoot() {
        return R.id.parent_layouts;
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
//
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getRawX();
                mShiftLogHandle.setImageResource(R.drawable.left_panel_btn_pressed);
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
                        mDownX = (int) event.getRawX();
                        mShiftLogAdapter.changeState(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mShiftLogHandle.setImageResource(R.drawable.left_panel_btn);
                if (!mNoData) {
                    if (event.getRawX() - mDownX > 5 || event.getRawX() - mDownX < -5) {
                        if (mShiftLogParams.width < mMiddleWidth) {
                            mIsOpen = false;
                            toggleWoopList(mShiftLogParams, mCloseWidth, null, false);
//                            mGridLayoutManager.setSpanCount(3);
//                            mWidgetAdapter.changeState(true);
                        } else {
                            mIsOpen = true;
                            toggleWoopList(mShiftLogParams, mOpenWidth, null, true);
//                            mGridLayoutManager.setSpanCount(2);
//                            mWidgetAdapter.changeState(false);
                        }

                    } else {
                        onButtonClick(mShiftLogParams, null);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            default:
                mShiftLogHandle.setImageResource(R.drawable.left_panel_btn);
                break;
        }
        return false;

    }

    public void setActionBar() {


        ActionBar actionBar = null;
        if ((getActivity()) != null) {
            actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            SpannableString spannableString = new SpannableString(getActivity().getString(R.string.screen_title));
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.white)), 0, spannableString.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.T12_color)), spannableString.length() - 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            LayoutInflater inflator = LayoutInflater.from(getActivity());


            mToolBarView = inflator.inflate(R.layout.actionbar_title_and_tools_view, null);

            final TextView title = mToolBarView.findViewById(R.id.toolbar_title);
            title.setText(spannableString);
            title.setVisibility(View.VISIBLE);

            final Spinner jobsSpinner = mToolBarView.findViewById(R.id.toolbar_job_spinner);


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
                mJobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }

            jobsSpinner.setAdapter(mJobsSpinnerAdapter);

            jobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null || mCurrentMachineStatus.getAllMachinesData().size() == 0) {
                        ZLogger.w(LOG_TAG, "missing machine status data in job spinner");
                        return;
                    }
                    String nameByLang = OperatorApplication.isEnglishLang() ? mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductEname() : mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductName();
                    if (mJobActionsSpinnerItems.get(position).isEnabled()) {
                        switch (position) {
                            case 0: {
                                ZLogger.d(LOG_TAG, "New Job");
                                mOnGoToScreenListener.goToFragment(new JobsFragment(), true);
                                break;
                            }
                            case 1: {
                                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                    mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance("--", 0), true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance(nameByLang, mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                                }
                                break;
                            }
                            case 2: {
                                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                    mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance("--", 0), true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance(nameByLang, mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                                }
                                break;
                            }
                            case 3: {
                                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                    mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance("--", 0), true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance(nameByLang, mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                                }
                                break;
                            }
                            case 4: {


                                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                    mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance("--", 0), true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance(nameByLang, mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                                }

                                break;
                            }
                        }
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (PersistenceManager.getInstance().getOperatorName() != null) {
                mOperatorsSpinnerArray[0] = getResources().getString(R.string.switch_user);
            } else {
                mOperatorsSpinnerArray[0] = getResources().getString(R.string.operator_sign_in_action_bar);
            }

            EmeraldSpinner operatorsSpinner = mToolBarView.findViewById(R.id.toolbar_operator_spinner);
            final ArrayAdapter<String> operatorSpinnerAdapter = new OperatorSpinnerAdapter(getActivity(), R.layout.spinner_operator_item, mOperatorsSpinnerArray, PersistenceManager.getInstance().getOperatorName());
            operatorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            operatorsSpinner.setAdapter(operatorSpinnerAdapter);
            operatorsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            operatorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        mOnGoToScreenListener.goToFragment(new SignInOperatorFragment(), true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mMachineIdStatusBarTextView = mToolBarView.findViewById(R.id.text_view_machine_id_name);
            mMachineIdStatusBarTextView.setSelected(true);
            mMachineStatusStatusBarTextView = (TextView) mToolBarView.findViewById(R.id.text_view_machine_status);
            mMachineStatusStatusBarTextView.setSelected(true);
            mStatusIndicatorImageView = (ImageView) mToolBarView.findViewById(R.id.job_indicator);

            if (mMachineStatusLayout == null) {
                mMachineStatusLayout = (ViewGroup) mToolBarView.findViewById(R.id.linearLayout2);
                mMachineStatusLayout.setVisibility(View.INVISIBLE);
            }
            ImageView settingsButton = (ImageView) mToolBarView.findViewById(R.id.settings_button);
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnGoToScreenListener.goToFragment(new SettingsFragment(), true);
                }
            });
            actionBar.setCustomView(mToolBarView);

            setToolBarHeight(mToolBarView);

            if (mCurrentMachineStatus != null && mCurrentMachineStatus.getAllMachinesData().size() > 0) {


                onDeviceStatusChanged(mCurrentMachineStatus);
            }
        }

    }

    private void setToolBarHeight(final View view) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ZLogger.d(LOG_TAG, "onPreDraw(), start ");
                if (view.getViewTreeObserver().isAlive()) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                ViewGroup.LayoutParams toolBarParams;
                toolBarParams = view.getLayoutParams();
                toolBarParams.height = (int) (mTollBarsHeight * 0.65);
                ZLogger.d(LOG_TAG, "onPreDraw(), pre request layout ");
                view.requestLayout();
                ZLogger.d(LOG_TAG, "onPreDraw(), end ");
                dismissProgressDialog();
                return false;
            }
        });
    }

    //
    @Override
    public void onShiftForMachineEnded() {
        if (mShiftLogParams != null) {
            closeWoopList(mShiftLogParams, null);
        }

        DataSupport.deleteAll(Event.class);

        mEventsQueue.clear();
        mNoData = true;
        mNoNotificationsText.setVisibility(View.VISIBLE);
        mLoadingDataText.setVisibility(View.GONE);
    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {

    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {

    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {
        mLoadingDataView.setVisibility(View.GONE);

    }

    //
    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events) {
        mLoadingDataText.setVisibility(View.GONE);

        if (events != null && events.size() > 0) {

            Log.e(LOG_TAG, "new events to add to shift log: " + events.size() + " new events list size: " + mNewEventsList.size());

            for (Event event : events) {
                Log.e(LOG_TAG, "new events to add to shift log: " + event.getEventID());
            }

            mNewEventsList.clear();

            mEventsQueue.clear();

            PersistenceManager.getInstance().setShiftLogStartingFrom(com.operatorsapp.utils.TimeUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));

            mNoData = false;

            for (Event event : events) {
                event.setTimeOfAdded(System.currentTimeMillis());

                if (!mIsNewShiftLogs) {
                    event.setIsDismiss(true);
                }

            }


            for (Event event : events) {


                if (DataSupport.count(Event.class) == 0 || !DataSupport.isExist(Event.class, DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()))) {

                    mNewEventsList.add(event);

                    event.save();

                    if (mIsNewShiftLogs) {
                        mEventsQueue.add(event);
                    }
                } else {

                    event.updateAll("meventid = ?", String.valueOf(event.getEventID()));
                }
            }


            mNoNotificationsText.setVisibility(View.GONE);

// TODO: 15/04/2018 David Vardi fix this code
          /*  if (mShiftLogAdapter != null) {
                mShiftLogAdapter.notifyDataSetChanged();
            } else {

                mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), mDatabaseHelper.getCursorOrderByTime(), !mIsOpen, mCloseWidth, this, mOpenWidth, mRecyclersHeight);
                mShiftLogRecycler.setAdapter(mShiftLogAdapter);
            }*/

            mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), mDatabaseHelper.getCursorOrderByTime(), !mIsOpen, mCloseWidth, this, mOpenWidth, mRecyclersHeight);
            mShiftLogRecycler.setAdapter(mShiftLogAdapter);

            if (mEventsQueue.size() > 0) // was !mIsdDialogOpen here.
            {
                Event event = mEventsQueue.peek();
                // we only need to show the stop report when the event is open (no end time) and hase no event reason ( == 0).
                if (event.getEventGroupID() != TYPE_ALERT && TextUtils.isEmpty(event.getEventEndTime()) && event.getEventReasonID() == REASON_UNREPORTED) {
//                    openStopReportScreen(event.getEventID(), event.getEventTime(), event.getEventEndTime(), event.getDuration());
                    //openDialog(event, true);
                    mEventsQueue.pop();
//                    mIsOpenDialog = true;

                } else if (event.getEventGroupID() == TYPE_ALERT) {
//                    openDialog(event, false);
                    mEventsQueue.pop();
//                    mIsOpenDialog = true;
                }

            }
        } else {
            if (DataSupport.count(Event.class) == 0) {
                mNoData = true;
                mNoNotificationsText.setVisibility(View.VISIBLE);
            }
        }
        mIsNewShiftLogs = true;
        PersistenceManager.getInstance().setIsNewShiftLogs(true);

        if (mShiftLogAdapter != null)
            mShiftLogAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {

    }

    @Override
    public void onDataFailure(ErrorObjectInterface reason, CallType callType) {
        mLoadingDataView.setVisibility(View.GONE);

    }

    //
    private void toggleWoopList(ViewGroup.LayoutParams mLeftLayoutParams, int newWidth, ViewGroup.MarginLayoutParams mRightLayoutParams, boolean isOpen) {
        mLeftLayoutParams.width = newWidth;
//        mRightLayoutParams.setMarginStart(newWidth);
        mShiftLogLayout.requestLayout();
//        mWidgetsLayout.setLayoutParams(mRightLayoutParams);

        mShiftLogAdapter.changeState(!isOpen);
//        mWidgetAdapter.changeState(!isOpen);

        //        ZLogger.clearPollingRequest(LOG_TAG, "setActionBar(),  " + " toolBar: " + mToolBarView.getHeight() + " -- " + mTollBarsHeight * 0.65);
        //        ZLogger.clearPollingRequest(LOG_TAG, "setActionBar(),  " + " status: " + mStatusLayout.getHeight() + " -- " + mTollBarsHeight * 0.35);
    }

    //
    @Override
    public void onResume() {
        ZLogger.d(LOG_TAG, "onResume(), Start ");
        super.onResume();

        mDatabaseHelper = new DatabaseHelper(getContext());

        mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), mDatabaseHelper.getCursorOrderByTime(), !mIsOpen, mCloseWidth, this, mOpenWidth, mRecyclersHeight);
        mShiftLogRecycler.setAdapter(mShiftLogAdapter);

        mShiftLogAdapter.notifyDataSetChanged();

        if (DataSupport.count(Event.class) > 0) {
            mNoData = false;
            //            mNoNotificationsText.setVisibility(View.GONE);
            mLoadingDataText.setVisibility(View.GONE);
        } else {
            mNoData = true;
            //            mNoNotificationsText.setVisibility(View.VISIBLE);
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
            //            openNextDialog();
            if (mEventsQueue.size() > 0) {
                Event event = mEventsQueue.peek();//
                if (event.getEventGroupID() == TYPE_STOP) {
                    // TODO Oren - doing this here before we have all the data causes an issue, figure out how to wait for the data or suppress this event
                    //openStopReportScreen(event.getEventID(),event.getEventStartTime(),event.getEventEndTime(),event.getDuration());

                    //openDialog(event, true); // to show pop up dialog, disabled for now
//                    mIsOpenDialog = true;
                } else if (event.getEventGroupID() == TYPE_ALERT) {
//                    openDialog(event, false);
//                    mIsOpenDialog = true;
                }


            }
        } else {
            mNoData = true;
            //            mNoNotificationsText.setVisibility(View.VISIBLE);
            mLoadingDataText.setVisibility(View.VISIBLE);
        }
//        if (mWidgets == null || mWidgets.size() == 0) {
        //            mNoDataView.setVisibility(View.VISIBLE);
//            mLoadingDataView.setVisibility(View.VISIBLE);
//        }
        ZLogger.d(LOG_TAG, "onResume(), end ");
        dismissProgressDialog();
    }

    private void initStatusLayout(MachineStatus machineStatus) {

        AllMachinesData machinesData = machineStatus.getAllMachinesData().get(0);
        String nameByLang = OperatorApplication.isEnglishLang() ? machinesData.getCurrentProductEname() : machinesData.getCurrentProductName();
        mProductNameTextView.setText(new StringBuilder(nameByLang).append(",").append(" ID - ").append(String.valueOf(machinesData.getCurrentProductID())));
        mJobIdTextView.setText((String.valueOf(machinesData.getCurrentJobName())));
        mShiftIdTextView.setText(String.valueOf(machinesData.getShiftIDString()));
        String machineName = OperatorApplication.isEnglishLang() ? machinesData.getMachineEName() : machinesData.getMachineLname();
        if (TextUtils.isEmpty(machineName)) {
            machineName = getString(R.string.dashes);
        }

        if(machinesData.getConfigName()!= null ){

            mConfigView.setVisibility(View.VISIBLE);
            mConfigLayout.setVisibility(View.VISIBLE);

            mConfigTextView.setText( machinesData.getConfigName());
        }else {
            mConfigView.setVisibility(View.GONE);
            mConfigLayout.setVisibility(View.GONE);


        }

        mMachineIdStatusBarTextView.setText(machineName);
        String statusNameByLang = OperatorApplication.isEnglishLang() ? machinesData.getMachineStatusEname() : machinesData.getMachineStatusLName();
        mMachineStatusStatusBarTextView.setText(statusNameByLang);
        statusAggregation(machineStatus);
        mMachineStatusLayout.setVisibility(View.VISIBLE);


    }
    private void statusAggregation(MachineStatus machineStatus) {

        int status = machineStatus.getAllMachinesData().get(0).getMachineStatusID();


        if (status == MachineStatus.MachineServerStatus.WORKING_OK.getId()) {


            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.working));
        } else if (status == MachineStatus.MachineServerStatus.STOPPED.getId()) {


            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.stopped));

        } else if (status == MachineStatus.MachineServerStatus.COMMUNICATION_FAILURE.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.communication_failure));

        } else if (status == MachineStatus.MachineServerStatus.SETUP_COMMUNICATION_FAILURE.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.communication_failure));

        } else if (status == MachineStatus.MachineServerStatus.NO_JOB.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.no_job));

        } else if (status == MachineStatus.MachineServerStatus.SETUP_WORKING.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.ic_indicator_setup));

        } else if (status == MachineStatus.MachineServerStatus.SETUP_STOPPED.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.setup_stopped));

        } else if (status == MachineStatus.MachineServerStatus.PARAMETER_DEVIATION.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.parameter_deviation));

        } else if (status == MachineStatus.MachineServerStatus.STOP_IDLE.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.stop_idle));

        } else {
            ZLogger.w(LOG_TAG, "Undefined parameter");

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_no_data));
        }

    }

    //
    private void closeWoopList(final ViewGroup.LayoutParams leftLayoutParams, final ViewGroup.MarginLayoutParams rightLayoutParams) {
        final ResizeWidthAnimation anim = new ResizeWidthAnimation(mShiftLogLayout, mCloseWidth);
        anim.setDuration(ANIM_DURATION_MILLIS);
        mShiftLogLayout.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mShiftLogAdapter.changeState(true);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toggleWoopList(leftLayoutParams, mCloseWidth, null, false);
//                mGridLayoutManager.setSpanCount(3);
//                mWidgetAdapter.changeState(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mIsOpen = false;
    }

    //
    private void onButtonClick(final ViewGroup.LayoutParams leftLayoutParams, final ViewGroup.MarginLayoutParams rightLayoutParams) {
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
                        toggleWoopList(leftLayoutParams, mOpenWidth, null, true);
                        //                        mShiftLogAdapter.notifyDataSetChanged();
//                        mGridLayoutManager.setSpanCount(2);
//                        mWidgetAdapter.changeState(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mIsOpen = true;
            }
        } else {

            closeWoopList(leftLayoutParams, null);
        }

    }

    @Override
    public void onStopClicked(int eventId, String startTime, String endTime, long duration) {

        openStopReportScreen(eventId, startTime, endTime, duration);
    }

    private void openStopReportScreen(int eventId, String start, String end, long duration) {
        mOnGoToScreenListener.goToFragment(ReportStopReasonFragment.newInstance(start, end, duration, eventId), true);
    }

    //
    @Override
    public void onSelectStopReason(int eventId, int reasonId, String en, String il) {

        List<Event> events = DataSupport.where(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(eventId)).find(Event.class);

        if (events != null && events.size() > 0) {
            {


                events.get(0).setTreated(true);
                events.get(0).setEventGroupID(reasonId);
                events.get(0).setEventGroupLname(il);
                events.get(0).setmEventGroupEname(en);

                if (mShiftLogAdapter != null) {

                    mShiftLogAdapter.notifyDataSetChanged();


                }

            }
        }
    }
}
