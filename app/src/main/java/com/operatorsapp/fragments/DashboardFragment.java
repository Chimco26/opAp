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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.operators.shiftlogcore.interfaces.ShiftLogUICallback;
import com.operators.shiftloginfra.ErrorObjectInterface;
import com.operators.shiftloginfra.ShiftLog;
import com.operatorsapp.R;
import com.operatorsapp.adapters.OperatorSpinnerAdapter;
import com.operatorsapp.adapters.ShiftLogAdapter;
import com.operatorsapp.dialogs.DialogFragment;
import com.operatorsapp.fragments.interfaces.DialogsShiftLogListener;
import com.operatorsapp.fragments.interfaces.OnCroutonRequestListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.ResizeWidthAnimation;

import java.util.ArrayList;
import java.util.Stack;

public class DashboardFragment extends Fragment implements DialogFragment.OnDialogButtonsListener {

    private static final String LOG_TAG = DashboardFragment.class.getSimpleName();
    public static final int DURATION_MILLIS = 200;
    private static final int DISMISS_DIALOG_FRAGMENT = 1003;
    private static final int DISMISS_ALL_DIALOG_FRAGMENT = 1004;

    private OnCroutonRequestListener mCroutonCallback;
    private DialogsShiftLogListener mDialogsShiftLogListener;
    private RecyclerView mShiftLogRecycler;
    private LinearLayout mLeftLayout, mRightLayout;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;
    private int mDownX;
    private ShiftLogAdapter mShiftLogAdapter;
    Stack<ShiftLog> mShiftLogsStack = new Stack<ShiftLog>();
    //    private ArrayList<ShiftLog> mShiftLogsList;
    private View mDividerView;

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
        final int openWidth = width / 2;
        final int closeWidth = width / 4;
        final int middleWidth = width * 3 / 8;

        mShiftLogRecycler = (RecyclerView) view.findViewById(R.id.fragment_dashboard_shift_log);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mShiftLogRecycler.setLayoutManager(linearLayoutManager);

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
                final ResizeWidthAnimation anim = new ResizeWidthAnimation(mLeftLayout, openWidth);
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
                final ResizeWidthAnimation anim = new ResizeWidthAnimation(mLeftLayout, closeWidth);
                anim.setDuration(DURATION_MILLIS);
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
                            mShiftLogAdapter.changeState(currentX < middleWidth);
                            mShiftLogAdapter.notifyDataSetChanged();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mLeftLayoutParams.width < middleWidth) {
                            toggleWoopList(mLeftLayoutParams, closeWidth, mRightLayoutParams, false);
                        } else {
                            toggleWoopList(mLeftLayoutParams, openWidth, mRightLayoutParams, true);
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
        mDialogsShiftLogListener.getShiftLogCore().getShiftLogs(PersistenceManager.getInstance().getSiteUrl(), PersistenceManager.getInstance().getSessionId(), PersistenceManager.getInstance().getMachineId(), "", new ShiftLogUICallback<ShiftLog>() {
            @Override
            public void onGetShiftLogSucceeded(ArrayList<ShiftLog> shiftLogs) {
                //    mShiftLogsList = shiftLogs;
                mShiftLogAdapter = new ShiftLogAdapter(getActivity(), shiftLogs, true);
                mShiftLogRecycler.setAdapter(mShiftLogAdapter);

                mShiftLogsStack.addAll(shiftLogs);

//                if ((System.currentTimeMillis() - mShiftLogsStack.get(0).getTimeOfAdded()) < (30 * 1000)) {
                DialogFragment dialogFragment = DialogFragment.newInstance(mShiftLogsStack.get(0).getSubtitle(), R.string.login_dialog_dismiss, R.string.login_dialog_dismiss_all);
                dialogFragment.show(getChildFragmentManager(), DialogFragment.DIALOG);
                mShiftLogsStack.pop();
                mDialogsShiftLogListener.getShiftLogCore().setShiftLogDialogStatus(mShiftLogsStack);
//                }

//                for (int i = 0; i < shiftLogs.size(); i++) {
//                    if (!shiftLogs.get(i).isDialogShown()) {
//                        if ((System.currentTimeMillis() - shiftLogs.get(i).getTimeOfAdded()) < (30 * 1000)) {
//                            DialogFragment dialogFragment = DialogFragment.newInstance(shiftLogs.get(i).getSubtitle(), R.string.login_dialog_dismiss, R.string.login_dialog_dismiss_all);
//                            dialogFragment.show(getChildFragmentManager(), DialogFragment.DIALOG);
//                            shiftLogs.get(i).setDialogShown(true);
//                            mShiftLogsStack.addAll(shiftLogs);
//                            mDialogsShiftLogListener.getShiftLogCore().setShiftLogDialogStatus(mShiftLogsStack);
//                            break;
//                        } else {
//                            shiftLogs.get(i).setDialogShown(true);
//                        }
//                    }
//                }
            }

            @Override
            public void onGetShiftLogFailed(ErrorObjectInterface reason) {

            }
        });
    }

    private void toggleWoopList(ViewGroup.LayoutParams mLeftLayoutParams, int newWidth, ViewGroup.MarginLayoutParams mRightLayoutParams, boolean open) {
        mLeftLayoutParams.width = newWidth;
        mRightLayoutParams.setMarginStart(newWidth);
        mLeftLayout.requestLayout();
        mRightLayout.setLayoutParams(mRightLayoutParams);
        if (open) {
            mArrowLeft.setVisibility(View.INVISIBLE);
            mArrowRight.setVisibility(View.VISIBLE);
        } else {
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
            mDialogsShiftLogListener = (DialogsShiftLogListener) getActivity();

        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCroutonCallback = null;
        mDialogsShiftLogListener = null;
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
            ((TextView) view.findViewById(R.id.toolbar_title)).setText(s);
            Spinner spinner = (Spinner) view.findViewById(R.id.toolbar_operator_spinner);
            final String[] data = new String[2];
            data[0] = "Sign in";
            data[1] = "Sergey";
            final ArrayAdapter<String> adapter = new OperatorSpinnerAdapter(getActivity(), R.layout.spinner_operator_item, data);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            actionBar.setCustomView(view);
            actionBar.setIcon(R.drawable.logo);
        }
    }

    @Override
    public void onPositiveButtonClick(DialogInterface dialog, int requestCode) {
        dialog.dismiss();
    }

    @Override
    public void onNegativeButtonClick(DialogInterface dialog, int requestCode) {
        mDialogsShiftLogListener.getShiftLogCore().setShiftLogDialogStatus(null);
        dialog.dismiss();
    }
}
