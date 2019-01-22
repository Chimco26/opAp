package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.utils.WidgetAdapterUtils;
import com.operatorsapp.view.RangeView;

import me.grantland.widget.AutofitTextView;

public class RangeViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout mParentLayout;
    private View mDivider;
    private AutofitTextView mTitle;
    private AutofitTextView mSubtitle;
    private TextView mValue;
    private View mCapsule;
    private RangeView mRangeViewBlue;
    private TextView mCurrentValue;
    private RangeView mRangeViewRed;
    private ImageView mRedMark;
    private TextView mMin;
    private TextView mStandard;
    private TextView mMax;
    private Context mContext;
    private boolean mClosedState;
    private int mRangeCapsuleWidth;
    private int mHeight;
    private int mWidth;


    public RangeViewHolder(View itemView, Context context, boolean closedState, int height, int width) {
        super(itemView);

        mContext = context;
        mClosedState = closedState;
        mHeight = height;
        mWidth = width;

        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mDivider = itemView.findViewById(R.id.divider);
        mTitle = itemView.findViewById(R.id.range_widget_title);
        mSubtitle = itemView.findViewById(R.id.range_widget_subtitle);
        mValue = itemView.findViewById(R.id.range_widget_current_value);
        mCapsule = itemView.findViewById(R.id.range_widget_oval);
        mRangeViewBlue = itemView.findViewById(R.id.range_widget_range_view_blue);
        mCurrentValue = itemView.findViewById(R.id.range_widget_current_value_in_chart);
        mRangeViewRed = itemView.findViewById(R.id.range_widget_range_view_red);
        mRedMark = itemView.findViewById(R.id.range_widget_red_mark);
        mMin = itemView.findViewById(R.id.range_widget_min);
        mStandard = itemView.findViewById(R.id.range_widget_standard);
        mMax = itemView.findViewById(R.id.range_widget_max);

    }

    public void setRangeItem(final Widget widget) {
        mDivider.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams3;
                mItemViewParams3 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
                mItemViewParams3.setMargins(0, (int) (mParentLayout.getHeight() * 0.4), 0, 0);
                mDivider.requestLayout();
            }
        });

        setSizes(mParentLayout);
        String nameByLang3 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang3);
        mSubtitle.setText(new StringBuilder(mContext.getString(R.string.standard)).append(widget.getStandardValue()));
        mValue.setText(widget.getCurrentValue());
        mCurrentValue.setText(widget.getCurrentValue());
        if (widget.isOutOfRange()) {
            mValue.setTextColor(ContextCompat.getColor(mContext, R.color.red_line));
        } else {
            mValue.setTextColor(ContextCompat.getColor(mContext, R.color.C16));
        }

        mRangeViewRed.setCurrentLine(true);
        mRangeViewBlue.setCurrentLine(false);
//                    if (mRangeCapsuleWidth == 0) {
        mCapsule.post(new Runnable() {
            @Override
            public void run() {
                mRangeCapsuleWidth = mCapsule.getWidth();
                setRangeData(widget);
            }
        });
//                    } else {
//                        mRangeCapsuleWidth = mCapsule.getWidth();
//                        setRangeData(widget, rangeViewHolder);
//                    }
        mMin.setText(String.valueOf(widget.getLowLimit()));
        mStandard.setText(String.valueOf(widget.getStandardValue()));
        mMax.setText(String.valueOf(widget.getHighLimit()));
    }

    private void setRangeData(Widget widget) {
        float currentValue = WidgetAdapterUtils.tryParse(widget.getCurrentValue(), WidgetAdapterUtils.StringParse.FLOAT);
        if (widget.isOutOfRange() && currentValue > widget.getHighLimit()) {
            mRangeViewBlue.setVisibility(View.INVISIBLE);
            mCurrentValue.setVisibility(View.INVISIBLE);
            mRangeViewRed.setVisibility(View.VISIBLE);
            mRedMark.setVisibility(View.VISIBLE);
            if (mClosedState) {
                mRangeViewRed.updateX((float) (mRangeCapsuleWidth * 0.89)/*max location*/);
            }  //todo

            mRedMark.setX(mRangeViewRed.getX());
        } else if (widget.isOutOfRange() && currentValue < widget.getLowLimit()) {
            mRangeViewBlue.setVisibility(View.INVISIBLE);
            mCurrentValue.setVisibility(View.INVISIBLE);
            mRangeViewRed.setVisibility(View.VISIBLE);
            mRedMark.setVisibility(View.VISIBLE);
            if (mClosedState) {
                mRangeViewRed.updateX((float) (mRangeCapsuleWidth * 0.001)/*min location*/);
            }  //todo

            mRedMark.setX(mRangeViewRed.getX());
        } else {
            mRangeViewRed.setVisibility(View.INVISIBLE);
            mRedMark.setVisibility(View.INVISIBLE);
            mRangeViewBlue.setVisibility(View.VISIBLE);
            mCurrentValue.setVisibility(View.VISIBLE);
            if (widget.getHighLimit() > widget.getLowLimit()) {
                float scaleValue = (widget.getHighLimit() - widget.getLowLimit());
                float currentFloatValue = currentValue - widget.getLowLimit();
                final float convertCurrentValue = currentFloatValue / scaleValue;
                if (convertCurrentValue > 0.5) {
                    mRangeViewBlue.updateX((mRangeViewBlue.getWidth() * convertCurrentValue - 7)/* half of the line*/);
                    mCurrentValue.setX(mRangeViewBlue.getX() - 7);
                } else {
                    mRangeViewBlue.updateX((mRangeViewBlue.getWidth() * convertCurrentValue)/* half of the line*/);
                    mCurrentValue.setX(mRangeViewBlue.getX());
                }
            }
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