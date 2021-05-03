package com.operatorsapp.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operatorsapp.R;

public class OperatorSpinnerAdapter extends ArrayAdapter<String> {

    private String[] mSpinnerItems;
    private String mCurrentOperatorName;
    private TextView spinnerTitle;

    public OperatorSpinnerAdapter(Context context, int resource, String[] operators, String currentOperator) {
        super(context, resource, operators);
        mSpinnerItems = operators;
        mCurrentOperatorName = currentOperator;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.spinner_operator_item, parent, false);
            spinnerTitle = row.findViewById(R.id.spinner_operator_item_name);
            if (mCurrentOperatorName == null  ) {
                spinnerTitle.setText(mSpinnerItems[0]);
            }
            else if(mCurrentOperatorName.equals("")){
                spinnerTitle.setText("--");
            }
            else {
                spinnerTitle.setText(mCurrentOperatorName);
            }
            spinnerTitle.setTextSize(20);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.spinner_operator_dropdown_item, parent, false);
        }
        String item = mSpinnerItems[position];
        if (item != null) {
            TextView name = row.findViewById(R.id.spinner_operator_item_name);
            TextViewCompat.setTextAppearance(name, R.style.FontStyle_T10);
            name.setTextColor(getContext().getResources().getColor(R.color.white));
            name.setText(item);
            name.setTextSize(18);
        }
        return row;
    }

    public void updateTitle(String newTitle){
        spinnerTitle.setText(newTitle);
    }
}