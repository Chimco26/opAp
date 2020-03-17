package com.operatorsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.common.SelectableString;
import com.operatorsapp.R;

import java.util.List;

public class SimpleSpinnerAdapter extends ArrayAdapter<SelectableString> {
    private Context mContext;
    private List<SelectableString> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public SimpleSpinnerAdapter(Context context, int resource, List<SelectableString> list) {
        super(context, resource, list);
        mSpinnerItems = list;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            if(mSpinnerItems != null && mSpinnerItems.get(0) != null)
            {
                mRowName.setText(mSpinnerItems.get(0).getString());
            }
            else
            {
                mRowName.setText(mContext.getString(R.string.dashes));
            }

            mRowName.setTextSize(22);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = inflater.inflate(R.layout.base_spinner_item_dropdown, parent, false);
        }
        String item = mSpinnerItems.get(position).getString();
        if (item != null) {
            TextView name = row.findViewById(R.id.spinner_item_name);
            name.setText(mSpinnerItems.get(position).getString());
            name.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            name.setTextSize(22);
        }
        return row;
    }

    public void setTitle(int position) {

        mRowName = mView.findViewById(R.id.spinner_item_name);
        mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
        mRowName.setText(mSpinnerItems.get(position).getString());
        mRowName.setTextSize(24);
    }
}
