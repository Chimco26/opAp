package com.operatorsapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.operatorinfra.Operator;
import com.operatorsapp.R;
import com.operatorsapp.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class OperatorSpinnerAdapter extends ArrayAdapter<String> {

    private Activity mContext;
    private String[] mSpinnerItems = null;

    public OperatorSpinnerAdapter(Activity context, int resource, String[] operators) {
        super(context, resource, operators);
        mSpinnerItems = operators;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_operator_item, parent, false);
        }
        String item = mSpinnerItems[position];
        if (item != null) {
            TextView name = (TextView) row.findViewById(R.id.spinner_operator_item_name);
            name.setTextColor(Color.WHITE);
            name.setText(item);

        }

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_operator_item, parent, false);
        }
        String item = mSpinnerItems[position];
        if (item != null) {
            TextView name = (TextView) row.findViewById(R.id.spinner_operator_item_name);
            name.setTextColor(Color.BLACK);
            name.setText(item);

        }

        return row;
    }

}