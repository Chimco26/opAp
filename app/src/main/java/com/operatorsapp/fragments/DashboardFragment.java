package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.operatorinfra.Operator;
import com.operators.errorobject.ErrorObjectInterface;
import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operators.operatorcore.OperatorCore;
import com.operators.operatorcore.interfaces.OperatorForMachineUICallbackListener;
import com.operators.shiftloginfra.Event;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.JobsSpinnerAdapter;
import com.operatorsapp.adapters.OperatorSpinnerAdapter;
import com.operatorsapp.adapters.ShiftLogAdapter;
import com.operatorsapp.adapters.WidgetAdapter;
import com.operatorsapp.dialogs.DialogFragment;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.OnStopClickListener;
import com.operatorsapp.interfaces.OperatorCoreToDashboardActivityCallback;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.ResizeWidthAnimation;
import com.operatorsapp.utils.ShowCrouton;
import com.operatorsapp.view.EmeraldSpinner;
import com.operatorsapp.view.GridSpacingItemDecoration;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class DashboardFragment extends Fragment implements DialogFragment.OnDialogButtonsListener, DashboardUICallbackListener, OnStopClickListener {

    private static final String LOG_TAG = DashboardFragment.class.getSimpleName();
    private static final int ANIM_DURATION_MILLIS = 200;
    private static final int THIRTY_SECONDS = 30 * 1000;

    private GoToScreenListener mOnGoToScreenListener;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private OperatorCore mOperatorCore;
    private OnCroutonRequestListener mCroutonCallback;
    private RecyclerView mShiftLogRecycler;
    private RecyclerView mWidgetRecycler;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayout mShiftLogLayout;
    private RelativeLayout mWidgetsLayout;
    private TextView mNoNotificationsText;
    private LinearLayout mNoDataView;
    private int mDownX;
    private ShiftLogAdapter mShiftLogAdapter;
    private WidgetAdapter mWidgetAdapter;
    private ArrayDeque<Event> mEventsQueue = new ArrayDeque<>();
    private ArrayList<Event> mEventsList = new ArrayList<>();
    private boolean mNoData;
    private boolean mIsOpen = false;
    private boolean mIsOpenDialog = false;
    private int mCloseWidth;
    private int mOpenWidth;
    private boolean mIsInTouch = false;
    private ArrayList<Widget> mWidgets = new ArrayList<>();
    private String[] mOperatorsSpinnerArray = {"Operator Sign in"};
    private TextView mProductNameTextView;
    private TextView mJobIdTextView;
    private TextView mShiftIdTextView;
    private TextView mTimerTextView;
    private TextView mMachineIdStatusBarTextView;
    private TextView mMachineStatusStatusBarTextView;
    private ImageView mStatusIndicatorImageView;

    private MachineStatus mCurrentMachineStatus;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsOpen = false;
        // get screen parameters
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        mOpenWidth = (int) (width * 0.448);
        mCloseWidth = (int) (width * 0.173);
        final int middleWidth = (int) (width * 0.31);

        mProductNameTextView = (TextView) view.findViewById(R.id.text_view_product_name_and_id);
        mJobIdTextView = (TextView) view.findViewById(R.id.text_view_job_id);
        mShiftIdTextView = (TextView) view.findViewById(R.id.text_view_shift_id);
        mTimerTextView = (TextView) view.findViewById(R.id.text_view_timer);

        mShiftLogRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_shift_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mShiftLogRecycler.setLayoutManager(linearLayoutManager);
        mShiftLogAdapter = new ShiftLogAdapter(getActivity(), mEventsList, !mIsOpen, mCloseWidth, this);
        mShiftLogRecycler.setAdapter(mShiftLogAdapter);

        mWidgetRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_widgets);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mWidgetRecycler.setLayoutManager(mGridLayoutManager);
        GridSpacingItemDecoration mGridSpacingItemDecoration = new GridSpacingItemDecoration(3, 30, true, 0);
        mWidgetRecycler.addItemDecoration(mGridSpacingItemDecoration);
        mWidgetAdapter = new WidgetAdapter(getActivity(), mWidgets, mOnGoToScreenListener);
        mWidgetRecycler.setAdapter(mWidgetAdapter);

        mShiftLogLayout = (LinearLayout) view.findViewById(R.id.fragment_dashboard_shiftlog);
        final ViewGroup.LayoutParams shiftLogParams = mShiftLogLayout.getLayoutParams();
        shiftLogParams.width = mCloseWidth;
        mShiftLogLayout.requestLayout();

        mWidgetsLayout = (RelativeLayout) view.findViewById(R.id.fragment_dashboard_widgets_layout);
        final ViewGroup.MarginLayoutParams widgetsParams = (ViewGroup.MarginLayoutParams) mWidgetsLayout.getLayoutParams();
        widgetsParams.setMarginStart(mCloseWidth);
        mWidgetsLayout.setLayoutParams(widgetsParams);

        mNoNotificationsText = (TextView) view.findViewById(R.id.fragment_dashboard_no_notif);
        mNoDataView = (LinearLayout) view.findViewById(R.id.fragment_dashboard_no_data);

        View mDividerView = view.findViewById(R.id.fragment_dashboard_divider);
        mDividerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mNoData) {
                            int currentX;
                            if (PersistenceManager.getInstance().getCurrentLang().equals("iw")) {
                                currentX = mShiftLogLayout.getLayoutParams().width - (int) event.getRawX() + mDownX;
                            }
                            else {
                                currentX = mShiftLogLayout.getLayoutParams().width + (int) event.getRawX() - mDownX;
                            }
                            if (currentX >= mCloseWidth && currentX <= mOpenWidth) {
                                mShiftLogLayout.getLayoutParams().width = currentX;
                                mShiftLogLayout.requestLayout();
                                mDownX = (int) event.getRawX();
                                mShiftLogAdapter.changeState(true);
                                mShiftLogAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mNoData) {
                            if (event.getRawX() - mDownX > 5 || event.getRawX() - mDownX < -5) {
                                if (shiftLogParams.width < middleWidth) {
                                    mIsOpen = false;
                                    toggleWoopList(shiftLogParams, mCloseWidth, widgetsParams, mIsOpen);
                                    mGridLayoutManager.setSpanCount(3);
                                    mWidgetAdapter.notifyDataSetChanged();
                                }
                                else {
                                    mIsOpen = true;
                                    toggleWoopList(shiftLogParams, mOpenWidth, widgetsParams, mIsOpen);
                                    mGridLayoutManager.setSpanCount(2);
                                    mWidgetAdapter.notifyDataSetChanged();
                                }

                            }
                            else {
                                onButtonClick(shiftLogParams, widgetsParams);
                            }
                        }
                        break;
                }
                return false;
            }
        });


        mOperatorCore.registerListener(mOperatorForMachineUICallbackListener);
        setActionBar();
    }

    private void onButtonClick(final ViewGroup.LayoutParams leftLayoutParams, final ViewGroup.MarginLayoutParams rightLayoutParams) {
        if (!mNoData && !mIsInTouch) {
            if (!mIsOpen) {
                final ResizeWidthAnimation anim = new ResizeWidthAnimation(mShiftLogLayout, mOpenWidth);
                anim.setDuration(ANIM_DURATION_MILLIS);
                mShiftLogLayout.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toggleWoopList(leftLayoutParams, mOpenWidth, rightLayoutParams, true);
                        mShiftLogAdapter.notifyDataSetChanged();
                        mGridLayoutManager.setSpanCount(2);
                        mWidgetAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mIsOpen = true;
            }
            else {
                final ResizeWidthAnimation anim = new ResizeWidthAnimation(mShiftLogLayout, mCloseWidth);
                anim.setDuration(ANIM_DURATION_MILLIS);
                mShiftLogLayout.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mShiftLogAdapter.changeState(true);
                        mShiftLogAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toggleWoopList(leftLayoutParams, mCloseWidth, rightLayoutParams, false);
                        mGridLayoutManager.setSpanCount(3);
                        mWidgetAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mIsOpen = false;
            }
        }
    }

    OperatorForMachineUICallbackListener mOperatorForMachineUICallbackListener = new OperatorForMachineUICallbackListener() {
        @Override
        public void onOperatorDataReceived(Operator operator) {

        }

        @Override
        public void onOperatorDataReceiveFailure(ErrorObjectInterface reason) {

        }

        @Override
        public void onSetOperatorSuccess() {
//            mOperatorCoreToDashboardActivityCallback.onSetOperatorForMachineSuccess(mSelectedOperator.getOperatorId(), mSelectedOperator.getOperatorName());
//            Log.d(LOG_TAG, "onSetOperatorSuccess() ");
        }

        @Override
        public void onSetOperatorFailed(ErrorObjectInterface reason) {
            Log.d(LOG_TAG, "onSetOperatorFailed() " + reason.getError());
        }

    };

    private boolean isExists(Event event) {
        for (int i = 0; i < mEventsList.size(); i++) {
            if (mEventsList.get(i).getEventID() == event.getEventID()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentMachineStatus != null) {
            initStatusLayout(mCurrentMachineStatus);
        }
    }

    private void toggleWoopList(ViewGroup.LayoutParams mLeftLayoutParams, int newWidth, ViewGroup.MarginLayoutParams mRightLayoutParams, boolean isOpen) {
        mLeftLayoutParams.width = newWidth;
        mRightLayoutParams.setMarginStart(newWidth);
        mShiftLogLayout.requestLayout();
        mWidgetsLayout.setLayoutParams(mRightLayoutParams);

        mShiftLogAdapter.changeState(!isOpen);
        mShiftLogAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCroutonCallback = (OnCroutonRequestListener) getActivity();
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
            mOnActivityCallbackRegistered.onFragmentAttached(this);
            OperatorCoreToDashboardActivityCallback operatorCoreToDashboardActivityCallback = (OperatorCoreToDashboardActivityCallback) getActivity();
            mOperatorCore = operatorCoreToDashboardActivityCallback.onSignInOperatorFragmentAttached();
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCroutonCallback = null;
        mOnGoToScreenListener = null;
        mOnActivityCallbackRegistered = null;
        mOperatorCore.unregisterListener();
        mOperatorCore = null;
    }

    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
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
            // rootView null
            @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_and_tools_view, null);
            final TextView title = ((TextView) view.findViewById(R.id.toolbar_title));
            title.setText(spannableString);
            title.setVisibility(View.VISIBLE);

            final Spinner jobsSpinner = (Spinner) view.findViewById(R.id.toolbar_job_spinner);
            final ArrayAdapter<String> jobsSpinnerAdapter = new JobsSpinnerAdapter(getActivity(), R.layout.spinner_job_item, getResources().getStringArray(R.array.jobs_spinner_array));
            jobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jobsSpinner.setAdapter(jobsSpinnerAdapter);
            jobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    switch (position) {
                        case 0: {
                            Log.d(LOG_TAG, "New Job");
                            mOnGoToScreenListener.goToFragment(new JobsFragment(), true);
                            break;
                        }
                        case 1: {
                            if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance("--",
                                        0), true);
                            }
                            else {
                                mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductName(),
                                        mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                            }
                            break;
                        }
                        case 2: {
                            if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance("--",
                                        0), true);
                            }
                            else {
                                mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductName(),
                                        mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                            }
                            break;
                        }
                        case 3: {
                            if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null) {
                                mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance("--",
                                        0), true);
                            }
                            else {
                                mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductName(),
                                        mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (PersistenceManager.getInstance().getOperatorName() != null) {
                mOperatorsSpinnerArray[0] = getResources().getString(R.string.switch_user);
            }else {
                mOperatorsSpinnerArray[0] = getResources().getString(R.string.operator_sign_in_action_bar);
            }

            EmeraldSpinner operatorsSpinner = (EmeraldSpinner) view.findViewById(R.id.toolbar_operator_spinner);
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

            mMachineIdStatusBarTextView = (TextView) view.findViewById(R.id.text_view_machine_id_name);
            mMachineStatusStatusBarTextView = (TextView) view.findViewById(R.id.text_view_machine_status);
            mStatusIndicatorImageView = (ImageView) view.findViewById(R.id.job_indicator);

            ImageView settingsButton = (ImageView) view.findViewById(R.id.settings_button);
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnGoToScreenListener.goToFragment(new SettingsFragment(), true);
                }
            });
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onDismissClick(DialogInterface dialog, int requestCode) {
        dialog.dismiss();
        openNextDialog();

    }

    private void openNextDialog() {
        if (mEventsQueue.peek() != null && (System.currentTimeMillis() - mEventsQueue.peek().getTimeOfAdded()) < THIRTY_SECONDS) {
            DialogFragment dialogFragment = null;
            Event event = mEventsQueue.pop();

            if (event.getEventGroupID() == 6) {
                if (event.getEndTime() == null || event.getStartTime() == null || event.getEndTime().equals("") || event.getStartTime().equals("")) {
                    openStopReportScreen(event.getEventID(), null, null, event.getDuration());
                }
                else {
                    dialogFragment = DialogFragment.newInstance(event, true);
                }
            }
            else if (event.getEventGroupID() == 20) {
                dialogFragment = DialogFragment.newInstance(event, false);
            }

            assert dialogFragment != null;
            dialogFragment.setTargetFragment(DashboardFragment.this, 0);
            dialogFragment.setCancelable(false);
            dialogFragment.show(getChildFragmentManager(), DialogFragment.DIALOG);
        }
        else if (mEventsQueue.peek() == null || mEventsQueue.size() == 0) {
            mIsOpenDialog = false;
        }
    }

    @Override
    public void onDismissAllClick(DialogInterface dialog, int eventId, int requestCode) {
        dialog.dismiss();
        for (Event event : mEventsQueue) {
            if (event.getEventID() == eventId) {
                mEventsQueue.remove(event);
            }
        }
        openNextDialog();
    }

    @Override
    public void onReportClick(int eventId, String start, String end, long duration) {
        openStopReportScreen(eventId, start, end, duration);
    }

    @Override
    public void onStopClicked(int eventId, String startTime, String endTime, long duration) {
        openStopReportScreen(eventId, startTime, endTime, duration);
    }

    private void openStopReportScreen(int eventId, String start, String end, long duration) {
        mOnGoToScreenListener.goToFragment(ReportStopReasonFragment.newInstance(start, end, duration, eventId), true);
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {
        Log.i(LOG_TAG, "onDeviceStatusChanged()");
        mCurrentMachineStatus = machineStatus;
        initStatusLayout(mCurrentMachineStatus);
    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList) {
        mWidgets = widgetList;
        if (widgetList != null && widgetList.size() > 0) {
            mNoDataView.setVisibility(View.GONE);
            if (mWidgetAdapter != null) {
                mWidgetAdapter.setNewData(widgetList);
            }
            else {
                mWidgetAdapter = new WidgetAdapter(getActivity(), widgetList, mOnGoToScreenListener);
                mWidgetRecycler.setAdapter(mWidgetAdapter);
            }
        }
        else {
            mNoDataView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events) {
        if (events != null && events.size() > 0) {

            mNoData = false;
            for (Event event : events) {
                event.setTimeOfAdded(System.currentTimeMillis());
            }

            if (mEventsList.size() == 0) {
                mEventsQueue.addAll(events);
                mEventsList.addAll(events);
            }
            else {

                for (int i = 0; i < events.size(); i++) {
                    if (!isExists(events.get(i))) {
                        mEventsList.add(events.get(i));
                    }

                }
            }
            mNoNotificationsText.setVisibility(View.GONE);

            if (mShiftLogAdapter != null) {
                mShiftLogAdapter.notifyDataSetChanged();
            }
            else {
                mShiftLogAdapter = new ShiftLogAdapter(getActivity(), mEventsList, !mIsOpen, mCloseWidth, this);
                mShiftLogRecycler.setAdapter(mShiftLogAdapter);
            }

            if (!mIsOpenDialog && mEventsQueue.size() > 0) {
                DialogFragment dialogFragment = null;
                Event event = mEventsQueue.pop();
                if (event.getEventGroupID() == 6) {
                    dialogFragment = DialogFragment.newInstance(event, true);
                }
                else if (event.getEventGroupID() == 20) {
                    dialogFragment = DialogFragment.newInstance(event, false);
                }
                dialogFragment.setTargetFragment(DashboardFragment.this, 0);
                dialogFragment.setCancelable(false);
                //TODO  dialogFragment.show(getChildFragmentManager(), DialogFragment.DIALOG);
                mIsOpenDialog = true;
            }
        }
        else {
            if (mEventsList == null || mEventsList.size() == 0) {
                mNoData = true;
                mNoNotificationsText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDataFailure(final ErrorObjectInterface reason, CallType callType) {
        if (callType == CallType.Status) {
            clearStatusLayout();
        }
        if (callType == CallType.ShiftLog) {
            if (mEventsList == null || mEventsList.size() == 0) {
                mNoData = true;
                mNoNotificationsText.setVisibility(View.VISIBLE);
            }
        }
        if (callType == CallType.MachineData) {
            if (mWidgets == null || mWidgets.size() == 0) {
                mNoDataView.setVisibility(View.VISIBLE);
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, reason);
            }
        });

        // do silentLogin if Credentials mismatch
        /*if (reason.getError() == ErrorObjectInterface.ErrorCode.Credentials_mismatch) {
            ((DashboardActivity) getActivity()).silentLoginFromDashBoard(mCroutonCallback, new SilentLoginCallback() {
                @Override
                public void onSilentLoginSucceeded() {
                    ZLogger.e(LOG_TAG, "onSilentLoginSucceeded");
                    getShiftLogs();
                }

                @Override
                public void onSilentLoginFailed(com.operators.infra.ErrorObjectInterface reason) {
                    ZLogger.e(LOG_TAG, "onSilentLoginFailed");
                }
            });
        }*/
        //todo clear data from widgets
        // todo clear data from shiftLog

    }

    private void initStatusLayout(MachineStatus machineStatus) {
        mProductNameTextView.setText(new StringBuilder(machineStatus.getAllMachinesData().get(0).getCurrentProductName() + "," + " ID - " + String.valueOf(machineStatus.getAllMachinesData().get(0).getCurrentProductID())));
        mJobIdTextView.setText((String.valueOf(machineStatus.getAllMachinesData().get(0).getCurrentJobID())));
        mShiftIdTextView.setText(String.valueOf(machineStatus.getAllMachinesData().get(0).getShiftID()));
        mMachineIdStatusBarTextView.setText(String.valueOf(machineStatus.getAllMachinesData().get(0).getMachineID()));
        mMachineStatusStatusBarTextView.setText(String.valueOf(machineStatus.getAllMachinesData().get(0).getMachineStatusEname()));
        statusAggregation(machineStatus);
    }

    private void clearStatusLayout() {
        mProductNameTextView.setText("");
        mJobIdTextView.setText("");
        mShiftIdTextView.setText("");
        mMachineIdStatusBarTextView.setText("");
        mMachineStatusStatusBarTextView.setText("");
        mTimerTextView.setText("");
        mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_no_data));
    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {
        mTimerTextView.setText(timeToEndInHours);
    }

    private void statusAggregation(MachineStatus machineStatus) {
        int status = machineStatus.getAllMachinesData().get(0).getMachineStatusID();
        if (status == MachineStatus.MachineServerStatus.WORKING_OK.getId()) {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_working));
        }
        else if (status == MachineStatus.MachineServerStatus.STOPPED.getId()) {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_stopped));
        }
        else if (status == MachineStatus.MachineServerStatus.NO_JOB.getId() || status == MachineStatus.MachineServerStatus.COMMUNICATION_FAILURE.getId() || status == MachineStatus.MachineServerStatus.SETUP_COMMUNICATION_FAILURE.getId()) {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_no_data));
        }
        else if (status == MachineStatus.MachineServerStatus.SETUP_WORKING.getId() || status == MachineStatus.MachineServerStatus.SETUP_STOPPED.getId()) {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_setup));
        }
        else if (status == MachineStatus.MachineServerStatus.PARAMETER_DEVIATION.getId()) {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_exceeding));
        }
        else {
            Log.w(LOG_TAG, "Undefined parameter");
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_no_data));
        }

    }


}
