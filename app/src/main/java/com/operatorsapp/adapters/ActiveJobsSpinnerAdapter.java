package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operators.reportfieldsformachineinfra.PackageTypes;
import com.operatorsapp.R;

import java.util.List;

/**
 * Created by Sergey on 04/08/2016.
 */
public class ActiveJobsSpinnerAdapter extends ArrayAdapter<ActiveJob> {
    private Activity mContext;
    private List<ActiveJob> mSpinnerItems;
    TextView mRowName;
    View mView;

    public ActiveJobsSpinnerAdapter(Activity context, int resource, List<ActiveJob> activeJobs) {
        super(context, resource, activeJobs);
        mSpinnerItems = activeJobs;
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
            mRowName.setText(mSpinnerItems.get(0).getJobName());
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
        String item = mSpinnerItems.get(position).getJobName();
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
        mRowName.setText(mSpinnerItems.get(position).getJobName());
        mRowName.setTextSize(20);
    }
}
