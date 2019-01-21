package com.operatorsapp.view.widgetViewHolders;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.utils.WidgetAdapterUtils;

import me.grantland.widget.AutofitTextView;

import static com.operatorsapp.utils.WidgetAdapterUtils.isNotNearestTexts;
import static com.operatorsapp.utils.WidgetAdapterUtils.valueInK;

public class ProjectionViewHolder extends RecyclerView.ViewHolder {

    private final View mRangeViewRv;
    private final ProjectionDrawablesHelper mProjectionDrawableHelper;
    private RelativeLayout mParentLayout;
    private View mDivider;
    private AutofitTextView mTitle;
    private AutofitTextView mSubtitle;
    private TextView mValue;
    private View mCapsule;
    private View mEndDivider;
    private View mProjectionView;
    private View mProjectionViewProjection;
    private View mRangeView;
    private View mProjectionViewStart;
    private View mProjectionViewEnd;
    private TextView mCurrentValueInChart;
    private TextView mGrayValueInChart;
    private TextView mGrayValueInEndChart;
    private LinearLayout mBluePlus;
    private TextView mMin;
    private TextView mMax;
    private Context mContext;
    private int mHeight;
    private int mWidth;
    private int mProjectionCapsuleWidth;

    public ProjectionViewHolder(View itemView, Context context, int height, int width) {
        super(itemView);

        mProjectionDrawableHelper = new ProjectionDrawablesHelper();
        mContext = context;
        mHeight = height;
        mWidth = width;

        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mDivider = itemView.findViewById(R.id.divider);
        mTitle = itemView.findViewById(R.id.projection_widget_title);
        mSubtitle = itemView.findViewById(R.id.projection_widget_subtitle);
        mValue = itemView.findViewById(R.id.projection_widget_current_value);
        mCapsule = itemView.findViewById(R.id.projection_widget_oval);
        mEndDivider = itemView.findViewById(R.id.projection_widget_end_divider);
        mProjectionView = itemView.findViewById(R.id.projection_widget_projectionView);
        mProjectionViewProjection = itemView.findViewById(R.id.projection_widget_projectionViewProjection);
        mRangeView = itemView.findViewById(R.id.projection_widget_range_view);
        mRangeViewRv = itemView.findViewById(R.id.projection_widget_range_view_rv);
        mProjectionViewStart = itemView.findViewById(R.id.projection_widget_projectionView_start);
        mProjectionViewEnd = itemView.findViewById(R.id.projection_widget_projectionView_end);
        mCurrentValueInChart = itemView.findViewById(R.id.projection_widget_current_value_in_chart);
        mGrayValueInChart = itemView.findViewById(R.id.projection_widget_gray_value_in_chart);
        mGrayValueInEndChart = itemView.findViewById(R.id.projection_widget_gray_value_in_end_chart);
        mBluePlus = itemView.findViewById(R.id.projection_widget_blue_plus);
        mMin = itemView.findViewById(R.id.projection_widget_min);
        mMax = itemView.findViewById(R.id.projection_widget_max);
    }

    public void setProjectionItem(final Widget widget) {
        mDivider.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams4;
                mItemViewParams4 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
                mItemViewParams4.setMargins(0, (int) (mParentLayout.getHeight() * 0.4), 0, 0);
                mDivider.requestLayout();
            }
        });
        mCapsule.setBackground(mProjectionDrawableHelper.createCapsuleDrawable(widget.getTargetColor()));
        mRangeView.setBackground(mProjectionDrawableHelper.createRangeShape(widget.getCurrentColor()));
        mProjectionView.setBackground(mProjectionDrawableHelper.createProjectionShape(widget.getCurrentColor()));
        mProjectionViewProjection.setBackground(mProjectionDrawableHelper.createProjectionShape(widget.getProjectionColor()));
        mProjectionViewEnd.setBackground(mProjectionDrawableHelper.createEndProjectionShape(widget.getProjectionColor(), mContext));
        mProjectionViewStart.setBackground(mProjectionDrawableHelper.createStartProjectionShape(widget.getCurrentColor(), mContext));
        setSizes(mParentLayout);
//                    mRangeView.setCurrentLine(false);
//                    mRangeView.setLineColor(mContext, "#000000");
        float currentFloat = WidgetAdapterUtils.tryParse(widget.getCurrentValue(), WidgetAdapterUtils.StringParse.FLOAT);
        mCurrentValueInChart.setText(valueInK(currentFloat));
        if (currentFloat >= widget.getTarget()) {
            mBluePlus.setVisibility(View.VISIBLE);
            mProjectionViewEnd.setBackground(mProjectionDrawableHelper.createEndProjectionShape(widget.getCurrentColor(), mContext));
            mProjectionViewStart.setBackground(mProjectionDrawableHelper.createStartProjectionShape(widget.getCurrentColor(), mContext));
//                    mCurrentValueInChart.setText("");
            mRangeView.setVisibility(View.GONE);
        } else if (widget.getProjection() >= widget.getTarget()) {
            mBluePlus.setVisibility(View.GONE);
            mProjectionViewEnd.setBackground(mProjectionDrawableHelper.createEndProjectionShape(widget.getProjectionColor(), mContext));
            mProjectionViewStart.setBackground(mProjectionDrawableHelper.createStartProjectionShape(widget.getCurrentColor(), mContext));
            if (isNotNearestTexts(widget)) {
                mGrayValueInEndChart.setText(valueInK(widget.getProjection()));
            } else {
                mGrayValueInEndChart.setText("");
            }
        } else {
            mBluePlus.setVisibility(View.GONE);
            mProjectionViewEnd.setBackground(mProjectionDrawableHelper.createEndProjectionShape(widget.getProjectionColor(), mContext));
            mProjectionViewStart.setBackground(mProjectionDrawableHelper.createStartProjectionShape(widget.getCurrentColor(), mContext));
            mProjectionViewEnd.setVisibility(View.GONE);
            if (isNotNearestTexts(widget)) {
                mGrayValueInChart.setText(valueInK(widget.getProjection()));
            } else {
                mGrayValueInChart.setText("");
            }
        }
        if (currentFloat <= widget.getLowLimit() && currentFloat != widget.getTarget()) {
            mProjectionViewEnd.setBackground(mProjectionDrawableHelper.createEndProjectionShape(widget.getProjectionColor(), mContext));
            mRangeView.setVisibility(View.GONE);
            mProjectionView.setVisibility(View.GONE);
            mProjectionViewProjection.setVisibility(View.VISIBLE);
            mProjectionViewStart.setBackground(mProjectionDrawableHelper.createStartProjectionShape(widget.getProjectionColor(), mContext));
            mGrayValueInEndChart.setText("");
            mGrayValueInChart.setText("");
            mCurrentValueInChart.setText("");
        }

        final float finalCurrentFloat = currentFloat;
//                    if (mProjectionCapsuleWidth == 0) {
        mCapsule.post(new Runnable() {
            @Override
            public void run() {
                mProjectionCapsuleWidth = mRangeViewRv.getWidth();
                setProjectionData(widget, finalCurrentFloat);
            }
        });
//                    } else {
//                        mProjectionCapsuleWidth = mRangeView.getWidth();
//                        setProjectionData(projectionViewHolder, widget, finalCurrentFloat);
//                    }
        mMin.setText(valueInK(widget.getLowLimit()));
        //                mStandard.setText(valueInK((int) widget.getStandardValue()));
        mMax.setText(valueInK(widget.getTarget()));

        if (widget.getTarget() == 0 && currentFloat > 0) {
            mCurrentValueInChart.setText("");
        }
    }

    private void setProjectionData(Widget widget, float finalCurrentFloat) {
        String nameByLang4 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang4);
        mSubtitle.setText(new StringBuilder(mContext.getString(R.string.total_required)).append(valueInK(widget.getTarget())));
        mValue.setText(valueInK(finalCurrentFloat));
        if (widget.getTarget() >= widget.getLowLimit()) {
            float scaleValue = (widget.getTarget() - widget.getLowLimit());
            float currentValue = finalCurrentFloat - widget.getLowLimit();
            float projectionValue = (widget.getProjection() - widget.getLowLimit());
            float convertCurrentValue = 1;
            float convertProjectionValue = 1;
            if (scaleValue != 0) {
                convertCurrentValue = currentValue / scaleValue;
                convertProjectionValue = projectionValue / scaleValue;
            }
            float currentWidth = mProjectionCapsuleWidth * convertCurrentValue;
            float projectionWidth = mProjectionCapsuleWidth * convertProjectionValue;
            if (currentWidth > 1000) {
                currentWidth = 1000;
            }
            if (projectionWidth > 1000) {
                projectionWidth = 1000;
            }
            mRangeView.setX(currentWidth /* half of the line*/);
            mCurrentValueInChart.setX(currentWidth + 5/* half of the line*/);
            mGrayValueInChart.setX(mProjectionCapsuleWidth * convertProjectionValue + 5/* half of the line*/);
//
//            if (convertCurrentValue > 0.9) {
//                mCurrentValueInChart.setX(currentWidth - 10/* half of the line*/);
//                mGrayValueInChart.setX(mProjectionCapsuleWidth * convertProjectionValue - 10/* half of the line*/);
//            } else {
//                mCurrentValueInChart.setX(currentWidth - 2/* half of the line*/);
//                mGrayValueInChart.setX(mProjectionCapsuleWidth * convertProjectionValue - 2/* half of the line*/);
//            }
//            mProjectionView.getLayoutParams().width = ((int) currentWidth);
//            mProjectionViewProjection.getLayoutParams().width = ((int) projectionWidth);
            final float finalCurrentWidth = currentWidth;
            mProjectionView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.MarginLayoutParams mItemViewParams4;
                    mItemViewParams4 = (ViewGroup.MarginLayoutParams) mProjectionView.getLayoutParams();
                    mItemViewParams4.width = (int) finalCurrentWidth;
                    mProjectionView.requestLayout();
                }
            });
            final float finalProjectionWidth = projectionWidth;
            mProjectionViewProjection.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.MarginLayoutParams mItemViewParams4;
                    mItemViewParams4 = (ViewGroup.MarginLayoutParams) mProjectionViewProjection.getLayoutParams();
                    mItemViewParams4.width = (int) finalProjectionWidth;
                    mProjectionViewProjection.requestLayout();
                }
            });
        }
        mEndDivider.setBackgroundColor(Color.parseColor(widget.getProjectionColor()));
        if (mEndDivider.getX() - mRangeView.getX() < 150 && mBluePlus.getVisibility() != View.VISIBLE && finalCurrentFloat != 0) {
            mEndDivider.setVisibility(View.INVISIBLE);
        } else {
            mEndDivider.setVisibility(View.VISIBLE);
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