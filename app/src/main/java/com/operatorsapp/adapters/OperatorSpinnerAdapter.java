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
    private String mCurrentOperatorName;

    public OperatorSpinnerAdapter(Activity context, int resource, String[] operators, String currentOperator) {
        super(context, resource, operators);
        mSpinnerItems = operators;
        mContext = context;
        mCurrentOperatorName = currentOperator;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_operator_item, parent, false);
            TextView spinnerTitle = (TextView) row.findViewById(R.id.spinner_operator_item_name);
            if (mCurrentOperatorName == null  ) {
                spinnerTitle.setText(mSpinnerItems[0]);
            }
            else if(mCurrentOperatorName.equals("")){
                spinnerTitle.setText("--");
            }
            else {
                spinnerTitle.setText(mCurrentOperatorName);
            }
            TextView rowName = (TextView)row.findViewById(R.id.spinner_operator_item_name);
            rowName.setTextSize(20);
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
            name.setText(item);
            name.setTextAppearance(getContext(),R.style.FontStyle_T10);
            name.setTextColor(Color.BLACK);
            name.setTextSize(17);
        }
        return row;
    }

}