package com.operatorsapp.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.operatorsapp.R;
import com.operatorsapp.interfaces.DashboardChartCallbackListener;
import com.operatorsapp.view.RangeView;

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

    private class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
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

        public TimeViewHolder(View itemView) {
            super(itemView);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case BASE: {
                return new BaseViewHolder(inflater.inflate(R.layout.base_widget_cardview, parent, false));
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
        return new BaseViewHolder(inflater.inflate(R.layout.base_widget_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String s = mWidgets.get(position);

        int type = getItemViewType(position);

        switch (type) {
            case TIME:
                mDashboardChartCallbackListener.onChartStart();
                break;

            case RANGE:
                final RangeViewHolder rangeViewHolder = (RangeViewHolder) holder;
                rangeViewHolder.mRangeView.updatePath(140);
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
