package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operators.reportfieldsformachineinfra.RejectReasons;
import com.operatorsapp.R;

import java.util.List;

public class RejectReasonSpinnerAdapter extends ArrayAdapter<RejectReasons> {
    private Activity mContext;
    private List<RejectReasons> mSpinnerItems;
    TextView mRowName;
    View mView;

    public RejectReasonSpinnerAdapter(Activity context, int resource, List<RejectReasons> reasonsList) {
        super(context, resource, reasonsList);
        mSpinnerItems = reasonsList;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
            mView = row;
            mRowName = (TextView) row.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            mRowName.setText(mSpinnerItems.get(0).getName());  //TODO need change to correct selection
            mRowName.setTextSize(20);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.base_spinner_item, parent, false);
        }
        String item = mSpinnerItems.get(position).getName();
        if (item != null) {
            TextView name = (TextView) row.findViewById(R.id.spinner_item_name);
            name.setText(item);
            name.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
            name.setTextSize(17);
        }
        return row;
    }

    public void setTitle(int position) {

        mRowName = (TextView) mView.findViewById(R.id.spinner_item_name);
        mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.status_bar));
        mRowName.setText(mSpinnerItems.get(position).getName());
        mRowName.setTextSize(20);
    }
}
