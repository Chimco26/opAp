package com.operatorsapp.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.machinestatusinfra.MachineStatus;
import com.operators.shiftlogcore.interfaces.ShiftLogUICallback;
import com.operators.shiftloginfra.ErrorObjectInterface;
import com.operators.shiftloginfra.ShiftLog;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.adapters.JobsSpinnerAdapter;
import com.operatorsapp.adapters.OperatorSpinnerAdapter;
import com.operatorsapp.adapters.ShiftLogAdapter;
import com.operatorsapp.dialogs.DialogFragment;
import com.operatorsapp.fragments.interfaces.DialogsShiftLogListener;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.interfaces.DashboardUICallbackListener;
import com.operatorsapp.interfaces.OnActivityCallbackRegistered;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.managers.ProgressDialogManager;
import com.operatorsapp.utils.ResizeWidthAnimation;
import com.operatorsapp.utils.ShowCrouton;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class DashboardFragment extends Fragment implements DialogFragment.OnDialogButtonsListener, DashboardUICallbackListener {

    private static final String LOG_TAG = DashboardFragment.class.getSimpleName();
    private static final int ANIM_DURATION_MILLIS = 200;
    private static final int THIRTY_SECONDS = 30 * 1000;
    private GoToScreenListener mOnGoToScreenListener;
    private OnActivityCallbackRegistered mOnActivityCallbackRegistered;

    private OnCroutonRequestListener mCroutonCallback;
    private DialogsShiftLogListener mDialogsShiftLogListener;
    private RecyclerView mShiftLogRecycler;
    private LinearLayout mLeftLayout;
    private RelativeLayout mRightLayout;
    private TextView mNoNotificationsText;
    private LinearLayout mNoDataView;
    private int mDownX;
    private ShiftLogAdapter mShiftLogAdapter;
    private ArrayDeque<ShiftLog> mShiftLogsQueue = new ArrayDeque<>();
    private ArrayList<ShiftLog> mShiftLogsList = new ArrayList<>();
    private boolean mNoData;

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
        final int openWidth = (int) (width * 0.448)/*/2*/;
        final int closeWidth = (int) (width * 0.173) /*/ 4*/;
        final int middleWidth = (int) (width * 0.31) /*3 / 8*/;

        mProductNameTextView = (TextView) view.findViewById(R.id.text_view_product_name);
        mProductIdTextView = (TextView) view.findViewById(R.id.text_view_product_id);
        mJobIdTextView = (TextView) view.findViewById(R.id.text_view_job_id);
        mShiftIdTextView = (TextView) view.findViewById(R.id.text_view_shift_id);
        mTimerTextView = (TextView) view.findViewById(R.id.text_view_timer);

        mShiftLogRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_shift_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mShiftLogRecycler.setLayoutManager(linearLayoutManager);

        mLeftLayout = (LinearLayout) view.findViewById(R.id.fragment_dashboard_leftLayout);
        final ViewGroup.LayoutParams mLeftLayoutParams = mLeftLayout.getLayoutParams();
        mLeftLayoutParams.width = closeWidth;
        mLeftLayout.requestLayout();

        mRightLayout = (RelativeLayout) view.findViewById(R.id.fragment_dashboard_rightLayout);
        final ViewGroup.MarginLayoutParams mRightLayoutParams = (ViewGroup.MarginLayoutParams) mRightLayout.getLayoutParams();
        mRightLayoutParams.setMarginStart(closeWidth);
        mRightLayout.setLayoutParams(mRightLayoutParams);

        mNoNotificationsText = (TextView) view.findViewById(R.id.fragment_dashboard_no_notif);
        mNoDataView = (LinearLayout) view.findViewById(R.id.fragment_dashboard_no_data);

        ImageView mLeftButton = (ImageView) view.findViewById(R.id.fragment_dashboard_left_btn);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            boolean isOpen;

            @Override
            public void onClick(View v) {
                if (!mNoData) {
                    if (!isOpen) {
                        final ResizeWidthAnimation anim = new ResizeWidthAnimation(mLeftLayout, openWidth);
                        anim.setDuration(ANIM_DURATION_MILLIS);
                        mLeftLayout.startAnimation(anim);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                toggleWoopList(mLeftLayoutParams, openWidth, mRightLayoutParams, true);
                                //set subTitle visible in adapter
//                                mShiftLogAdapter.changeState(false);
                                mShiftLogAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        isOpen = true;
                    } else {
                        final ResizeWidthAnimation anim = new ResizeWidthAnimation(mLeftLayout, closeWidth);
                        anim.setDuration(ANIM_DURATION_MILLIS);
                        mLeftLayout.startAnimation(anim);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                //set subTitle invisible in adapter
                                mShiftLogAdapter.changeState(true);
                                mShiftLogAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                toggleWoopList(mLeftLayoutParams, closeWidth, mRightLayoutParams, false);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        isOpen = false;
                    }
                }
            }
        });

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
                            if (currentX >= closeWidth && currentX <= openWidth) {
                                mLeftLayout.getLayoutParams().width = currentX;
                                mLeftLayout.requestLayout();
                                mDownX = (int) event.getRawX();
                                //set subTitle invisible in adapter
//                                mShiftLogAdapter.changeState(currentX < middleWidth);
                                mShiftLogAdapter.changeState(true);
                                mShiftLogAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mNoData) {
                            if (mLeftLayoutParams.width < middleWidth) {
                                toggleWoopList(mLeftLayoutParams, closeWidth, mRightLayoutParams, false);
                            } else {
                                toggleWoopList(mLeftLayoutParams, openWidth, mRightLayoutParams, true);
                            }
                        }
                        break;
                }
                return false;
            }
        });

        getShiftLogs();
        setActionBar();
    }

    private void getShiftLogs() {
        ProgressDialogManager.show(getActivity());
        mDialogsShiftLogListener.getShiftLogCore().getShiftLogs(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getSessionId(), PersistenceManager.getInstance().getMachineId(), "", new ShiftLogUICallback<ShiftLog>() {
            @Override
            public void onGetShiftLogSucceeded(ArrayList<ShiftLog> shiftLogs) {
                dismissProgressDialog();
                if (shiftLogs != null && shiftLogs.size() > 0) {
                    mShiftLogsQueue.clear();
                    mShiftLogsList.clear();

                    mNoDataView.setVisibility(View.GONE);
                    mNoNotificationsText.setVisibility(View.GONE);
                    mShiftLogsQueue.addAll(shiftLogs);
                    mShiftLogsList.addAll(shiftLogs);

                    mShiftLogAdapter = new ShiftLogAdapter(getActivity(), mShiftLogsList, true);
                    mShiftLogRecycler.setAdapter(mShiftLogAdapter);

                    ShiftLog shiftLog = mShiftLogsQueue.pop();
                    DialogFragment dialogFragment = DialogFragment.newInstance(shiftLog.getTitle(), shiftLog.getSubtitle(), shiftLog.getStartTime(), shiftLog.getEndTime());
                    dialogFragment.setTargetFragment(DashboardFragment.this, 0);
                    dialogFragment.setCancelable(false);
                    dialogFragment.show(getChildFragmentManager(), DialogFragment.DIALOG);
                } else {
                    mNoData = true;
                }

            }

            @Override
            public void onGetShiftLogFailed(ErrorObjectInterface reason) {
                mNoData = true;
                dismissProgressDialog();
                ShowCrouton.jobsLoadingErrorCrouton(mCroutonCallback, (com.operators.infra.ErrorObjectInterface) reason);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mIsFirstJobSpinnerSelection = true;
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
            mDialogsShiftLogListener = (DialogsShiftLogListener) getActivity();
            mOnGoToScreenListener = (GoToScreenListener) getActivity();
            mOnActivityCallbackRegistered = (OnActivityCallbackRegistered) context;
            mOnActivityCallbackRegistered.onFragmentAttached(this);

        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCroutonCallback = null;
        mDialogsShiftLogListener = null;
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
            SpannableString s = new SpannableString(getActivity().getString(R.string.screen_title));
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
                    } else {

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
        }
    }

    @Override
    public void onPositiveButtonClick(DialogInterface dialog, int requestCode) {
        dialog.dismiss();
        if (mShiftLogsQueue.peek() != null && (System.currentTimeMillis() - mShiftLogsQueue.peek().getTimeOfAdded()) < THIRTY_SECONDS) {
            ShiftLog shiftLog = mShiftLogsQueue.pop();
            DialogFragment dialogFragment = DialogFragment.newInstance(shiftLog.getTitle(), shiftLog.getSubtitle(), shiftLog.getStartTime(), shiftLog.getEndTime());
            dialogFragment.setTargetFragment(DashboardFragment.this, 0);
            dialogFragment.setCancelable(false);
            dialogFragment.show(getChildFragmentManager(), DialogFragment.DIALOG);
        }
    }

    @Override
    public void onNegativeButtonClick(DialogInterface dialog, int requestCode) {
        mShiftLogsQueue.clear();
        dialog.dismiss();
    }

    private void dismissProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialogManager.dismiss();
            }
        });
    }

    public void onDeviceStatusChanged(MachineStatus machineStatus) {
        Log.i(LOG_TAG, "onDeviceStatusChanged()");
        mCurrentMachineStatus = machineStatus;
        initStatusLayout(mCurrentMachineStatus);
    }

    private void initStatusLayout(MachineStatus machineStatus) {
        mProductNameTextView.setText(new StringBuilder(machineStatus.getProductName() + ","));
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
