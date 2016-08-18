package com.operatorsapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.fragments.ChartFragment;
import com.operatorsapp.view.LineChartTimeSmall;
import com.operatorsapp.view.ProjectionView;
import com.operatorsapp.view.ProjectionViewStart;
import com.operatorsapp.view.ProjectionViewEnd;
import com.operatorsapp.view.RangeView;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;

public class WidgetAdapter extends RecyclerView.Adapter {
    private static final long FOUR_HOURS = 60000L * 60 * 4;
    private static final long TEN_HOURS = 60000L * 60 * 10;
    private Context mContext;
    private List<Widget> mWidgets;
    private final int NUMERIC = 0;
    private final int RANGE = 1;
    private final int PROJECTION = 2;
    private final int TIME = 3;
    private GoToScreenListener mGoToScreenListener;

    public WidgetAdapter(Context context, List<Widget> widgets, GoToScreenListener goToScreenListener) {
        mWidgets = widgets;
        mContext = context;
        mGoToScreenListener = goToScreenListener;
    }

    public void setNewData(List<Widget> widgets) {
        mWidgets = widgets;
        notifyDataSetChanged();
    }

    private class NumericViewHolder extends RecyclerView.ViewHolder {

        private AutofitTextView mTitle;
        private AutofitTextView mSubtitle;
        private TextView mValue;
        private TextView mChangeMaterial;

        public NumericViewHolder(View itemView) {
            super(itemView);

            mTitle = (AutofitTextView) itemView.findViewById(R.id.numeric_widget_title);
            mSubtitle = (AutofitTextView) itemView.findViewById(R.id.numeric_widget_subtitle);
            mValue = (TextView) itemView.findViewById(R.id.numeric_widget_value);
            mChangeMaterial = (TextView) itemView.findViewById(R.id.numeric_widget_change_material);

        }
    }

    private class RangeViewHolder extends RecyclerView.ViewHolder {

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

        public RangeViewHolder(View itemView) {
            super(itemView);

            mTitle = (AutofitTextView) itemView.findViewById(R.id.range_widget_title);
            mSubtitle = (AutofitTextView) itemView.findViewById(R.id.range_widget_subtitle);
            mValue = (TextView) itemView.findViewById(R.id.range_widget_current_value);
            mCapsule = itemView.findViewById(R.id.range_widget_oval);
            mRangeViewBlue = (RangeView) itemView.findViewById(R.id.range_widget_range_view_blue);
            mCurrentValue = (TextView) itemView.findViewById(R.id.range_widget_current_value_in_chart);
            mRangeViewRed = (RangeView) itemView.findViewById(R.id.range_widget_range_view_red);
            mRedMark = (ImageView) itemView.findViewById(R.id.range_widget_red_mark);
            mMin = (TextView) itemView.findViewById(R.id.range_widget_min);
            mStandard = (TextView) itemView.findViewById(R.id.range_widget_standard);
            mMax = (TextView) itemView.findViewById(R.id.range_widget_max);

        }
    }

    private class ProjectionViewHolder extends RecyclerView.ViewHolder {

        private AutofitTextView mTitle;
        private AutofitTextView mSubtitle;
        private TextView mValue;
        private View mCapsule;
        private View mEndDivider;
        private ProjectionView mProjectionView;
        private RangeView mRangeView;
        private ProjectionViewStart mProjectionViewStart;
        private ProjectionViewEnd mProjectionViewEnd;
        private TextView mCurrentValueInChart;
        private TextView mGrayValueInChart;
        private TextView mGrayValueInEndChart;
        private LinearLayout mBluePlus;
        private TextView mMin;
        //        private TextView mStandard;
        private TextView mMax;

        public ProjectionViewHolder(View itemView) {
            super(itemView);

            mTitle = (AutofitTextView) itemView.findViewById(R.id.projection_widget_title);
            mSubtitle = (AutofitTextView) itemView.findViewById(R.id.projection_widget_subtitle);
            mValue = (TextView) itemView.findViewById(R.id.projection_widget_current_value);
            mCapsule = itemView.findViewById(R.id.projection_widget_oval);
            mEndDivider = itemView.findViewById(R.id.projection_widget_end_divider);
            mProjectionView = (ProjectionView) itemView.findViewById(R.id.projection_widget_projectionView);
            mRangeView = (RangeView) itemView.findViewById(R.id.projection_widget_range_view);
            mProjectionViewStart = (ProjectionViewStart) itemView.findViewById(R.id.projection_widget_projectionView_start);
            mProjectionViewEnd = (ProjectionViewEnd) itemView.findViewById(R.id.projection_widget_projectionView_end);
            mCurrentValueInChart = (TextView) itemView.findViewById(R.id.projection_widget_current_value_in_chart);
            mGrayValueInChart = (TextView) itemView.findViewById(R.id.projection_widget_gray_value_in_chart);
            mGrayValueInEndChart = (TextView) itemView.findViewById(R.id.projection_widget_gray_value_in_end_chart);
            mBluePlus = (LinearLayout) itemView.findViewById(R.id.projection_widget_blue_plus);
            mMin = (TextView) itemView.findViewById(R.id.projection_widget_min);
//            mStandard = (TextView) itemView.findViewById(R.id.projection_widget_standard);
            mMax = (TextView) itemView.findViewById(R.id.projection_widget_max);
        }
    }

    private class TimeViewHolder extends RecyclerView.ViewHolder {

        private LineChartTimeSmall mChart;
        private AutofitTextView mTitle;
        private AutofitTextView mSubtitle;
        private TextView mValue;

        public TimeViewHolder(View itemView) {
            super(itemView);

            mTitle = (AutofitTextView) itemView.findViewById(R.id.time_widget_title);
            mSubtitle = (AutofitTextView) itemView.findViewById(R.id.time_widget_subtitle);
            mValue = (TextView) itemView.findViewById(R.id.time_widget_current_value);
            mChart = (LineChartTimeSmall) itemView.findViewById(R.id.lineChart_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case NUMERIC: {
                return new NumericViewHolder(inflater.inflate(R.layout.numeric_widget_cardview, parent, false));
            }
            case RANGE: {
                return new RangeViewHolder(inflater.inflate(R.layout.range_widget_cardview, parent, false));
            }
            case PROJECTION: {
                return new ProjectionViewHolder(inflater.inflate(R.layout.projection_widget_cardview, parent, false));
            }
            case TIME: {
                return new TimeViewHolder(inflater.inflate(R.layout.time_widget_cardview, parent, false));
            }
        }
        return new NumericViewHolder(inflater.inflate(R.layout.numeric_widget_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        final Widget widget = mWidgets.get(position);
        switch (type) {
            case NUMERIC:
                final NumericViewHolder numericViewHolder = (NumericViewHolder) holder;
                numericViewHolder.mTitle.setText(widget.getFieldName());
//                numericViewHolder.mSubtitle.setText();
                numericViewHolder.mValue.setText(widget.getCurrentValue());

                break;
            case TIME:
                final TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
                timeViewHolder.mTitle.setText(widget.getFieldName());
                timeViewHolder.mSubtitle.setText(new StringBuilder("Standard " + (int) widget.getStandardValue()));
                timeViewHolder.mValue.setText(widget.getCurrentValue());
                final ArrayList<Entry> tenHoursValues = new ArrayList<>();
                final ArrayList<Entry> fourHoursValues = new ArrayList<>();
                if (widget.getMachineParamHistoricData() != null && widget.getMachineParamHistoricData().size() > 0) {

                    for (int i = 0; i < widget.getMachineParamHistoricData().size(); i++) {
                        Entry entry = new Entry();
                        entry.setX(widget.getMachineParamHistoricData().get(i).getTime());
                        entry.setY(widget.getMachineParamHistoricData().get(i).getValue());
//                        if (widget.getMachineParamHistoricData().get(i).getTime() > (System.currentTimeMillis() - TEN_HOURS)) {
                        tenHoursValues.add(entry);
//                        }
//                        if (widget.getMachineParamHistoricData().get(i).getTime() > (System.currentTimeMillis() - FOUR_HOURS)) {
                        fourHoursValues.add(entry);
//                        }
                    }
                    timeViewHolder.mChart.setData(fourHoursValues);
                }
                timeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tenHoursValues.size() > 0) {
                            mGoToScreenListener.goToFragment(ChartFragment.newInstance(mContext, tenHoursValues, widget.getLowLimit(), widget.getStandardValue(), widget.getHighLimit()), true);
                        }
                    }
                });
                break;

            case RANGE:
                final RangeViewHolder rangeViewHolder = (RangeViewHolder) holder;
                rangeViewHolder.mTitle.setText(widget.getFieldName());
                rangeViewHolder.mSubtitle.setText(new StringBuilder("Standard " + (int) widget.getStandardValue()));
                rangeViewHolder.mValue.setText(widget.getCurrentValue());
                rangeViewHolder.mCurrentValue.setText(widget.getCurrentValue());
                if (widget.isOutOfRange()) {
                    rangeViewHolder.mValue.setTextColor(ContextCompat.getColor(mContext, R.color.red_line));
                } else {
                    rangeViewHolder.mValue.setTextColor(ContextCompat.getColor(mContext, R.color.C16));
                }

                rangeViewHolder.mRangeViewRed.setCurrentLine(true);
                rangeViewHolder.mRangeViewBlue.setCurrentLine(false);
                rangeViewHolder.mCapsule.post(new Runnable() {
                    @Override
                    public void run() {
                        if (widget.isOutOfRange() && Integer.parseInt(widget.getCurrentValue()) > widget.getHighLimit()) {
                            rangeViewHolder.mRangeViewBlue.setVisibility(View.GONE);
                            rangeViewHolder.mCurrentValue.setVisibility(View.GONE);
                            rangeViewHolder.mRangeViewRed.setVisibility(View.VISIBLE);
                            rangeViewHolder.mRedMark.setVisibility(View.VISIBLE);
                            rangeViewHolder.mRangeViewRed.updateX((float) (rangeViewHolder.mRangeViewRed.getWidth() * 0.91)/*max location*/);
                            rangeViewHolder.mRedMark.setX(rangeViewHolder.mRangeViewRed.getX());
                        } else if (widget.isOutOfRange() && Integer.parseInt(widget.getCurrentValue()) < widget.getLowLimit()) {
                            rangeViewHolder.mRangeViewBlue.setVisibility(View.GONE);
                            rangeViewHolder.mCurrentValue.setVisibility(View.GONE);
                            rangeViewHolder.mRangeViewRed.setVisibility(View.VISIBLE);
                            rangeViewHolder.mRedMark.setVisibility(View.VISIBLE);
                            rangeViewHolder.mRangeViewRed.updateX((float) (rangeViewHolder.mRangeViewRed.getWidth() * 0.05)/*min location*/);
                            rangeViewHolder.mRedMark.setX(rangeViewHolder.mRangeViewRed.getX());
                        } else {
                            rangeViewHolder.mRangeViewRed.setVisibility(View.GONE);
                            rangeViewHolder.mRedMark.setVisibility(View.GONE);
                            rangeViewHolder.mRangeViewBlue.setVisibility(View.VISIBLE);
                            rangeViewHolder.mCurrentValue.setVisibility(View.VISIBLE);
                            float scaleValue = (widget.getHighLimit() - widget.getLowLimit());
                            float currentValue = (Integer.parseInt(widget.getCurrentValue()) - widget.getLowLimit());
                            if (scaleValue > 0) {
                                final float convertCurrentValue = currentValue / scaleValue;
                                rangeViewHolder.mRangeViewBlue.updateX(((rangeViewHolder.mRangeViewBlue.getWidth()) * convertCurrentValue) - 5/* half of the line*/);
                                rangeViewHolder.mCurrentValue.setX(rangeViewHolder.mRangeViewBlue.getX());
                            }
                        }
                    }
                });
                rangeViewHolder.mMin.setText(String.valueOf((int) widget.getLowLimit()));
                rangeViewHolder.mStandard.setText(String.valueOf((int) widget.getStandardValue()));
                rangeViewHolder.mMax.setText(String.valueOf((int) widget.getHighLimit()));
                break;

            case PROJECTION:
                final ProjectionViewHolder projectionViewHolder = (ProjectionViewHolder) holder;
                projectionViewHolder.mRangeView.setCurrentLine(false);
                projectionViewHolder.mCurrentValueInChart.setText(valueInK(Integer.parseInt(widget.getCurrentValue())));
                if (Integer.parseInt(widget.getCurrentValue()) >= widget.getTarget()) {
                    projectionViewHolder.mBluePlus.setVisibility(View.VISIBLE);
                    projectionViewHolder.mProjectionViewEnd.setCurrentView(true);
                    projectionViewHolder.mCurrentValueInChart.setText("");
                    projectionViewHolder.mRangeView.hideView();
                } else if (widget.getProjection() >= widget.getTarget()) {
                    projectionViewHolder.mBluePlus.setVisibility(View.GONE);
                    projectionViewHolder.mProjectionViewEnd.setCurrentView(false);
                    projectionViewHolder.mGrayValueInEndChart.setText(valueInK(widget.getProjection()));
                } else {
                    projectionViewHolder.mBluePlus.setVisibility(View.GONE);
                    projectionViewHolder.mProjectionViewEnd.hideView();
                    projectionViewHolder.mGrayValueInChart.setText(valueInK(widget.getProjection()));
                }
                if (Integer.parseInt(widget.getCurrentValue()) <= widget.getLowLimit()) {
                    projectionViewHolder.mRangeView.hideView();
                    projectionViewHolder.mProjectionView.hideViews();
                    projectionViewHolder.mProjectionViewStart.hideView();
                    projectionViewHolder.mGrayValueInEndChart.setText("");
                    projectionViewHolder.mGrayValueInChart.setText("");
                    projectionViewHolder.mCurrentValueInChart.setText("");
                }
                projectionViewHolder.mCapsule.post(new Runnable() {
                    @Override
                    public void run() {
                        projectionViewHolder.mTitle.setText(widget.getFieldName());
                        projectionViewHolder.mSubtitle.setText(new StringBuilder("Standard " + valueInK((int) widget.getStandardValue())));
                        projectionViewHolder.mValue.setText(valueInK(Integer.parseInt(widget.getCurrentValue())));
                        float scaleValue = (widget.getTarget() - widget.getLowLimit());
                        float currentValue = (Integer.parseInt(widget.getCurrentValue()) - widget.getLowLimit());
                        float projectionValue = (widget.getProjection() - widget.getLowLimit());
                        if (scaleValue > 0) {
                            final float convertCurrentValue = currentValue / scaleValue;
                            final float convertProjectionValue = projectionValue / scaleValue;
                            projectionViewHolder.mRangeView.updateX(projectionViewHolder.mProjectionView.getWidth() * convertCurrentValue - 5/* half of the line*/);
                            projectionViewHolder.mProjectionView.updateWidth((projectionViewHolder.mProjectionView.getWidth() * convertCurrentValue), (projectionViewHolder.mProjectionView.getWidth() * convertProjectionValue));
                            projectionViewHolder.mCurrentValueInChart.setX(projectionViewHolder.mProjectionView.getWidth() * convertCurrentValue - 5/* half of the line*/);
                            projectionViewHolder.mGrayValueInChart.setX(projectionViewHolder.mProjectionView.getWidth() * convertProjectionValue - 5/* half of the line*/);
                        }
                        if (projectionViewHolder.mEndDivider.getX() - projectionViewHolder.mRangeView.getX() < 150 && projectionViewHolder.mBluePlus.getVisibility() != View.VISIBLE) {
                            projectionViewHolder.mEndDivider.setVisibility(View.GONE);
                        } else {
                            projectionViewHolder.mEndDivider.setVisibility(View.VISIBLE);
                        }
                    }
                });
                projectionViewHolder.mMin.setText(valueInK((int) widget.getLowLimit()));
//                projectionViewHolder.mStandard.setText(valueInK((int) widget.getStandardValue()));
                projectionViewHolder.mMax.setText(valueInK(widget.getTarget()));
                break;

        }
    }

    private String valueInK(int value) {
        String valueString = String.valueOf(value);
        if (value >= 1000) {
            if (value % 1000 == 0) {
                valueString = String.valueOf(value / 1000);
            } else {
                float valueFloat = (float) value / 1000;
                valueString = String.valueOf(valueFloat);
            }
            return valueString + "k";
        } else {
            return valueString;
        }
    }

    @Override
    public int getItemCount() {
        return mWidgets.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        switch (mWidgets.get(position).getFieldType()) {
            case 0:
                type = NUMERIC;
                break;
            case 1:
                type = RANGE;
                break;
            case 2:
                type = PROJECTION;
                break;
            case 3:
                type = TIME;
                break;
            default:
                type = NUMERIC;
                break;
        }
        return type;
    }
}
