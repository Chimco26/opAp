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
import com.operators.loginnetworkbridge.server.ErrorObject;
import com.operators.machinedatainfra.models.Widget;
import com.google.gson.Gson;
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
import com.operatorsapp.managers.ProgressDialogManager;
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
    private static final String CURRENT_MACHINE_STATUS = "current_machine_status";
    private static final String END_TIME = "end_time";
    private static final String START_TIME = "start_time";
    private static final String DURATION = "duration";

    private GoToScreenListener mOnGoToScreenListener;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;
    private OperatorCore mOperatorCore;
    private OnCroutonRequestListener mCroutonCallback;
    private RecyclerView mShiftLogRecycler;
    private RecyclerView mWidgetRecycler;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayout mLeftLayout;
    private RelativeLayout mRightLayout;
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

    private OperatorCoreToDashboardActivityCallback mOperatorCoreToDashboardActivityCallback;
    private Operator mSelectedOperator;

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

        mLeftLayout = (LinearLayout) view.findViewById(R.id.fragment_dashboard_leftLayout);
        final ViewGroup.LayoutParams mLeftLayoutParams = mLeftLayout.getLayoutParams();
        mLeftLayoutParams.width = mCloseWidth;
        mLeftLayout.requestLayout();

        mRightLayout = (RelativeLayout) view.findViewById(R.id.fragment_dashboard_rightLayout);
        final ViewGroup.MarginLayoutParams mRightLayoutParams = (ViewGroup.MarginLayoutParams) mRightLayout.getLayoutParams();
        mRightLayoutParams.setMarginStart(mCloseWidth);
        mRightLayout.setLayoutParams(mRightLayoutParams);

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
                            int currentX = mLeftLayout.getLayoutParams().width + (int) event.getRawX() - mDownX;
                            if (currentX >= mCloseWidth && currentX <= mOpenWidth) {
                                mLeftLayout.getLayoutParams().width = currentX;
                                mLeftLayout.requestLayout();
                                mDownX = (int) event.getRawX();
                                mShiftLogAdapter.changeState(true);
                                mShiftLogAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mNoData) {
                            if (event.getRawX() - mDownX > 5 || event.getRawX() - mDownX < -5) {
                                if (mLeftLayoutParams.width < middleWidth) {
                                    mIsOpen = false;
                                    toggleWoopList(mLeftLayoutParams, mCloseWidth, mRightLayoutParams, mIsOpen);
                                    mGridLayoutManager.setSpanCount(3);
                                    mWidgetAdapter.notifyDataSetChanged();
                                }
                                else {
                                    mIsOpen = true;
                                    toggleWoopList(mLeftLayoutParams, mOpenWidth, mRightLayoutParams, mIsOpen);
                                    mGridLayoutManager.setSpanCount(2);
                                    mWidgetAdapter.notifyDataSetChanged();
                                }

                            }
                            else {
                                onButtonClick(mLeftLayoutParams, mRightLayoutParams);
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

    private void onButtonClick(final ViewGroup.LayoutParams mLeftLayoutParams, final ViewGroup.MarginLayoutParams mRightLayoutParams) {
        if (!mNoData && !mIsInTouch) {
            if (!mIsOpen) {
                final ResizeWidthAnimation anim = new ResizeWidthAnimation(mLeftLayout, mOpenWidth);
                anim.setDuration(ANIM_DURATION_MILLIS);
                mLeftLayout.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toggleWoopList(mLeftLayoutParams, mOpenWidth, mRightLayoutParams, true);
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
                final ResizeWidthAnimation anim = new ResizeWidthAnimation(mLeftLayout, mCloseWidth);
                anim.setDuration(ANIM_DURATION_MILLIS);
                mLeftLayout.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mShiftLogAdapter.changeState(true);
                        mShiftLogAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toggleWoopList(mLeftLayoutParams, mCloseWidth, mRightLayoutParams, false);
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
        public void onOperatorDataReceiveFailure(com.app.operatorinfra.ErrorObjectInterface reason) {

        }

        @Override
        public void onSetOperatorSuccess() {
            mOperatorCoreToDashboardActivityCallback.onSetOperatorForMachineSuccess(mSelectedOperator.getOperatorId(), mSelectedOperator.getOperatorName());
            Log.d(LOG_TAG, "onSetOperatorSuccess() ");
        }

        @Override
        public void onSetOperatorFailed(com.app.operatorinfra.ErrorObjectInterface reason) {
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
        mLeftLayout.requestLayout();
        mRightLayout.setLayoutParams(mRightLayoutParams);

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
            mOperatorCoreToDashboardActivityCallback = (OperatorCoreToDashboardActivityCallback) getActivity();
            mOperatorCore = mOperatorCoreToDashboardActivityCallback.onSignInOperatorFragmentAttached();
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
            title.setVisibility(View.GONE);

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
                            Log.d(LOG_TAG, "Report Rejects selected");
                            ReportRejectsFragment reportRejectsFragment = new ReportRejectsFragment();
                            Bundle bundle = new Bundle();
                            Gson gson = new Gson();
                            String jobString = gson.toJson(mCurrentMachineStatus, MachineStatus.class);
                            bundle.putString(CURRENT_MACHINE_STATUS, jobString);

                            reportRejectsFragment.setArguments(bundle);
                            mOnGoToScreenListener.goToFragment(reportRejectsFragment, true);
                            break;
                        }
                        case 2: {
//                            ReportStopReasonFragment reportStopReasonFragment = new ReportStopReasonFragment();
//                            Bundle bundle = new Bundle();
//                            Gson gson = new Gson();
//                            String jobString = gson.toJson(mCurrentMachineStatus, MachineStatus.class);
//                            bundle.putString(CURRENT_MACHINE_STATUS, jobString);
//
//                            reportStopReasonFragment.setArguments(bundle);
//                            mOnGoToScreenListener.goToFragment(reportStopReasonFragment, true);
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (PersistenceManager.getInstance().getOperatorName() != null) {
                mOperatorsSpinnerArray[0] = "Switch user";
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
            actionBar.setCustomView(view);
        }
    }

    @Override
    public void onDismissClick(DialogInterface dialog, int requestCode) {
        dialog.dismiss();
        if (mEventsQueue.peek() != null && (System.currentTimeMillis() - mEventsQueue.peek().getTimeOfAdded()) < THIRTY_SECONDS) {
            DialogFragment dialogFragment = null;
            Event event = mEventsQueue.pop();
            if (event.getEventGroupID() == 6) {
                if (event.getEndTime() == null || event.getStartTime() == null || event.getEndTime().equals("") || event.getStartTime().equals("")) {
                    openStopReportScreen(null, null, event.getDuration());
                }
                else {
                    dialogFragment = DialogFragment.newInstance(event.getTime(), event.getEndTime(), event.getDuration());
                }
            }
            else if (event.getEventGroupID() == 20) {
                dialogFragment = DialogFragment.newInstance(16, 10, 8, 12);
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
    public void onDismissAllClick(DialogInterface dialog, int requestCode) {
        mEventsQueue.clear();
        dialog.dismiss();
        mIsOpenDialog = false;
    }

    @Override
    public void onReportClick(String start, String end, int duration) {
        openStopReportScreen(start, end, duration);
    }

    @Override
    public void onStopClicked(String startTime, String endTime, int duration) {
        openStopReportScreen(startTime, endTime, duration);
    }

    private void openStopReportScreen(String start, String end, int duration) {
        ReportStopReasonFragment reportStopReasonFragment = new ReportStopReasonFragment();
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String jobString = gson.toJson(mCurrentMachineStatus, MachineStatus.class);
        bundle.putString(CURRENT_MACHINE_STATUS, jobString);
        bundle.putString(END_TIME, end);
        bundle.putString(START_TIME, start);
        bundle.putInt(DURATION, duration);
        reportStopReasonFragment.setArguments(bundle);
        mOnGoToScreenListener.goToFragment(reportStopReasonFragment, true);
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
                    dialogFragment = DialogFragment.newInstance(event.getTime(), event.getEndTime(), event.getDuration());
                }
                else if (event.getEventGroupID() == 20) {
                    dialogFragment = DialogFragment.newInstance(16, 10, 8, 12);
                }
                dialogFragment.setTargetFragment(DashboardFragment.this, 0);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getChildFragmentManager(), DialogFragment.DIALOG);
                mIsOpenDialog = true;
            }
        }
        else {
            mNoData = true;
            mNoNotificationsText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDataFailure(com.operators.infra.ErrorObjectInterface reason) {
        clearStatusLayout();

        mNoData = true;
        final ErrorObject errorObject = new ErrorObject(ErrorObject.ErrorCode.Credentials_mismatch, "Credentials mismatch");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, errorObject);
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
