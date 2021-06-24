package com.operatorsapp.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;

import android.content.Context;
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
    private List<ActiveJob> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public JoshProductNameSpinnerAdapter(Context context, int resource, List<ActiveJob> activeJobs)
    {
        super(context, resource, activeJobs);
        mSpinnerItems = activeJobs;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.item_product_spinner, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.IPSL_name);
            String name = mSpinnerItems.get(position).getProductName();
            String jobID = String.valueOf(mSpinnerItems.get(position).geteRPJobID());
            mRowName.setText(name);
//            mRowName.setText(mSpinnerItems.get(position).getProductName());
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View row = convertView;
        if (row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.item_product_spinner_list, parent, false);
        }
        TextView name = row.findViewById(R.id.IPSL_name);
        String productName = mSpinnerItems.get(position).getProductName();
        String jobID = String.valueOf(mSpinnerItems.get(position).geteRPJobID());
        String catalogId = String.valueOf(mSpinnerItems.get(position).getProductCatalogId());
        name.setText(String.format("%s- %s- %s", jobID, catalogId, productName));
//        name.setText(mSpinnerItems.get(position).getProductName());

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
