package com.operatorsapp.view.widgetViewHolders;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.application.OperatorApplication;
import com.operatorsapp.utils.WidgetAdapterUtils;

import org.w3c.dom.Text;

import static com.operatorsapp.utils.WidgetAdapterUtils.isNotNearestTextsNew;
import static com.operatorsapp.utils.WidgetAdapterUtils.valueInK;

public class ProjectionViewHolderNew extends RecyclerView.ViewHolder {

    private final LinearLayout mParentLayout;
    private final int mHeight;
    private final int mWidth;
    private final View mProducedView;
    private final View mTheoricalView;
    private final View mProducedCompleteView;
    private final View mTargetRl;
    private final TextView mTargetReachedTv;
    private final View mLegendGoodUnitsView;
    private View mDivider;
    private TextView mTitle;
    private TextView mSubtitle;
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
        mLegendGoodUnitsView = itemView.findViewById(R.id.PWCN_legend_ic);

        setSizes(mParentLayout);
    }

    public void setProjectionItem(final Widget widget) {
//        widget.setCurrentValue(String.valueOf(7050)); //for test
//        widget.setProjection(6350f);
//        widget.setTarget(8000f);//
        mCurrentValue = WidgetAdapterUtils.tryParse(widget.getCurrentValue(), WidgetAdapterUtils.StringParse.FLOAT);
        mTargetRl.post(new Runnable() {
            @Override
            public void run() {
                if (mTargetRl.getContext() != null && mTargetRl.getContext() instanceof Activity && !((Activity) mTargetRl.getContext()).isDestroyed()) {

                    float finalCurrentWidth = 0;
                    float finalProjectionWidth = 0;
                    if (widget.getTarget() != 0) {
                        finalCurrentWidth = mTargetRl.getWidth() * (mCurrentValue / widget.getTarget());
                        finalProjectionWidth = mTargetRl.getWidth() * (widget.getProjection() / widget.getTarget());
                    }
                    initValuesTv(widget);
//                initWidgetHeight();
                    updateColors(widget);

                    if (mCurrentValue < widget.getTarget()) {
                        mProducedCompleteView.setVisibility(View.GONE);
                        mProducedCompleteTv.setVisibility(View.GONE);
                        mTargetReachedTv.setVisibility(View.INVISIBLE);
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
                        if (widget.getFieldName().equals("UnitsProducedOKjob")) {
                            mTargetReachedTv.setText(mTargetReachedTv.getContext().getResources().getString(R.string.you_ve_reached_the_production_target));
                        } else {
                            mTargetReachedTv.setText(mTargetReachedTv.getContext().getResources().getString(R.string.you_ve_reached_the_shift_production_target));
                        }
                    }
                    if (mCurrentValue == 0 && widget.getProjection() == 0) {
                        setEmptyMode();
                    }
                }
            }
        });

    }

    private void setEmptyMode() {
        mTargetReachedTv.setVisibility(View.INVISIBLE);
        updateViewsWidth(0, 0);
    }

    private void updateColors(Widget widget) {
        if (isLowState(widget)) {
            mProducedTv.setTextColor(mProducedTv.getContext().getResources().getColor(R.color.C7));
            mProducedView.setBackgroundColor(mProducedTv.getContext().getResources().getColor(R.color.C7));
            mLegendGoodUnitsView.setBackgroundColor(mLegendGoodUnitsView.getContext().getResources().getColor(R.color.C7));
        } else {
            mProducedTv.setTextColor(mProducedTv.getContext().getResources().getColor(R.color.new_green));
            mProducedView.setBackgroundColor(mProducedTv.getContext().getResources().getColor(R.color.new_green));
            mLegendGoodUnitsView.setBackgroundColor(mLegendGoodUnitsView.getContext().getResources().getColor(R.color.new_green));
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
            } else {
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
                if (mProducedView.getContext() != null && mProducedView.getContext() instanceof Activity && !((Activity) mProducedView.getContext()).isDestroyed()) {

                    ViewGroup.MarginLayoutParams mItemViewParams4;
                    mItemViewParams4 = (ViewGroup.MarginLayoutParams) mProducedView.getLayoutParams();
                    mItemViewParams4.width = (int) finalCurrentWidth;
                    mProducedView.requestLayout();
                }
            }
        });
        mTheoricalView.post(new Runnable() {
            @Override
            public void run() {
                if (mTheoricalView.getContext() != null && mTheoricalView.getContext() instanceof Activity && !((Activity) mTheoricalView.getContext()).isDestroyed()) {

                    ViewGroup.MarginLayoutParams mItemViewParams4;
                    mItemViewParams4 = (ViewGroup.MarginLayoutParams) mTheoricalView.getLayoutParams();
                    mItemViewParams4.width = (int) finalProjectionWidth;
                    mTheoricalView.requestLayout();
                }
            }
        });
    }

    private boolean isLowState(Widget widget) {
        return mCurrentValue < widget.getTarget()
                && mCurrentValue * 100 / widget.getProjection() < 80;
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

    private void setSizes(final LinearLayout parent) {
        ViewGroup.LayoutParams layoutParams;
        layoutParams = parent.getLayoutParams();
        layoutParams.height = (int) (mHeight * 0.5);
        layoutParams.width = (int) (mWidth * 0.325);
        parent.requestLayout();

    }

    private void initWidgetHeight() {
        mDivider.post(new Runnable() {
            @Override
            public void run() {
                if (mDivider.getContext() != null && mDivider.getContext() instanceof Activity && !((Activity) mDivider.getContext()).isDestroyed()) {

                    ViewGroup.MarginLayoutParams mItemViewParams4;
                    mItemViewParams4 = (ViewGroup.MarginLayoutParams) mDivider.getLayoutParams();
                    mItemViewParams4.setMargins(0, (int) (mParentLayout.getHeight() * 0.3), 0, 0);
                    mDivider.requestLayout();
                }
            }
        });
    }
}
