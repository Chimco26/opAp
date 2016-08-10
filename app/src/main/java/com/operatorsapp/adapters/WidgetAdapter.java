package com.operatorsapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.activities.interfaces.GoToScreenListener;
import com.operatorsapp.fragments.ChartFragment;
import com.operatorsapp.view.LineChartTimeSmall;
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
        private RangeView mRangeView;
        private TextView mMin;
        private TextView mStandard;
        private TextView mMax;

        public RangeViewHolder(View itemView) {
            super(itemView);

            mTitle = (AutofitTextView) itemView.findViewById(R.id.range_widget_title);
            mSubtitle = (AutofitTextView) itemView.findViewById(R.id.range_widget_subtitle);
            mValue = (TextView) itemView.findViewById(R.id.range_widget_current_value);
            mRangeView = (RangeView) itemView.findViewById(R.id.range_widget_range_view);
            mMin = (TextView) itemView.findViewById(R.id.range_widget_min);
            mStandard = (TextView) itemView.findViewById(R.id.range_widget_standard);
            mMax = (TextView) itemView.findViewById(R.id.range_widget_max);

        }
    }

    private class ProjectionViewHolder extends RecyclerView.ViewHolder {

        public ProjectionViewHolder(View itemView) {
            super(itemView);

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
                timeViewHolder.mSubtitle.setText(new StringBuilder("Standard " + widget.getStandardValue()));
                timeViewHolder.mValue.setText(widget.getCurrentValue());
                final ArrayList<Entry> tenHoursValues = new ArrayList<>();
                final ArrayList<Entry> fourHoursValues = new ArrayList<>();
                if (widget.getMachineParamHistoricData() != null && widget.getMachineParamHistoricData().size() > 0) {

                    for (int i = 0; i < widget.getMachineParamHistoricData().size(); i++) {
                        Entry entry = new Entry();
                        entry.setX(widget.getMachineParamHistoricData().get(i).getTime());
                        entry.setY(widget.getMachineParamHistoricData().get(i).getValue());
                        if (widget.getMachineParamHistoricData().get(i).getTime() > (System.currentTimeMillis() - TEN_HOURS)) {
                            tenHoursValues.add(entry);
                        }
                        if (widget.getMachineParamHistoricData().get(i).getTime() > (System.currentTimeMillis() - FOUR_HOURS)) {
                            fourHoursValues.add(entry);
                        }
                    }
                    timeViewHolder.mChart.setData(fourHoursValues);
                }
                timeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGoToScreenListener.goToFragment(ChartFragment.newInstance(mContext, tenHoursValues, widget.getLowLimit(), widget.getStandardValue(), widget.getHighLimit()), true);
                    }
                });
                break;

            case RANGE:
                final RangeViewHolder rangeViewHolder = (RangeViewHolder) holder;
                rangeViewHolder.mTitle.setText(widget.getFieldName());
                rangeViewHolder.mSubtitle.setText(new StringBuilder("Standard " + widget.getStandardValue()));
                rangeViewHolder.mValue.setText(widget.getCurrentValue());
                if (widget.isOutOfRange()) {
                    rangeViewHolder.mValue.setTextColor(ContextCompat.getColor(mContext, R.color.red_line));
                } else {
                    rangeViewHolder.mValue.setTextColor(ContextCompat.getColor(mContext, R.color.C16));
                }

                rangeViewHolder.mRangeView.setCurrentLine(widget.isOutOfRange());
                if (widget.isOutOfRange() && Integer.parseInt(widget.getCurrentValue()) > widget.getHighLimit()) {
                    rangeViewHolder.mRangeView.updateX(232);
                } else if (widget.isOutOfRange() && Integer.parseInt(widget.getCurrentValue()) < widget.getLowLimit()) {
                    rangeViewHolder.mRangeView.updateX(15);
                } else {
                    rangeViewHolder.mRangeView.updateX(Integer.parseInt(widget.getCurrentValue()));
                }

                rangeViewHolder.mMin.setText(String.valueOf(widget.getLowLimit()));
                rangeViewHolder.mStandard.setText(String.valueOf(widget.getStandardValue()));
                rangeViewHolder.mMax.setText(String.valueOf(widget.getHighLimit()));
                break;

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
