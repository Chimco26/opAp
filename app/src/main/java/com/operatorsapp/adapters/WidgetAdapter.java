package com.operatorsapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.data.Entry;
import com.operators.machinedatainfra.models.Widget;
import com.operatorsapp.R;
import com.operatorsapp.interfaces.DashboardChartCallbackListener;
import com.operatorsapp.view.LineChartTime;
import com.operatorsapp.view.RangeView;

import java.util.ArrayList;
import java.util.List;

public class WidgetAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Widget> mWidgets;
    private final int NUMERIC = 0;
    private final int RANGE = 1;
    private final int PROJECTION = 2;
    private final int TIME = 3;

    public WidgetAdapter(Context context, List<Widget> widgets) {
        mWidgets = widgets;
        mContext = context;
    }

    public void setNewData(List<Widget> widgets) {
        mWidgets = widgets;
        notifyDataSetChanged();
    }

    private class NumericViewHolder extends RecyclerView.ViewHolder {

        public NumericViewHolder(View itemView) {
            super(itemView);

        }
    }

    private class RangeViewHolder extends RecyclerView.ViewHolder {

        private RangeView mRangeView;

        public RangeViewHolder(View itemView) {
            super(itemView);

            mRangeView = (RangeView) itemView.findViewById(R.id.range_view);

        }
    }

    private class ProjectionViewHolder extends RecyclerView.ViewHolder {

        public ProjectionViewHolder(View itemView) {
            super(itemView);

        }
    }

    private class TimeViewHolder extends RecyclerView.ViewHolder {

        private LineChartTime mChart;

        public TimeViewHolder(View itemView) {
            super(itemView);

            mChart = (LineChartTime) itemView.findViewById(R.id.lineChart_time);
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
        Widget widget = mWidgets.get(position);
        switch (type) {
            case NUMERIC:

                break;
            case TIME:
                final TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
                if (widget.getMachineParamHistoricData() != null && widget.getMachineParamHistoricData().size() > 0) {
                    ArrayList<Entry> values = new ArrayList<>();
                    for (int i = 0; i < widget.getMachineParamHistoricData().size(); i++) {
                        Entry entry = new Entry();
                        entry.setX(widget.getMachineParamHistoricData().get(i).getTime());
                        entry.setY(widget.getMachineParamHistoricData().get(i).getValue());
                        values.add(entry);
                    }
                    timeViewHolder.mChart.setData(values);
                }
                break;

            case RANGE:
                final RangeViewHolder rangeViewHolder = (RangeViewHolder) holder;
                rangeViewHolder.mRangeView.updateX(Integer.parseInt(widget.getCurrentValue()));
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
