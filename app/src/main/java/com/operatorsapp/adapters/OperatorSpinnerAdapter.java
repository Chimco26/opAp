package com.operatorsapp.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operatorsapp.R;

public class OperatorSpinnerAdapter extends ArrayAdapter<String> {

    private Activity mContext;
    private String[] mSpinnerItems;
    private String mCurrentOperatorName;
    private TextView spinnerTitle;

    public OperatorSpinnerAdapter(Activity context, int resource, String[] operators, String currentOperator) {
        super(context, resource, operators);
        mSpinnerItems = operators;
        mContext = context;
        mCurrentOperatorName = currentOperator;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
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
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_operator_dropdown_item, parent, false);
        }
        String item = mSpinnerItems[position];
        if (item != null) {
            TextView name = row.findViewById(R.id.spinner_operator_item_name);
            TextViewCompat.setTextAppearance(name, R.style.FontStyle_T10);
            name.setTextColor(mContext.getResources().getColor(R.color.white));
            name.setText(item);
            name.setTextSize(18);
        }
        return row;
    }

    public void updateTitle(String newTitle){
        spinnerTitle.setText(newTitle);
    }
}