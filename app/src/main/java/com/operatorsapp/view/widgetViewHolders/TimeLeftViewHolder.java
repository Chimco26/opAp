package com.operatorsapp.view.widgetViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operators.machinestatusinfra.models.MachineStatus;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.utils.StringUtil;

public class TimeLeftViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private boolean mEndSetupDisable;
    private TextView mTitle;
    private TextView mSubTitle;
    private View m1Ly;
    private TextView m1TimeTv;
    private View m1Btn;
    private View m3Ly;
    private View m2Btn;
    private View m2CountDownLy;
    private CountDownView m3CountDownView;
    private TextView m3Text;
    private TextView m3Btn;
    private DashboardCentralContainerListener mListener;
    private MachineStatus mMachineStatus;

    public TimeLeftViewHolder(View itemView, DashboardCentralContainerListener listener, MachineStatus machineStatus, boolean endSetupDisable) {
        super(itemView);

        mListener = listener;
        mMachineStatus = machineStatus;
        mEndSetupDisable = endSetupDisable;
        mTitle = itemView.findViewById(R.id.TLWC_title);
        mSubTitle = itemView.findViewById(R.id.TLWC_subtitle);
        m1Ly = itemView.findViewById(R.id.TLWC_time_ly);
        m1TimeTv = itemView.findViewById(R.id.TLWC_time_tv);
        m1Btn = itemView.findViewById(R.id.TLWC_see_jobs_btn);
        m3Ly = itemView.findViewById(R.id.TLWC_end_setup_ly);
        m2Btn = itemView.findViewById(R.id.TLWC_end_setup_btn);
        m2CountDownLy = itemView.findViewById(R.id.TLWC_countdown_ly);
        m3CountDownView = itemView.findViewById(R.id.TLWC_countdown);
        m3Text = itemView.findViewById(R.id.TLWC_countdown_text_tv);
        m3Btn = itemView.findViewById(R.id.TLWC_countdown_btn);
    }

    public void setData(Widget widget, MachineStatus machineStatus, boolean endSetupDisable) {
        mMachineStatus = machineStatus;
        mEndSetupDisable = endSetupDisable;
        initListener();
        setView(widget);
    }

    private void setView(Widget widget) {
        int time = Integer.parseInt(widget.getCurrentValue());
        int endLimit = 50;
        if (mMachineStatus != null &&
                mMachineStatus.getAllMachinesData() != null
                && mMachineStatus.getAllMachinesData().get(0) != null &&
//                !mEndSetupDisable &&
                mMachineStatus.getAllMachinesData().get(0).getmProductionModeID() <= 1
                        && mMachineStatus.getAllMachinesData().get(0).canReportApproveFirstItem()) {

            m1Ly.setVisibility(View.GONE);
            m2CountDownLy.setVisibility(View.GONE);
            m3Ly.setVisibility(View.VISIBLE);
        } else {
            if (time >= 60) {
                m1Ly.setVisibility(View.VISIBLE);
                m2CountDownLy.setVisibility(View.GONE);
                m3Ly.setVisibility(View.GONE);
                m1TimeTv.setText(String.format("%s:%s", StringUtil.add0ToNumber(time / 60), StringUtil.add0ToNumber(time % 60)));
            } else {
                m1Ly.setVisibility(View.GONE);
                m2CountDownLy.setVisibility(View.VISIBLE);
                m3Ly.setVisibility(View.GONE);
                initCountDown(time, endLimit);
                update2LyViews(time, endLimit);
            }
        }
    }

    private void update2LyViews(int time, int endLimit) {
        if (time > endLimit) {
            m3Text.setText(m3Text.getContext().getString(R.string.dont_forget_to_activate_job));
            m3Btn.setText(m3Btn.getContext().getString(R.string.activate));
            m3Text.setTextColor(m3Text.getContext().getResources().getColor(R.color.red_line));
            m3Btn.setBackgroundColor(m3Btn.getContext().getResources().getColor(R.color.red_line));
        } else {
            m3Text.setText(m3Text.getContext().getString(R.string.get_ready_for_your_next_job));
            m3Btn.setText(m3Btn.getContext().getString(R.string.see_job));
            m3Text.setTextColor(m3Text.getContext().getResources().getColor(R.color.blue1));
            m3Btn.setBackgroundColor(m3Btn.getContext().getResources().getColor(R.color.blue1));
        }
    }

    private void initListener() {
        m1Btn.setOnClickListener(this);
        m2Btn.setOnClickListener(this);
        m3Btn.setOnClickListener(this);
    }

    private void initCountDown(int time, int endLimit) {
        m3CountDownView.setEndModeTimeInMinute(endLimit);
        m3CountDownView.update(time, "00:");
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
}
