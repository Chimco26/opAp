package com.operatorsapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
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
    private TextView mRowName;
    private View mView;

    public JoshProductNameSpinnerAdapter(Activity context, int resource, List<ActiveJob> activeJobs)
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
            row = inflater.inflate(R.layout.item_product_spinner, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.IPSL_name);
            mRowName.setText(mSpinnerItems.get(position).getProductName());
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
            row = inflater.inflate(R.layout.item_product_spinner_list, parent, false);
        }
        TextView name = row.findViewById(R.id.IPSL_name);
        name.setText(mSpinnerItems.get(position).getProductName());

        return row;
    }

    public void setTitle(int position)
    {
        if (mView != null)
        {
            mRowName = mView.findViewById(R.id.IPSL_name);
            mRowName.setText(String.valueOf(mSpinnerItems.get(position).getProductName()));
        }
    }
}
