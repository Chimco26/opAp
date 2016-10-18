package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
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
import android.view.ViewTreeObserver;
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
import com.operatorsapp.interfaces.CroutonRootProvider;
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
import com.zemingo.logrecorder.ZLogger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DashboardFragment extends Fragment implements DialogFragment.OnDialogButtonsListener, DashboardUICallbackListener, OnStopClickListener, CroutonRootProvider
{

    private static final String LOG_TAG = DashboardFragment.class.getSimpleName();
    private static final int ANIM_DURATION_MILLIS = 200;
    private static final int THIRTY_SECONDS = 30 * 1000;

    private View mToolBarView;
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
    private LinearLayout mStatusLayout;
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
    private int mWidgetsLayoutWidth;
    private int mTollBarsHeight;
    private int mRecyclersHeight;
    private ArrayList<Widget> mWidgets = new ArrayList<>();
    private String[] mOperatorsSpinnerArray = {"Operator Sign In"};
    private TextView mProductNameTextView;
    private TextView mJobIdTextView;
    private TextView mShiftIdTextView;
    private TextView mTimerTextView;
    private TextView mMachineIdStatusBarTextView;
    private TextView mMachineStatusStatusBarTextView;
    private ImageView mStatusIndicatorImageView;
    private DialogFragment mDialogFragment;
    private ViewGroup.MarginLayoutParams mWidgetsParams;
    private ViewGroup.LayoutParams mShiftLogParams;
    private boolean mIsNewShiftLogs;
    private MachineStatus mCurrentMachineStatus;

    public static DashboardFragment newInstance()
    {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ProgressDialogManager.show(getActivity());
        View inflate = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        ZLogger.d(LOG_TAG, "onViewCreated(), start ");
        super.onViewCreated(view, savedInstanceState);
        mEventsList = PersistenceManager.getInstance().getShiftLogs();
        mIsOpen = false;
        mIsNewShiftLogs = PersistenceManager.getInstance().isNewShiftLogs();
        // get screen parameters
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("moo", "width is: " + width);
        mOpenWidth = (int) (width * 0.4);
        mCloseWidth = (int) (width * 0.2);
        mWidgetsLayoutWidth = (int) (width * 0.8);
        mTollBarsHeight = (int) (height * 0.25);
        mRecyclersHeight = (int) (height * 0.75);

        final int middleWidth = (int) (width * 0.3);

        mStatusLayout = (LinearLayout) view.findViewById(R.id.status_layout);
        ViewGroup.LayoutParams statusBarParams;
        statusBarParams = mStatusLayout.getLayoutParams();
        statusBarParams.height = (int) (mTollBarsHeight * 0.35);
        mStatusLayout.requestLayout();

        mProductNameTextView = (TextView) view.findViewById(R.id.text_view_product_name_and_id);
        mJobIdTextView = (TextView) view.findViewById(R.id.text_view_job_id);
        mShiftIdTextView = (TextView) view.findViewById(R.id.text_view_shift_id);
        mTimerTextView = (TextView) view.findViewById(R.id.text_view_timer);

        mShiftLogLayout = (LinearLayout) view.findViewById(R.id.fragment_dashboard_shiftlog);
        mShiftLogParams = mShiftLogLayout.getLayoutParams();
        mShiftLogParams.width = mCloseWidth;
        mShiftLogLayout.requestLayout();

        mWidgetsLayout = (RelativeLayout) view.findViewById(R.id.fragment_dashboard_widgets_layout);
        mWidgetsParams = (ViewGroup.MarginLayoutParams) mWidgetsLayout.getLayoutParams();
        mWidgetsParams.setMarginStart(mCloseWidth);
        mWidgetsLayout.setLayoutParams(mWidgetsParams);

        mShiftLogRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_shift_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mShiftLogRecycler.setLayoutManager(linearLayoutManager);
        mShiftLogAdapter = new ShiftLogAdapter(getActivity(), mEventsList, !mIsOpen, mCloseWidth, this, mOpenWidth, mRecyclersHeight);
        mShiftLogRecycler.setAdapter(mShiftLogAdapter);

        mWidgetRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_widgets);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mWidgetRecycler.setLayoutManager(mGridLayoutManager);
        GridSpacingItemDecoration mGridSpacingItemDecoration = new GridSpacingItemDecoration(3, 24, true, 0);
        mWidgetRecycler.addItemDecoration(mGridSpacingItemDecoration);
        mWidgetAdapter = new WidgetAdapter(getActivity(), mWidgets, mOnGoToScreenListener, true, mRecyclersHeight, mWidgetsLayoutWidth);
        mWidgetRecycler.setAdapter(mWidgetAdapter);

        mNoNotificationsText = (TextView) view.findViewById(R.id.fragment_dashboard_no_notif);
        mNoDataView = (LinearLayout) view.findViewById(R.id.fragment_dashboard_no_data);

        View mDividerView = view.findViewById(R.id.fragment_dashboard_divider);
        mDividerView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mNoData)
                        {
                            int currentX;
                            if (PersistenceManager.getInstance().getCurrentLang().equals("iw"))
                            {
                                currentX = mShiftLogLayout.getLayoutParams().width - (int) event.getRawX() + mDownX;
                            }
                            else
                            {
                                currentX = mShiftLogLayout.getLayoutParams().width + (int) event.getRawX() - mDownX;
                            }
                            if (currentX >= mCloseWidth && currentX <= mOpenWidth)
                            {
                                mShiftLogLayout.getLayoutParams().width = currentX;
                                mShiftLogLayout.requestLayout();
                                mDownX = (int) event.getRawX();
                                mShiftLogAdapter.changeState(true);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mNoData)
                        {
                            if (event.getRawX() - mDownX > 5 || event.getRawX() - mDownX < -5)
                            {
                                if (mShiftLogParams.width < middleWidth)
                                {
                                    mIsOpen = false;
                                    toggleWoopList(mShiftLogParams, mCloseWidth, mWidgetsParams, false);
                                    mGridLayoutManager.setSpanCount(3);
                                    mWidgetAdapter.changeState(true);
                                }
                                else
                                {
                                    mIsOpen = true;
                                    toggleWoopList(mShiftLogParams, mOpenWidth, mWidgetsParams, true);
                                    mGridLayoutManager.setSpanCount(2);
                                    mWidgetAdapter.changeState(false);
                                }

                            }
                            else
                            {
                                onButtonClick(mShiftLogParams, mWidgetsParams);
                            }
                        }
                        break;
                }
                return false;
            }
        });

        if (mEventsList.size() > 0)
        {
            mNoData = false;
            mNoNotificationsText.setVisibility(View.GONE);
        }
        else
        {
            mNoData = true;
            mNoNotificationsText.setVisibility(View.VISIBLE);
        }

        mOperatorCore.registerListener(mOperatorForMachineUICallbackListener);

        ZLogger.d(LOG_TAG, "onViewCreated(), end ");
    }

    private void onButtonClick(final ViewGroup.LayoutParams leftLayoutParams, final ViewGroup.MarginLayoutParams rightLayoutParams)
    {
        if (!mIsOpen)
        {
            if (!mNoData)
            {
                final ResizeWidthAnimation anim = new ResizeWidthAnimation(mShiftLogLayout, mOpenWidth);
                anim.setDuration(ANIM_DURATION_MILLIS);
                mShiftLogLayout.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        toggleWoopList(leftLayoutParams, mOpenWidth, rightLayoutParams, true);
                        //                        mShiftLogAdapter.notifyDataSetChanged();
                        mGridLayoutManager.setSpanCount(2);
                        mWidgetAdapter.changeState(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }
                });
                mIsOpen = true;
            }
        }
        else
        {

            closeWoopList(leftLayoutParams, rightLayoutParams);
        }

    }

    private void closeWoopList(final ViewGroup.LayoutParams leftLayoutParams, final ViewGroup.MarginLayoutParams rightLayoutParams)
    {
        final ResizeWidthAnimation anim = new ResizeWidthAnimation(mShiftLogLayout, mCloseWidth);
        anim.setDuration(ANIM_DURATION_MILLIS);
        mShiftLogLayout.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                mShiftLogAdapter.changeState(true);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                toggleWoopList(leftLayoutParams, mCloseWidth, rightLayoutParams, false);
                mGridLayoutManager.setSpanCount(3);
                mWidgetAdapter.changeState(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        mIsOpen = false;
    }

    OperatorForMachineUICallbackListener mOperatorForMachineUICallbackListener = new OperatorForMachineUICallbackListener()
    {
        @Override
        public void onOperatorDataReceived(Operator operator)
        {

        }

        @Override
        public void onOperatorDataReceiveFailure(ErrorObjectInterface reason)
        {

        }

        @Override
        public void onSetOperatorSuccess()
        {
            //            mOperatorCoreToDashboardActivityCallback.onSetOperatorForMachineSuccess(mSelectedOperator.getOperatorId(), mSelectedOperator.getOperatorName());
            //            Log.d(LOG_TAG, "onSetOperatorSuccess() ");
        }

        @Override
        public void onSetOperatorFailed(ErrorObjectInterface reason)
        {
            Log.d(LOG_TAG, "onSetOperatorFailed() " + reason.getError());
        }

    };

    private boolean isExists(Event event)
    {
        for (int i = 0; i < mEventsList.size(); i++)
        {
            if (mEventsList.get(i).getEventID() == event.getEventID())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mDialogFragment != null)
        {
            mDialogFragment.dismiss();
            mIsOpenDialog = false;
        }
    }

    @Override
    public void onResume()
    {
        ZLogger.d(LOG_TAG, "onResume(), Start ");
        super.onResume();
        setActionBar();
        if (mCurrentMachineStatus != null)
        {
            initStatusLayout(mCurrentMachineStatus);
        }
        if (mEventsList != null && mEventsList.size() > 0)
        {
            for (Event event : mEventsList)
            {
                if (!event.isIsDismiss())
                {
                    event.setTimeOfAdded(System.currentTimeMillis());
                    mEventsQueue.add(event);
                }
            }
            //            openNextDialog();
            if (mEventsQueue.size() > 0)
            {
                Event event = mEventsQueue.peek();//
                if (event.getEventGroupID() == 6)
                {
                    mDialogFragment = DialogFragment.newInstance(event, true);
                }
                else if (event.getEventGroupID() == 20)
                {
                    mDialogFragment = DialogFragment.newInstance(event, false);
                }
                if (mDialogFragment != null)
                {
                    mDialogFragment.setTargetFragment(DashboardFragment.this, 0);
                    mDialogFragment.setCancelable(false);
                    mDialogFragment.show(getFragmentManager(), DialogFragment.DIALOG);
                }
                mIsOpenDialog = true;
            }
        }
        else
        {
            mNoData = true;
            mNoNotificationsText.setVisibility(View.VISIBLE);
        }
        if (mWidgets == null || mWidgets.size() == 0)
        {
            mNoDataView.setVisibility(View.VISIBLE);
        }
        ZLogger.d(LOG_TAG, "onResume(), end ");
    }

    private void toggleWoopList(ViewGroup.LayoutParams mLeftLayoutParams, int newWidth, ViewGroup.MarginLayoutParams mRightLayoutParams, boolean isOpen)
    {
        mLeftLayoutParams.width = newWidth;
        mRightLayoutParams.setMarginStart(newWidth);
        mShiftLogLayout.requestLayout();
        mWidgetsLayout.setLayoutParams(mRightLayoutParams);

        mShiftLogAdapter.changeState(!isOpen);
        mWidgetAdapter.changeState(!isOpen);

        ZLogger.d(LOG_TAG, "setActionBar(),  " + " toolBar: " + mToolBarView.getHeight() + " -- " + mTollBarsHeight * 0.65);
        ZLogger.d(LOG_TAG, "setActionBar(),  " + " status: " + mStatusLayout.getHeight() + " -- " + mTollBarsHeight * 0.35);
    }

    @Override
    public void onAttach(Context context)
    {
        ZLogger.d(LOG_TAG, "onAttach(), start ");
        super.onAttach(context);
        try
        {
            mCroutonCallback = (OnCroutonRequestListener) getActivity();
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
            mOnActivityCallbackRegistered.onFragmentAttached(this);
            OperatorCoreToDashboardActivityCallback operatorCoreToDashboardActivityCallback = (OperatorCoreToDashboardActivityCallback) getActivity();
            mOperatorCore = operatorCoreToDashboardActivityCallback.onSignInOperatorFragmentAttached();
        } catch (ClassCastException e)
        {
            throw new ClassCastException("Calling fragment must implement interface");
        }
        ZLogger.d(LOG_TAG, "onAttach(), end ");
    }

    @Override
    public void onDetach()
    {
        ZLogger.d(LOG_TAG, "onDetach(), start ");
        super.onDetach();
        mCroutonCallback = null;
        mOnGoToScreenListener = null;
        mOnActivityCallbackRegistered = null;
        mOperatorCore.unregisterListener();
        mOperatorCore = null;
        ZLogger.d(LOG_TAG, "onDetach(), end ");
    }

    @SuppressLint("InflateParams")
    public void setActionBar()
    {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
        {
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

            final TextView title = ((TextView) mToolBarView.findViewById(R.id.toolbar_title));
            title.setText(spannableString);
            title.setVisibility(View.VISIBLE);

            final Spinner jobsSpinner = (Spinner) mToolBarView.findViewById(R.id.toolbar_job_spinner);
            final ArrayAdapter<String> jobsSpinnerAdapter = new JobsSpinnerAdapter(getActivity(), R.layout.spinner_job_item, getResources().getStringArray(R.array.jobs_spinner_array));
            jobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jobsSpinner.setAdapter(jobsSpinnerAdapter);
            jobsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {

                    switch (position)
                    {
                        case 0:
                        {
                            Log.d(LOG_TAG, "New Job");
                            mOnGoToScreenListener.goToFragment(new JobsFragment(), true);
                            break;
                        }
                        case 1:
                        {
                            if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null)
                            {
                                mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance("--", 0), true);
                            }
                            else
                            {
                                mOnGoToScreenListener.goToFragment(ReportRejectsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductName(), mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                            }
                            break;
                        }
                        case 2:
                        {
                            if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null)
                            {
                                mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance("--", 0), true);
                            }
                            else
                            {
                                mOnGoToScreenListener.goToFragment(ReportCycleUnitsFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductName(), mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                            }
                            break;
                        }
                        case 3:
                        {
                            if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null)
                            {
                                mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance("--", 0), true);
                            }
                            else
                            {
                                mOnGoToScreenListener.goToFragment(ReportInventoryFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductName(), mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                            }
                            break;
                        }
                        case 4:
                        {
                            if (mCurrentMachineStatus == null || mCurrentMachineStatus.getAllMachinesData() == null)
                            {
                                mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance("--", 0), true);
                            }
                            else
                            {
                                mOnGoToScreenListener.goToFragment(ApproveFirstItemFragment.newInstance(mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductName(), mCurrentMachineStatus.getAllMachinesData().get(0).getCurrentProductID()), true);
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });

            if (PersistenceManager.getInstance().getOperatorName() != null)
            {
                mOperatorsSpinnerArray[0] = getResources().getString(R.string.switch_user);
            }
            else
            {
                mOperatorsSpinnerArray[0] = getResources().getString(R.string.operator_sign_in_action_bar);
            }

            EmeraldSpinner operatorsSpinner = (EmeraldSpinner) mToolBarView.findViewById(R.id.toolbar_operator_spinner);
            final ArrayAdapter<String> operatorSpinnerAdapter = new OperatorSpinnerAdapter(getActivity(), R.layout.spinner_operator_item, mOperatorsSpinnerArray, PersistenceManager.getInstance().getOperatorName());
            operatorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            operatorsSpinner.setAdapter(operatorSpinnerAdapter);
            operatorsSpinner.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.T12_color), PorterDuff.Mode.SRC_ATOP);

            operatorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    if (position == 0)
                    {
                        mOnGoToScreenListener.goToFragment(new SignInOperatorFragment(), true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            mMachineIdStatusBarTextView = (TextView) mToolBarView.findViewById(R.id.text_view_machine_id_name);
            mMachineStatusStatusBarTextView = (TextView) mToolBarView.findViewById(R.id.text_view_machine_status);
            mStatusIndicatorImageView = (ImageView) mToolBarView.findViewById(R.id.job_indicator);

            ImageView settingsButton = (ImageView) mToolBarView.findViewById(R.id.settings_button);
            settingsButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnGoToScreenListener.goToFragment(new SettingsFragment(), true);
                }
            });
            actionBar.setCustomView(mToolBarView);

            setToolBarHeight(mToolBarView);
        }
    }

    private void setToolBarHeight(final View view)
    {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                ZLogger.d(LOG_TAG, "onPreDraw(), start ");
                if (view.getViewTreeObserver().isAlive())
                {
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

    @Override
    public void onDismissClick(DialogInterface dialog, int requestCode)
    {
        dialog.dismiss();
        openNextDialog();

    }

    private void openNextDialog()
    {
        if (mEventsQueue.peek() != null && (System.currentTimeMillis() - mEventsQueue.peek().getTimeOfAdded()) < THIRTY_SECONDS)
        {
            Event event = mEventsQueue.pop();
            //            event.setAlarmDismissed(true);
            event.setIsDismiss(true);

            if (event.getEventGroupID() == 6)
            {
                if (event.getEndTime() == null || event.getTime() == null || event.getEndTime().equals("") || event.getTime().equals(""))
                {
                    openStopReportScreen(event.getEventID(), null, null, event.getDuration());
                }
                else
                {
                    mDialogFragment = DialogFragment.newInstance(event, true);
                }
            }
            else if (event.getEventGroupID() == 20)
            {
                mDialogFragment = DialogFragment.newInstance(event, false);
            }

            mDialogFragment.setTargetFragment(DashboardFragment.this, 0);
            mDialogFragment.setCancelable(false);
            mDialogFragment.show(getChildFragmentManager(), DialogFragment.DIALOG);
        }
        else if (mEventsQueue.peek() == null || mEventsQueue.size() == 0)
        {
            mIsOpenDialog = false;
        }
        PersistenceManager.getInstance().saveShiftLogs(mEventsList);
    }

    @Override
    public void onDismissAllClick(DialogInterface dialog, int eventGroupId, int requestCode)
    {
        dialog.dismiss();
        Iterator<Event> iterator = mEventsQueue.iterator();
        while (iterator.hasNext())
        {
            Event next = iterator.next();
            if (next.getEventGroupID() == eventGroupId)
            {
                //                event.setAlarmDismissed(true);
                next.setIsDismiss(true);
                iterator.remove();
            }
        }
        PersistenceManager.getInstance().saveShiftLogs(mEventsList);
        openNextDialog();
    }

    @Override
    public void onReportClick(int eventId, String start, String end, long duration)
    {
        openStopReportScreen(eventId, start, end, duration);
    }

    @Override
    public void onStopClicked(int eventId, String startTime, String endTime, long duration)
    {
        openStopReportScreen(eventId, startTime, endTime, duration);
    }

    private void openStopReportScreen(int eventId, String start, String end, long duration)
    {
        mOnGoToScreenListener.goToFragment(ReportStopReasonFragment.newInstance(start, end, duration, eventId), true);
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus)
    {
        Log.i(LOG_TAG, "onDeviceStatusChanged()");
        mCurrentMachineStatus = machineStatus;
        initStatusLayout(mCurrentMachineStatus);
    }

    @Override
    public void onMachineDataReceived(ArrayList<Widget> widgetList)
    {
        mWidgets = widgetList;
        if (widgetList != null && widgetList.size() > 0)
        {
            saveAndRestoreChartData(widgetList);

            PersistenceManager.getInstance().setMachineDataStartingFrom(com.operatorsapp.utils.TimeUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));
            mNoDataView.setVisibility(View.GONE);
            if (mWidgetAdapter != null)
            {
                mWidgetAdapter.setNewData(widgetList);
            }
            else
            {
                mWidgetAdapter = new WidgetAdapter(getActivity(), widgetList, mOnGoToScreenListener, !mIsOpen, mRecyclersHeight, mWidgetsLayoutWidth);
                mWidgetRecycler.setAdapter(mWidgetAdapter);
            }
        }
        else
        {
            mNoDataView.setVisibility(View.VISIBLE);
        }
    }

    private void saveAndRestoreChartData(ArrayList<Widget> widgetList)
    {
        //historic data from prefs
        ArrayList<HashMap<String, ArrayList<Widget.HistoricData>>> prefsHistoric = PersistenceManager.getInstance().getChartHistoricData();
        for (Widget widget : widgetList)
        {
            // if have chart widget (field type 3)
            if (widget.getFieldType() == 3)
            {
                // if have historic in prefs
                if (prefsHistoric.size() > 0)
                {
                    for (HashMap<String, ArrayList<Widget.HistoricData>> hashMap : prefsHistoric)
                    {
                        // if get new data
                        if (widget.getMachineParamHistoricData().size() > 0)
                        {
                            // if is old widget
                            if (hashMap.containsKey(String.valueOf(widget.getID())))
                            {
                                //add new data to prefs
                                if (widget.getMachineParamHistoricData() != null)
                                {
                                    hashMap.get(String.valueOf(widget.getID())).addAll(widget.getMachineParamHistoricData());
                                    widget.getMachineParamHistoricData().clear();
                                }
                                //set all data (old + new) to widget
                                if (hashMap.get(String.valueOf(widget.getID())) != null)
                                {
                                    widget.getMachineParamHistoricData().addAll(hashMap.get(String.valueOf(widget.getID())));
                                }
                            }
                            else
                            {
                                // if is new widget,  save to prefs
                                HashMap<String, ArrayList<Widget.HistoricData>> historicById = new HashMap<>();
                                historicById.put(String.valueOf(widget.getID()), widget.getMachineParamHistoricData());
                                prefsHistoric.add(historicById);
                            }
                        }
                        else
                        {
                            // if no new data,  set old data to widget
                            if (hashMap.get(String.valueOf(widget.getID())) != null)
                            {
                                widget.getMachineParamHistoricData().addAll(hashMap.get(String.valueOf(widget.getID())));
                            }
                        }
                    }
                }
                else
                {
                    // if is the firs chart data,  save to prefs
                    HashMap<String, ArrayList<Widget.HistoricData>> historicById = new HashMap<>();
                    historicById.put(String.valueOf(widget.getID()), widget.getMachineParamHistoricData());
                    prefsHistoric.add(historicById);
                }
            }
        }
        PersistenceManager.getInstance().saveChartHistoricData(prefsHistoric);
    }

    @Override
    public void onShiftLogDataReceived(ArrayList<Event> events)
    {
        if (events != null && events.size() > 0)
        {
            PersistenceManager.getInstance().setShiftLogStartingFrom(com.operatorsapp.utils.TimeUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));
            mNoData = false;
            for (Event event : events)
            {
                event.setTimeOfAdded(System.currentTimeMillis());
                if (!mIsNewShiftLogs)
                {
                    event.setIsDismiss(true);
                }
            }

            if (mEventsList.size() == 0)
            {
                mEventsList.addAll(events);
                if (mIsNewShiftLogs)
                {
                    mEventsQueue.addAll(events);
                }
                /*for (Event event : mEventsQueue) {
                    if (!event.isAlarmDismissed()) {
                        mEventsList.add(event);
                        mEventsQueue.add(event);
                    }
                }*/
            }
            else
            {
                for (int i = 0; i < events.size(); i++)
                {
                    if (!isExists(events.get(i)) /*&& !events.get(i).isAlarmDismissed()*/)
                    {
                        mEventsList.add(events.get(i));
                    }

                }
            }
            PersistenceManager.getInstance().saveShiftLogs(mEventsList);
            mNoNotificationsText.setVisibility(View.GONE);

            if (mShiftLogAdapter != null)
            {
                mShiftLogAdapter.setNewData(mEventsList);
            }
            else
            {
                mShiftLogAdapter = new ShiftLogAdapter(getActivity(), mEventsList, !mIsOpen, mCloseWidth, this, mOpenWidth, mRecyclersHeight);
                mShiftLogRecycler.setAdapter(mShiftLogAdapter);
            }

            if (!mIsOpenDialog && mEventsQueue.size() > 0)
            {
                Event event = mEventsQueue.peek();
                if (event.getEventGroupID() == 6)
                {
                    mDialogFragment = DialogFragment.newInstance(event, true);
                }
                else if (event.getEventGroupID() == 20)
                {
                    mDialogFragment = DialogFragment.newInstance(event, false);
                }
                if (mDialogFragment != null)
                {
                    mDialogFragment.setTargetFragment(DashboardFragment.this, 0);
                    mDialogFragment.setCancelable(false);
                    mDialogFragment.show(getFragmentManager(), DialogFragment.DIALOG);
                }
                mIsOpenDialog = true;
            }
        }
        else
        {
            if (mEventsList == null || mEventsList.size() == 0)
            {
                mNoData = true;
                mNoNotificationsText.setVisibility(View.VISIBLE);
            }
        }
        mIsNewShiftLogs = true;
        PersistenceManager.getInstance().setIsNewShiftLogs(true);
    }

    @Override
    public void onShiftForMachineEnded()
    {
        if (mShiftLogParams != null && mWidgetsParams != null)
        {
            closeWoopList(mShiftLogParams, mWidgetsParams);
        }
        mEventsList.clear();
        mEventsQueue.clear();
        PersistenceManager.getInstance().saveShiftLogs(mEventsList);
        mNoData = true;
        mNoNotificationsText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataFailure(final ErrorObjectInterface reason, CallType callType)
    {
        if (callType == CallType.Status)
        {
            clearStatusLayout();
        }
       /* if (callType == CallType.ShiftLog) {
            if (mEventsList == null || mEventsList.size() == 0) {
                mNoData = true;
                mNoNotificationsText.setVisibility(View.VISIBLE);
            }
        }*/
        if (callType == CallType.MachineData)
        {
            if (mWidgets == null || mWidgets.size() == 0)
            {
                mNoDataView.setVisibility(View.VISIBLE);
            }
        }
        if (getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, reason);
                }
            });
        }
    }

    private void initStatusLayout(MachineStatus machineStatus)
    {
        mProductNameTextView.setText(new StringBuilder(machineStatus.getAllMachinesData().get(0).getCurrentProductName()).append(",").append(" ID - ").append(String.valueOf(machineStatus.getAllMachinesData().get(0).getCurrentProductID())));
        mJobIdTextView.setText((String.valueOf(machineStatus.getAllMachinesData().get(0).getCurrentJobID())));
        mShiftIdTextView.setText(String.valueOf(machineStatus.getAllMachinesData().get(0).getShiftID()));
        mMachineIdStatusBarTextView.setText(String.valueOf(machineStatus.getAllMachinesData().get(0).getMachineID()));
        mMachineStatusStatusBarTextView.setText(String.valueOf(machineStatus.getAllMachinesData().get(0).getMachineStatusEname()));
        statusAggregation(machineStatus);
    }

    private void clearStatusLayout()
    {
        mProductNameTextView.setText("");
        mJobIdTextView.setText("");
        mShiftIdTextView.setText("");
        mMachineIdStatusBarTextView.setText("");
        mMachineStatusStatusBarTextView.setText("");
        mTimerTextView.setText("");
        mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_no_data));
    }

    @Override
    public void onTimerChanged(String timeToEndInHours)
    {
        mTimerTextView.setText(timeToEndInHours);
    }

    private void statusAggregation(MachineStatus machineStatus)
    {
        int status = machineStatus.getAllMachinesData().get(0).getMachineStatusID();
        if (status == MachineStatus.MachineServerStatus.WORKING_OK.getId())
        {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_working));
        }
        else if (status == MachineStatus.MachineServerStatus.STOPPED.getId())
        {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_stopped));
        }
        else if (status == MachineStatus.MachineServerStatus.NO_JOB.getId() || status == MachineStatus.MachineServerStatus.COMMUNICATION_FAILURE.getId() || status == MachineStatus.MachineServerStatus.SETUP_COMMUNICATION_FAILURE.getId())
        {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_no_data));
        }
        else if (status == MachineStatus.MachineServerStatus.SETUP_WORKING.getId() || status == MachineStatus.MachineServerStatus.SETUP_STOPPED.getId())
        {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_setup));
        }
        else if (status == MachineStatus.MachineServerStatus.PARAMETER_DEVIATION.getId())
        {
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_exceeding));
        }
        else
        {
            Log.w(LOG_TAG, "Undefined parameter");
            mStatusIndicatorImageView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_indicator_no_data));
        }

    }

    @Override
    public int getCroutonRoot()
    {
        return R.id.parent_layouts;
    }

    private void dismissProgressDialog()
    {
        if (getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    ProgressDialogManager.dismiss();
                }
            });
        }
    }
}
