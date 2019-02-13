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

import static com.operatorsapp.utils.WidgetAdapterUtils.valueInK;

public class ReportStopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int END_LIMIT = 10;//in minute
    private final int mHeight;
    private final int mWidth;
    private final RelativeLayout mParentLayout;
    private final TextView mCurrentValueTv;
    private final TextView mTargetValueTv;
    private final TextView mPercentValueTv;
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

        mCurrentValueTv = itemView.findViewById(R.id.RPWC_current_value_tv);
        mTargetValueTv = itemView.findViewById(R.id.RPWC_target_value_tv);
        mPercentValueTv = itemView.findViewById(R.id.RPWC_percent_value_tv);
    }

    public void setData(Widget widget) {
        initListener();
        setView();
        float currentValue = WidgetAdapterUtils.tryParse(widget.getCurrentValue(), WidgetAdapterUtils.StringParse.FLOAT);
//        currentValue = 0;
        float target = widget.getTarget();
        int percent = initPercentage(currentValue, target);
        updateTextViews(percent, currentValue, target);
    }

    private void setView() {
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

    private void updateTextViews(int percent, float currentValue, Float target) {
        if (percent < 50) {
            mText.setText(mText.getContext().getString(R.string.report_events_reason));
            mText.setTextColor(mText.getContext().getResources().getColor(R.color.blue1));
            mBtn.setVisibility(View.VISIBLE);
        } else if (currentValue < target){
            mText.setText(mText.getContext().getString(R.string.keep_reporting_events_reason));
            mText.setTextColor(mText.getContext().getResources().getColor(R.color.blue1));
            mBtn.setVisibility(View.VISIBLE);
        }else {
            mText.setText(mText.getContext().getString(R.string.target_reached));
            mText.setTextColor(mText.getContext().getResources().getColor(R.color.new_green));
            mBtn.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        mBtn.setOnClickListener(this);
    }

    private int initPercentage(float currentValue, float target) {
        int percent = 0;
        if (target != 0) {
            percent = (int) (currentValue * 100 / target);
            mCurrentValueTv.setText(valueInK(currentValue));
            mTargetValueTv.setText(String.format("/%s min", valueInK(target)));
            mPercentValueTv.setText(String.format("%s%%", valueInK(percent)));
            mPercentageView.update(percent);
        }
        return percent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RPWC_percentage_btn:
                mListener.onReportStopEvent();
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
