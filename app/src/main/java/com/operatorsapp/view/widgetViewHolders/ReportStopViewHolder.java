package com.operatorsapp.view.widgetViewHolders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.utils.WidgetAdapterUtils;

import java.util.Locale;

public class ReportStopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int END_LIMIT = 10;//in minute
    private final View mFilterShort;
    private final View mReported;
    private final View mNotReported;
    private final RelativeLayout mCenterLayout;
    private final View mLegFilterShort;
    private final TextView mLegFilterShortTv;
    private final View mLegReported;
    private final TextView mLegReportedTv;
    private final View mLegNotReported;
    private final TextView mLegNotReportedTv;
    private final TextView mFilterShortMinTv;
    private final TextView mReportedMinTv;
    private int mHeight;
    private int mWidth;
    private RelativeLayout mParentLayout;
    private TextView mFilterShortTv;
    private TextView mReportedTv;
    //    private final View mNoDataFilterView;
    private TextView mTitle;
    private TextView mSubTitle;
    private CounterView mPercentageView;
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
        mCenterLayout = itemView.findViewById(R.id.RPWC_center_ly);
        mDivider = itemView.findViewById(R.id.RPWC_divider);
        mTitle = itemView.findViewById(R.id.RPWC_title);
        mSubTitle = itemView.findViewById(R.id.RPWC_subtitle);
        mText = itemView.findViewById(R.id.RPWC_percentage_text_tv);
        mBtn = itemView.findViewById(R.id.RPWC_report_btn);
        mBtn.setOnClickListener(this);

        mFilterShort = itemView.findViewById(R.id.RPWC_filter_short);
        mReported = itemView.findViewById(R.id.RPWC_reported);
        mNotReported = itemView.findViewById(R.id.RPWC_not_reported);

        mFilterShortTv = itemView.findViewById(R.id.RPWC_filter_short_tv);
        mFilterShortMinTv = itemView.findViewById(R.id.RPWC_filter_short_min_tv);
        mReportedTv = itemView.findViewById(R.id.RPWC_reported_tv);
        mReportedMinTv = itemView.findViewById(R.id.RPWC_reported_min_tv);

        mLegFilterShort = itemView.findViewById(R.id.filter_square);
        mLegFilterShortTv = itemView.findViewById(R.id.filter_square_tv);
        mLegReported = itemView.findViewById(R.id.reported_square);
        mLegReportedTv = itemView.findViewById(R.id.reported_square_tv);
        mLegNotReported = itemView.findViewById(R.id.no_reported_square);
        mLegNotReportedTv = itemView.findViewById(R.id.no_reported_square_tv);

    }

    public void setData(Widget widget) {
        setView();

        setColors(widget);
        int reportedValue = (int) (WidgetAdapterUtils.tryParse(widget.getCurrentValue(), WidgetAdapterUtils.StringParse.FLOAT));
        int totalMinutes = (int) (widget.getProjection() + reportedValue + widget.getTarget());
        int filterShortPercent = 0;
        int reportedPercent = 0;
        if (totalMinutes != 0) {
            filterShortPercent = (int) (widget.getProjection() * 100 / totalMinutes);
            reportedPercent = (int) reportedValue * 100 / totalMinutes;
            reportedPercent += filterShortPercent;
        }

        updateViews(reportedPercent, filterShortPercent, reportedValue + widget.getProjection().intValue(), widget.getProjection().intValue());
        if (totalMinutes >= 1) {
            mSubTitle.setText(String.format("%s/%s min", (int) (reportedValue + widget.getProjection()), totalMinutes));
            updateTextViews(reportedPercent);
        } else {
            setEmptyMode(widget);
        }
    }

    private void setEmptyMode(Widget widget) {
        mSubTitle.setText(String.format("%s/%s min", 0, 0));
        updateTextViews(100);
        if (widget.getCurrentColor() != null && widget.getCurrentColor().length() > 0) {
            mNotReported.setBackgroundColor(Color.parseColor(widget.getCurrentColor()));
        }else {
            mNotReported.setBackgroundColor(mNotReported.getContext().getResources().getColor(R.color.red_line));
        }
    }

    private void updateViews(final int reportedPercent, final int filterShortPercent, int reportedValue, int filterValue) {

        if (reportedPercent - filterShortPercent < 14) {
            mFilterShortTv.setVisibility(View.GONE);
            mFilterShortMinTv.setVisibility(View.GONE);
        } else {
            mFilterShortTv.setVisibility(View.VISIBLE);
            mFilterShortMinTv.setVisibility(View.VISIBLE);
            mFilterShortTv.setText(String.format("%s%%", filterShortPercent));
            mFilterShortMinTv.setText(String.format(Locale.US,"%d%s", filterValue, mFilterShortMinTv.getContext().getString(R.string.min)));
        }
        mReportedTv.setText(String.format("%s%%", reportedPercent));
        mReportedMinTv.setText(String.format(Locale.US,"%d%s", reportedValue, mReportedMinTv.getContext().getString(R.string.min)));
        mCenterLayout.post(new Runnable() {
            @Override
            public void run() {
                final float reportedWidth = mCenterLayout.getWidth() * reportedPercent / 100;
                final float filterShortWidth = mCenterLayout.getWidth() * filterShortPercent / 100;
                updateViewsWidth(reportedWidth, filterShortWidth);
            }
        });
    }


    private void updateViewsWidth(final float reportedWidth, final float filterShortWidth) {
        mFilterShort.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams4;
                mItemViewParams4 = (ViewGroup.MarginLayoutParams) mFilterShort.getLayoutParams();
                mItemViewParams4.width = (int) filterShortWidth;
                mFilterShort.requestLayout();
            }
        });
        mReported.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams4;
                mItemViewParams4 = (ViewGroup.MarginLayoutParams) mReported.getLayoutParams();
                mItemViewParams4.width = (int) reportedWidth;
                mReported.requestLayout();
            }
        });
    }

    private void updateTextViews(int percent) {
        if (percent != 100) {
            mText.setText(mText.getContext().getString(R.string.report_events_reason));
            mText.setTextColor(mText.getContext().getResources().getColor(R.color.blue1));
            mBtn.setVisibility(View.VISIBLE);
        } else {
            mText.setText(mText.getContext().getString(R.string.target_reached));
            mText.setTextColor(mText.getContext().getResources().getColor(R.color.new_green));
            mBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.RPWC_report_btn:
                mListener.onReportStopEvent();
                break;
        }
    }

    private void setColors(Widget widget) {
        if (widget.getProjectionColor() != null && widget.getProjectionColor().length() > 0) {
            mFilterShort.setBackgroundColor(Color.parseColor(widget.getProjectionColor()));
            mFilterShortTv.setTextColor(Color.parseColor(widget.getProjectionColor()));
            mLegFilterShortTv.setTextColor(Color.parseColor(widget.getProjectionColor()));
            mLegFilterShort.setBackgroundColor(Color.parseColor(widget.getProjectionColor()));
        }
        if (widget.getCurrentColor() != null && widget.getCurrentColor().length() > 0) {
            mReported.setBackgroundColor(Color.parseColor(widget.getCurrentColor()));
            mLegReported.setBackgroundColor(Color.parseColor(widget.getCurrentColor()));
            mReportedTv.setTextColor(Color.parseColor(widget.getCurrentColor()));
            mLegReportedTv.setTextColor(Color.parseColor(widget.getCurrentColor()));
        }
        if (widget.getTargetColor() != null && widget.getTargetColor().length() > 0) {
            mNotReported.setBackgroundColor(Color.parseColor(widget.getTargetColor()));
            mLegNotReported.setBackgroundColor(Color.parseColor(widget.getTargetColor()));
            mLegNotReportedTv.setTextColor(Color.parseColor(widget.getTargetColor()));
        }

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

    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.45);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
