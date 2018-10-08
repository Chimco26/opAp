package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.operatorinfra.Operator;
import com.example.oppapplog.OppAppLogger;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operators.activejobslistformachineinfra.ActiveJobsListForMachine;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.AllMachinesData;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operators.reportfieldsformachineinfra.PackageTypes;
import com.operators.reportfieldsformachineinfra.ReportFieldsForMachine;
import com.operatorsapp.R;
import com.operatorsapp.activities.DashboardActivity;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.activities.interfaces.SilentLoginCallback;
import com.operatorsapp.adapters.JobsSpinnerAdapter;
import com.operatorsapp.adapters.JoshProductNameSpinnerAdapter;
import com.operatorsapp.adapters.OperatorSpinnerAdapter;
import com.operatorsapp.adapters.ShiftLogSqlAdapter;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.dialogs.DialogFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.CroutonRootProvider;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.model.JobActionsSpinnerItem;
import com.operatorsapp.utils.DavidVardi;
import com.operatorsapp.utils.ResizeWidthAnimation;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.utils.SoftKeyboardUtil;
import com.operatorsapp.utils.TimeUtils;
import com.operatorsapp.utils.broadcast.SelectStopReasonBroadcast;
import com.operatorsapp.utils.broadcast.SendBroadcast;
import com.operatorsapp.view.EmeraldSpinner;
import com.ravtech.david.sqlcore.DatabaseHelper;
import com.ravtech.david.sqlcore.Event;
import org.litepal.crud.DataSupport;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.text.format.DateUtils.DAY_IN_MILLIS;


public class ActionBarAndEventsFragment extends Fragment implements DialogFragment.OnDialogButtonsListener,
        DashboardUICallbackListener,
        OnStopClickListener, CroutonRootProvider, SelectStopReasonBroadcast.SelectStopReasonListener, View.OnClickListener {

    private static final String LOG_TAG = ActionBarAndEventsFragment.class.getSimpleName();
    private static final int ANIM_DURATION_MILLIS = 200;
    //    public static final int TYPE_STOP = 6;
    public static final int TYPE_ALERT = 20;
    private static final int STOPPED = 2;
    private static final double MINIMUM_VERSION_FOR_NEW_ACTIVATE_JOB = 1.8f;

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
    private View mConfigView;
    private View mConfigLayout;
    private TextView mConfigTextView;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        OppAppLogger.getInstance().d(LOG_TAG, "onViewCreated(), start ");
        super.onViewCreated(view, savedInstanceState);


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
        ViewGroup.LayoutParams statusBarParams;
        statusBarParams = mStatusLayout.getLayoutParams();
        statusBarParams.height = (int) (mTollBarsHeight * 0.35);
        mStatusLayout.requestLayout();

        mListener.onResize(mCloseWidth, statusBarParams.height);

        //mSwipeToRefresh = view.findViewById(R.id.swipe_refresh_actionbar_events);
        mProductNameTextView = view.findViewById(R.id.text_view_product_name_and_id);
        mMultipleProductImg = view.findViewById(R.id.FAAE_multiple_product_img);
        mProductSpinner = view.findViewById(R.id.FAAE_product_spinner);
        mJobIdTextView = view.findViewById(R.id.text_view_job_id);
        mShiftIdTextView = view.findViewById(R.id.text_view_shift_id);
        mTimerTextView = view.findViewById(R.id.text_view_timer);
        mSelectedNumberTv = view.findViewById(R.id.FAAE_selected_nmbr);

        mSelectedNumberLy = view.findViewById(R.id.FAAE_event_selected_ly);

        mConfigLayout = view.findViewById(R.id.pConfig_layout);
        mConfigView = view.findViewById(R.id.pConfig_view);
        mConfigTextView = view.findViewById(R.id.text_view_config);
        mConfigTextView.setSelected(true);
        mShiftLogLayout = view.findViewById(R.id.fragment_dashboard_shiftlog);
        mShiftLogParams = mShiftLogLayout.getLayoutParams();
        mShiftLogParams.width = mCloseWidth;
        mShiftLogLayout.requestLayout();

        mShiftLogRecycler = view.findViewById(R.id.fragment_dashboard_shift_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mShiftLogRecycler.setLayoutManager(linearLayoutManager);

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

        //mMinDurationText.setText(String.valueOf(PersistenceManager.getInstance().getMinEventDuration()) + " " + getString(R.string.minutes));
        // mMinDurationLil.setLayoutParams(new RelativeLayout.LayoutParams(mCloseWidth, RelativeLayout.LayoutParams.WRAP_CONTENT));

        final ImageView shiftLogHandle = view.findViewById(R.id.fragment_dashboard_left_btn);

        view.findViewById(R.id.FAAE_unselect_all).setOnClickListener(this);

//        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                SendBroadcast.refreshPolling(getContext());
//                //mSwipeToRefresh.setRefreshing(true);
//            }
//        });

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
                                mDownX = (int) event.getRawX();
                                mShiftLogAdapter.changeState(true);
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

        initJobsSpinner();
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
                mShiftLogAdapter.changeState(true);

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

        mDatabaseHelper = new DatabaseHelper(getContext());

        Cursor mTempCursor = mDatabaseHelper.getCursorOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration());

        if (mTempCursor.moveToFirst()) {
            mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), mTempCursor, !mIsOpen, mCloseWidth,
                    this, mOpenWidth, mRecyclersHeight,
                    false, null);
            mShiftLogRecycler.setAdapter(mShiftLogAdapter);

            mShiftLogAdapter.notifyDataSetChanged();
        }

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


    private void toggleWoopList(ViewGroup.LayoutParams mLeftLayoutParams, int newWidth, boolean isOpen) {
        mLeftLayoutParams.width = newWidth;
        mShiftLogLayout.requestLayout();
//        mListener.onWidgetUpdatemargine(newWidth);
        mListener.onResize(newWidth, mStatusLayout.getLayoutParams().height);

        mShiftLogAdapter.changeState(!isOpen);
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


    private void initJobsSpinner() {
        //TODO
        mActiveJobs = new ArrayList<>();
        if (getActivity() != null) {
            mJoshProductNameSpinnerAdapter = new JoshProductNameSpinnerAdapter(getActivity(), R.layout.item_product_spinner, mActiveJobs);
            mJoshProductNameSpinnerAdapter.setDropDownViewResource(R.layout.item_product_spinner_list);
            mProductSpinner.setAdapter(mJoshProductNameSpinnerAdapter);
            mProductSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

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

            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            SpannableString spannableString = new SpannableString(getActivity().getString(R.string.screen_title));
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.white)), 0, spannableString.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.T12_color)), spannableString.length() - 3, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            LayoutInflater inflator = LayoutInflater.from(getActivity());

            // TODO: 08/07/2018 update to new toolbar
            mToolBarView = inflator.inflate(R.layout.actionbar_title_and_tools_view, null);


//            final TextView title = mToolBarView.findViewById(R.id.toolbar_title);
//            title.setText(spannableString);
//            title.setVisibility(View.VISIBLE);

            ImageView tutorialIv = mToolBarView.findViewById(R.id.toolbar_tutorial_iv);
            tutorialIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startToolbarTutorial();
//                    if (PersistenceManager.getInstance().isDisplayToolbarTutorial()){
//                    }
                }
            });

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

            jobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null || mCurrentMachineStatus.getAllMachinesData().size() == 0) {
                        OppAppLogger.getInstance().w(LOG_TAG, "missing machine status data in job spinner");
                        return;
                    }
                    if (mJobActionsSpinnerItems.get(position).isEnabled()) {
                        // TODO: 5/10/2018 NATAN
                        switch (position) {
                            case 0: {
                                OppAppLogger.getInstance().d(LOG_TAG, "New Job");

                                if (PersistenceManager.getInstance().getVersion() >= MINIMUM_VERSION_FOR_NEW_ACTIVATE_JOB) {

                                    mListener.onJobActionItemClick();

                                } else {

                                    mOnGoToScreenListener.goToFragment(new JobsFragment(), true, true);

                                }
                                break;
                            }
                            case 1: {
                                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                    mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true, true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true, true);
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

                                if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                    mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance(0, mActiveJobsListForMachine, mSelectedPosition), true, true);
                                } else {
                                    mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID(), mActiveJobsListForMachine, mSelectedPosition), true, true);
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


            setupOperatorSpinner();
            //setupProductionStatusSpinner();


            mMachineIdStatusBarTextView = mToolBarView.findViewById(R.id.text_view_machine_id_name);
            mMachineIdStatusBarTextView.setSelected(true);
            mMachineStatusStatusBarTextView = mToolBarView.findViewById(R.id.text_view_machine_status);
            mMachineStatusStatusBarTextView.setSelected(true);
            mStatusIndicatorImageView = mToolBarView.findViewById(R.id.job_indicator);

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
        }

        if (PersistenceManager.getInstance().isDisplayToolbarTutorial()) {
            startToolbarTutorial();
        }

    }

    public void setupProductionStatusSpinner() {

        if (getActivity() == null) {
            return;
        }

        int selected = 0;

        final EmeraldSpinner productionStatusSpinner = mToolBarView.findViewById(R.id.toolbar_production_status);
        ReportFieldsForMachine reportForMachine = ((DashboardActivity) getActivity()).getReportForMachine();
        String[] items;
        String newTitle = "";
        List<PackageTypes> statusList = new ArrayList<>();
        if (reportForMachine != null && reportForMachine.getProductionStatus() != null) {

            statusList = ((DashboardActivity) getActivity()).getReportForMachine().getProductionStatus();
            items = new String[statusList.size()];
            for (int i = 0; i < statusList.size(); i++) {
                items[i] = OperatorApplication.isEnglishLang() ? statusList.get(i).getEName() : statusList.get(i).getLName();
                if (statusList.get(i).getId() == mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID()) {
                    selected = i;
                }
            }
            newTitle = OperatorApplication.isEnglishLang() ? statusList.get(selected).getEName() : statusList.get(selected).getLName();
        } else {
            items = new String[]{""};
        }

        final ArrayAdapter<String> productionStatusSpinnerAdapter = new OperatorSpinnerAdapter(getActivity(), R.layout.spinner_operator_item, items, newTitle);
        productionStatusSpinner.setAdapter(productionStatusSpinnerAdapter);
        productionStatusSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

        final List<PackageTypes> finalStatusList = statusList;
        productionStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mCurrentMachineStatus.getAllMachinesData().get(0).getmProductionModeID() != finalStatusList.get(position).getId()) {
                    ProgressDialogManager.show(getActivity());
                    String newTitle = OperatorApplication.isEnglishLang() ? finalStatusList.get(position).getEName() : finalStatusList.get(position).getLName();
                    ((OperatorSpinnerAdapter) productionStatusSpinner.getAdapter()).updateTitle(newTitle);
                    mListener.onProductionStatusChanged(finalStatusList.get(position).getId());
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
            operatorsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);
            operatorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        mOnGoToScreenListener.goToFragment(new SignInOperatorFragment(), true, true);
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
//        if (mShiftLogAdapter.getItemCount() > 0 && mShiftLogAdapter.getmFirstStopEventPosition() < 9999){
//            mShiftLogRecycler.scrollToPosition(mShiftLogAdapter.getmFirstStopEventPosition());
//            ShiftLogSqlAdapter.ShiftLogViewHolder holder = (ShiftLogSqlAdapter.ShiftLogViewHolder) mShiftLogRecycler.getChildViewHolder(mShiftLogRecycler.getChildAt(mShiftLogAdapter.getmFirstStopEventPosition()));
//            targets.add(TapTarget.forView(holder.mSplitEvent,  getString(R.string.tutorial_split_event_title), getString(R.string.tutorial_split_event_Desc)).targetRadius(50));
//        }

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

//    public void openNextDialog() {//TODO disable by nathan in all the places it's called because it's cause process of show / dismiss alerts too many times
//        OppAppLogger.getInstance().d(LOG_TAG, "openNextDialog(), start ");
//        if (mEventsQueue.peek() != null && (System.currentTimeMillis() - mEventsQueue.peek().getTimeOfAdded()) < THIRTY_SECONDS) {
//            Event event = mEventsQueue.pop();
//            //            event.setAlarmDismissed(true);
//            event.setIsDismiss(true);
//
//            if (event.getEventGroupID() == TYPE_STOP) {
//                //TODO              openStopReportScreen(event.getEventID(), null, null, event.getDuration());
//                startSelectMode(STOPPED, event.getEventID());
//
//
//            } else if (event.getEventGroupID() == TYPE_ALERT) {
//                openDialog(event, false);
//            }
//        } else if (mEventsQueue.peek() == null || mEventsQueue.size() == 0) {
//        }
//    }

    public void setAlertChecked() {

        if (mLastEvent != null && mLastEvent.getEventGroupID() == TYPE_ALERT) {

            mLastEvent.setTreated(true);

            mLastEvent.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(mLastEvent.getEventID()));

            mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), mDatabaseHelper.getCursorOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration()),
                    !mIsOpen, mCloseWidth, this, mOpenWidth, mRecyclersHeight,
                    false, null);

            mShiftLogRecycler.setAdapter(mShiftLogAdapter);

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
    public void onReportClick(int eventId, String start, String end, long duration) {
        //TODO    openStopReportScreen(eventId, start, end, duration);
        startSelectMode(eventId);
    }

    @Override
    public void onStopClicked(int eventId, String startTime, String endTime, long duration) {

        //TODO    openStopReportScreen(eventId, startTime, endTime, duration);
    }

    @Override
    public void onStopEventSelected(Integer event, boolean b) {
        mListener.onEventSelected(event, b);
    }

    @Override
    public void onSplitEventPressed(int eventID) {
        // TODO: 05/07/2018 call server split event
        mListener.onSplitEventPressed(eventID);

    }

    @Override
    public void onSelectMode(int type, int eventID) {

        startSelectMode(eventID);
    }

    private void startSelectMode(int eventID) {

        mListener.onOpenReportStopReasonFragment(ReportStopReasonFragment.newInstance(mIsOpen, mActiveJobsListForMachine, mSelectedPosition));

        mIsSelectionMode = true;

        mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), mDatabaseHelper.getStopTypeShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration()),
                !mIsOpen, mCloseWidth, this, mOpenWidth, mRecyclersHeight,
                true, null);
        mShiftLogRecycler.setAdapter(mShiftLogAdapter);

        onStopEventSelected(eventID, true);
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {
        OppAppLogger.getInstance().i(LOG_TAG, "onDeviceStatusChanged()");

        mCurrentMachineStatus = machineStatus;

        initStatusLayout(mCurrentMachineStatus);

        onApproveFirstItemEnabledChanged(machineStatus.getAllMachinesData().get(0).canReportApproveFirstItem());

        mMinDurationText.setText(String.format(Locale.getDefault(), "%d %s", machineStatus.getAllMachinesData().get(0).getMinEventDuration(), getString(R.string.minutes)));
//        if (mSwipeToRefresh.isRefreshing()){
//            mSwipeToRefresh.setRefreshing(false);
//        }
        setupProductionStatusSpinner();

        setupOperatorSpinner();

    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {

        if (mShiftLogSwipeRefresh.isRefreshing()) {
            mShiftLogSwipeRefresh.setRefreshing(false);
        }
    }


    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events) {

        clearOver24HShift();

        if (mShiftLogSwipeRefresh.isRefreshing()) {
            mShiftLogSwipeRefresh.setRefreshing(false);
        }

//        if (!mIsSelectionMode) {
        mLoadingDataText.setVisibility(View.GONE);

        if (events != null && events.size() > 0) {

            ArrayList<Integer> checkedAlarms = PersistenceManager.getInstance().getCheckedAlarms();

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

                    event.save();

                    if (mIsNewShiftLogs) {
                        mEventsQueue.add(event);
                    }
                } else {

                    if (checkedAlarms != null) {

                        for (Integer integer : checkedAlarms) {

                            if (integer == event.getEventID()) {

                                event.setTreated(true);
                            }

                        }
                    }

                    PersistenceManager.getInstance().setCheckedAlarms(null);


                    event.updateAll(DatabaseHelper.KEY_EVENT_ID + " = ?", String.valueOf(event.getEventID()));
                }
            }

            mNoNotificationsText.setVisibility(View.GONE);


            if (mIsSelectionMode) {

                mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), mDatabaseHelper.getStopTypeShiftOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration()), !mIsOpen, mCloseWidth,
                        this, mOpenWidth, mRecyclersHeight, mIsSelectionMode, mSelectedEvents);
                mShiftLogRecycler.setAdapter(mShiftLogAdapter);

            } else {

                mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), mDatabaseHelper.getCursorOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration()), !mIsOpen, mCloseWidth,
                        this, mOpenWidth, mRecyclersHeight, mIsSelectionMode, mSelectedEvents);
                mShiftLogRecycler.setAdapter(mShiftLogAdapter);

            }

            if (mEventsQueue.size() > 0) {
                Event event = mEventsQueue.peek();
                // we only need to show the stop report when the event is open (no end time) and hase no event reason ( == 0).
                if (event.getEventGroupID() != TYPE_ALERT && TextUtils.isEmpty(event.getEventEndTime()) && event.getEventReasonID() == REASON_UNREPORTED) {

                    if (!mIsSelectionMode) {
                        startSelectMode(event.getEventID());
                    }
                    mEventsQueue.pop();

                } else if (event.getEventGroupID() == TYPE_ALERT) {
                    openDialog(event);
                    mLastEvent = event;
                    mEventsQueue.pop();
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

    private void clearOver24HShift() {

        ArrayList<Event> toDelete = new ArrayList<>();

        for (Event event: mDatabaseHelper.getAlEvents()){

            if (TimeUtils.getLongFromDateString(event.getEventTime(), "dd/MM/yyyy HH:mm:ss")
                < System.currentTimeMillis() - DAY_IN_MILLIS){

                toDelete.add(event);

            }
        }

        for (Event event: toDelete){

            mDatabaseHelper.deleteEvent(event.getEventID());
        }

    }

    @Override
    public void onStart() {
        super.onStart();


    }
//
//    @Override
//    public void onShiftForMachineEnded() {
//       TODO if (mShiftLogParams != null && mWidgetsParams != null) {
//            closeWoopList(mShiftLogParams, mWidgetsParams);
//        }
//
//        DataSupport.deleteAll(Event.class);
//
//        mEventsQueue.clear();
//        mNoData = true;
//        mNoNotificationsText.setVisibility(View.VISIBLE);
//        mLoadingDataText.setVisibility(View.GONE);
//    }

    @Override
    public void onApproveFirstItemEnabledChanged(boolean enabled) {
        if (mJobsSpinnerAdapter != null) {
            for (JobActionsSpinnerItem item : mJobActionsSpinnerItems) {
                if (item.getUniqueID() == mApproveItemID) {
                    item.setEnabled(enabled);
                }
            }
            mJobsSpinnerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActiveJobsListForMachineUICallbackListener(ActiveJobsListForMachine activeJobsListForMachine) {

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

        if (machinesData.getConfigName() != null) {

            mConfigView.setVisibility(View.VISIBLE);
            mConfigLayout.setVisibility(View.VISIBLE);

            mConfigTextView.setText(machinesData.getConfigName());
        } else {
            mConfigView.setVisibility(View.GONE);
            mConfigLayout.setVisibility(View.GONE);

        }

        mMachineIdStatusBarTextView.setText(machineName);
        String statusNameByLang = OperatorApplication.isEnglishLang() ? machinesData.getMachineStatusEname() : machinesData.getMachineStatusLName();
        mMachineStatusStatusBarTextView.setText(statusNameByLang);
        statusAggregation(machineStatus);
        mMachineStatusLayout.setVisibility(View.VISIBLE);


    }

    private void clearStatusLayout() {
        mProductNameTextView.setText("");
        mJobIdTextView.setText("");
        mShiftIdTextView.setText("");
        mMachineIdStatusBarTextView.setText("");
        mMachineStatusStatusBarTextView.setText("");
        mTimerTextView.setText("");
        mConfigTextView.setText("");
        mConfigView.setVisibility(View.GONE);
        mConfigLayout.setVisibility(View.GONE);

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
    }


    private void removeBroadcasts() {

        if (getActivity() != null) {
            try {

                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReasonBroadcast);

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


            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.working));
        } else if (status == MachineStatus.MachineServerStatus.STOPPED.getId()) {


            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.stopped));

        } else if (status == MachineStatus.MachineServerStatus.COMMUNICATION_FAILURE.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.communication_failure));

        } else if (status == MachineStatus.MachineServerStatus.SETUP_COMMUNICATION_FAILURE.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.communication_failure));

        } else if (status == MachineStatus.MachineServerStatus.NO_JOB.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.no_job));

        } else if (status == MachineStatus.MachineServerStatus.SETUP_WORKING.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_indicator_setup));

        } else if (status == MachineStatus.MachineServerStatus.SETUP_STOPPED.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.setup_stopped));

        } else if (status == MachineStatus.MachineServerStatus.PARAMETER_DEVIATION.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.parameter_deviation));

        } else if (status == MachineStatus.MachineServerStatus.STOP_IDLE.getId()) {

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.mipmap.stop_idle));

        } else {
            OppAppLogger.getInstance().w(LOG_TAG, "Undefined parameter");

            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.ic_no_data));
        }

    }

    @Override
    public int getCroutonRoot() {

        return R.id.parent_layouts;

    }

//    private void dismissProgressDialog() {
//        if (getActivity() != null) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ProgressDialogManager.dismiss();
//                }
//            });
//        }
//
//    }

    @Override
    public void onSelectStopReason(int eventId, int reasonId, String en, String il, String eSubTitle, String lSubtitle) {

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


                    event.updateAll("meventid = ?", String.valueOf(eventId));
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

        mShiftLogAdapter = new ShiftLogSqlAdapter(getActivity(), mDatabaseHelper.getCursorOrderByTimeFilterByDuration(PersistenceManager.getInstance().getMinEventDuration()),
                !mIsOpen, mCloseWidth, this, mOpenWidth, mRecyclersHeight,
                false, null);

        mShiftLogRecycler.setAdapter(mShiftLogAdapter);

        mIsSelectionMode = false;

        mSelectedNumberLy.setVisibility(View.GONE);
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
        }
    }

    public void setVisiblefragment(Fragment visibleFragment) {

        mVisiblefragment = visibleFragment;

        if (mToolBarView != null){
            
        if (mVisiblefragment instanceof ActionBarAndEventsFragment ||
                mVisiblefragment instanceof RecipeFragment ||
                mVisiblefragment instanceof WidgetFragment){

            mToolBarView.findViewById(R.id.toolbar_job_spinner).setVisibility(View.VISIBLE);

        }else {

            mToolBarView.findViewById(R.id.toolbar_job_spinner).setVisibility(View.GONE);
        }
        }
    }

    public void setFromAnotherActivity(boolean fromAnotherActivity) {
        this.mFromAnotherActivity = fromAnotherActivity;
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

        void onProductionStatusChanged(int id);
    }

}
