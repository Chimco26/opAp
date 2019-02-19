package com.operatorsapp.view.widgetViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.utils.WidgetAdapterUtils;

import me.grantland.widget.AutofitTextView;

import static com.operatorsapp.utils.WidgetAdapterUtils.isNotNearestTextsNew;
import static com.operatorsapp.utils.WidgetAdapterUtils.valueInK;

public class ProjectionViewHolderNew extends RecyclerView.ViewHolder {

    private final RelativeLayout mParentLayout;
    private final int mHeight;
    private final int mWidth;
    private final View mProducedView;
    private final View mTheoricalView;
    private final View mProducedCompleteView;
    private final View mTargetRl;
    private final View mTargetReachedTv;
    private View mDivider;
    private AutofitTextView mTitle;
    private AutofitTextView mSubtitle;
    private TextView mValue;
    private final TextView mProducedTv;
    private final TextView mTheoricalTv;
    private final TextView mtargetTv;
    private final TextView mProducedCompleteTv;
    private float mCurrentValue;

    public ProjectionViewHolderNew(View itemView, int height, int width) {
        super(itemView);
        mHeight = height;
        mWidth = width;

        mParentLayout = itemView.findViewById(R.id.widget_parent_layout);
        mDivider = itemView.findViewById(R.id.divider);
        mTitle = itemView.findViewById(R.id.projection_widget_title);
        mSubtitle = itemView.findViewById(R.id.projection_widget_subtitle);
        mValue = itemView.findViewById(R.id.projection_widget_current_value);

        mProducedTv = itemView.findViewById(R.id.FCNW_produced_tv);
        mTheoricalTv = itemView.findViewById(R.id.FCNW_theorical_tv);
        mtargetTv = itemView.findViewById(R.id.PWCN_target_value_tv);
        mProducedCompleteTv = itemView.findViewById(R.id.FCNW_unit_produced_completed);
        mTargetReachedTv = itemView.findViewById(R.id.FWCN_target_reached_alert_tv);

        mProducedView = itemView.findViewById(R.id.PWCN_produced_view);
        mTheoricalView = itemView.findViewById(R.id.PWCN_theorical_view);
        mProducedCompleteView = itemView.findViewById(R.id.PWCN_completion_view);
        mTargetRl = itemView.findViewById(R.id.PWCN_target_rl);

        setSizes(mParentLayout);
    }

    public void setProjectionItem(final Widget widget) {
//        widget.setCurrentValue(String.valueOf(2050)); //for test
//        widget.setProjection(2350f);
//        widget.setTarget(8000f);
        mCurrentValue = WidgetAdapterUtils.tryParse(widget.getCurrentValue(), WidgetAdapterUtils.StringParse.FLOAT);
        mTargetRl.post(new Runnable() {
            @Override
            public void run() {
                final float finalCurrentWidth = mTargetRl.getWidth() * (mCurrentValue / widget.getTarget());
                final float finalProjectionWidth = mTargetRl.getWidth() * (widget.getProjection() / widget.getTarget());

                initValuesTv(widget);
                initWidgetHeight();

                if (mCurrentValue < widget.getTarget()) {
                    updateColors(widget);
                    mProducedCompleteView.setVisibility(View.GONE);
                    mProducedCompleteTv.setVisibility(View.GONE);
                    mTargetReachedTv.setVisibility(View.GONE);
                    showProducedValue();
                    showTheoricalValue(widget);
                    updateViewsWidth(finalCurrentWidth, finalProjectionWidth);
                } else {
                    updateViewsWidth(mTargetRl.getWidth(), 0);
                    mProducedView.setBackgroundColor(mProducedTv.getContext().getResources().getColor(R.color.new_green));
                    mProducedCompleteView.setVisibility(View.VISIBLE);
                    mProducedCompleteTv.setVisibility(View.VISIBLE);
                    mTargetReachedTv.setVisibility(View.VISIBLE);
                    mProducedTv.setVisibility(View.GONE);
                    mTheoricalTv.setVisibility(View.GONE);
                    mTheoricalView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateColors(Widget widget) {
        if (isLowState(widget)) {
            mProducedTv.setTextColor(mProducedTv.getContext().getResources().getColor(R.color.C7));
            mProducedView.setBackgroundColor(mProducedTv.getContext().getResources().getColor(R.color.C7));
        } else {
            mProducedTv.setTextColor(mProducedTv.getContext().getResources().getColor(R.color.new_green));
            mProducedView.setBackgroundColor(mProducedTv.getContext().getResources().getColor(R.color.new_green));
        }
    }

    private void showProducedValue() {
        if (mCurrentValue != 0) {
            mProducedTv.setVisibility(View.VISIBLE);
        } else {
            mProducedTv.setVisibility(View.GONE);
        }
    }

    private void showTheoricalValue(Widget widget) {
        if (widget.getProjection() <= widget.getTarget()) {
            mTheoricalView.setVisibility(View.VISIBLE);
            if (isNotNearestTextsNew(widget, mCurrentValue)) {
                mTheoricalTv.setVisibility(View.VISIBLE);
            }else {
                mTheoricalTv.setVisibility(View.GONE);
            }
        } else {
            mTheoricalTv.setVisibility(View.GONE);
            mTheoricalView.setVisibility(View.GONE);
        }
    }

    private void updateViewsWidth(final float finalCurrentWidth, final float finalProjectionWidth) {
        mProducedView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams4;
                mItemViewParams4 = (ViewGroup.MarginLayoutParams) mProducedView.getLayoutParams();
                mItemViewParams4.width = (int) finalCurrentWidth;
                mProducedView.requestLayout();
            }
        });
        mTheoricalView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams4;
                mItemViewParams4 = (ViewGroup.MarginLayoutParams) mTheoricalView.getLayoutParams();
                mItemViewParams4.width = (int) finalProjectionWidth;
                mTheoricalView.requestLayout();
            }
        });
    }

    private boolean isLowState(Widget widget) {
        return mCurrentValue < widget.getTarget()
                && mCurrentValue * 100 / widget.getProjection() < 80;
    }

    private void initWidgetHeight() {
        mDivider.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.MarginLayoutParams mItemViewParams4;
                mItemViewParams4 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
                mItemViewParams4.setMargins(0, (int) (mParentLayout.getHeight() * 0.3), 0, 0);
                mDivider.requestLayout();
            }
        });
    }

    private void initValuesTv(Widget widget) {
        String nameByLang4 = OperatorApplication.isEnglishLang() ? widget.getFieldEName() : widget.getFieldLName();
        mTitle.setText(nameByLang4);
        mSubtitle.setText(new StringBuilder(mSubtitle.getContext().getString(R.string.total_required)).append(valueInK(widget.getTarget())));
        mValue.setText(valueInK(mCurrentValue));

        mProducedTv.setText(valueInK(mCurrentValue));
        mTheoricalTv.setText(valueInK(widget.getProjection()));
        mtargetTv.setText(valueInK(widget.getTarget()));
        mProducedCompleteTv.setText(valueInK(mCurrentValue));
    }

    private void setSizes(final RelativeLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.45);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }
}
