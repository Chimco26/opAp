package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operatorsapp.R;

import java.util.List;

public class JoshProductNameSpinnerAdapter extends ArrayAdapter<ActiveJob>
{
    private Activity mContext;
    private List<ActiveJob> mSpinnerItems;
    TextView mRowName;
    View mView;

    public JoshProductNameSpinnerAdapter(Activity context, int resource, List<ActiveJob> activeJobs)
    {
        super(context, resource, activeJobs);
        mSpinnerItems = activeJobs;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.item_product_spinner, parent, false);
            mView = row;
            mRowName = (TextView) row.findViewById(R.id.IPSL_name);
            mRowName.setText(mSpinnerItems.get(position).getProductName());
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.item_product_spinner_list, parent, false);
        }
        TextView name = (TextView) row.findViewById(R.id.IPSL_name);
        name.setText(mSpinnerItems.get(position).getProductName());

        return row;
    }

    public void setTitle(int position)
    {
        if (mView != null)
        {
            mRowName = (TextView) mView.findViewById(R.id.IPSL_name);
            mRowName.setText(String.valueOf(mSpinnerItems.get(position).getProductName()));
        }
    }
}
