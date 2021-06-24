package com.operatorsapp.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.common.QCModels.SubType;
import com.operatorsapp.R;

import java.util.List;

public class SubTypeAdapter extends ArrayAdapter<SubType> {
    private List<SubType> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public SubTypeAdapter(Context context, int resource, List<SubType> models) {
        super(context, resource, models);
        mSpinnerItems = models;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            mRowName.setText(mSpinnerItems.get(0).getName());
            mRowName.setTextSize(22);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.base_spinner_item_dropdown, parent, false);
        }
        String item = mSpinnerItems.get(position).getName();
        if (item != null) {
            TextView name = row.findViewById(R.id.spinner_item_name);
            name.setText(item);
            name.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            name.setTextSize(22);
        }
        return row;
    }

    public void setTitle(int position) {
        if (mView != null) {
            mRowName = mView.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.status_bar));
            mRowName.setText(mSpinnerItems.get(position).getName());
            mRowName.setTextSize(24);
        }
    }
}
