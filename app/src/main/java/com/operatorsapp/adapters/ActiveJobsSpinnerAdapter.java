package com.operatorsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.operators.activejobslistformachineinfra.ActiveJob;
import com.operatorsapp.R;

import java.util.List;

public class ActiveJobsSpinnerAdapter extends ArrayAdapter<ActiveJob> {
    private List<ActiveJob> mSpinnerItems;
    private TextView mRowName;
    private View mView;

    public ActiveJobsSpinnerAdapter(Context context, int resource, List<ActiveJob> activeJobs) {
        super(context, resource, activeJobs);
        mSpinnerItems = activeJobs;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.active_jobs_spinner_item, parent, false);
            mView = row;
            mRowName = row.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.dialog_text_gray));
            mRowName.setText(String.valueOf(mSpinnerItems.get(position).getJoshName()));
            //mRowName.setText(String.valueOf(mSpinnerItems.get(0).getJoshID()));
            mRowName.setTextSize(16);
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.active_jobs_spinner_item, parent, false);
        }
        TextView name = row.findViewById(R.id.spinner_item_name);
        name.setText(String.valueOf(mSpinnerItems.get(position).getJoshName()));
        //name.setText(String.valueOf(mSpinnerItems.get(position).getJoshID()));
        name.setTextColor(ContextCompat.getColor(getContext(), R.color.dialog_text_gray));
        name.setTextSize(16);

        return row;
    }

    public void setTitle(int position) {
        if (mView != null) {
            mRowName = mView.findViewById(R.id.spinner_item_name);
            mRowName.setTextColor(ContextCompat.getColor(getContext(), R.color.dialog_text_gray));
            mRowName.setText(String.valueOf(mSpinnerItems.get(position).getJoshName()));
            //mRowName.setText(String.valueOf(mSpinnerItems.get(position).getJoshID()));
            mRowName.setTextSize(20);
        }
    }
}
