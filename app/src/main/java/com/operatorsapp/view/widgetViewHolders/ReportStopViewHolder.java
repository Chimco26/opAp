package com.operatorsapp.view.widgetViewHolders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.DashboardCentralContainerListener;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.utils.WidgetAdapterUtils;

import java.util.Locale;

public class ReportStopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int END_LIMIT = 10;//in minute
    private final View mFilterShort;
    private final View mReported;
    private final View mNotReported;
    private final LinearLayout mCenterLayout;
    private final View mLegFilterShort;
    private final TextView mLegFilterShortTv;
    private final View mLegReported;
    private final TextView mLegReportedTv;
    private final View mLegNotReported;
    private final TextView mLegNotReportedTv;
    private final TextView mFilterShortMinTv;
    private final TextView mReportedMinTv;
    private final TextView mNotReportedTv;
    private final TextView mNotReportedMinTv;
    private final View mDefault;
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
        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mCenterLayout = itemView.findViewById(R.id.RPWC_center_ly);
//        mDivider = itemView.findViewById(R.id.RPWC_divider);
        mTitle = itemView.findViewById(R.id.RPWC_title);
        mSubTitle = itemView.findViewById(R.id.RPWC_subtitle);
        mText = itemView.findViewById(R.id.RPWC_percentage_text_tv);
        mBtn = itemView.findViewById(R.id.RPWC_report_btn);
        mBtn.setOnClickListener(this);

        mDefault = itemView.findViewById(R.id.RPWC_default);
        mFilterShort = itemView.findViewById(R.id.RPWC_filter_short);
        mReported = itemView.findViewById(R.id.RPWC_reported);
        mNotReported = itemView.findViewById(R.id.RPWC_not_reported);

        mFilterShortTv = itemView.findViewById(R.id.RPWC_filter_short_tv);
        mFilterShortMinTv = itemView.findViewById(R.id.RPWC_filter_short_min_tv);
        mReportedTv = itemView.findViewById(R.id.RPWC_reported_tv);
        mReportedMinTv = itemView.findViewById(R.id.RPWC_reported_min_tv);
        mNotReportedTv = itemView.findViewById(R.id.RPWC_not_reported_tv);
        mNotReportedMinTv = itemView.findViewById(R.id.RPWC_not_reported_min_tv);

        mLegFilterShort = itemView.findViewById(R.id.filter_square);
        mLegFilterShortTv = itemView.findViewById(R.id.filter_square_tv);
        mLegReported = itemView.findViewById(R.id.reported_square);
        mLegReportedTv = itemView.findViewById(R.id.reported_square_tv);
        mLegNotReported = itemView.findViewById(R.id.no_reported_square);
        mLegNotReportedTv = itemView.findViewById(R.id.no_reported_square_tv);

    }

    public void setData(Widget widget) {
        setSizes(mParentLayout);

        setColors(widget);
        int reportedValue = (int) (WidgetAdapterUtils.tryParse(widget.getCurrentValue(), WidgetAdapterUtils.StringParse.FLOAT));
        int totalMinutes = (int) (widget.getProjection() + reportedValue + widget.getTarget());
        int noReportedValue = totalMinutes - reportedValue - widget.getProjection().intValue();
        int filterShortPercent = 0;
        int reportedPercent = 0;
        int notReportedPercent = 0;
        if (totalMinutes != 0) {
            filterShortPercent = (int) (widget.getProjection() * 100 / totalMinutes);
            reportedPercent = (int) reportedValue * 100 / totalMinutes;
            notReportedPercent = 100 - filterShortPercent - reportedPercent;
        }

        updateViews(reportedPercent, filterShortPercent, notReportedPercent, reportedValue, widget.getProjection().intValue(), noReportedValue);
        if (totalMinutes >= 1) {
            mDefault.setVisibility(View.GONE);
            mSubTitle.setText(String.format(Locale.getDefault(), "%d/%d - %d%%", (int) (reportedValue + widget.getProjection()), totalMinutes, reportedPercent + filterShortPercent));
            updateTextViews(reportedPercent + filterShortPercent);
        } else {
            setEmptyMode(widget);
        }
    }

    private void setEmptyMode(Widget widget) {
        mBtn.setVisibility(View.GONE);
        mDefault.setVisibility(View.VISIBLE);
        mSubTitle.setText(String.format(Locale.getDefault(), "%d/%d - %d%%", 0, 0, 0));
        setWeight(0, mBtn);
        setWeight(10, mText);
        mText.setText(mText.getContext().getString(R.string.there_are_no_stop_event));
//        mText.setTextColor(mText.getContext().getResources().getColor(R.color.new_green));
        if (widget.getCurrentColor() != null && widget.getCurrentColor().length() > 0) {
            mNotReported.setBackgroundColor(Color.parseColor(widget.getCurrentColor()));
        } else {
            mNotReported.setBackgroundColor(mNotReported.getContext().getResources().getColor(R.color.red_line));
        }
    }

    private void setWeight(double weight, View view) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.weight = (float) weight;
        view.setLayoutParams(params);
        view.requestLayout();
    }

    private void updateViews(final int reportedPercent, final int filterShortPercent, final int noReportedPercent, int reportedValue, int filterValue, int noReportedValue) {

        showTextValues(reportedPercent, filterShortPercent, noReportedPercent, filterValue, reportedValue, noReportedValue);
        mCenterLayout.post(new Runnable() {
            @Override
            public void run() {
                if (mCenterLayout.getContext() != null && mCenterLayout.getContext() instanceof Activity && !((Activity) mCenterLayout.getContext()).isDestroyed()) {

                    final float reportedWidth = mCenterLayout.getWidth() * reportedPercent / 100;
                    final float filterShortWidth = mCenterLayout.getWidth() * filterShortPercent / 100;
                    final float noReportedWidth = mCenterLayout.getWidth() * noReportedPercent / 100;
                    updateViewsWidth(reportedWidth, filterShortWidth, noReportedWidth);
                }
            }
        });
    }

    private void showTextValues(int reportedPercent, int filterShortPercent, int noReportedPercent, int filterValue, int reportedValue, int noReportedValue) {
        setFilterLegendText();
        if (filterShortPercent > 14) {
            mFilterShortTv.setVisibility(View.VISIBLE);
            mFilterShortMinTv.setVisibility(View.VISIBLE);
            mFilterShortTv.setText(String.format("%s%%", filterShortPercent));
            mFilterShortMinTv.setText(String.format(Locale.US, "%d%s", filterValue, mFilterShortMinTv.getContext().getString(R.string.min)));
        } else {
            mFilterShortTv.setVisibility(View.GONE);
            mFilterShortMinTv.setVisibility(View.GONE);
        }
        if (reportedPercent > 14) {
            mReportedTv.setVisibility(View.VISIBLE);
            mReportedMinTv.setVisibility(View.VISIBLE);
            mReportedTv.setText(String.format("%s%%", reportedPercent));
            mReportedMinTv.setText(String.format(Locale.US, "%d%s", reportedValue, mReportedMinTv.getContext().getString(R.string.min)));
        } else {
            mReportedTv.setVisibility(View.GONE);
            mReportedMinTv.setVisibility(View.GONE);
        }
        if (noReportedPercent > 14) {
            mNotReportedTv.setVisibility(View.VISIBLE);
            mNotReportedMinTv.setVisibility(View.VISIBLE);
            mNotReportedTv.setText(String.format("%s%%", noReportedPercent));
            mNotReportedMinTv.setText(String.format(Locale.US, "%d%s", noReportedValue, mFilterShortMinTv.getContext().getString(R.string.min)));
        } else {
            mNotReportedTv.setVisibility(View.GONE);
            mNotReportedMinTv.setVisibility(View.GONE);
        }
    }

    private void setFilterLegendText() {
        Context context = mLegFilterShortTv.getContext();
        mLegFilterShortTv.setText(String.format(Locale.getDefault(),
                "%s < %s %s",
                context.getString(R.string.filter_short),
                PersistenceManager.getInstance().getMinEventDuration(), context.getString(R.string.min)));
    }


    private void updateViewsWidth(final float reportedWidth, final float filterShortWidth, final float noReportedPercent) {
        mFilterShort.post(new Runnable() {
            @Override
            public void run() {
                if (mFilterShort.getContext() != null && mFilterShort.getContext() instanceof Activity && !((Activity) mFilterShort.getContext()).isDestroyed()) {

                    ViewGroup.MarginLayoutParams mItemViewParams4;
                    mItemViewParams4 = (ViewGroup.MarginLayoutParams) mFilterShort.getLayoutParams();
                    mItemViewParams4.width = (int) filterShortWidth;
                    mFilterShort.requestLayout();
                }
            }
        });
        mReported.post(new Runnable() {
            @Override
            public void run() {
                if (mReported.getContext() != null && mReported.getContext() instanceof Activity && !((Activity) mReported.getContext()).isDestroyed()) {

                    ViewGroup.MarginLayoutParams mItemViewParams4;
                    mItemViewParams4 = (ViewGroup.MarginLayoutParams) mReported.getLayoutParams();
                    mItemViewParams4.width = (int) reportedWidth;
                    mReported.requestLayout();
                }
            }
        });
        mNotReported.post(new Runnable() {
            @Override
            public void run() {
                if (mNotReported.getContext() != null && mNotReported.getContext() instanceof Activity && !((Activity) mNotReported.getContext()).isDestroyed()) {

                    ViewGroup.MarginLayoutParams mItemViewParams4;
                    mItemViewParams4 = (ViewGroup.MarginLayoutParams) mNotReported.getLayoutParams();
                    mItemViewParams4.width = (int) noReportedPercent;
                    mReported.requestLayout();
                }
            }
        });
    }

    private void updateTextViews(int percent) {
        if (percent != 100) {
            setWeight(3.5, mBtn);
            setWeight(6, mText);
            mText.setText(mText.getContext().getString(R.string.report_events_reason));
//            mText.setTextColor(mText.getContext().getResources().getColor(R.color.blue1));
            mBtn.setVisibility(View.VISIBLE);
        } else {
            setWeight(0, mBtn);
            setWeight(10, mText);
            mText.setText(mText.getContext().getString(R.string.target_reached));
//            mText.setTextColor(mText.getContext().getResources().getColor(R.color.new_green));
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
            mFilterShortMinTv.setTextColor(Color.parseColor(widget.getProjectionColor()));
            mLegFilterShort.setBackgroundColor(Color.parseColor(widget.getProjectionColor()));
        }
        if (widget.getCurrentColor() != null && widget.getCurrentColor().length() > 0) {
            mReported.setBackgroundColor(Color.parseColor(widget.getCurrentColor()));
            mLegReported.setBackgroundColor(Color.parseColor(widget.getCurrentColor()));
            mReportedTv.setTextColor(Color.parseColor(widget.getCurrentColor()));
            mReportedMinTv.setTextColor(Color.parseColor(widget.getCurrentColor()));
        }
        if (widget.getTargetColor() != null && widget.getTargetColor().length() > 0) {
            mNotReported.setBackgroundColor(Color.parseColor(widget.getTargetColor()));
            mLegNotReported.setBackgroundColor(Color.parseColor(widget.getTargetColor()));
            mNotReportedTv.setTextColor(Color.parseColor(widget.getTargetColor()));
            mNotReportedMinTv.setTextColor(Color.parseColor(widget.getTargetColor()));
        }

    }

//    private void setView() {
//        mDivider.post(new Runnable() {
//            @Override
//            public void run() {
//                ViewGroup.MarginLayoutParams mItemViewParams4;
//                mItemViewParams4 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
//                mItemViewParams4.setMargins(0, (int) (mParentLayout.getHeight() * 0.3), 0, 0);
//                mDivider.requestLayout();
//            }
//        });
//
//        setSizes(mParentLayout);
//    }
//
    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
