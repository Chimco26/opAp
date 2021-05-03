package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.managers.PersistenceManager;
import com.operatorsapp.view.RangeView2;

public class RangeViewHolder extends RecyclerView.ViewHolder {

    private static final String CYCLE_TIME = "CycleTime";

    private TextView mStandardTv;
    private TextView mAverageTv;
    private final ImageView mAverageImg;
    private LinearLayout mCycleTimeLy;
    private RangeView2 mCycleRange;
    private LinearLayout mParentLayout;
    private View mDivider;
    private TextView mTitle;
    private TextView mSubtitle;
    private TextView mValue;
    private boolean mClosedState;
    private int mRangeCapsuleWidth;
    private int mHeight;
    private int mWidth;


    public RangeViewHolder(View itemView, boolean closedState, int height, int width) {
        super(itemView);

        mClosedState = closedState;
        mHeight = height;
        mWidth = width;

        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mDivider = itemView.findViewById(R.id.divider);
        mTitle = itemView.findViewById(R.id.range_widget_title);
        mSubtitle = itemView.findViewById(R.id.range_widget_subtitle);
        mValue = itemView.findViewById(R.id.range_widget_current_value);
        mCycleRange = itemView.findViewById(R.id.RWC_cycleTime);
        mStandardTv = itemView.findViewById(R.id.RWC_standard_tv);
        mAverageTv = itemView.findViewById(R.id.RWC_average_tv);
        mAverageImg = itemView.findViewById(R.id.RWC_average_img);
        mCycleTimeLy = itemView.findViewById(R.id.RWC_cycleTime_ly);


    }

    public void setRangeItem(Context context, final Widget widget) {

        setSizes(mParentLayout);
        String nameByLang3 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        if (nameByLang3.equals("[CycleTimeEfficiency]")){
            nameByLang3 = PersistenceManager.getInstance().getTranslationForKPIS().getKPIByName("CycleTimeEfficiency");
        }
        mTitle.setText(nameByLang3);
        mSubtitle.setText(new StringBuilder(context.getString(R.string.standard)).append(widget.getStandardValue()));
        mValue.setText(widget.getCurrentValue());
        if (widget.isOutOfRange()) {
            mValue.setTextColor(ContextCompat.getColor(context, R.color.red_line));
        } else {
            mValue.setTextColor(ContextCompat.getColor(context, R.color.C16));
        }

        setCycleTime(context, widget);
    }

    private void setSizes(final LinearLayout parent) {

        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }


    private void setCycleTime(Context context, final Widget widget) {

        try {
            mCycleRange.setCurrentValue(Float.valueOf(widget.getCurrentValue()));
        } catch (NumberFormatException e) {
            mCycleRange.setCurrentValue(0);
        }
        mCycleRange.setHighLimit(widget.getHighLimit());
        mCycleRange.setLowLimit(widget.getLowLimit());
        mCycleRange.setmStandardValue(widget.getStandardValue());

        mCycleTimeLy.setVisibility(View.VISIBLE);
        mCycleRange.setAvgValue(Float.parseFloat(widget.getCycleTimeAvg()));
        mStandardTv.setText(String.format("%s%s", context.getString(R.string.standard), widget.getStandardValue()));
        mAverageTv.setVisibility(View.GONE);
        mAverageImg.setVisibility(View.GONE);
        if (widget.getCycleTimeAvg() != null) {
            try {
                if (Float.parseFloat(widget.getCycleTimeAvg()) > 0) {
                    mAverageTv.setVisibility(View.VISIBLE);
                    mAverageImg.setVisibility(View.VISIBLE);
                }
            } catch (Exception ignored) {

            }
        }
        mAverageTv.setText(String.format("%s%s", context.getString(R.string.average), widget.getCycleTimeAvg()));
        mCycleRange.postInvalidate();
    }

}
