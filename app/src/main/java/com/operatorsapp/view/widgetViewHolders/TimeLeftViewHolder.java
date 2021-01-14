package com.operatorsapp.view.widgetViewHolders;

import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.utils.StringUtil;
import com.operatorsapp.utils.TimeUtils;

import java.util.Date;
import java.util.Locale;

public class TimeLeftViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int END_LIMIT = 10;//in minute
    private final int mHeight;
    private final int mWidth;
    private final TextView m1EstimatedTv;
    private boolean mShowActivateJobBtn = true;
    private boolean mShowEndSetupBtn = true;
    private LinearLayout mParentLayout;
    private boolean mEndSetupDisable;
    private TextView mTitle;
    private TextView mSubTitle;
    private View m1Ly;
    private TextView m1TimeTv;
    private LinearLayout m1Btn;
    private View m3Ly;
    private View m2Btn;
    private View m2CountDownLy;
    private CountDownView m3CountDownView;
    private TextView m3Text;
    private TextView m3Btn;
    private DashboardCentralContainerListener mListener;
    private MachineStatus mMachineStatus;
    private View mDivider;

    public TimeLeftViewHolder(View itemView, DashboardCentralContainerListener listener, MachineStatus machineStatus, boolean endSetupDisable,
                              int height, int width, boolean showEndSetupBtn, boolean showActivateJobBtn) {
        super(itemView);

        mListener = listener;
        mHeight = height;
        mWidth = width;
        mMachineStatus = machineStatus;
        mEndSetupDisable = endSetupDisable;
        mShowEndSetupBtn = showEndSetupBtn;
        mShowActivateJobBtn = showActivateJobBtn;
        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
//        mDivider = itemView.findViewById(R.id.TLWC_divider);
        mTitle = itemView.findViewById(R.id.TLWC_title);
        mSubTitle = itemView.findViewById(R.id.TLWC_subtitle);
        m1Ly = itemView.findViewById(R.id.TLWC_time_ly);
        m1TimeTv = itemView.findViewById(R.id.TLWC_time_tv);
        m1EstimatedTv = itemView.findViewById(R.id.TLWC_estimated_tv);
        m1Btn = itemView.findViewById(R.id.TLWC_see_jobs_btn);
        m3Ly = itemView.findViewById(R.id.TLWC_end_setup_ly);
        m2Btn = itemView.findViewById(R.id.TLWC_end_setup_btn);
        m2CountDownLy = itemView.findViewById(R.id.TLWC_countdown_ly);
        m3CountDownView = itemView.findViewById(R.id.TLWC_countdown);
        m3Text = itemView.findViewById(R.id.TLWC_countdown_text_tv);
        m3Btn = itemView.findViewById(R.id.TLWC_countdown_btn);
        setSizes(mParentLayout);
    }

    public void setData(Widget widget, MachineStatus machineStatus, boolean endSetupDisable, boolean showEndSetupBtn, boolean showActivateJobBtn) {
        mMachineStatus = machineStatus;
        mEndSetupDisable = endSetupDisable;
        mShowEndSetupBtn = showEndSetupBtn;
        mShowActivateJobBtn = showActivateJobBtn;
        initListener();
        setView(widget);
    }

    private void setView(Widget widget) {

        String nameByLang2 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang2);
        long time = 0;
        try {
            time = Long.parseLong(widget.getCurrentValue());
        }catch (Exception ignored){}
        int endLimit = END_LIMIT;
//        time = 45;// test states
        if (mMachineStatus != null &&
                mMachineStatus.getAllMachinesData() != null
                && mMachineStatus.getAllMachinesData().get(0) != null &&
//                !mEndSetupDisable &&
                mMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                && mMachineStatus.getAllMachinesData().get(0).canReportApproveFirstItem()) {

            m1Ly.setVisibility(View.GONE);
            m2CountDownLy.setVisibility(View.GONE);
            m3Ly.setVisibility(View.VISIBLE);
            if (mShowEndSetupBtn){
                m2Btn.setVisibility(View.VISIBLE);
            }else {
                m2Btn.setVisibility(View.INVISIBLE);
            }
//            mSubTitle.setText("");
            mTitle.setText(R.string.setup_mode);
        } else {
            if (time >= 60) {
                m1Ly.setVisibility(View.VISIBLE);
                if (mShowActivateJobBtn) {
                    m1Btn.setVisibility(View.VISIBLE);
                }else {
                    m1Btn.setVisibility(View.GONE);
                }
                m2CountDownLy.setVisibility(View.GONE);
                m3Ly.setVisibility(View.GONE);
                if (time > 24 * 60) {
                    m1EstimatedTv.setVisibility(View.VISIBLE);
                    m1EstimatedTv.setText(String.format("%s: %s", m1TimeTv.getContext().getString(R.string.estimated_date),
                            TimeUtils.convertMillisecondDateTo(new Date().getTime() + time * 60 * 1000)));
                    m1TimeTv.setText(Html.fromHtml(String.format(Locale.getDefault(), "<b>%s %s</b>", ((int) (time / 60)),
                            m1TimeTv.getContext().getString(R.string.hr2))));
                } else {
                    m1EstimatedTv.setVisibility(View.GONE);
                    m1TimeTv.setText(Html.fromHtml(String.format(Locale.getDefault(), "<b>%s %s %s %s</b>", ((int) (time / 60)),
                            m1TimeTv.getContext().getString(R.string.hr2),
                            StringUtil.add0ToNumber((int) (time % 60)),
                            m1TimeTv.getContext().getString(R.string.min))));
                }
            } else {
                m1Ly.setVisibility(View.GONE);
                m2CountDownLy.setVisibility(View.VISIBLE);
                m3Ly.setVisibility(View.GONE);
                initCountDown((int) time, endLimit);
                update2LyViews((int) time, endLimit);
            }
        }
        mTitle.post(new Runnable() {
            @Override
            public void run() {
                TextViewCompat
                        .setAutoSizeTextTypeUniformWithConfiguration(mTitle,
                                10, 23, 1, TypedValue.COMPLEX_UNIT_SP);
            }
        });
    }

    private void update2LyViews(int time, int endLimit) {
        if (time <= endLimit) {
            m3Text.setText(m3Text.getContext().getString(R.string.dont_forget_to_activate_job));
            m3Btn.setText(m3Btn.getContext().getString(R.string.activate));
            if (mShowActivateJobBtn) {
                m3Btn.setVisibility(View.VISIBLE);
            }else {
                m3Btn.setVisibility(View.INVISIBLE);
            }
            m3Text.setTextColor(m3Text.getContext().getResources().getColor(R.color.red_line));
//            mSubTitle.setText(mSubTitle.getContext().getString(R.string.hr));
//            m3Btn.setBackgroundColor(m3Btn.getContext().getResources().getColor(R.color.red_line));
        } else {
            m3Text.setText(m3Text.getContext().getString(R.string.get_ready_for_your_next_job));
            m3Btn.setText(m3Btn.getContext().getString(R.string.see_job));
            if (mShowActivateJobBtn) {
                m3Btn.setVisibility(View.VISIBLE);
            }else {
                m3Btn.setVisibility(View.INVISIBLE);
            }
            m3Text.setTextColor(m3Text.getContext().getResources().getColor(R.color.blue1));
        }
        m3Text.post(new Runnable() {
            @Override
            public void run() {
                TextViewCompat
                        .setAutoSizeTextTypeUniformWithConfiguration(m3Text,
                                10, 18, 1, TypedValue.COMPLEX_UNIT_SP);
            }
        });
        m3Btn.post(new Runnable() {
            @Override
            public void run() {
                TextViewCompat
                        .setAutoSizeTextTypeUniformWithConfiguration(m3Btn,
                                10, 20, 1, TypedValue.COMPLEX_UNIT_SP);
            }
        });
    }

    private void initListener() {
        m1Btn.setOnClickListener(this);
        m2Btn.setOnClickListener(this);
        m3Btn.setOnClickListener(this);
    }

    private void initCountDown(int time, int endLimit) {
        m3CountDownView.setEndModeTimeInMinute(endLimit);
        m3CountDownView.update(time, m3CountDownView.getContext().getString(R.string.min));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TLWC_see_jobs_btn:
                mListener.onOpenPendingJobs();
                break;
            case R.id.TLWC_end_setup_btn:
                if (mMachineStatus != null &&
                        mMachineStatus.getAllMachinesData() != null
                        && mMachineStatus.getAllMachinesData().get(0) != null &&
//                        !mEndSetupDisable &&
                        mMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                        && mMachineStatus.getAllMachinesData().get(0).canReportApproveFirstItem()) {
                    mListener.onEndSetup();
                }
                break;
            case R.id.TLWC_countdown_btn:
                mListener.onOpenPendingJobs();
                break;
        }
    }

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
