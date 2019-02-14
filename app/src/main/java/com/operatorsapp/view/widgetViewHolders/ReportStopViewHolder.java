package com.operatorsapp.view.widgetViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.utils.WidgetAdapterUtils;

public class ReportStopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int END_LIMIT = 10;//in minute
    private final int mHeight;
    private final int mWidth;
    private final RelativeLayout mParentLayout;
    private TextView mTitle;
    private TextView mSubTitle;
    private PercentageView mPercentageView;
    private TextView mText;
    private TextView mBtn;
    private DashboardCentralContainerListener mListener;
    private View mDivider;

    public ReportStopViewHolder(View itemView, DashboardCentralContainerListener listener, int height, int width) {
        super(itemView);

        mListener = listener;
        mHeight = height;
        mWidth = width;
        mParentLayout = itemView.findViewById(R.id.RPWC_parent_layout);
        mDivider = itemView.findViewById(R.id.RPWC_divider);
        mTitle = itemView.findViewById(R.id.RPWC_title);
        mSubTitle = itemView.findViewById(R.id.RPWC_subtitle);
        mPercentageView = itemView.findViewById(R.id.RPWC_percentage);
        mText = itemView.findViewById(R.id.RPWC_percentage_text_tv);
        mBtn = itemView.findViewById(R.id.RPWC_percentage_btn);
    }

    public void setData(Widget widget) {
        initListener();
        setView(widget);
        initPercentage(WidgetAdapterUtils.tryParse(widget.getCurrentValue(), WidgetAdapterUtils.StringParse.FLOAT), widget.getTarget());
    }

    private void setView(Widget widget) {
        mDivider.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams4;
                mItemViewParams4 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
                mItemViewParams4.setMargins(0, (int) (mParentLayout.getHeight() * 0.3), 0, 0);
                mDivider.requestLayout();
            }
        });

        setSizes(mParentLayout);
    }

    private void update2LyViews(int time, int endLimit) {
        if (time <= endLimit) {
            mText.setText(mText.getContext().getString(R.string.dont_forget_to_activate_job));
            mBtn.setText(mBtn.getContext().getString(R.string.activate));
            mText.setTextColor(mText.getContext().getResources().getColor(R.color.red_line));
            mBtn.setBackgroundColor(mBtn.getContext().getResources().getColor(R.color.red_line));
        } else {
            mText.setText(mText.getContext().getString(R.string.get_ready_for_your_next_job));
            mBtn.setText(mBtn.getContext().getString(R.string.see_job));
            mText.setTextColor(mText.getContext().getResources().getColor(R.color.blue1));
            mBtn.setBackgroundColor(mBtn.getContext().getResources().getColor(R.color.blue1));
        }
    }

    private void initListener() {
        mBtn.setOnClickListener(this);
    }

    private void initPercentage(float currentValue, float target) {
        currentValue = 75;
        target = 100;
        if (target != 0) {
            mPercentageView.update((int) (currentValue * 100 / target));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RPWC_percentage_btn:
                mListener.onOpenPendingJobs();
                break;
        }
    }

    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.45);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
