package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operators.reportrejectnetworkbridge.server.response.activateJob.Header;
import com.operatorsapp.R;

import java.util.ArrayList;

public class JobHeadersSpinnerAdapter extends ArrayAdapter<Header> {

    private final ArrayList<Header> mHeaders;
    private Activity mContext;
    private TextView mRowName;
    private boolean mIsFirst = true;
    private String mTitle;

    public JobHeadersSpinnerAdapter(Activity context, int resource, ArrayList<Header> list) {
        super(context, resource, list);
        mContext = context;
        mHeaders = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_language_item, parent, false);
            mRowName = row.findViewById(R.id.spinner_language_item_name);
            mRowName.setTextSize(20);
            if (mIsFirst) {
                setTitle(mHeaders.get(0).getDisplayName());
                mIsFirst = false;
            }
            else {
                setTitle(mTitle);
            }


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

        String item = mHeaders.get(position).getDisplayName();
        if (item != null) {
            mRowName = row.findViewById(R.id.spinner_language_item_name);
            mRowName.setText(item);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            mRowName.setTextSize(17);
        }
        return row;
    }

    public void setTitle(String name) {
        mTitle = name;
        mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
        mRowName.setText(name);
        mRowName.setTextSize(20);
    }
}