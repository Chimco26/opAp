package com.operatorsapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;
import com.operatorsapp.interfaces.DashboardChartCallbackListener;

import java.util.ArrayList;

public class WidgetAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<String> mWidgets;
    private DashboardChartCallbackListener mDashboardChartCallbackListener;

    private final int BASE = 0;
    private final int RANGE = 1;
    private final int PROJECTION = 2;
    private final int TIME = 3;

    public WidgetAdapter(Context context, ArrayList<String> widgets, DashboardChartCallbackListener dashboardChartCallbackListener) {
        mWidgets = widgets;
        mContext = context;
        mDashboardChartCallbackListener = dashboardChartCallbackListener;
    }

    private class OneViewHolder extends RecyclerView.ViewHolder {

        public OneViewHolder(View itemView) {
            super(itemView);

        }
    }

    private class TwoViewHolder extends RecyclerView.ViewHolder {

        public TwoViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case BASE: {
                return new OneViewHolder(inflater.inflate(R.layout.base_widget_cardview, parent, false));
            }
            case RANGE: {
                return new TwoViewHolder(inflater.inflate(R.layout.range_widget_cardview, parent, false));
            }
            case PROJECTION: {
                return new TwoViewHolder(inflater.inflate(R.layout.projection_widget_cardview, parent, false));
            }
            case TIME: {
                return new TwoViewHolder(inflater.inflate(R.layout.time_widget_cardview, parent, false));
            }
        }
        return new OneViewHolder(inflater.inflate(R.layout.base_widget_cardview, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String s = mWidgets.get(position);

        int type = getItemViewType(position);

        switch (type) {
            case TIME:
                mDashboardChartCallbackListener.onChartStart();
                break;

        }


    }

    @Override
    public int getItemCount() {
        return mWidgets.size();
    }

    @Override
    public int getItemViewType(int position) {
        String s = mWidgets.get(position);
        int type;
        switch (mWidgets.get(position)) {
            case "0":
                type = BASE;
                break;
            case "1":
                type = RANGE;
                break;
            case "2":
                type = PROJECTION;
                break;
            case "3":
                type = TIME;
                break;
            default:
                type = BASE;
                break;
        }
        return type;
    }
}
