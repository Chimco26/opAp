package com.operatorsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.operators.shiftloginfra.Event;
import com.operatorsapp.R;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

public class WidgetAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<String> mWidgets;

    private final int ONE = 1;
    private final int TWO = 2;

    public WidgetAdapter(Context context, ArrayList<String> widgets) {
        mWidgets = widgets;
        mContext = context;
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
            case ONE: {
                return new OneViewHolder(inflater.inflate(R.layout.widget_one_cardview, parent, false));
            }
            case TWO: {
                return new TwoViewHolder(inflater.inflate(R.layout.widget_two_cardview, parent, false));
            }
        }
        return new OneViewHolder(inflater.inflate(R.layout.widget_one_cardview, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String s = mWidgets.get(position);

        int type = getItemViewType(position);


    }

    @Override
    public int getItemCount() {
        return mWidgets.size();
    }

    @Override
    public int getItemViewType(int position) {
        String s = mWidgets.get(position);
        if (s == "1") {
            return ONE;
        }
        return TWO;
    }

}
