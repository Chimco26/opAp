package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.machinestatusinfra.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.OnGoToScreenListener;
import com.operatorsapp.adapters.JobsSpinnerAdapter;
import com.operatorsapp.adapters.OperatorSpinnerAdapter;
import com.operatorsapp.adapters.ShiftLogAdapter;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.model.ShiftLog;
import com.operatorsapp.utils.ResizeWidthAnimation;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements DashboardUICallbackListener {

    private static final String LOG_TAG = DashboardFragment.class.getSimpleName();
    public static final int DURATION_MILLIS = 200;
    private OnCroutonRequestListener mCroutonCallback;
    private OnGoToScreenListener mOnGoToScreenListener;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;

    private RecyclerView mShiftLogRecycler;
    private ArrayList<ShiftLog> mShiftLogsList;
    private View mDividerView;
    private LinearLayout mLeftLayout, mRightLayout;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;
    private int mDownX;
    private ShiftLogAdapter mShiftLogAdapter;

    private TextView mProductNameTextView;
    private TextView mProductIdTextView;
    private TextView mJobIdTextView;
    private TextView mShiftIdTextView;
    private TextView mTimerTextView;
    private TextView mMachineIdStatusBarTextView;
    private TextView mMachineStatusStatusBarTextView;

    private boolean mIsFirstJobSpinnerSelection = true;
    private MachineStatus mCurrentMachineStatus;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mShiftLogsList = new ArrayList<>();

        ShiftLog shiftLog = new ShiftLog();
        shiftLog.setPriority(true);
        shiftLog.setIcon(R.drawable.face);
        shiftLog.setTitle("Machine Stopped");
        shiftLog.setSubtitle("Machine: 'ID\nStopped at: 05.11.1984");
        shiftLog.setTime("17:30");

        ShiftLog shiftLog1 = new ShiftLog();
        shiftLog1.setPriority(false);
        shiftLog1.setIcon(R.drawable.face);
        shiftLog1.setTitle("Material Weight Low");
        shiftLog1.setSubtitle("Machine: 'ID\nStopped at: 05.11.1984");
        shiftLog1.setTime("17:30");

        ShiftLog shiftLog2 = new ShiftLog();
        shiftLog2.setPriority(true);
        shiftLog2.setIcon(R.drawable.face);
        shiftLog2.setTitle("Cycle Time High");
        shiftLog2.setSubtitle("Machine: 'ID\nStopped at: 05.11.1984");
        shiftLog2.setTime("17:30");

        mShiftLogsList.add(shiftLog);
        mShiftLogsList.add(shiftLog1);
        mShiftLogsList.add(shiftLog2);

        setActionBar();
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get screen parameters
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        final int openWidth = width / 2;
        final int closeWidth = width / 4;
        final int betweenWidth = width * 3 / 8;

        mShiftLogRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_shift_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mShiftLogRecycler.setLayoutManager(linearLayoutManager);
        mShiftLogAdapter = new ShiftLogAdapter(getActivity(), mShiftLogsList, true);
        mShiftLogRecycler.setAdapter(mShiftLogAdapter);

        mLeftLayout = (LinearLayout) view.findViewById(R.id.fragment_dashboard_leftLayout);
        final ViewGroup.LayoutParams mLeftLayoutParams = mLeftLayout.getLayoutParams();
        mLeftLayoutParams.width = closeWidth;
        mLeftLayout.requestLayout();

        mRightLayout = (LinearLayout) view.findViewById(R.id.fragment_dashboard_rightLayout);
        final ViewGroup.MarginLayoutParams mRightLayoutParams = (ViewGroup.MarginLayoutParams) mRightLayout.getLayoutParams();
        mRightLayoutParams.setMarginStart(closeWidth);
        mRightLayout.setLayoutParams(mRightLayoutParams);

        mArrowLeft = (ImageView) view.findViewById(R.id.fragment_dashboard_arrow_right);
        mArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResizeWidthAnimation anim = new ResizeWidthAnimation(mLeftLayout, openWidth);
                anim.setDuration(DURATION_MILLIS);
                mLeftLayout.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toggleWoopList(mLeftLayoutParams, openWidth, mRightLayoutParams, true);
                        //set subTitle visible in adapter
                        mShiftLogAdapter.changeState(false);
                        mShiftLogAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
        mArrowRight = (ImageView) view.findViewById(R.id.fragment_dashboard_arrow_left);
        mArrowRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ResizeWidthAnimation anim = new ResizeWidthAnimation(mLeftLayout, closeWidth);
                anim.setDuration(DURATION_MILLIS);
                mLeftLayout.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        toggleWoopList(mLeftLayoutParams, closeWidth, mRightLayoutParams, false);
                        //set subTitle invisible in adapter
                        mShiftLogAdapter.changeState(true);
                        mShiftLogAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        mDividerView = view.findViewById(R.id.fragment_dashboard_divider);
        mDividerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int currentX = mLeftLayout.getLayoutParams().width + (int) event.getRawX() - mDownX;
                        if (currentX >= closeWidth && currentX <= openWidth) {
                            mLeftLayout.getLayoutParams().width = currentX;
                            mLeftLayout.requestLayout();
                            mDownX = (int) event.getRawX();
                            //set subTitle invisible in adapter
//                            mShiftLogAdapter.changeState(currentX < betweenWidth);
                            mShiftLogAdapter.changeAlpha((float) currentX / 1000);
                            mShiftLogAdapter.notifyDataSetChanged();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mLeftLayoutParams.width < betweenWidth) {
                            toggleWoopList(mLeftLayoutParams, closeWidth, mRightLayoutParams, false);
                        }
                        else {
                            toggleWoopList(mLeftLayoutParams, openWidth, mRightLayoutParams, true);
                        }
                        break;
                }
                return false;
            }
        });

        mProductNameTextView = (TextView) view.findViewById(R.id.text_view_product_name);
        mProductIdTextView = (TextView) view.findViewById(R.id.text_view_product_id);
        mJobIdTextView = (TextView) view.findViewById(R.id.text_view_job_id);
        mShiftIdTextView = (TextView) view.findViewById(R.id.text_view_shift_id);
        mTimerTextView = (TextView) view.findViewById(R.id.text_view_timer);
    }


    @Override
    public void onResume() {
        super.onResume();
        mIsFirstJobSpinnerSelection = true;
        if (mCurrentMachineStatus != null) {
            initStatusLayout(mCurrentMachineStatus);
        }
    }

    private void toggleWoopList(ViewGroup.LayoutParams mLeftLayoutParams, int newWidth, ViewGroup.MarginLayoutParams mRightLayoutParams, boolean open) {
        mLeftLayoutParams.width = newWidth;
        mRightLayoutParams.setMarginStart(newWidth);
        mLeftLayout.requestLayout();
        mRightLayout.setLayoutParams(mRightLayoutParams);
        if (open) {
            mArrowLeft.setVisibility(View.INVISIBLE);
            mArrowRight.setVisibility(View.VISIBLE);
        }
        else {
            mArrowLeft.setVisibility(View.VISIBLE);
            mArrowRight.setVisibility(View.INVISIBLE);
        }
        mShiftLogAdapter.changeState(!open);
        mShiftLogAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCroutonCallback = (OnCroutonRequestListener) getActivity();
            mOnGoToScreenListener = (OnGoToScreenListener) getActivity();
            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
            mOnActivityCallbackRegistered.onFragmentAttached(this);

        }
        catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement OnCroutonRequestListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCroutonCallback = null;
        mOnActivityCallbackRegistered = null;
    }


    private void setActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            SpannableString s = new SpannableString(getString(R.string.screen_title));
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.white)), 0, s.length() - 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.T12_color)), s.length() - 3, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            LayoutInflater inflator = LayoutInflater.from(getActivity());
            // rootView null
            @SuppressLint("InflateParams") View view = inflator.inflate(R.layout.actionbar_title_and_tools_view, null);
            final TextView title = ((TextView) view.findViewById(R.id.toolbar_title));
            title.setText(s);
            title.setVisibility(View.GONE);

            Spinner operatorsSpinner = (Spinner) view.findViewById(R.id.toolbar_operator_spinner);
            final ArrayAdapter<String> operatorSpinnerAdapter = new OperatorSpinnerAdapter(getActivity(), R.layout.spinner_job_item, getResources().getStringArray(R.array.operators_spinner_array));
            operatorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            operatorsSpinner.setAdapter(operatorSpinnerAdapter);


            final Spinner jobsSpinner = (Spinner) view.findViewById(R.id.toolbar_job_spinner);
            final ArrayAdapter<String> jobsSpinnerAdapter = new JobsSpinnerAdapter(getActivity(), R.layout.spinner_job_item, getResources().getStringArray(R.array.jobs_spinner_array));
            jobsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jobsSpinner.setAdapter(jobsSpinnerAdapter);

            jobsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mIsFirstJobSpinnerSelection) {
                        mIsFirstJobSpinnerSelection = false;
                    }
                    else {

                        Log.i(LOG_TAG, "Selected: " + position);
                        if (position == 0) {
                            mOnGoToScreenListener.goToFragment(new JobsFragment(), true);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            jobsSpinner.setSelection(3);
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
            operatorsSpinner.setSelection(1);

            mMachineIdStatusBarTextView = (TextView) view.findViewById(R.id.text_view_machine_id_name);
            mMachineStatusStatusBarTextView = (TextView) view.findViewById(R.id.text_view_machine_status);
            actionBar.setCustomView(view);
//            actionBar.setIcon(R.drawable.logo);
        }
    }

    @Override
    public void onDeviceStatusChanged(MachineStatus machineStatus) {
        Log.i(LOG_TAG, "onDeviceStatusChanged()");
        mCurrentMachineStatus = machineStatus;
        initStatusLayout(mCurrentMachineStatus);
    }

    private void initStatusLayout(MachineStatus machineStatus) {
        mProductNameTextView.setText(machineStatus.getProductName() + ",");
        mProductIdTextView.setText(String.valueOf(machineStatus.getProductId()));
        mJobIdTextView.setText((String.valueOf(machineStatus.getJobId())));
        mShiftIdTextView.setText(String.valueOf(machineStatus.getShiftId()));
        mMachineIdStatusBarTextView.setText(String.valueOf(machineStatus.getMachineID()));
        mMachineStatusStatusBarTextView.setText(String.valueOf(machineStatus.getMachineStatusID()));
    }

    @Override
    public void onTimerChanged(String timeToEndInHours) {
        mTimerTextView.setText(timeToEndInHours);
    }

}
