package com.operatorsapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operatorsapp.R;
import com.operatorsapp.model.JobActionsSpinnerItem;

import java.util.List;

//public class JobsSpinnerAdapter extends ArrayAdapter<JobsSpinnerAdapter.JobActionsSpinnerItem> {
public class JobsSpinnerAdapter extends ArrayAdapter<JobActionsSpinnerItem> {
    private Activity mContext;
    private List<JobActionsSpinnerItem> mSpinnerItems = null;
    TextView mRowName;


    public JobsSpinnerAdapter(Activity context, int resource, List<JobActionsSpinnerItem> spinnerItems)
    {
        super(context, resource, spinnerItems);
        this.mContext = context;
        this.mSpinnerItems = spinnerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_job_item, parent, false);
            TextView rowName = (TextView) row.findViewById(R.id.spinner_job_item_name);
            rowName.setTextSize(18);

        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_job_item, parent, false);
        }

        JobActionsSpinnerItem item = mSpinnerItems.get(position);
        if (item != null) {
            mRowName = (TextView) row.findViewById(R.id.spinner_job_item_name);
            mRowName.setText(item.getName());
            TextViewCompat.setTextAppearance(mRowName, R.style.FontStyle_T10);
            mRowName.setTextColor(Color.BLACK);
            mRowName.setTextSize(30);
            if(!item.isEnabled())
            {
                mRowName.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.default_gray));
            }
            else
            {
                mRowName.setBackgroundColor(ContextCompat.getColor(getContext(),android.R.color.white));
            }
        }
        return row;

    }

}