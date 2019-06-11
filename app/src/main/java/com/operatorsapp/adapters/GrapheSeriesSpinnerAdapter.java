package com.operatorsapp.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.common.reportShift.Graph;
import com.operatorsapp.R;

import java.util.ArrayList;

public class GrapheSeriesSpinnerAdapter extends ArrayAdapter<Graph> {

    private final ArrayList<Graph> mGraphs;
    private Activity mContext;
    private TextView mRowName;
    private boolean mIsFirst = true;
    private String mTitle;

    public GrapheSeriesSpinnerAdapter(Activity context, int resource, ArrayList<Graph> list, String title) {
        super(context, resource, list);
        mContext = context;
        mGraphs = list;
        mTitle = title;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_language_item, parent, false);
            mRowName = row.findViewById(R.id.spinner_language_item_name);
            mRowName.setTextSize(25);
            mRowName.setTypeface(mRowName.getTypeface(), Typeface.BOLD);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            mRowName.setText(mGraphs.get(position).getDisplayName());
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_language_item, parent, false);
//            mView = row;
        }

        String item = mGraphs.get(position).getDisplayName();
        if (item != null) {
            mRowName = row.findViewById(R.id.spinner_language_item_name);
            mRowName.setText(item);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            mRowName.setTextSize(17);
        }
        return row;
    }

}
