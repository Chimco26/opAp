package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operatorsapp.R;

import java.util.List;

public class ActiveJobsSpinnerAdapter extends ArrayAdapter<ActiveJob>
{
    private Activity mContext;
    private List<ActiveJob> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public ActiveJobsSpinnerAdapter(Activity context, int resource, List<ActiveJob> activeJobs)
    {
        super(context, resource, activeJobs);
        mSpinnerItems = activeJobs;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.active_jobs_spinner_item, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray_status));
            mRowName.setText(String.valueOf(mSpinnerItems.get(position).getJoshName()));
            mRowName.setTextSize(20);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.active_jobs_spinner_item, parent, false);
        }
        TextView name =  row.findViewById(R.id.spinner_item_name);
        name.setText(String.valueOf(mSpinnerItems.get(position).getJoshName()));
        //name.setText(String.valueOf(mSpinnerItems.get(position).getJoshID()));
        name.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray_status));
        name.setTextSize(20);

        return row;
    }

    public void setTitle(int position)
    {
        if (mView != null)
        {
            mRowName =  mView.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(mContext, R.color.default_gray_status));
            mRowName.setText(String.valueOf(mSpinnerItems.get(position).getJoshName()));
            //mRowName.setText(String.valueOf(mSpinnerItems.get(position).getJoshID()));
            mRowName.setTextSize(20);
        }
    }
}
